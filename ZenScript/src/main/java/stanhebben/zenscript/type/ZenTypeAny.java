package stanhebben.zenscript.type;

import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionString;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.java.method.JavaMethod;
import stanhebben.zenscript.type.natives.JavaMethodPrefixed;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleAnyAs;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

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
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return new AnyMember(position, environment, value.eval(), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		environment.error(position, "any values don't have static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		// TODO: implement iteration on any values
		throw new UnsupportedOperationException("iteration on any values is not yet supported");
	}
	
	@Override
	public ICastingRule getCastingRule(AccessScope accessScope, ZenType type) {
		ICastingRule base = super.getCastingRule(accessScope, type);
		if (base == null) {
			return new CastingRuleAnyAs(type, getScope().getTypes());
		} else {
			return base;
		}
	}
	
	@Override
	public void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getScope().getTypes();
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
			constructExpansionCastingRules(accessScope, rules);
		}
	}
	
	@Override
	public boolean canCastExplicit(AccessScope accessScope, ZenType type) {
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
	public Expression operator(CodePosition position, IScopeMethod environment, OperatorType operator, Expression... values) {
		CommonMethods methods = environment.getTypes().getCommonMethods();
		ZenType any = environment.getTypes().ANY;
		
		switch (operator) {
			case NEG:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_NEG,
						values[0]);
			case NOT:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_NOT,
						values[0]);
			case ADD:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_ADD,
						values[0],
						values[1].cast(position, any));
			case CAT:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_CAT,
						values[0],
						values[1].cast(position, any));
			case SUB:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_SUB,
						values[0],
						values[1].cast(position, any));
			case MUL:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_MUL,
						values[0],
						values[1].cast(position, any));
			case DIV:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_DIV,
						values[0],
						values[1].cast(position, any));
			case MOD:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_MOD,
						values[0],
						values[1].cast(position, any));
			case AND:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_AND,
						values[0],
						values[1].cast(position, any));
			case OR:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_OR,
						values[0],
						values[1].cast(position, any));
			case XOR:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_XOR,
						values[0],
						values[1].cast(position, any));
			case CONTAINS:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_CONTAINS,
						values[0],
						values[1].cast(position, any));
			case INDEXGET:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_INDEXGET,
						values[0],
						values[1].cast(position, any));
			case RANGE:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_RANGE,
						values[0],
						values[1].cast(position, any));
			case COMPARE:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_COMPARETO,
						values[0],
						values[1].cast(position, any));
			case MEMBERGETTER:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_MEMBERGET,
						values[0],
						values[1].cast(position, environment.getTypes().STRING));
			case INDEXSET:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_INDEXSET,
						values[0],
						values[1].cast(position, any),
						values[2].cast(position, any));
			case MEMBERSETTER:
				return new ExpressionCallVirtual(
						position,
						environment,
						methods.METHOD_MEMBERSET,
						values[0],
						values[1].cast(position, environment.getTypes().STRING),
						values[2].cast(position, any));
			default:
				return new ExpressionInvalid(position, environment);
		}
	}
	
	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
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
	
	@Override
	public String getName() {
		return "any";
	}
	
	@Override
	public String getAnyClassName() {
		throw new UnsupportedOperationException("Cannot get any class name from the any type. That's like trying to stuff a freezer into a freezer.");
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
	
	@Override
	public List<IMethod> getMethods() {
		if (methods == null)
			methods = Collections.singletonList(JavaMethod.get(getScope().getTypes(), IAny.class, "call", IAny[].class));
		
		return methods;
	}
	
	private class AnyMember implements IPartialExpression {
		private final CodePosition position;
		private final IScopeMethod environment;
		private final Expression value;
		private final String name;
		private final List<IMethod> methods;
		
		public AnyMember(CodePosition position, IScopeMethod environment, Expression value, String name) {
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
		public Expression assign(CodePosition position, Expression other) {
			return new ExpressionCallVirtual(position, environment, environment.getTypes().getCommonMethods().METHOD_MEMBERSET, value, new ExpressionString(position, environment, name), other);
		}

		@Override
		public IPartialExpression getMember(CodePosition position, String name) {
			return new AnyMember(position, environment, this.eval(), name);
		}

		@Override
		public List<IMethod> getMethods() {
			return methods;
		}
		
		@Override
		public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments)
		{
			return method.callVirtual(position, environment, value, arguments);
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
