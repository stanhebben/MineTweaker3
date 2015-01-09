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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SynchronizedStatement<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final Statement<E, T> contents;
	
	public SynchronizedStatement(CodePosition position, IMethodScope<E, T> scope, Statement<E, T> contents)
	{
		super(position, scope);
		
		this.contents = contents;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onSynchronized(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		return contents.createFlowBlock(next, builder);
	}
}
