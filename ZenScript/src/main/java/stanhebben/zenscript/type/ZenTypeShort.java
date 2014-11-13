package stanhebben.zenscript.type;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInt;
import org.openzen.zencode.java.method.JavaMethod;
import stanhebben.zenscript.util.AnyClassWriter;
import static stanhebben.zenscript.util.AnyClassWriter.throwCastException;
import static stanhebben.zenscript.util.AnyClassWriter.throwUnsupportedException;
import stanhebben.zenscript.util.IAnyDefinition;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.runtime.IAny;
import static org.openzen.zencode.runtime.IAny.NUM_SHORT;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2B;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2D;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2F;
import org.openzen.zencode.symbolic.type.casting.CastingRuleI2L;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNone;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

public class ZenTypeShort extends ZenTypeArithmetic {
	private static final String ANY_NAME = "any/AnyShort";
	private static final String ANY_NAME_2 = "any.AnyShort";
	
	public ZenTypeShort(IScopeGlobal environment) {
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
		
		rules.registerCastingRule(types.BYTE, new CastingRuleI2B(null, types));
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleStaticMethod(methods.BYTE_VALUEOF));
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleStaticMethod(methods.SHORT_VALUEOF));
		rules.registerCastingRule(types.INT, new CastingRuleNone(types.SHORT, types.INT));
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleStaticMethod(methods.INT_VALUEOF));
		rules.registerCastingRule(types.LONG, new CastingRuleI2L(null, types));
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleStaticMethod(methods.LONG_VALUEOF, new CastingRuleI2L(null, types)));
		rules.registerCastingRule(types.FLOAT, new CastingRuleI2F(null, types));
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleStaticMethod(methods.FLOAT_VALUEOF, new CastingRuleI2F(null, types)));
		rules.registerCastingRule(types.DOUBLE, new CastingRuleI2D(null, types));
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleStaticMethod(methods.DOUBLE_VALUEOF, new CastingRuleI2D(null, types)));
		rules.registerCastingRule(types.ANY, new CastingRuleStaticMethod(JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, types.BYTE)));
		
		if (followCasters) {
			constructExpansionCastingRules(access, rules);
		}
	}

	@Override
	public Type toASMType() {
		return Type.SHORT_TYPE;
	}

	@Override
	public int getNumberType() {
		return NUM_SHORT;
	}

	@Override
	public String getSignature() {
		return "S";
	}

	@Override
	public Class toJavaClass() {
		return short.class;
	}

	@Override
	public String getName() {
		return "short";
	}
	
	@Override
	public String getAnyClassName() {
		IScopeGlobal environment = getScope();
		
		if (!environment.containsClass(ANY_NAME_2)) {
			environment.putClass(ANY_NAME_2, new byte[0]);
			environment.putClass(ANY_NAME_2, AnyClassWriter.construct(new AnyDefinitionShort(environment), ANY_NAME, Type.SHORT_TYPE));
		}
		
		return ANY_NAME;
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionInt(position, environment, 0, environment.getTypes().SHORT);
	}
	
	@Override
	public ZenType nullable() {
		return getScope().getTypes().SHORTOBJECT;
	}
	
	private class AnyDefinitionShort implements IAnyDefinition {
		private final IScopeGlobal environment;
		private final TypeRegistry types;
		
		public AnyDefinitionShort(IScopeGlobal environment) {
			this.environment = environment;
			types = environment.getTypes();
		}

		@Override
		public void defineMembers(ClassVisitor output) {
			output.visitField(Opcodes.ACC_PRIVATE, "value", "S", null, null);
			
			MethodOutput valueOf = new MethodOutput(output, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "valueOf", "(S)" + signature(IAny.class), null, null);
			valueOf.start();
			valueOf.newObject(ANY_NAME);
			valueOf.dup();
			valueOf.load(Type.SHORT_TYPE, 0);
			valueOf.construct(ANY_NAME, "S");
			valueOf.returnObject();
			valueOf.end();
			
			MethodOutput constructor = new MethodOutput(output, Opcodes.ACC_PUBLIC, "<init>", "(S)V", null, null);
			constructor.start();
			constructor.loadObject(0);
			constructor.invokeSpecial(internal(Object.class), "<init>", "()V");
			constructor.loadObject(0);
			constructor.load(Type.SHORT_TYPE, 1);
			constructor.putField(ANY_NAME, "value", "S");
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
				expansion.compileAnyCanCastImplicit(types.SHORT, output, environment, 0);
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
				expansion.compileAnyCast(types.SHORT, output, environment, 0, 1);
			}*/
			
			throwCastException(output, "short", 1);
		}

		@Override
		public void defineNot(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.iNot();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineNeg(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.iNeg();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineAdd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iAdd();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineSub(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iSub();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
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
			output.invokeInterface(IAny.NAME, "asString", "()Ljava/lang/String;");
			output.invokeVirtual(StringBuilder.class, "append", StringBuilder.class, String.class);
			output.invokeVirtual(StringBuilder.class, "toString", String.class);
			output.invokeStatic(types.STRING.getAnyClassName(), "valueOf", "(Ljava/lang/String;)" + IAny.NAME);
			output.returnObject();
		}

		@Override
		public void defineMul(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iMul();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineDiv(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iDiv();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineMod(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iRem();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineAnd(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iAnd();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineOr(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iOr();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
			output.returnObject();
		}

		@Override
		public void defineXor(MethodOutput output) {
			output.newObject(ANY_NAME);
			output.dup();
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iXor();
			output.invokeSpecial(ANY_NAME, "<init>", "(S)V");
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
			output.invokeInterface(IAny.NAME, "asShort", "()S");
			output.iSub();
			output.returnInt();
		}

		@Override
		public void defineContains(MethodOutput output) {
			throwUnsupportedException(output, "short", "in");
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
			throwUnsupportedException(output, "short", "get []");
		}

		@Override
		public void defineIndexSet(MethodOutput output) {
			throwUnsupportedException(output, "short", "set []");
		}

		@Override
		public void defineCall(MethodOutput output) {
			throwUnsupportedException(output, "short", "call");
		}

		@Override
		public void defineAsBool(MethodOutput output) {
			throwCastException(output, ANY_NAME, "bool");
		}

		@Override
		public void defineAsByte(MethodOutput output) {
			getValue(output);
			output.i2b();
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
			output.invokeStatic(Short.class, "toString", String.class, short.class);
			output.returnObject();
		}

		@Override
		public void defineAs(MethodOutput output) {
			int localValue = output.local(Type.SHORT_TYPE);
			
			getValue(output);
			output.store(Type.SHORT_TYPE, localValue);
			/*TypeExpansion expansion = environment.getExpansion(getName());
			if (expansion != null) {
				expansion.compileAnyCast(types.SHORT, output, environment, localValue, 1);
			}*/
			
			throwCastException(output, "short", 1);
		}

		@Override
		public void defineIs(MethodOutput output) {
			Label lblEq = new Label();
			
			output.loadObject(1);
			output.constant(Type.SHORT_TYPE);
			output.ifACmpEq(lblEq);
			output.iConst0();
			output.returnInt();
			output.label(lblEq);
			output.iConst1();
			output.returnInt();
		}
		
		@Override
		public void defineGetNumberType(MethodOutput output) {
			output.constant(IAny.NUM_SHORT);
			output.returnInt();
		}

		@Override
		public void defineIteratorSingle(MethodOutput output) {
			throwUnsupportedException(output, "short", "iterator");
		}

		@Override
		public void defineIteratorMulti(MethodOutput output) {
			throwUnsupportedException(output, "short", "iterator");
		}
		
		private void getValue(MethodOutput output) {
			output.loadObject(0);
			output.getField(ANY_NAME, "value", "S");
		}

		@Override
		public void defineEquals(MethodOutput output) {
			Label lblNope = new Label();
			
			output.loadObject(1);
			output.instanceOf(IAny.NAME);
			output.ifEQ(lblNope);
			
			getValue(output);
			output.loadObject(1);
			output.invokeInterface(IAny.NAME, "asShort", "()S");
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
	}
}
