/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.SwitchFlowInstruction;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class StatementSwitch<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E value;
	private final List<Statement<E>> contents = new ArrayList<Statement<E>>();
	private final List<E> caseValues = new ArrayList<E>();
	private final List<Integer> caseLabels = new ArrayList<Integer>();
	private int defaultLabel = -1;
	
	public StatementSwitch(CodePosition position, IMethodScope<E> scope, E value)
	{
		super(position, scope);

		this.value = value;
	}
	
	public IGenericType<E> getType()
	{
		return value.getType();
	}

	public void onCase(CodePosition position, E value)
	{
		if (caseValues.contains(value))
			getScope().getErrorLogger().errorDuplicateCase(position, value);
		else {
			caseValues.add(value);
			caseLabels.add(contents.size());
		}
	}

	public void onDefault(CodePosition position)
	{
		if (defaultLabel >= 0)
			getScope().getErrorLogger().errorDuplicateDefault(position);
		else
			defaultLabel = contents.size();
	}

	public void onStatement(Statement<E> statement)
	{
		contents.add(statement);
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onSwitch(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		builder.pushSwitch(this, next);
		
		FlowBlock<E> switchBlock = new FlowBlock<E>();
		
		int caseIndex = caseLabels.size() - 1;
		Map<E, FlowBlock<E>> caseBlocks = new HashMap<E, FlowBlock<E>>();
		FlowBlock<E> currentFlowBlock = new FlowBlock<E>();
		FlowBlock<E> defaultFlowBlock = next;
		
		for (int i = contents.size() - 1; i >= 0; i--) {
			
			currentFlowBlock = contents.get(i).createFlowBlock(currentFlowBlock, builder);
			
			while (caseIndex >= 0 && i == caseLabels.get(caseIndex)) {
				switchBlock.addOutgoing(currentFlowBlock);
				caseBlocks.put(caseValues.get(caseIndex), currentFlowBlock);
				caseIndex--;
			}
			
			if (defaultLabel == i) {
				switchBlock.addOutgoing(currentFlowBlock);
				defaultFlowBlock = currentFlowBlock;
			}
		}
		
		builder.pop();
		
		return switchBlock.prependInstruction(new SwitchFlowInstruction<E>(
				value,
				defaultFlowBlock,
				caseBlocks));
	}
}
