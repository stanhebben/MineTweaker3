/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.IfFlowInstruction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class StatementWhile<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E condition;
	private Statement<E> contents;

	public StatementWhile(CodePosition position, IMethodScope<E> scope, E condition)
	{
		super(position, scope);

		this.condition = condition;
	}

	public void setContents(Statement<E> contents)
	{
		this.contents = contents;
	}
	
	public E getCondition()
	{
		return condition;
	}
	
	public Statement<E> getContents()
	{
		return contents;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onWhile(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		IAny conditionConstant = condition.getCompileTimeValue();
		if (conditionConstant != null && conditionConstant.canCastImplicit(boolean.class)) {
			if (conditionConstant.asBool()) {
				FlowBlock<E> loop = new FlowBlock<E>();
				builder.pushLoop(this, next, loop);
				
				FlowBlock<E> bodyEnd = new FlowBlock<E>();
				FlowBlock<E> bodyStart = contents.createFlowBlock(bodyEnd, builder);
				bodyEnd.addOutgoing(bodyStart);
				
				builder.pop();
				loop.addOutgoing(bodyStart);
				return loop;
			} else {
				return next;
			}
		}
		
		FlowBlock<E> loop = new FlowBlock<E>();
		builder.pushLoop(this, next, loop);
		
		FlowBlock<E> bodyEnd = new FlowBlock<E>();
		FlowBlock<E> bodyStart = contents.createFlowBlock(bodyEnd, builder);
		
		loop.prependInstruction(new IfFlowInstruction<E>(condition, bodyStart, next));
		loop.addOutgoing(bodyStart);
		loop.addOutgoing(next);
		bodyEnd.addOutgoing(loop);
		
		builder.pop();
		return loop;
	}
}
