/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.ImportFlowInstruction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ImportStatement<E extends IPartialExpression<E>> extends Statement<E>
{
	private final List<String> importName;
	private final String rename;
	private final boolean wildcard;
	
	public ImportStatement(CodePosition position, IMethodScope<E> scope, List<String> importName, String rename, boolean wildcard)
	{
		super(position, scope);
		
		this.importName = importName;
		this.rename = rename;
		this.wildcard = wildcard;
	}

	public List<String> getImportName()
	{
		return importName;
	}

	public String getRename()
	{
		return rename;
	}

	public boolean isWildcard()
	{
		return wildcard;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onImport(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		return next.prependInstruction(new ImportFlowInstruction<E>(this));
	}
}
