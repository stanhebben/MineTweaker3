package stanhebben.zenscript.type;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import stanhebben.zenscript.TypeExpansion;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArithmeticBinary;
import stanhebben.zenscript.expression.ExpressionArithmeticCompare;
import stanhebben.zenscript.expression.ExpressionArithmeticUnary;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.method.JavaMethod;
import stanhebben.zenscript.util.AnyClassWriter;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import stanhebben.zenscript.util.MethodOutput;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.runtime.IAny;
import static org.openzen.zencode.runtime.IAny.NUM_BYTE;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2D;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2F;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2L;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNone;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

public class ZenTypeByte extends ZenType {
	private static final String ANY_NAME = "any/AnyByte";
	private static final String ANY_NAME_2 = "any.AnyByte";
	
	public ZenTypeByte(IScopeGlobal environment) {
		super(environment);
	}
	
	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}

	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleStaticMethod(methods.BYTE_VALUEOF));
		rules.registerCastingRule(types.SHORT, new CastingRuleNone(types.BYTE, types.SHORT));
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleStaticMethod(methods.SHORT_VALUEOF));
		rules.registerCastingRule(types.INT, new CastingRuleNone(types.BYTE, types.INT));
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleStaticMethod(methods.INT_VALUEOF));
		rules.registerCastingRule(types.LONG, new CastingRuleI2L(null, types));
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleStaticMethod(methods.LONG_VALUEOF, new CastingRuleI2L(null, types)));
		rules.registerCastingRule(types.FLOAT, new CastingRuleI2F(null, types));
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleStaticMethod(methods.FLOAT_VALUEOF, new CastingRuleI2F(null, types)));
		rules.registerCastingRule(types.DOUBLE, new CastingRuleI2D(null, types));
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleStaticMethod(methods.DOUBLE_VALUEOF, new CastingRuleI2D(null, types)));
		
		rules.registerCastingRule(types.STRING, new CastingRuleStaticMethod(methods.BYTE_TOSTRING_STATIC));
		rules.registerCastingRule(types.ANY, new CastingRuleStaticMethod(JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, types.BYTE)));
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}
	
	@Override
	public Class toJavaClass() {
		return byte.class;
	}

	@Override
	public Type toASMType() {
		return Type.BYTE_TYPE;
	}

	@Override
	public int getNumberType() {
		return NUM_BYTE;
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		IPartialExpression result = memberExpansion(position, environment, value.eval(), name);
		if (result == null) {
			environment.error(position, "bool value has no member named " + name);
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		IPartialExpression result = staticMemberExpansion(position, environment, name);
		if (result == null) {
			environment.error(position, "bool value has no static member named " + name);
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public String getSignature() {
		return "B";
	}

	@Override
	public boolean isNullable() {
		return false;
	}
	
	@Override
	public Expression unary(CodePosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		return new ExpressionArithmeticUnary(position, environment, operator, value);
	}

	@Override
	public Expression binary(CodePosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		if (operator == OperatorType.CAT) {
			TypeRegistry types = environment.getTypes();
			
			return types.STRING.binary(
					position,
					environment,
					left.cast(position, types.STRING),
					right.cast(position, types.STRING),
					OperatorType.CAT);
		}
		
		return new ExpressionArithmeticBinary(position, environment, operator, left, right.cast(position, this));
	}
	
	@Override
	public Expression trinary(CodePosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		environment.error(position, "byte doesn't support this operation");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return new ExpressionArithmeticCompare(position, environment, type, left, right.cast(position, this));
	}

	/*@Override
	public Expression call(
			CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		environment.error(position, "cannot call a byte value");
		return new ExpressionInvalid(position, environment);
	}*/

	@Override
	public String getName() {
		return "byte";
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal environment = getEnvironment();
		
		if (!environment.containsClass(ANY_NAME_2)) {
			environment.putClass(ANY_NAME_2, new byte[0]);
			environment.putClass(ANY_NAME_2, AnyClassWriter.construct(new AnyDefinitionByte(environment), ANY_NAME, Type.BYTE_TYPE));
		}
		
		return ANY_NAME;
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionInt(position, environment, 0, environment.getTypes().BYTE);
	}
	
	@Override
	public ZenType nullable() {
		return getEnvironment().getTypes().BYTEOBJECT;
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
	
	private class AnyDefinitionByte implements IAnyDefinition {
		private final IScopeGlobal environment;
		private final TypeRegistry types;
		
		public AnyDefinitionByte(IScopeGlobal environment) {
			this.environment = environment;
			types = environment.getTypes();
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PRIVATE, "value", "B", null, null);
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(B)" + signature(IAny.class), null, null);
			valueOf.start();
			valueOf.newObject(ANY_NAME);
			valueOf.dup();
			valueOf.load(Type.BYTE_TYPE, 0);
			valueOf.construct(ANY_NAME, "B");
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(B)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(Type.BYTE_TYPE, 1);
			constructor.putField(ANY_NAME, "value", "B");
			constructor.returnType(Type.VOID_TYPE);
			constructor.end();
		}
		
		@Override
		public void defineStaticCanCastImplicit(MethodOutput output) {
			Label lblCan = new Label();
			
			output.constant(Type.BYTE_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			output.constant(Type.SHORT_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			output.constant(Type.INT_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			output.constant(Type.LONG_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			output.constant(Type.FLOAT_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			output.constant(Type.DOUBLE_TYPE);
			output.loadObject(0);
			output.ifACmpEq(lblCan);
			
			TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCanCastImplicit(types.BYTE, output, environment, 0);
			}
			
			output.iConst0();
			output.returnInt();
			
			output.label(lblCan);
			output.iConst1();
			output.returnInt();
		}

		@Override
		public void defineStaticAs(MethodOutput output) {
			TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.BYTE, output, environment, 0, 1);
			}
			
			AnyClassWriter.throwCastException(output, "byte", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.iNot();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineNeg(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.iNeg();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineAdd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iAdd();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineSub(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iSub();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
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
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, int.class);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asString", String.class);
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, String.class);
			output.invokeVirtual(StringBuilder.class, "toString", String.class);
			output.invokeStatic(types.STRING.getAnyClassName(), "valueOf", "(Ljava/lang/String;)" + signature(IAny.class));
			output.returnObject();
		}

		@Override
		public void defineMul(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iMul();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineDiv(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iDiv();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineMod(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iRem();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineAnd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iAnd();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineOr(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iOr();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineXor(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iXor();
			output.invokeSpecial(ANY_NAME, "<init>", "(B)V");
			output.returnObject();
		}

		@Override
		public void defineRange(MethodOutput output) {
			// TODO
			output.aConstNull();
			output.returnObject();
		}

		@Override
		public void defineCompareTo(MethodOutput output) {
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.iSub();
			output.returnInt();
		}

		@Override
		public void defineContains(MethodOutput output) {
			throwUnsupportedException(output, "byte", "in");
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
			throwUnsupportedException(output, "byte", "get []");
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			throwUnsupportedException(output, "byte", "set []");
		}

		@Override
		public void defineCall(MethodOutput output) {
			throwUnsupportedException(output, "byte", "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			AnyClassWriter.throwCastException(output, ANY_NAME, "bool");
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			getValue(output);
			output.returnType(Type.BYTE_TYPE);
		}

		@Override
		public void defineAsShort(MethodOutput output) {
			getValue(output);
			output.returnType(Type.SHORT_TYPE);
		}

		@Override
		public void defineAsInt(MethodOutput output) {
			getValue(output);
			output.returnType(Type.INT_TYPE);
		}

		@Override
		public void defineAsLong(MethodOutput output) {
			getValue(output);
			output.i2l();
			output.returnType(Type.LONG_TYPE);
		}

		@Override
		public void defineAsFloat(MethodOutput output) {
			getValue(output);
			output.i2f();
			output.returnType(Type.FLOAT_TYPE);
		}

		@Override
		public void defineAsDouble(MethodOutput output) {
			getValue(output);
			output.i2d();
			output.returnType(Type.DOUBLE_TYPE);
		}

		@Override
		public void defineAsString(MethodOutput output) {
			getValue(output);
			output.invokeStatic(Byte.class, "toString", String.class, byte.class);
			output.returnObject();
		}

		@Override
		public void defineAs(MethodOutput output) {
			int localValue = output.local(Type.BYTE_TYPE);
			
			getValue(output);
			output.store(Type.BYTE_TYPE, localValue);
			TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.BYTE, output, environment, localValue, 1);
			}
			
			AnyClassWriter.throwCastException(output, "byte", 1);
		}

		@Override
		public void defineIs(MethodOutput output) {
			Label lblEq = new Label();
			
			output.loadObject(1);
			output.constant(Type.BYTE_TYPE);
			output.ifACmpEq(lblEq);
			output.iConst0();
			output.returnInt();
			output.label(lblEq);
			output.iConst1();
			output.returnInt();
		}
		
		@Override
		public void defineGetNumberType(MethodOutput output) {
			output.constant(IAny.NUM_BYTE);
			output.returnInt();
		}

		@Override
		public void defineIteratorSingle(MethodOutput output) {
			throwUnsupportedException(output, "byte", "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			throwUnsupportedException(output, "byte", "iterator");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			Label lblNope = new Label();
			
			output.loadObject(1);
			output.instanceOf(IAny.NAME);
			output.ifEQ(lblNope);
			
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asByte", byte.class);
			output.ifICmpNE(lblNope);
			
			output.iConst1();
			output.returnInt();
			
			output.label(lblNope);
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineHashCode(MethodOutput output) {
			getValue(output);
			output.returnInt();
		}
		
		private void getValue(MethodOutput output) {
			output.loadObject(0);
			output.getField(ANY_NAME, "value", "B");
		}
	}
}
