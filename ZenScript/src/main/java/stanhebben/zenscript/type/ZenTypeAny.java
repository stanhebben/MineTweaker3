package stanhebben.zenscript.type;

import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Type;
import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.symbolic.method.IMethod;
import zenscript.symbolic.method.JavaMethod;
import stanhebben.zenscript.type.natives.JavaMethodPrefixed;
import stanhebben.zenscript.util.MethodOutput;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import zenscript.runtime.IAny;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.type.casting.CastingRuleAnyAs;
import zenscript.symbolic.type.casting.CastingRuleNullableStaticMethod;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.symbolic.util.CommonMethods;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeAny extends ZenType {
	private List<IMethod> methods = null;
	
	public ZenTypeAny(IScopeGlobal environment) {
		super(environment);
	}
	
	@Override
	public IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return new AnyMember(position, environment, value.eval(), name);
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		environment.error(position, "any values don't have static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		// TODO: implement iteration on any values
		throw new UnsupportedOperationException("iteration on any values is not yet supported");
	}
	
	@Override
	public ICastingRule getCastingRule(ZenType type) {
		ICastingRule base = super.getCastingRule(type);
		if (base == null) {
			return new CastingRuleAnyAs(type, getEnvironment().getTypes());
		} else {
			return base;
		}
	}
	
	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		rules.registerCastingRule(types.BOOL, methods.CAST_ANY_BOOL);
		rules.registerCastingRule(types.BOOLOBJECT, new CastingRuleNullableStaticMethod(methods.BOOL_VALUEOF, methods.CAST_ANY_BOOL));
		rules.registerCastingRule(types.BYTE, methods.CAST_ANY_BYTE);
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleNullableStaticMethod(methods.BYTE_VALUEOF, methods.CAST_ANY_BYTE));
		rules.registerCastingRule(types.SHORT, methods.CAST_ANY_SHORT);
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleNullableStaticMethod(methods.SHORT_VALUEOF, methods.CAST_ANY_SHORT));
		rules.registerCastingRule(types.INT, methods.CAST_ANY_INT);
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleNullableStaticMethod(methods.INT_VALUEOF, methods.CAST_ANY_INT));
		rules.registerCastingRule(types.LONG, methods.CAST_ANY_LONG);
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleNullableStaticMethod(methods.LONG_VALUEOF, methods.CAST_ANY_LONG));
		rules.registerCastingRule(types.FLOAT, methods.CAST_ANY_FLOAT);
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleNullableStaticMethod(methods.FLOAT_VALUEOF, methods.CAST_ANY_FLOAT));
		rules.registerCastingRule(types.DOUBLE, methods.CAST_ANY_DOUBLE);
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleNullableStaticMethod(methods.DOUBLE_VALUEOF, methods.CAST_ANY_DOUBLE));
		rules.registerCastingRule(types.STRING, methods.CAST_ANY_STRING);
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}
	
	@Override
	public boolean canCastExplicit(ZenType type) {
		return true;
	}
	
	@Override
	public Class toJavaClass() {
		return IAny.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(IAny.class);
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return signature(IAny.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}
	
	@Override
	public Expression unary(ZenPosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		CommonMethods methods = environment.getTypes().getCommonMethods();
		
		switch (operator) {
			case NEG:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_NEG, value);
			case NOT:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_NOT, value);
			default:
				return new ExpressionInvalid(position, environment);
		}
	}

	@Override
	public Expression binary(ZenPosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		ZenType any = types.ANY;
		
		switch (operator) {
			case ADD:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_ADD, left, right.cast(position, any));
			case CAT:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_CAT, left, right.cast(position, any));
			case SUB:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_SUB, left, right.cast(position, any));
			case MUL:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_MUL, left, right.cast(position, any));
			case DIV:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_DIV, left, right.cast(position, any));
			case MOD:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_MOD, left, right.cast(position, any));
			case AND:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_AND, left, right.cast(position, any));
			case OR:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_OR, left, right.cast(position, any));
			case XOR:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_XOR, left, right.cast(position, any));
			case CONTAINS:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_CONTAINS, left, right.cast(position, any));
			case INDEXGET:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_INDEXGET, left, right.cast(position, any));
			case RANGE:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_RANGE, left, right.cast(position, any));
			case COMPARE:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_COMPARETO, left, right.cast(position, any));
			case MEMBERGETTER:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_MEMBERGET, left, right.cast(position, types.STRING));
			default:
				return new ExpressionInvalid(position, environment);
		}
	}

	@Override
	public Expression trinary(ZenPosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		switch (operator) {
			case INDEXSET:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_INDEXSET, first, second.cast(position, types.ANY), third.cast(position, types.ANY));
			case MEMBERSETTER:
				return new ExpressionCallVirtual(position, environment, methods.METHOD_MEMBERSET, first, second.cast(position, types.STRING), third.cast(position, types.ANY));
			default:
				return new ExpressionInvalid(position, environment);
		}
	}

	@Override
	public Expression compare(ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		Expression comparator = new ExpressionCallVirtual(
				position,
				environment,
				environment.getTypes().getCommonMethods().METHOD_COMPARETO,
				left,
				right);
		
		return new ExpressionCompareGeneric(
				position,
				environment,
				comparator,
				type);
	}

	/*@Override
	public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
		return new ExpressionCallVirtual(
				position,
				environment,
				METHOD_CALL,
				receiver,
				arguments);
	}
	
	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		ZenType[] result = new ZenType[numArguments];
		for (int i = 0; i < result.length; i++) {
			result[i] = ANY;
		}
		return result;
	}*/

	@Override
	public String getName() {
		return "any";
	}
	
	@Override
	public String getAnyClassName() {
		throw new UnsupportedOperationException("Cannot get any class name from the any type. That's like trying to stuff a freezer into a freezer.");
	}

	@Override
	public Expression defaultValue(ZenPosition position, IScopeMethod environment) {
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
	
	@Override
	public List<IMethod> getMethods() {
		if (methods == null)
			methods = Collections.singletonList(JavaMethod.get(getEnvironment().getTypes(), IAny.class, "call", IAny.class, IAny[].class));
		
		return methods;
	}
	
	private class AnyMember implements IPartialExpression {
		private final ZenPosition position;
		private final IScopeMethod environment;
		private final Expression value;
		private final String name;
		private final List<IMethod> methods;
		
		public AnyMember(ZenPosition position, IScopeMethod environment, Expression value, String name) {
			this.position = position;
			this.environment = environment;
			this.value = value;
			this.name = name;
			methods = Collections.<IMethod>singletonList(new JavaMethodPrefixed(
					new ExpressionString(position, environment, name),
					environment.getTypes().getCommonMethods().METHOD_MEMBERCALL));
		}

		@Override
		public Expression eval() {
			return new ExpressionCallVirtual(position, environment, environment.getTypes().getCommonMethods().METHOD_MEMBERGET, value, new ExpressionString(position, environment, name));
		}

		@Override
		public Expression assign(ZenPosition position, Expression other) {
			return new ExpressionCallVirtual(position, environment, environment.getTypes().getCommonMethods().METHOD_MEMBERSET, value, new ExpressionString(position, environment, name), other);
		}

		@Override
		public IPartialExpression getMember(ZenPosition position, String name) {
			return new AnyMember(position, environment, this.eval(), name);
		}

		@Override
		public List<IMethod> getMethods() {
			return methods;
		}

		@Override
		public IZenSymbol toSymbol() {
			return null;
		}

		@Override
		public ZenType getType() {
			return ZenTypeAny.this;
		}

		@Override
		public ZenType toType(List<ZenType> genericTypes) {
			return null;
		}

		@Override
		public IPartialExpression via(SymbolicFunction function) {
			return this;
		}
	}
}
