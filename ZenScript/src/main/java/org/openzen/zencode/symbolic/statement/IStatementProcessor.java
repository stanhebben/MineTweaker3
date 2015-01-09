/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 * @param <U>
 */
public interface IStatementProcessor<E extends IPartialExpression<E>, U>
{
	public U onBlock(StatementBlock<E> statement);
	
	public U onBreak(StatementBreak<E> statement);
	
	public U onContinue(StatementContinue<E> statement);
	
	public U onDoWhile(StatementDoWhile<E> statement);
	
	public U onExpression(StatementExpression<E> statement);
	
	public U onForeach(StatementForeach<E> statement);
	
	public U onIf(StatementIf<E> statement);
	
	public U onEmpty(StatementNull<E> statement);
	
	public U onReturn(StatementReturn<E> statement);
	
	public U onSwitch(StatementSwitch<E> statement);
	
	public U onVar(StatementVar<E> statement);
	
	public U onWhile(StatementWhile<E> statement);
	
	public U onTryCatch(TryStatement<E> statement);
	
	public U onThrow(ThrowStatement<E> statement);
	
	public U onSynchronized(SynchronizedStatement<E> statement);
}
