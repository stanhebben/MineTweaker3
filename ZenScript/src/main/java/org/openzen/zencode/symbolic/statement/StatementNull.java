/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class StatementNull<E extends IPartialExpression<E>> extends Statement<E>
{
	public StatementNull(CodePosition position, IMethodScope<E> environment)
	{
		super(position, environment);
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onEmpty(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		return next;
	}
}
