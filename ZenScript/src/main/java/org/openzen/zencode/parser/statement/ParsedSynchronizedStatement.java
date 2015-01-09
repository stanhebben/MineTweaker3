/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.statement.SynchronizedStatement;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedSynchronizedStatement extends ParsedStatement
{
	private final ParsedStatement contents;
	
	public ParsedSynchronizedStatement(CodePosition position, ParsedStatement contents)
	{
		super(position);
		
		this.contents = contents;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> Statement<E, T> compile(IMethodScope<E, T> scope)
	{
		return new SynchronizedStatement<E, T>(getPosition(), scope, contents.compile(scope));
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> void compileSwitch(IMethodScope<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
