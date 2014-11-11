package stanhebben.zenscript.type;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionFloat;
import org.openzen.zencode.symbolic.method.JavaMethod;
import stanhebben.zenscript.util.AnyClassWriter;
import static stanhebben.zenscript.util.AnyClassWriter.throwCastException;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.runtime.IAny;
import static org.openzen.zencode.runtime.IAny.NUM_DOUBLE;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleD2F;
import org.openzen.zencode.symbolic.type.casting.CastingRuleD2I;
import org.openzen.zencode.symbolic.type.casting.CastingRuleD2L;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2B;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2S;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

public class ZenTypeDouble extends ZenTypeArithmetic {
	private static final String ANY_NAME = "any/AnyDouble";
	private static final String ANY_NAME_2 = "any.AnyDouble";
	
	public ZenTypeDouble(IScopeGlobal environment) {
		super(environment);
	}

	@Override
	public void constructCastingRules(AccessScope access, ICastingRuleDelegate rules, boolean followCasters)
	{
		TypeRegistry types = getScope().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.BYTE, new CastingRuleI2B(new CastingRuleD2I(null, types), types));
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleStaticMethod(methods.BYTE_VALUEOF, new CastingRuleI2B(new CastingRuleD2I(null, types), types)));
		rules.registerCastingRule(types.SHORT, new CastingRuleI2S(new CastingRuleD2I(null, types), types));
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleStaticMethod(methods.SHORT_VALUEOF, new CastingRuleI2S(new CastingRuleD2I(null, types), types)));
		rules.registerCastingRule(types.INT, new CastingRuleD2I(null, types));
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleStaticMethod(methods.INT_VALUEOF, new CastingRuleD2I(null, types)));
		rules.registerCastingRule(types.LONG, new CastingRuleD2L(null, types));
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleStaticMethod(methods.LONG_VALUEOF, new CastingRuleD2L(null, types)));
		rules.registerCastingRule(types.FLOAT, new CastingRuleD2F(null, types));
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleStaticMethod(methods.FLOAT_VALUEOF, new CastingRuleD2F(null, types)));
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleStaticMethod(methods.DOUBLE_VALUEOF));
		
		rules.registerCastingRule(types.STRING, new CastingRuleStaticMethod(methods.DOUBLE_TOSTRING_STATIC));
		rules.registerCastingRule(types.ANY, new CastingRuleStaticMethod(JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, types.DOUBLE)));
		
		if (followCasters) {
			constructExpansionCastingRules(access, rules);
		}
	}

	@Override
	public Type toASMType() {
		return Type.DOUBLE_TYPE;
	}
	
	@Override
	public Class toJavaClass() {
		return double.class;
	}

	@Override
	public int getNumberType() {
		return NUM_DOUBLE;
	}
	
	@Override
	public String getSignature() {
		return "D";
	}
	
	@Override
	public String getName() {
		return "double";
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal environment = getScope();
		
		if (!environment.containsClass(ANY_NAME_2)) {
			environment.putClass(ANY_NAME_2, new byte[0]);
			environment.putClass(ANY_NAME_2, AnyClassWriter.construct(new AnyDefinitionDouble(environment), ANY_NAME, Type.DOUBLE_TYPE));
		}
		
		return ANY_NAME;
	}
	
	@Override
	public boolean isLarge() {
		return true;
	}
	
	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionFloat(position, environment, 0.0, environment.getTypes().DOUBLE);
	}
	
	@Override
	public ZenType nullable() {
		return getScope().getTypes().DOUBLEOBJECT;
	}
	
	private class AnyDefinitionDouble implements IAnyDefinition {
		private final IScopeGlobal environment;
		private final TypeRegistry types;
		
		public AnyDefinitionDouble(IScopeGlobal environment) {
			this.environment = environment;
			types = environment.getTypes();
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PRIVATE, "value", "D", null, null);
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(D)" + signature(IAny.class), null, null);
			valueOf.start();
			valueOf.newObject(ANY_NAME);
			valueOf.dup();
			valueOf.load(Type.DOUBLE_TYPE, 0);
			valueOf.construct(ANY_NAME, "D");
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(D)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(Type.DOUBLE_TYPE, 1);
			constructor.putField(ANY_NAME, "value", "D");
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
			
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCanCastImplicit(types.FLOAT, output, environment, 0);
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
				expansion.compileAnyCast(types.DOUBLE, output, environment, 0, 1);
			}*/
			
			throwCastException(output, "double", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			throwUnsupportedException(output, "double", "not");
		}

		@Override
		public void defineNeg(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.dNeg();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
			output.returnObject();
		}

		@Override
		public void defineAdd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dAdd();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
			output.returnObject();
		}

		@Override
		public void defineSub(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dSub();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
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
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, double.class);
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
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dMul();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
			output.returnObject();
		}

		@Override
		public void defineDiv(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dDiv();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
			output.returnObject();
		}

		@Override
		public void defineMod(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dRem();
			output.invokeSpecial(ANY_NAME, "<init>", "(D)V");
			output.returnObject();
		}

		@Override
		public void defineAnd(MethodOutput output) {
			throwUnsupportedException(output, "double", "and");
		}

		@Override
		public void defineOr(MethodOutput output) {
			throwUnsupportedException(output, "double", "or");
		}

		@Override
		public void defineXor(MethodOutput output) {
			throwUnsupportedException(output, "double", "xor");
		}

		@Override
		public void defineRange(MethodOutput output) {
			throwUnsupportedException(output, "double", "range");
		}

		@Override
		public void defineCompareTo(MethodOutput output) {
			// return Double.compare(x, y)
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.invokeStatic(Float.class, "compare", int.class, double.class, double.class);
			output.returnInt();
		}

		@Override
		public void defineContains(MethodOutput output) {
			throwUnsupportedException(output, "double", "in");
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
			throwUnsupportedException(output, "double", "get []");
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			throwUnsupportedException(output, "double", "set []");
		}

		@Override
		public void defineCall(MethodOutput output) {
			throwUnsupportedException(output, "double", "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			throwCastException(output, ANY_NAME, "bool");
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			getValue(output);
			output.d2i();
			output.i2b();
			output.returnType(Type.BYTE_TYPE);
		}

		@Override
		public void defineAsShort(MethodOutput output) {
			getValue(output);
			output.d2i();
			output.i2s();
			output.returnType(Type.SHORT_TYPE);
		}

		@Override
		public void defineAsInt(MethodOutput output) {
			getValue(output);
			output.d2i();
			output.returnType(Type.INT_TYPE);
		}

		@Override
		public void defineAsLong(MethodOutput output) {
			getValue(output);
			output.d2l();
			output.returnType(Type.LONG_TYPE);
		}

		@Override
		public void defineAsFloat(MethodOutput output) {
			getValue(output);
			output.d2f();
			output.returnType(Type.FLOAT_TYPE);
		}

		@Override
		public void defineAsDouble(MethodOutput output) {
			getValue(output);
			output.returnType(Type.DOUBLE_TYPE);
		}

		@Override
		public void defineAsString(MethodOutput output) {
			getValue(output);
			output.invokeStatic(Double.class, "toString", String.class, double.class);
			output.returnObject();
		}

		@Override
		public void defineAs(MethodOutput output) {
			int localValue = output.local(Type.DOUBLE_TYPE);
			
			getValue(output);
			output.store(Type.DOUBLE_TYPE, localValue);
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.DOUBLE, output, environment, localValue, 1);
			}*/
			
			throwCastException(output, "double", 1);
		}

		@Override
		public void defineIs(MethodOutput output) {
			Label lblEq = new Label();
			
			output.loadObject(1);
			output.constant(Type.DOUBLE_TYPE);
			output.ifACmpEq(lblEq);
			output.iConst0();
			output.returnInt();
			output.label(lblEq);
			output.iConst1();
			output.returnInt();
		}
		
		@Override
		public void defineGetNumberType(MethodOutput output) {
			output.constant(IAny.NUM_DOUBLE);
			output.returnInt();
		}

		@Override
		public void defineIteratorSingle(MethodOutput output) {
			throwUnsupportedException(output, "double", "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			throwUnsupportedException(output, "double", "iterator");
		}
		
		private void getValue(MethodOutput output) {
			output.loadObject(0);
			output.getField(ANY_NAME, "value", "D");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			Label lblNope = new Label();
			
			output.loadObject(1);
			output.instanceOf(IAny.NAME);
			output.ifEQ(lblNope);
			
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.class, "asDouble", double.class);
			output.dCmp();
			output.ifNE(lblNope);
			
			output.iConst1();
			output.returnInt();
			
			output.label(lblNope);
			output.iConst0();
			output.returnInt();
		}

		@Override
		public void defineHashCode(MethodOutput output) {
			// long bits = Double.doubleToLongBits(value);
			// return (int) (bits ^ bits >> 32)
			int bits = output.local(Type.LONG_TYPE);
			output.invokeStatic(Double.class, "doubleToLongBits", double.class, long.class);
			output.store(Type.LONG_TYPE, bits);
			
			output.load(Type.LONG_TYPE, bits);
			output.l2i();
			output.load(Type.LONG_TYPE, bits);
			output.constant(32);
			output.lShr();
			output.l2i();
			output.iXor();
			
			output.returnInt();
		}
	}
}
