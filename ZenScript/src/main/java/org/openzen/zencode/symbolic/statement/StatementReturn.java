package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public class StatementReturn<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final E expression;

	public StatementReturn(CodePosition position, IScopeMethod<E, T> environment, E expression)
	{
		super(position, environment);

		this.expression = expression;
	}

	public E getExpression()
	{
		return expression;
	}

	@Override
	public boolean isReturn()
	{
		return false;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onReturn(this);
	}
}
