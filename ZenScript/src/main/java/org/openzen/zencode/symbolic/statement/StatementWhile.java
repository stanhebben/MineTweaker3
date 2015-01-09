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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class StatementWhile<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final E condition;
	private Statement<E, T> contents;

	public StatementWhile(CodePosition position, IMethodScope<E, T> scope, E condition)
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

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		IAny conditionConstant = condition.getCompileTimeValue();
		if (conditionConstant != null && conditionConstant.canCastImplicit(boolean.class)) {
			if (conditionConstant.asBool()) {
				FlowBlock<E, T> loop = new FlowBlock<E, T>();
				builder.pushLoop(this, next, loop);
				
				FlowBlock<E, T> bodyEnd = new FlowBlock<E, T>();
				FlowBlock<E, T> bodyStart = contents.createFlowBlock(bodyEnd, builder);
				bodyEnd.addOutgoing(bodyStart);
				
				builder.pop();
				loop.addOutgoing(bodyStart);
				return loop;
			} else {
				return next;
			}
		}
		
		FlowBlock<E, T> loop = new FlowBlock<E, T>();
		builder.pushLoop(this, next, loop);
		
		FlowBlock<E, T> bodyEnd = new FlowBlock<E, T>();
		FlowBlock<E, T> bodyStart = contents.createFlowBlock(bodyEnd, builder);
		
		loop.prependInstruction(new IfFlowInstruction<E, T>(condition, bodyStart, next));
		loop.addOutgoing(bodyStart);
		loop.addOutgoing(next);
		bodyEnd.addOutgoing(loop);
		
		builder.pop();
		return loop;
	}
}
