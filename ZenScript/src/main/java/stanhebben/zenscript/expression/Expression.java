package stanhebben.zenscript.expression;

import java.util.List;
import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementExpression;
import stanhebben.zenscript.statements.StatementReturn;

public abstract class Expression implements IPartialExpression
{
	public static final Expression parse(ZenLexer parser, IScopeMethod environment, ZenType predictedType)
	{
		return ParsedExpression.parse(parser, environment)
				.compilePartial(environment, predictedType)
				.eval();
	}

	private final CodePosition position;
	private final IScopeMethod scope;

	public Expression(CodePosition position, IScopeMethod scope)
	{
		this.position = position;
		this.scope = scope;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public IScopeMethod getScope()
	{
		return scope;
	}

	public Expression cast(CodePosition position, ZenType type)
	{
		if (getType().equals(type))
			return this;
		else {
			ICastingRule castingRule = getType().getCastingRule(getScope().getAccessScope(), type);
			if (castingRule == null) {
				getScope().error(position, "Cannot cast " + this.getType() + " to " + type);
				return new ExpressionInvalid(position, getScope(), type);
			} else
				return new ExpressionAs(position, getScope(), this, castingRule);
		}
	}

	public abstract void compile(boolean result, MethodOutput output);

	public void compileIf(Label onIf, MethodOutput output)
	{
		if (getType() == getScope().getTypes().BOOL) {
			compile(true, output);
			output.ifNE(onIf);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNonNull(onIf);
		} else
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
	}

	public void compileElse(Label onElse, MethodOutput output)
	{
		if (getType() == getScope().getTypes().BOOL) {
			compile(true, output);
			output.ifEQ(onElse);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNull(onElse);
		} else
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
	}

	public Statement asStatement()
	{
		return new StatementExpression(position, scope, this);
	}
	
	public Statement asReturnStatement()
	{
		return new StatementReturn(position, scope, this);
	}

	// #########################################
	// ### IPartialExpression implementation ###
	// #########################################
	
	@Override
	public Expression eval()
	{
		return this;
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		scope.error(position, "not a valid lvalue");
		return new ExpressionInvalid(position, getScope(), getType());
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		return getType().getMember(position, getScope(), this, name);
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return null;
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		scope.error(position, "not a valid type");
		return scope.getTypes().ANY;
	}

	@Override
	public List<IMethod> getMethods()
	{
		return getType().getMethods();
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return this;
	}
}
