package stanhebben.zenscript.type;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionStringConcat;
import stanhebben.zenscript.expression.ExpressionStringContains;
import stanhebben.zenscript.expression.ExpressionStringIndex;
import org.openzen.zencode.java.method.JavaMethod;
import stanhebben.zenscript.util.AnyClassWriter;
import static stanhebben.zenscript.util.AnyClassWriter.throwCastException;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableStaticMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

public class ZenTypeString extends ZenTypePrimitive {
	private static final String ANY_NAME = "any/AnyString";
	private static final String ANY_NAME_2 = "any.AnyString";
	
	private final Type type = Type.getType(String.class);
	
	public ZenTypeString(IScopeGlobal environment) {
		super(environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}

	@Override
	public void constructCastingRules(AccessScope access, ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getScope().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.BOOL, new CastingRuleStaticMethod(methods.PARSE_BOOL));
		rules.registerCastingRule(types.BOOLOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_BOOL_OBJECT));
		rules.registerCastingRule(types.BYTE, new CastingRuleStaticMethod(methods.PARSE_BYTE));
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_BYTE_OBJECT));
		rules.registerCastingRule(types.SHORT, new CastingRuleStaticMethod(methods.PARSE_SHORT));
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_SHORT_OBJECT));
		rules.registerCastingRule(types.INT, new CastingRuleStaticMethod(methods.PARSE_INT));
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_INT_OBJECT));
		rules.registerCastingRule(types.LONG, new CastingRuleStaticMethod(methods.PARSE_LONG));
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_LONG_OBJECT));
		rules.registerCastingRule(types.FLOAT, new CastingRuleStaticMethod(methods.PARSE_FLOAT));
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_FLOAT_OBJECT));
		rules.registerCastingRule(types.DOUBLE, new CastingRuleStaticMethod(methods.PARSE_DOUBLE));
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleNullableStaticMethod(methods.PARSE_DOUBLE_OBJECT));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(JavaMethod.getStatic(
				getAnyClassName(),
				"valueOf",
				types.ANY,
				types.STRING
		)));
		
		if (followCasters) {
			constructExpansionCastingRules(access, rules);
		}
	}

	@Override
	public Type toASMType() {
		return type;
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return "Ljava/lang/String;";
	}

	@Override
	public boolean isNullable() {
		return true;
	}
	
	@Override
	public Expression operator(
			CodePosition position,
			IScopeMethod scope,
			OperatorType operator,
			Expression... arguments)
	{
		if (operator == OperatorType.CAT || operator == OperatorType.ADD) {
			Expression left = arguments[0];
			Expression right = arguments[1];
			
			if (left instanceof ExpressionStringConcat) {
				((ExpressionStringConcat) left).add(right.cast(position, this));
				return left;
			} else {
				if (right.getType().canCastImplicit(ACCESS_GLOBAL, this)) {
					List<Expression> values = new ArrayList<Expression>();
					values.add(left);
					values.add(right.cast(position, this));
					return new ExpressionStringConcat(position, scope, values);
				} else {
					Expression expanded = expandOperator(position, scope, operator, left, right);
					if (expanded == null) {
						scope.error(position, "cannot add " + right.getType().getName() + " to a string");
						return new ExpressionInvalid(position, scope, this);
					} else {
						return expanded;
					}
				}
			}
		} else if (operator == OperatorType.INDEXGET) {
			Expression left = arguments[0];
			Expression right = arguments[1];
			
			return new ExpressionStringIndex(position, scope, left, right.cast(position, getScope().getTypes().INT));
		} else if (operator == OperatorType.CONTAINS) {
			Expression left = arguments[0];
			Expression right = arguments[1];
			
			return new ExpressionStringContains(position, scope, left, right.cast(position, getScope().getTypes().STRING));
		} else {
			Expression result = expandOperator(position, scope, operator, arguments);
			if (result == null) {
				scope.error(position, "operator not supported on strings");
				return new ExpressionInvalid(position, scope);
			} else {
				return result;
			}
		}
	}

	@Override
	public Expression compare(
			CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		if (right.getType().canCastImplicit(null, this)) {
			return new ExpressionCompareGeneric(position, environment, new ExpressionCallVirtual(
					position,
					environment,
					getScope().getTypes().getCommonMethods().METHOD_STRING_COMPARETO,
					left,
					right.cast(position, this)),
					type);
		}
		
		Expression result = expandOperator(position, environment, OperatorType.COMPARE, left, right);
		if (result == null) {
			environment.error(position, "cannot compare strings");
			return new ExpressionInvalid(position, environment, getScope().getTypes().BOOL);
		} else {
			return new ExpressionCompareGeneric(position, environment, result, type);
		}
	}

	@Override
	public Class toJavaClass() {
		return String.class;
	}

	@Override
	public String getName() {
		return "string";
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal scope = getScope();
		
		if (!scope.containsClass(ANY_NAME_2)) {
			scope.putClass(ANY_NAME_2, new byte[0]);
			scope.putClass(ANY_NAME_2, AnyClassWriter.construct(new AnyDefinitionString(scope), ANY_NAME, type));
		}
		
		return ANY_NAME;
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
	
	private class AnyDefinitionString implements IAnyDefinition {
		private final IScopeGlobal environment;
		private final TypeRegistry types;
		
		private AnyDefinitionString(IScopeGlobal environment) {
			this.environment = environment;
			types = environment.getTypes();
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PRIVATE, "value", "Ljava/lang/String;", null, null);
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(Ljava/lang/String;)" + signature(IAny.class), null, null);
			valueOf.start();
			valueOf.newObject(ANY_NAME);
			valueOf.dup();
			valueOf.loadObject(0);
			valueOf.construct(ANY_NAME, "Ljava/lang/String;");
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(type, 1);
			constructor.putField(ANY_NAME, "value", "Ljava/lang/String;");
			constructor.returnType(Type.VOID_TYPE);
			constructor.end();
		}

		@Override
		public void defineStaticCanCastImplicit(MethodOutput output) {
			Label lblOthers = new Label();
			
			output.constant(type);
			output.loadObject(0);
			output.ifACmpNe(lblOthers);
			output.iConst1();
			output.returnInt();
			
			output.label(lblOthers);
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCanCastImplicit(types.STRING, output, environment, 0);
			}*/
			
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineStaticAs(MethodOutput output) {
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.STRING, output, environment, 0, 1);
			}*/
			
			throwCastException(output, "string", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			throwUnsupportedException(output, "string", "not");
		}

		@Override
		public void defineNeg(MethodOutput output) {
			throwUnsupportedException(output, "string", "negate");
		}

		@Override
		public void defineAdd(MethodOutput output) {
			defineCat(output);
		}

		@Override
		public void defineSub(MethodOutput output) {
			throwUnsupportedException(output, "string", "-");
		}

		@Override
		public void defineCat(MethodOutput output) {
			output.newObject(StringBuilder.class);
			output.dup();
			output.invokeSpecial(internal(StringBuilder.class), "<init>", "()V");
			getValue(output);
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, String.class);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asString", "()Ljava/lang/String;");
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, String.class);
			output.invokeVirtual(StringBuilder.class, "toString", String.class);
			output.invokeStatic(ANY_NAME, "valueOf", "(Ljava/lang/String;)" + signature(IAny.class));
			output.returnObject();
		}

		@Override
		public void defineMul(MethodOutput output) {
			throwUnsupportedException(output, "string", "*");
		}

		@Override
		public void defineDiv(MethodOutput output) {
			throwUnsupportedException(output, "string", "/");
		}

		@Override
		public void defineMod(MethodOutput output) {
			throwUnsupportedException(output, "string", "*");
		}

		@Override
		public void defineAnd(MethodOutput output) {
			throwUnsupportedException(output, "string", "&");
		}

		@Override
		public void defineOr(MethodOutput output) {
			throwUnsupportedException(output, "string", "|");
		}

		@Override
		public void defineXor(MethodOutput output) {
			throwUnsupportedException(output, "string", "^");
		}

		@Override
		public void defineRange(MethodOutput output) {
			throwUnsupportedException(output, "string", "..");
		}

		@Override
		public void defineCompareTo(MethodOutput output) {
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asString", "()Ljava/lang/String;");
			output.invokeVirtual(String.class, "compareTo", int.class, String.class);
			output.returnInt();
		}

		@Override
		public void defineContains(MethodOutput output) {
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asString", "()Ljava/lang/String;");
			output.invokeVirtual(String.class, "contains", boolean.class, CharSequence.class);
			output.returnInt();
		}

		@Override
		public void defineMemberGet(MethodOutput output) {
			// TODO
			output.aConstNull();
			output.returnObject();
		}

		@Override
		public void defineMemberSet(MethodOutput output) {
			// TODO
			output.returnType(Type.VOID_TYPE);
		}

		@Override
		public void defineMemberCall(MethodOutput output) {
			// TODO
			output.aConstNull();
			output.returnObject();
		}

		@Override
		public void defineIndexGet(MethodOutput output) {
			// return new AnyString(String.substring(other.asInt(), other.asInt() + 1))
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asInt", "()I");
			output.dup();
			output.iConst1();
			output.iAdd();
			output.invokeVirtual(String.class, "substring", String.class, int.class, int.class);
			output.invokeStatic(ANY_NAME, "valueOf", "(Ljava/lang/String;)" + signature(IAny.class));
			output.returnObject();
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			throwUnsupportedException(output, "string", "[]=");
		}

		@Override
		public void defineCall(MethodOutput output) {
			throwUnsupportedException(output, "string", "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			// return Boolean.parseBoolean(value);
			getValue(output);
			output.invokeStatic(Boolean.class, "parseBoolean", boolean.class, String.class);
			output.returnInt();
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			// return Byte.parseByte(value);
			getValue(output);
			output.invokeStatic(Byte.class, "parseByte", byte.class, String.class);
			output.returnInt();
		}

		@Override
		public void defineAsShort(MethodOutput output) {
			// return Short.parseShort(value);
			getValue(output);
			output.invokeStatic(Short.class, "parseShort", short.class, String.class);
			output.returnInt();
		}

		@Override
		public void defineAsInt(MethodOutput output) {
			// return Integer.parseInt(value);
			getValue(output);
			output.invokeStatic(Integer.class, "parseInt", int.class, String.class);
			output.returnInt();
		}

		@Override
		public void defineAsLong(MethodOutput output) {
			// return Integer.parseLong(value);
			getValue(output);
			output.invokeStatic(Long.class, "parseLong", long.class, String.class);
			output.returnType(Type.LONG_TYPE);
		}

		@Override
		public void defineAsFloat(MethodOutput output) {
			// return Float.parseFloat(value);
			getValue(output);
			output.invokeStatic(Float.class, "parseFloat", float.class, String.class);
			output.returnType(Type.FLOAT_TYPE);
		}

		@Override
		public void defineAsDouble(MethodOutput output) {
			// return Double.parseDouble(value);
			getValue(output);
			output.invokeStatic(Double.class, "parseDouble", double.class, String.class);
			output.returnType(Type.DOUBLE_TYPE);
		}

		@Override
		public void defineAsString(MethodOutput output) {
			getValue(output);
			output.returnObject();
		}

		@Override
		public void defineAs(MethodOutput output) {
			int localValue = output.local(type);
			
			getValue(output);
			output.store(type, localValue);
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.STRING, output, environment, localValue, 1);
			}*/
			
			throwCastException(output, "string", 1);
		}

		@Override
		public void defineIs(MethodOutput output) {
			Label lblEq = new Label();
			
			output.loadObject(1);
			output.constant(type);
			output.ifACmpEq(lblEq);
			output.iConst0();
			output.returnInt();
			output.label(lblEq);
			output.iConst1();
			output.returnInt();
		}

		@Override
		public void defineGetNumberType(MethodOutput output) {
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineIteratorSingle(MethodOutput output) {
			throwUnsupportedException(output, "string", "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			throwUnsupportedException(output, "string", "iterator");
		}
		
		private void getValue(MethodOutput output) {
			output.loadObject(0);
			output.getField(ANY_NAME, "value", "Ljava/lang/String;");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asString", "()Ljava/lang/String;");
			output.invokeVirtual(String.class, "equals", boolean.class, Object.class);
			output.returnInt();
		}

		@Override
		public void defineHashCode(MethodOutput output) {
			getValue(output);
			output.invokeVirtual(String.class, "hashCode", int.class);
			output.returnInt();
		}
	}
}
