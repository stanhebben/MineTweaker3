package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public class StatementBlock<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T> {
	private final List<Statement<E, T>> statements;
	
	public StatementBlock(CodePosition position, IScopeMethod<E, T> environment, List<Statement<E, T>> statements)
	{
		super(position, environment);
		
		this.statements = statements;
	}
	
	public List<Statement<E, T>> getStatements()
	{
		return statements;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onBlock(this);
	}
}
