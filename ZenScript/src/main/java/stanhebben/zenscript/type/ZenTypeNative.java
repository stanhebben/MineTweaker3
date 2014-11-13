/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArithmeticUnary;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionString;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.iterator.IteratorIterable;
import stanhebben.zenscript.type.iterator.IteratorList;
import stanhebben.zenscript.type.iterator.IteratorMap;
import stanhebben.zenscript.type.iterator.IteratorMapKeys;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.java.method.JavaMethod;
import stanhebben.zenscript.type.natives.ZenNativeCaster;
import stanhebben.zenscript.type.natives.ZenNativeMember;
import stanhebben.zenscript.type.natives.ZenNativeOperator;
import static stanhebben.zenscript.util.AnyClassWriter.throwCastException;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.IterableList;
import org.openzen.zencode.annotations.IterableMap;
import org.openzen.zencode.annotations.IterableSimple;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMemberGetter;
import org.openzen.zencode.annotations.ZenMemberSetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenOperator;
import org.openzen.zencode.annotations.ZenSetter;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingNotNull;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNone;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeNative extends ZenType {
	private static final int ITERATOR_NONE = 0;
	private static final int ITERATOR_ITERABLE = 1;
	private static final int ITERATOR_LIST = 2;
	private static final int ITERATOR_MAP = 3;
	
	private final Class cls;
	private final TypeCapture capture;
	
	private final String anyName;
	private final String anyName2;
	private final List<ZenTypeNative> implementing;
	private final Map<String, ZenNativeMember> members;
	private final Map<String, ZenNativeMember> staticMembers;
	private final List<ZenNativeCaster> casters;
	private final List<ZenNativeOperator> operators;
	
	private int iteratorType;
	private String classPkg;
	private String className;
	private Annotation iteratorAnnotation;
	private ZenType iteratorKeyType;
	private ZenType iteratorValueType;
	private IMethod functionalInterface;
	
	public ZenTypeNative(IScopeGlobal environment, Class cls, TypeCapture capture) {
		super(environment);
		
		this.cls = cls;
		this.capture = capture;
		
		members = new HashMap<String, ZenNativeMember>();
		staticMembers = new HashMap<String, ZenNativeMember>();
		casters = new ArrayList<ZenNativeCaster>();
		operators = new ArrayList<ZenNativeOperator>();
		implementing = new ArrayList<ZenTypeNative>();
		
		anyName2 = cls.getName() + "Any";
		anyName = anyName2.replace('.', '/');
	}
	
	public void complete(TypeRegistry types) {
		int iterator = ITERATOR_NONE;
		Annotation _iteratorAnnotation = null;
		String _classPkg = cls.getPackage().getName().replace('/', '.');
		String _className = cls.getSimpleName();
		boolean fully = false;
		
		Queue<ZenTypeNative> todo = new LinkedList<ZenTypeNative>();
		todo.add(this);
		addSubtypes(todo, types);
		
		Annotation[] clsAnnotations = cls.getAnnotations();
		for (Annotation annotation : clsAnnotations) {
			if (annotation instanceof ZenClass) {
				String value = ((ZenClass) annotation).value();
				int dot = value.lastIndexOf('.');
				if (dot < 0) {
					_classPkg = null;
					_className = value;
				} else {
					_classPkg = value.substring(0, dot);
					_className = value.substring(dot + 1);
				}
			}
			if (annotation instanceof IterableSimple) {
				iterator = ITERATOR_ITERABLE;
				_iteratorAnnotation = annotation;
				if (!Iterable.class.isAssignableFrom(cls)) {
					// TODO: illegal
				}
			}
			if (annotation instanceof IterableList) {
				iterator = ITERATOR_LIST;
				_iteratorAnnotation = annotation;
				if (!List.class.isAssignableFrom(cls)) {
					// TODO: illegal
				}
			}
			if (annotation instanceof IterableMap) {
				iterator = ITERATOR_MAP;
				_iteratorAnnotation = annotation;
				if (!Map.class.isAssignableFrom(cls)) {
					// TODO: illegal
				}
			}
		}
		
		for (Method method : cls.getMethods()) {
			boolean isMethod = fully;
			String methodName = method.getName();
			
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof ZenCaster) {
					casters.add(new ZenNativeCaster(JavaMethod.get(types, method, capture)));
					isMethod = false;
				} else if (annotation instanceof ZenGetter) {
					ZenGetter getterAnnotation = (ZenGetter) annotation;
					String name = getterAnnotation.value().length() == 0 ? method.getName() : getterAnnotation.value();
					
					if (!members.containsKey(name)) {
						members.put(name, new ZenNativeMember());
					}
					members.get(name).setGetter(new JavaMethod(method, types, capture));
					isMethod = false;
				} else if (annotation instanceof ZenSetter) {
					ZenSetter setterAnnotation = (ZenSetter) annotation;
					String name = setterAnnotation.value().length() == 0 ? method.getName() : setterAnnotation.value();
					
					if (!members.containsKey(name)) {
						members.put(name, new ZenNativeMember());
					}
					members.get(name).setSetter(new JavaMethod(method, types, capture));
					isMethod = false;
				} else if (annotation instanceof ZenMemberGetter) {
					operators.add(new ZenNativeOperator(OperatorType.MEMBERGETTER, new JavaMethod(method, types, capture)));
				} else if (annotation instanceof ZenMemberSetter) {
					operators.add(new ZenNativeOperator(OperatorType.MEMBERSETTER, new JavaMethod(method, types, capture)));
				} else if (annotation instanceof ZenOperator) {
					ZenOperator operatorAnnotation = (ZenOperator) annotation;
					switch (operatorAnnotation.value()) {
						case NEG:
						case NOT:
							if (method.getParameterTypes().length != 0) {
								// TODO: error
							} else {
								operators.add(new ZenNativeOperator(
										operatorAnnotation.value(),
										new JavaMethod(method, types, capture)));
							}
							break;
						case ADD:
						case SUB:
						case CAT:
						case MUL:
						case DIV:
						case MOD:
						case AND:
						case OR:
						case XOR:
						case INDEXGET:
						case RANGE:
						case CONTAINS:
						case COMPARE:
							if (method.getParameterTypes().length != 1) {
								// TODO: error
							} else {
								operators.add(new ZenNativeOperator(
										operatorAnnotation.value(),
										new JavaMethod(method, types, capture)));
							}
							break;
						case INDEXSET:
							if (method.getParameterTypes().length != 2) {
								// TODO: error
							} else {
								operators.add(new ZenNativeOperator(
										operatorAnnotation.value(),
										new JavaMethod(method, types, capture)));
							}
							break;
					}
					isMethod = false;
				} else if (annotation instanceof ZenMethod) {
					isMethod = true;
					
					ZenMethod methodAnnotation = (ZenMethod) annotation;
					if (methodAnnotation.value().length() > 0) {
						methodName = methodAnnotation.value();
					}
				}
			}
			
			if (isMethod) {
				if ((method.getModifiers() & Modifier.STATIC) > 0) {
					if (!staticMembers.containsKey(methodName)) {
						staticMembers.put(methodName, new ZenNativeMember());
					}
					staticMembers.get(methodName).addMethod(new JavaMethod(method, types, capture));
				} else {
					if (!members.containsKey(methodName)) {
						members.put(methodName, new ZenNativeMember());
					}
					members.get(methodName).addMethod(new JavaMethod(method, types, capture));
				}
			}
		}
		
		this.iteratorType = iterator;
		this.iteratorAnnotation = _iteratorAnnotation;
		this.classPkg = _classPkg;
		this.className = _className;
		
		functionalInterface = null;
		
		if (cls.isInterface() && cls.getMethods().length == 1) {
			functionalInterface = JavaMethod.get(types, cls.getMethods()[0], capture);
		}
	}
	
	public Class getNativeClass() {
		return cls;
	}
	
	public void complete() {
		if (iteratorAnnotation instanceof IterableSimple) {
			IterableSimple annotation = (IterableSimple) iteratorAnnotation;
			iteratorValueType = TypeParser.parseDirect(annotation.value(), getScope());
		}
		if (iteratorAnnotation instanceof IterableList) {
			IterableList annotation = (IterableList) iteratorAnnotation;
			iteratorKeyType = getScope().getTypes().INT;
			iteratorValueType = TypeParser.parseDirect(annotation.value(), getScope());
		}
		if (iteratorAnnotation instanceof IterableMap) {
			IterableMap annotation = (IterableMap) iteratorAnnotation;
			iteratorKeyType = TypeParser.parseDirect(annotation.key(), getScope());
			iteratorValueType = TypeParser.parseDirect(annotation.value(), getScope());
		}
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal environment = getScope();
		
		if (!environment.containsClass(anyName2)) {
			environment.putClass(anyName2, new byte[0]);
			//global.putClass(anyName2, AnyClassWriter.construct(new AnyNativeDefinition(global), anyName2, toASMType()));
		}
		
		return anyName;
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		ZenNativeMember member = members.get(name);
		if (member == null) {
			for (ZenTypeNative type : implementing) {
				if (type.members.containsKey(name)) {
					member = type.members.get(name);
					break;
				}
			}
		}
		
		if (member == null) {
			MemberVirtual result = new MemberVirtual(position, environment, value.eval(), name);
			memberExpansion(result);
			
			if (result.isEmpty()) {
				if (hasBinary(getScope().getTypes().STRING, OperatorType.MEMBERGETTER)) {
					return operator(
							position,
							environment,
							OperatorType.MEMBERGETTER,
							value.eval(),
							new ExpressionString(position, environment, name));
				} else {
					environment.error(position, "No such member in " + getName() + ": " + name);
					return new ExpressionInvalid(position, environment);
				}
			} else {
				return result;
			}
		} else {
			return member.instance(position, environment, value);
		}
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		ZenNativeMember member = staticMembers.get(name);
		if (member == null) {
			for (ZenTypeNative type : implementing) {
				if (type.staticMembers.containsKey(name)) {
					member = type.staticMembers.get(name);
					break;
				}
			}
		}
		
		if (member == null) {
			MemberStatic result = new MemberStatic(position, environment, this, name);
			staticMemberExpansion(result);
			if (result.isEmpty()) {
				environment.error(position, getName() + " value has no static member named " + name);
				return new ExpressionInvalid(position, environment);
			} else {
				return result;
			}
		} else {
			return member.instance(position, environment);
		}
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		switch (iteratorType) {
			case ITERATOR_NONE:
				break;
			case ITERATOR_ITERABLE:
				if (numValues == 1) {
					return new IteratorIterable(iteratorValueType);
				} else if (numValues == 2) {
					return new IteratorList(getScope(), iteratorValueType);
				}
				break;
			case ITERATOR_MAP:
				if (numValues == 1) {
					return new IteratorMapKeys(new ZenTypeAssociative(iteratorValueType, iteratorKeyType));
				} else if (numValues == 2) {
					return new IteratorMap(new ZenTypeAssociative(iteratorValueType, iteratorKeyType));
				}
				break;
			case ITERATOR_LIST:
				if (numValues == 1) {
					// list is also iterable
					return new IteratorIterable(iteratorValueType);
				} else if (numValues == 2) {
					return new IteratorList(getScope(), iteratorValueType);
				}
				break;
		}
		return null;
	}
	
	@Override
	public void constructCastingRules(AccessScope access, ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getScope().getTypes();
		
		if (cls.getSuperclass() != null) {
			ZenType superType = types.getNativeType(null, cls.getGenericSuperclass(), capture);
			
			rules.registerCastingRule(superType, new CastingRuleNone(this, superType));
			
			superType.constructCastingRules(access, rules, followCasters);
		}
		
		for (java.lang.reflect.Type iface : cls.getGenericInterfaces()) {
			ZenType ifaceType = types.getNativeType(null, iface, capture);
			
			rules.registerCastingRule(ifaceType, new CastingRuleNone(this, ifaceType));
			
			ifaceType.constructCastingRules(access, rules, followCasters);
		}
		
		if (followCasters) {
			for (ZenNativeCaster caster : casters) {
				// TODO: implement
			}
			
			constructExpansionCastingRules(access, rules);
		}
		
		rules.registerCastingRule(types.BOOL, new CastingNotNull(this, types.BOOL));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(JavaMethod.getStatic(
				getAnyClassName(),
				"valueOf",
				types.ANY,
				this)));
	}

	@Override
	public boolean canCastExplicit(AccessScope access, ZenType type) {
		return type == this
				|| canCastImplicit(access, type)
				|| type.canCastExplicit(access, this);
	}
	
	@Override
	public Class toJavaClass() {
		return cls;
	}

	@Override
	public Type toASMType() {
		return Type.getType(cls);
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return signature(cls);
	}

	@Override
	public boolean isNullable() {
		return true;
	}
	
	@Override
	public Expression operator(CodePosition position, IScopeMethod scope, OperatorType operator, Expression... values)
	{
		for (ZenNativeOperator nativeOperator : operators) {
			if (nativeOperator.getOperator() == operator) {
				return new ExpressionCallVirtual(position, scope, nativeOperator.getMethod(), values[0], Arrays.copyOfRange(values, 1, values.length));
			}
		}
		
		scope.error(position, "operator not supported");
		return new ExpressionInvalid(position, scope);
	}
	
	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		if (type == CompareType.EQ || type == CompareType.NE) {
			for (ZenNativeOperator binaryOperator : operators) {
				if (binaryOperator.getOperator() == OperatorType.EQUALS) {
					Expression result = new ExpressionCallVirtual(position, environment, binaryOperator.getMethod(), left, right);
					if (type == CompareType.EQ) {
						return result;
					} else {
						return new ExpressionArithmeticUnary(position, environment, OperatorType.NOT, result);
					}
				}
			}
		}
		
		return new ExpressionCompareGeneric(position, environment, operator(position, environment, OperatorType.COMPARE, left, right), type);
	}
	
	@Override
	public List<IMethod> getMethods() {
		IMethod functionalInterface = getFunction();
		
		if (functionalInterface == null) {
			return Collections.EMPTY_LIST;
		} else {
			return Collections.singletonList(getFunction());
		}
	}
	
	@Override
	public IMethod getFunction() {
		return functionalInterface;
	}
	
	private boolean hasBinary(ZenType type, OperatorType operator) {
		for (ZenNativeOperator binaryOperator : operators) {
			if (binaryOperator.getOperator() == operator) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String getName() {
		return classPkg + '.' + className;
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionNull(position, environment);
	}
	
	@Override
	public ZenType nullable() {
		return this;
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
	
	private void addSubtypes(Queue<ZenTypeNative> todo, TypeRegistry types) {
		while (!todo.isEmpty()) {
			ZenTypeNative current = todo.poll();
			if (current.cls.getGenericSuperclass() != null) {
				ZenType type = types.getNativeType(null, current.cls.getGenericSuperclass(), capture);
				if (type instanceof ZenTypeNative) {
					todo.offer((ZenTypeNative) type);
					implementing.add((ZenTypeNative) type);
				}
			}
			for (java.lang.reflect.Type iface : current.cls.getGenericInterfaces()) {
				ZenType type = types.getNativeType(null, iface, capture);
				if (type instanceof ZenTypeNative) {
					todo.offer((ZenTypeNative) type);
					implementing.add((ZenTypeNative) type);
				}
			}
		}
	}
	
	@Override
	protected void memberExpansion(MemberVirtual member)
	{
		super.memberExpansion(member);
		
		for (ZenTypeNative implementingType : implementing) {
			implementingType.memberExpansion(member);
		}
	}
	
	private class AnyNativeDefinition implements IAnyDefinition {
		private final IScopeGlobal environment;
		
		public AnyNativeDefinition(IScopeGlobal environment) {
			this.environment = environment;
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PRIVATE, "value", "F", null, null);
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(F)" + signature(IAny.class), null, null);
			valueOf.start();
			valueOf.newObject(anyName2);
			valueOf.dup();
			valueOf.load(toASMType(), 0);
			valueOf.construct(anyName2, "L" + cls.getName() + ";");
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(L" + cls.getName() + ";)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(Type.FLOAT_TYPE, 1);
			constructor.putField(anyName2, "value", "L" + cls.getName() + ";");
			constructor.returnType(Type.VOID_TYPE);
			constructor.end();
		}

		@Override
		public void defineStaticCanCastImplicit(MethodOutput output) {
			// Class
			// if (cls.isAssignableFrom(param)) return true;
			output.constant(Type.getType(cls));
			output.loadObject(1);
			output.invokeVirtual(Class.class, "isAssignableFrom", boolean.class, Class.class);
			
			Label lblNotAssignable = new Label();
			output.ifEQ(lblNotAssignable);
			
			output.iConst1();
			output.returnInt();
			
			output.label(lblNotAssignable);
			
			for (ZenNativeCaster caster : casters) {
				caster.compileAnyCanCastImplicit(ZenTypeNative.this, output, environment, 0);
			}
			
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCanCastImplicit(ZenTypeNative.this, output, environment, 0);
			}*/
			
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineStaticAs(MethodOutput output) {
			output.constant(Type.getType(cls));
			output.loadObject(1);
			output.invokeVirtual(Class.class, "isAssignableFrom", boolean.class, Class.class);
			
			Label lblNotAssignable = new Label();
			output.ifEQ(lblNotAssignable);
			
			output.loadObject(0);
			output.returnObject();
			
			output.label(lblNotAssignable);
			
			for (ZenNativeCaster caster : casters) {
				caster.compileAnyCast(ZenTypeNative.this, output, environment, 0, 1);
			}
			
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(ZenTypeNative.this, output, environment, 0, 1);
			}*/
			
			throwCastException(output, "float", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "not");
			/*for (ZenNativeOperator unaryOperator : unaryOperators) {
				if (unaryOperator.getOperator() == OperatorType.NOT) {
					
				}
			}
			
			TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				if (expansion.compileAnyUnary(output, OperatorType.NOT)) {
					
				}
			}*/
		}

		@Override
		public void defineNeg(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "negate");
		}

		@Override
		public void defineAdd(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "+");
		}

		@Override
		public void defineSub(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "-");
		}

		@Override
		public void defineCat(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "~");
		}

		@Override
		public void defineMul(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "*");
		}

		@Override
		public void defineDiv(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "/");
		}

		@Override
		public void defineMod(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "%");
		}

		@Override
		public void defineAnd(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "&");
		}

		@Override
		public void defineOr(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "|");
		}

		@Override
		public void defineXor(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "^");
		}

		@Override
		public void defineRange(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "..");
		}

		@Override
		public void defineCompareTo(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "compare");
		}

		@Override
		public void defineContains(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "in");
		}

		@Override
		public void defineMemberGet(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "member get");
		}

		@Override
		public void defineMemberSet(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "member set");
		}

		@Override
		public void defineMemberCall(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "member call");
		}

		@Override
		public void defineIndexGet(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "index get");
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "index set");
		}

		@Override
		public void defineCall(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asBool");
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asByte");
		}

		@Override
		public void defineAsShort(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asShort");
		}

		@Override
		public void defineAsInt(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asInt");
		}

		@Override
		public void defineAsLong(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asLong");
		}

		@Override
		public void defineAsFloat(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asFloat");
		}

		@Override
		public void defineAsDouble(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asDouble");
		}

		@Override
		public void defineAsString(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "asString");
		}

		@Override
		public void defineAs(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "as");
		}

		@Override
		public void defineIs(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "is");
		}

		@Override
		public void defineGetNumberType(MethodOutput output) {
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineIteratorSingle(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "iterator");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "equals");
		}

		@Override
		public void defineHashCode(MethodOutput output) {
			// TODO: implement
			throwUnsupportedException(output, getName(), "hashCode");
		}
	}
}
