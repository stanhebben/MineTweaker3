package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.ForeachFlowInstruction;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.util.CodePosition;

public class StatementForeach<E extends IPartialExpression<E>> extends Statement<E>
{
	private final List<SymbolLocal<E>> variables;
	private final E list;
	private Statement<E> body;

	public StatementForeach(CodePosition position, IMethodScope<E> environment, List<SymbolLocal<E>> variables, E list)
	{
		super(position, environment);

		this.variables = variables;
		this.list = list;
	}

	public void setBody(Statement<E> body)
	{
		this.body = body;
	}

	public List<SymbolLocal<E>> getVariables()
	{
		return variables;
	}

	public E getList()
	{
		return list;
	}

	public Statement<E> getBody()
	{
		return body;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onForeach(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		FlowBlock<E> loop = new FlowBlock<E>();
		builder.pushLoop(this, next, loop);
		
		FlowBlock<E> bodyEnd = new FlowBlock<E>();
		FlowBlock<E> bodyStart = body.createFlowBlock(bodyEnd, builder);
		
		builder.pop();
		
		loop.prependInstruction(new ForeachFlowInstruction<E>(list, bodyStart, next));
		bodyEnd.addOutgoing(loop);
		return loop;
	}
}
