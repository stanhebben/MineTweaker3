package stanhebben.zenscript.expression;

import java.util.List;
import org.objectweb.asm.Label;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.parser.expression.ParsedExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.util.ZenPosition;
import zenscript.lexer.ZenTokener;
import zenscript.symbolic.unit.SymbolicFunction;

public abstract class Expression implements IPartialExpression {	
	public static final Expression parse(ZenTokener parser, IScopeMethod environment, ZenType predictedType) {
		return ParsedExpression.parse(parser, environment)
				.compilePartial(environment, predictedType)
				.eval();
	}
	
	private final ZenPosition position;
	private final IScopeMethod environment;
	
	public Expression(ZenPosition position, IScopeMethod environment) {
		this.position = position;
		this.environment = environment;
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public IScopeMethod getEnvironment() {
		return environment;
	}
	
	public Expression cast(ZenPosition position, ZenType type) {
		if (getType().equals(type)) {
			return this;
		} else {
			ICastingRule castingRule = getType().getCastingRule(type);
			if (castingRule == null) {
				getEnvironment().error(position, "Cannot cast " + this.getType() + " to " + type);
				return new ExpressionInvalid(position, getEnvironment(), type);
			} else {
				return new ExpressionAs(position, getEnvironment(), this, castingRule);
			}
		}
	}
	
	public abstract void compile(boolean result, MethodOutput output);
	
	public void compileIf(Label onIf, MethodOutput output) {
		if (getType() == getEnvironment().getTypes().BOOL) {
			compile(true, output);
			output.ifNE(onIf);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNonNull(onIf);
		} else {
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
		}
	}
	
	public void compileElse(Label onElse, MethodOutput output) {
		if (getType() == getEnvironment().getTypes().BOOL) {
			compile(true, output);
			output.ifEQ(onElse);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNull(onElse);
		} else {
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
		}
	}
	
	// #########################################
	// ### IPartialExpression implementation ###
	// #########################################
	
	@Override
	public Expression eval() {
		return this;
	}
	
	@Override
	public Expression assign(ZenPosition position, Expression other) {
		environment.error(position, "not a valid lvalue");
		return new ExpressionInvalid(position, getEnvironment(), getType());
	}
	
	@Override
	public IPartialExpression getMember(ZenPosition position, String name) {
		return getType().getMember(position, getEnvironment(), this, name);
	}
	
	@Override
	public IZenSymbol toSymbol() {
		return null;
	}
	
	@Override
	public ZenType toType(List<ZenType> genericTypes) {
		environment.error(position, "not a valid type");
		return environment.getTypes().ANY;
	}
	
	@Override
	public List<IMethod> getMethods() {
		return getType().getMethods();
	}
	
	@Override
	public IPartialExpression via(SymbolicFunction function) {
		return this;
	}
}
