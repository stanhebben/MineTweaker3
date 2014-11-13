package stanhebben.zenscript.type;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArithmeticBinary;
import stanhebben.zenscript.expression.ExpressionArithmeticCompare;
import stanhebben.zenscript.expression.ExpressionArithmeticUnary;
import stanhebben.zenscript.expression.ExpressionBool;
import stanhebben.zenscript.expression.ExpressionInvalid;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.java.method.JavaMethod;
import stanhebben.zenscript.util.AnyClassWriter;
import static stanhebben.zenscript.util.AnyClassWriter.throwCastException;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.any.AnyClass;
import org.openzen.zencode.symbolic.member.MethodMember;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.scope.ScopeMethod;

public class ZenTypeBool extends ZenType {
	private static final String ANY_NAME = "any/AnyBool";
	private static final String ANY_NAME_2 = "any.AnyBool";
	private static final String ANY_NAME_DESC = "Lany/AnyBool;";
	
	public ZenTypeBool(IScopeGlobal environment) {
		super(environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}
	
	@Override
	public void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getScope().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.STRING, new CastingRuleStaticMethod(methods.BOOL_TOSTRING_STATIC));
		rules.registerCastingRule(types.BOOLOBJECT, new CastingRuleStaticMethod(methods.BOOL_VALUEOF));
		rules.registerCastingRule(types.ANY, new CastingRuleStaticMethod(JavaMethod.getStatic(
				getAnyClassName(), "valueOf", types.ANY, types.BOOL
		)));
		
		if (followCasters) {
			constructExpansionCastingRules(accessScope, rules);
		}
	}
	
	@Override
	public Class toJavaClass() {
		return boolean.class;
	}

	@Override
	public Type toASMType() {
		return Type.BOOLEAN_TYPE;
	}

	@Override
	public int getNumberType() {
		return 0;
	}
	
	@Override
	public Expression operator(CodePosition position, IScopeMethod environment, OperatorType operator, Expression... values) {
		TypeRegistry types = getScope().getTypes();
		
		switch (operator) {
			case NOT:
			case INVERT:
				return new ExpressionArithmeticUnary(position, environment, operator, values[0]);
			case CAT:
				return types.STRING.operator(
						position,
						environment,
						OperatorType.CAT,
						values[0].cast(position, types.STRING),
						values[1].cast(position, types.STRING));
			case AND:
			case OR:
			case XOR:
				return new ExpressionArithmeticBinary(position, environment, operator, values[0], values[1].cast(position, this));
			default:
				environment.error(position, "unsupported bool operator: " + operator.getOperatorString());
				return new ExpressionInvalid(position, environment);
		}
	}
	
	@Override
	public Expression compare(
			CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		if (type == CompareType.EQ || type == CompareType.NE) {
			return new ExpressionArithmeticCompare(position, environment, type, left, right);
		} else {
			environment.error(position, "such comparison not supported on a bool");
			return new ExpressionInvalid(position, environment);
		}
	}
	
	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		MemberVirtual result = new MemberVirtual(position, environment, value.eval(), name);
		memberExpansion(result);
		if (result.isEmpty()) {
			environment.error(position, "bool value has no members");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		environment.error(position, "bool type has no static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public String getSignature() {
		return "Z";
	}

	@Override
	public boolean isNullable() {
		return false;
	}
	
	@Override
	public String getName() {
		return "bool";
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal scope = getScope();
		
		if (!scope.containsClass(ANY_NAME_2)) {
			scope.putClass(ANY_NAME_2, new byte[0]);
			scope.putClass(ANY_NAME_2, AnyClassWriter.construct(new AnyDefinitionBool(scope), ANY_NAME, Type.BOOLEAN_TYPE));
		}
		
		return ANY_NAME;
	}
	
	private SymbolicClass constructAnyClass()
	{
		AnyClassBool anyClass = new AnyClassBool();
		return anyClass.generate();
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionBool(position, environment, false);
	}
	
	@Override
	public ZenType nullable() {
		return getScope().getTypes().BOOLOBJECT;
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
	
	private class AnyClassBool extends AnyClass
	{
		public AnyClassBool()
		{
			super(ZenTypeBool.this);
		}
		
		@Override
		protected void implementAnyNot(AnyClassMembers members, MethodMember method, ScopeMethod scope)
		{
			Expression value = members.makeValueExpression(scope);
			Expression not = new ExpressionArithmeticUnary(members.position, scope, OperatorType.NOT, value);
			method.addStatement(not.asReturnStatement());
		}

		@Override
		protected void implementAnyAdd(AnyClassMembers members, MethodMember method, ScopeMethod scope)
		{
			implementAnyAddExpansions(members, method, scope);
		}
	}
	
	private class AnyDefinitionBool implements IAnyDefinition {
		private final IScopeGlobal environment;
		private final TypeRegistry types;
		
		public AnyDefinitionBool(IScopeGlobal environment) {
			this.environment = environment;
			types = environment.getTypes();
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, "TRUE", ANY_NAME_DESC, null, null);
			output.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, "FALSE", ANY_NAME_DESC, null, null);
			
			output.visitField(Opcodes.ACC_PRIVATE, "value", "Z", null, null);
			
			MethodOutput clinit = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
			clinit.start();
			clinit.newObject(ANY_NAME);
			clinit.dup();
			clinit.iConst0();
			clinit.invokeSpecial(ANY_NAME, "<init>", "(Z)V");
			clinit.putStaticField(ANY_NAME, "FALSE", ANY_NAME_DESC);
			clinit.newObject(ANY_NAME);
			clinit.dup();
			clinit.iConst1();
			clinit.invokeSpecial(ANY_NAME, "<init>", "(Z)V");
			clinit.putStaticField(ANY_NAME, "TRUE", ANY_NAME_DESC);
			clinit.returnType(Type.VOID_TYPE);
			clinit.end();
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(Z)" + signature(IAny.class), null, null);
			Label lblFalse = new Label();
			valueOf.start();
			valueOf.load(Type.BOOLEAN_TYPE, 0);
			valueOf.ifEQ(lblFalse);
			valueOf.getStaticField(ANY_NAME, "TRUE", ANY_NAME_DESC);
			valueOf.returnObject();
			valueOf.label(lblFalse);
			valueOf.getStaticField(ANY_NAME, "FALSE", ANY_NAME_DESC);
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(Z)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(Type.BOOLEAN_TYPE, 1);
			constructor.putField(ANY_NAME, "value", "Z");
			constructor.returnType(Type.VOID_TYPE);
			constructor.end();
		}
		
		@Override
		public void defineStaticCanCastImplicit(MethodOutput output) {
			Label lblCan = new Label();
			
			output.constant(Type.BOOLEAN_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			/*TypeExpansion expansion = environment.getTypes().getExpansion(ACCESS_GLOBAL, getName());
			if (expansion != null) {
				expansion.compileAnyCanCastImplicit(types.BOOL, output, environment, 0);
			}*/
			
			output.iConst0();
			output.returnInt();
			
			output.label(lblCan);
			output.iConst1();
			output.returnInt();
		}

		@Override
		public void defineStaticAs(MethodOutput output) {
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.BOOL, output, environment, 0, 1);
			}*/
			
			throwCastException(output, "bool", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			getValue(output);
			output.iNot();
			valueOf(output);
			output.returnObject();
		}

		@Override
		public void defineNeg(MethodOutput output) {
			throwUnsupportedException(output, "bool", "negate");
		}

		@Override
		public void defineAdd(MethodOutput output) {
			throwUnsupportedException(output, "bool", "+");
		}

		@Override
		public void defineSub(MethodOutput output) {
			throwUnsupportedException(output, "bool", "-");
		}

		@Override
		public void defineCat(MethodOutput output) {
			// StringBuilder builder = new StringBuilder();
			// builder.append(value);
			// builder.append(other.asString());
			// return new AnyString(builder.toString());
			output.newObject(StringBuilder.class);
			output.dup();
			output.invokeSpecial(internal(StringBuilder.class), "<init>", "()V");
			getValue(output);
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, boolean.class);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asString", String.class);
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, String.class);
			output.invokeVirtual(StringBuilder.class, "toString", String.class);
			output.invokeStatic(types.STRING.getAnyClassName(), "valueOf", "(Ljava/lang/String;)" + signature(IAny.class));
			output.returnObject();
		}

		@Override
		public void defineMul(MethodOutput output) {
			throwUnsupportedException(output, "bool", "*");
		}

		@Override
		public void defineDiv(MethodOutput output) {
			throwUnsupportedException(output, "bool", "/");
		}

		@Override
		public void defineMod(MethodOutput output) {
			throwUnsupportedException(output, "bool", "%");
		}

		@Override
		public void defineAnd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asBool", boolean.class);
			output.iAnd();
			valueOf(output);
			output.returnObject();
		}

		@Override
		public void defineOr(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asBool", boolean.class);
			output.iOr();
			valueOf(output);
			output.returnObject();
		}

		@Override
		public void defineXor(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asBool", boolean.class);
			output.iXor();
			valueOf(output);
			output.returnObject();
		}

		@Override
		public void defineRange(MethodOutput output) {
			throwUnsupportedException(output, "bool", "range");
		}

		@Override
		public void defineCompareTo(MethodOutput output) {
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asBool", boolean.class);
			output.iSub();
			output.returnInt();
		}

		@Override
		public void defineContains(MethodOutput output) {
			throwUnsupportedException(output, "bool", "in");
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
			throwUnsupportedException(output, "bool", "get []");
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			throwUnsupportedException(output, "bool", "set []");
		}

		@Override
		public void defineCall(MethodOutput output) {
			throwUnsupportedException(output, "bool", "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			throwCastException(output, ANY_NAME, "bool");
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			throwCastException(output, "bool", "byte");
		}

		@Override
		public void defineAsShort(MethodOutput output) {
			throwCastException(output, "bool", "short");
		}

		@Override
		public void defineAsInt(MethodOutput output) {
			throwCastException(output, "bool", "int");
		}

		@Override
		public void defineAsLong(MethodOutput output) {
			throwCastException(output, "bool", "long");
		}

		@Override
		public void defineAsFloat(MethodOutput output) {
			throwCastException(output, "bool", "float");
		}

		@Override
		public void defineAsDouble(MethodOutput output) {
			throwCastException(output, "bool", "double");
		}

		@Override
		public void defineAsString(MethodOutput output) {
			getValue(output);
			output.invokeStatic(Boolean.class, "toString", String.class, boolean.class);
			output.returnObject();
		}

		@Override
		public void defineAs(MethodOutput output) {
			int localValue = output.local(Type.BYTE_TYPE);
			
			getValue(output);
			output.store(Type.BYTE_TYPE, localValue);
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.BYTE, output, environment, localValue, 1);
			}*/
			
			throwCastException(output, "bool", 1);
		}

		@Override
		public void defineIs(MethodOutput output) {
			Label lblEq = new Label();
			
			output.loadObject(1);
			output.constant(Type.BOOLEAN_TYPE);
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
			throwUnsupportedException(output, "bool", "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			throwUnsupportedException(output, "bool", "iterator");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			Label lblEqual = new Label();
			output.loadObject(0);
			output.loadObject(1);
			
			output.ifACmpEq(lblEqual);
			
			output.iConst0();
			output.returnInt();
			
			output.label(lblEqual);
			output.iConst1();
			output.returnInt();
		}

		@Override
		public void defineHashCode(MethodOutput output) {
			output.invokeSpecial("java/lang/Object", "hashCode", "()I");
		}
		
		private void getValue(MethodOutput output) {
			output.loadObject(0);
			output.getField(ANY_NAME, "value", "Z");
		}
		
		private void valueOf(MethodOutput output) {
			output.invokeStatic(ANY_NAME, "valueOf", "(Z)" + signature(IAny.class));
		}
	}
}
