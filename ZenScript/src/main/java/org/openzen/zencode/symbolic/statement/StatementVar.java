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
import org.openzen.zencode.symbolic.statement.graph.VarFlowInstruction;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class StatementVar<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final SymbolLocal<E, T> symbol;
	private final E initializer;

	public StatementVar(CodePosition position, IMethodScope<E, T> method, SymbolLocal<E, T> symbol, E initializer)
	{
		super(position, method);

		this.symbol = symbol;
		this.initializer = initializer;
	}
	
	public SymbolLocal<E, T> getSymbol()
	{
		return symbol;
	}
	
	public E getInitializer()
	{
		return initializer;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onVar(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		return next.prependInstruction(new VarFlowInstruction<E, T>(symbol, initializer));
	}
}
