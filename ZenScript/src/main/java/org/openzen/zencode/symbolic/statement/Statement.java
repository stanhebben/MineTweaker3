package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.FlowGraph;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

public abstract class Statement<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final CodePosition position;
	private final IMethodScope<E, T> scope;

	public Statement(CodePosition position, IMethodScope<E, T> environment)
	{
		this.position = position;
		this.scope = environment;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public IMethodScope<E, T> getScope()
	{
		return scope;
	}

	public boolean isReturn()
	{
		return false;
	}
	
	public abstract <U> U process(IStatementProcessor<E, T, U> processor);
	
	public abstract FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder);
	
	public void validate()
	{
		FlowBuilder<E, T> builder = new FlowBuilder<E, T>();
		FlowBlock<E, T> mainBlock = createFlowBlock(new FlowBlock<E, T>(), builder);
		FlowGraph<E, T> graph = new FlowGraph<E, T>(mainBlock);
		graph.validate(scope);
	}
}
