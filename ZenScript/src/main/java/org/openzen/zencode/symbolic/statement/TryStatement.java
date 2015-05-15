/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.ExpressionFlowInstruction;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.VarFlowInstruction;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class TryStatement<E extends IPartialExpression<E>> extends Statement<E>
{
	private final StatementVar<E> withStatement;
	private final Statement<E> contents;
	private final List<CatchClause<E>> catches;
	private final Statement<E> finallyContents;
	
	public TryStatement(
			CodePosition position, 
			IMethodScope<E> scope,
			StatementVar<E> withStatement,
			Statement<E> contents,
			List<CatchClause<E>> catches,
			Statement<E> finallyContents)
	{
		super(position, scope);
		
		this.withStatement = withStatement;
		this.contents = contents;
		this.catches = catches;
		this.finallyContents = finallyContents;
	}
	
	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onTryCatch(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		FlowBlock<E> endBlock = next;
		
		if (finallyContents != null) {
			endBlock = new FlowBlock<E>();
			
			if (withStatement != null)
				endBlock.prependInstruction(new VarFlowInstruction<E>(
						withStatement.getSymbol(),
						getScope().getExpressionCompiler().constantNull(getPosition(), getScope()))
				);
			
			endBlock.addOutgoing(next);
			endBlock = finallyContents.createFlowBlock(endBlock, builder);
		}
		
		FlowBlock<E> contentsBlock = new FlowBlock<E>();
		contentsBlock.addOutgoing(endBlock);
		contentsBlock = contents.createFlowBlock(contentsBlock, builder);
		
		FlowBlock<E> result = new FlowBlock<E>();
		
		if (withStatement != null)
		{
			contentsBlock = contentsBlock.prependInstruction(
					new ExpressionFlowInstruction<E>(
							getScope().getExpressionCompiler().localSet(
									getPosition(),
									getScope(),
									withStatement.getSymbol(),
									withStatement.getInitializer()))
			);
			
			result.prependInstruction(new VarFlowInstruction<E>(
					withStatement.getSymbol(),
					getScope().getExpressionCompiler().constantNull(getPosition(), getScope()))
			);
		}
		
		result.addOutgoing(contentsBlock);
		
		for (CatchClause<E> catchClause : catches) {
			FlowBlock<E> catchBlockEnd = new FlowBlock<E>();
			FlowBlock<E> catchBlockStart = catchClause.contents.createFlowBlock(catchBlockEnd, builder);
			
			catchBlockEnd.addOutgoing(endBlock);
			result.addOutgoing(catchBlockStart);
		}
		
		return result;
	}
	
	public static class CatchClause<E extends IPartialExpression<E>>
	{
		private final List<IGenericType<E>> types;
		private final Statement<E> contents;
		
		public CatchClause(List<IGenericType<E>> types, Statement<E> contents)
		{
			this.types = types;
			this.contents = contents;
		}
	}
}
