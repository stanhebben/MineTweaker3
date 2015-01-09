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
import org.openzen.zencode.symbolic.statement.graph.ThrowFlowInstruction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ThrowStatement<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E value;
	
	public ThrowStatement(CodePosition position, IMethodScope<E> scope, E value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onThrow(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		FlowBlock<E> result = new FlowBlock<E>();
		return result.prependInstruction(new ThrowFlowInstruction<E>(value));
	}
}
