package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public abstract class Statement<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final CodePosition position;
	private final IScopeMethod<E, T> scope;

	public Statement(CodePosition position, IScopeMethod<E, T> environment)
	{
		this.position = position;
		this.scope = environment;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public IScopeMethod<E, T> getScope()
	{
		return scope;
	}

	public boolean isReturn()
	{
		return false;
	}

	public abstract <U> U process(IStatementProcessor<E, T, U> processor);
}
