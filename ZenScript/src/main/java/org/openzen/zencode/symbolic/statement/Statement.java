package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.FlowGraph;
import org.openzen.zencode.util.CodePosition;

public abstract class Statement<E extends IPartialExpression<E>>
{
	private final CodePosition position;
	private final IMethodScope<E> scope;

	public Statement(CodePosition position, IMethodScope<E> environment)
	{
		this.position = position;
		this.scope = environment;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public IMethodScope<E> getScope()
	{
		return scope;
	}

	public boolean isReturn()
	{
		return false;
	}
	
	public abstract <U> U process(IStatementProcessor<E, U> processor);
	
	public abstract FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder);
	
	public void validate()
	{
		FlowBuilder<E> builder = new FlowBuilder<E>();
		FlowBlock<E> mainBlock = createFlowBlock(new FlowBlock<E>(), builder);
		FlowGraph<E> graph = new FlowGraph<E>(mainBlock);
		graph.validate(scope);
	}
}
