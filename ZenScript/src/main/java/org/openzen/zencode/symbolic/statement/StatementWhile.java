/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class StatementWhile<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final E condition;
	private Statement<E, T> contents;

	public StatementWhile(CodePosition position, IScopeMethod<E, T> scope, E condition)
	{
		super(position, scope);

		this.condition = condition;
	}

	public void setContents(Statement<E, T> contents)
	{
		this.contents = contents;
	}
	
	public E getCondition()
	{
		return condition;
	}
	
	public Statement<E, T> getContents()
	{
		return contents;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onWhile(this);
	}
}
