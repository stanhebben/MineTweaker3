package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.ForeachFlowInstruction;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public class StatementForeach<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final List<SymbolLocal<E, T>> variables;
	private final E list;
	private Statement<E, T> body;

	public StatementForeach(CodePosition position, IMethodScope<E, T> environment, List<SymbolLocal<E, T>> variables, E list)
	{
		super(position, environment);

		this.variables = variables;
		this.list = list;
	}

	public void setBody(Statement<E, T> body)
	{
		this.body = body;
	}

	public List<SymbolLocal<E, T>> getVariables()
	{
		return variables;
	}

	public E getList()
	{
		return list;
	}

	public Statement<E, T> getBody()
	{
		return body;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onForeach(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		FlowBlock<E, T> loop = new FlowBlock<E, T>();
		builder.pushLoop(this, next, loop);
		
		FlowBlock<E, T> bodyEnd = new FlowBlock<E, T>();
		FlowBlock<E, T> bodyStart = body.createFlowBlock(bodyEnd, builder);
		
		builder.pop();
		
		loop.prependInstruction(new ForeachFlowInstruction<E, T>(list, bodyStart, next));
		bodyEnd.addOutgoing(loop);
		return loop;
	}
}
