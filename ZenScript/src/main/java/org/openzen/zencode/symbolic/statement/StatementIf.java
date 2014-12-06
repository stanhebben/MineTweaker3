package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public class StatementIf<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final E condition;
	private final Statement<E, T> onThen;
	private final Statement<E, T> onElse;

	public StatementIf(CodePosition position, IScopeMethod<E, T> environment, E condition, Statement<E, T> onThen, Statement<E, T> onElse)
	{
		super(position, environment);

		this.condition = condition;
		this.onThen = onThen;
		this.onElse = onElse;
	}

	public E getCondition()
	{
		return condition;
	}

	public Statement<E, T> getThen()
	{
		return onThen;
	}

	public Statement<E, T> getElse()
	{
		return onElse;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onIf(this);
	}
}
