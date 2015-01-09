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
public class StatementDoWhile<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E condition;
	private Statement<E> contents;

	public StatementDoWhile(CodePosition position, IMethodScope<E> environment, E condition)
	{
		super(position, environment);

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
		return processor.onDoWhile(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		IAny conditionValue = condition.getCompileTimeValue();
		if (conditionValue != null && conditionValue.canCastImplicit(boolean.class)) {
			// non-boolean constant condition is an error,
			// but the error will be generated during validation
			if (conditionValue.asBool()) {
				FlowBlock<E> loop = new FlowBlock<E>();
				builder.pushLoop(this, next, loop);
				
				FlowBlock<E> bodyEnd = new FlowBlock<E>();
				FlowBlock<E> bodyStart = contents.createFlowBlock(bodyEnd, builder);
				bodyEnd.addOutgoing(bodyStart);
				
				loop.addOutgoing(bodyStart);
				
				builder.pop();
				return loop;
			} else {
				FlowBlock<E> loop = new FlowBlock<E>();
				builder.pushLoop(contents, next, loop);
				
				FlowBlock<E> body = contents.createFlowBlock(next, builder);
				loop.addOutgoing(body);
				builder.pop();
				return loop;
			}
		}
		
		FlowBlock<E> loop = new FlowBlock<E>();
		builder.pushLoop(contents, next, loop);
		
		FlowBlock<E> bodyEnd = new FlowBlock<E>();
		FlowBlock<E> bodyStart = contents.createFlowBlock(bodyEnd, builder);
		
		FlowBlock<E> conditionBlock = new FlowBlock<E>();
		conditionBlock.prependInstruction(new IfFlowInstruction<E>(condition, bodyStart, next));
		conditionBlock.addOutgoing(bodyStart);
		conditionBlock.addOutgoing(next);
		
		builder.pop();
		
		return loop;
	}
}
