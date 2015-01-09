/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 * @param <U>
 */
public interface IStatementProcessor<E extends IPartialExpression<E, T>, T extends IZenType<E, T>, U>
{
	public U onBlock(StatementBlock<E, T> statement);
	
	public U onBreak(StatementBreak<E, T> statement);
	
	public U onContinue(StatementContinue<E, T> statement);
	
	public U onDoWhile(StatementDoWhile<E, T> statement);
	
	public U onExpression(StatementExpression<E, T> statement);
	
	public U onForeach(StatementForeach<E, T> statement);
	
	public U onIf(StatementIf<E, T> statement);
	
	public U onEmpty(StatementNull<E, T> statement);
	
	public U onReturn(StatementReturn<E, T> statement);
	
	public U onSwitch(StatementSwitch<E, T> statement);
	
	public U onVar(StatementVar<E, T> statement);
	
	public U onWhile(StatementWhile<E, T> statement);
	
	public U onTryCatch(TryStatement<E, T> statement);
	
	public U onThrow(ThrowStatement<E, T> statement);
	
	public U onSynchronized(SynchronizedStatement<E, T> statement);
}
