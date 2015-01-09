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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class TryStatement<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final StatementVar<E, T> withStatement;
	private final Statement<E, T> contents;
	private final List<CatchClause<E, T>> catches;
	private final Statement<E, T> finallyContents;
	
	public TryStatement(
			CodePosition position, 
			IMethodScope<E, T> scope,
			StatementVar<E, T> withStatement,
			Statement<E, T> contents,
			List<CatchClause<E, T>> catches,
			Statement<E, T> finallyContents)
	{
		super(position, scope);
		
		this.withStatement = withStatement;
		this.contents = contents;
		this.catches = catches;
		this.finallyContents = finallyContents;
	}
	
	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onTryCatch(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		FlowBlock<E, T> endBlock = next;
		
		if (finallyContents != null) {
			endBlock = new FlowBlock<E, T>();
			
			if (withStatement != null)
				endBlock.prependInstruction(new VarFlowInstruction<E, T>(
						withStatement.getSymbol(),
						getScope().getExpressionCompiler().constantNull(getPosition(), getScope()))
				);
			
			endBlock.addOutgoing(next);
			endBlock = finallyContents.createFlowBlock(endBlock, builder);
		}
		
		FlowBlock<E, T> contentsBlock = new FlowBlock<E, T>();
		contentsBlock.addOutgoing(endBlock);
		contentsBlock = contents.createFlowBlock(contentsBlock, builder);
		
		FlowBlock<E, T> result = new FlowBlock<E, T>();
		
		if (withStatement != null)
		{
			contentsBlock = contentsBlock.prependInstruction(
					new ExpressionFlowInstruction<E, T>(
							getScope().getExpressionCompiler().localSet(
									getPosition(),
									getScope(),
									withStatement.getSymbol(),
									withStatement.getInitializer()))
			);
			
			result.prependInstruction(new VarFlowInstruction<E, T>(
					withStatement.getSymbol(),
					getScope().getExpressionCompiler().constantNull(getPosition(), getScope()))
			);
		}
		
		result.addOutgoing(contentsBlock);
		
		for (CatchClause<E, T> catchClause : catches) {
			FlowBlock<E, T> catchBlockEnd = new FlowBlock<E, T>();
			FlowBlock<E, T> catchBlockStart = catchClause.contents.createFlowBlock(catchBlockEnd, builder);
			
			catchBlockEnd.addOutgoing(endBlock);
			result.addOutgoing(catchBlockStart);
		}
		
		return result;
	}
	
	public static class CatchClause<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	{
		private final List<T> types;
		private final Statement<E, T> contents;
		
		public CatchClause(List<T> types, Statement<E, T> contents)
		{
			this.types = types;
			this.contents = contents;
		}
	}
}
