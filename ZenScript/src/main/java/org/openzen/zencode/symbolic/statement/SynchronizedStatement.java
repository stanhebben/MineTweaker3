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
 * @author Stan
 * @param <E>
 */
public class SynchronizedStatement<E extends IPartialExpression<E>> extends Statement<E>
{
	private final Statement<E> contents;
	
	public SynchronizedStatement(CodePosition position, IMethodScope<E> scope, Statement<E> contents)
	{
		super(position, scope);
		
		this.contents = contents;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onSynchronized(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		return contents.createFlowBlock(next, builder);
	}
}
