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
public class StatementDoWhile<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final E condition;
	private Statement<E, T> contents;

	public StatementDoWhile(CodePosition position, IMethodScope<E, T> environment, E condition)
	{
		super(position, environment);

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
		return processor.onDoWhile(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		IAny conditionValue = condition.getCompileTimeValue();
		if (conditionValue != null && conditionValue.canCastImplicit(boolean.class)) {
			// non-boolean constant condition is an error,
			// but the error will be generated during validation
			if (conditionValue.asBool()) {
				FlowBlock<E, T> loop = new FlowBlock<E, T>();
				builder.pushLoop(this, next, loop);
				
				FlowBlock<E, T> bodyEnd = new FlowBlock<E, T>();
				FlowBlock<E, T> bodyStart = contents.createFlowBlock(bodyEnd, builder);
				bodyEnd.addOutgoing(bodyStart);
				
				loop.addOutgoing(bodyStart);
				
				builder.pop();
				return loop;
			} else {
				FlowBlock<E, T> loop = new FlowBlock<E, T>();
				builder.pushLoop(contents, next, loop);
				
				FlowBlock<E, T> body = contents.createFlowBlock(next, builder);
				loop.addOutgoing(body);
				builder.pop();
				return loop;
			}
		}
		
		FlowBlock<E, T> loop = new FlowBlock<E, T>();
		builder.pushLoop(contents, next, loop);
		
		FlowBlock<E, T> bodyEnd = new FlowBlock<E, T>();
		FlowBlock<E, T> bodyStart = contents.createFlowBlock(bodyEnd, builder);
		
		FlowBlock<E, T> conditionBlock = new FlowBlock<E, T>();
		conditionBlock.prependInstruction(new IfFlowInstruction<E, T>(condition, bodyStart, next));
		conditionBlock.addOutgoing(bodyStart);
		conditionBlock.addOutgoing(next);
		
		builder.pop();
		
		return loop;
	}
}
