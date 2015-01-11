/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.ImportStatement;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ImportFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final ImportStatement<E> importStatement;
	
	public ImportFlowInstruction(ImportStatement<E> importStatement)
	{
		this.importStatement = importStatement;
	}

	@Override
	public boolean doesFallthough()
	{
		return true;
	}

	@Override
	public void validate(IMethodScope<E> scope)
	{
		if (importStatement.isWildcard() && importStatement.getRename() != null)
			scope.getErrorLogger().errorNamedWildcardImport(importStatement.getPosition(), importStatement.getImportName());
	}
}
