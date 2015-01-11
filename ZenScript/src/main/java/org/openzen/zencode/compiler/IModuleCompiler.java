/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.definition.SymbolicClass;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.definition.SymbolicInterface;
import org.openzen.zencode.symbolic.definition.SymbolicStruct;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.definition.SymbolicEnum;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IModuleCompiler<E extends IPartialExpression<E>>
{
	public void compileScript(List<Statement<E>> statements);
	
	public void compileFunction(SymbolicFunction<E> function);
	
	public void compileClass(SymbolicClass<E> _class);
	
	public void compileInterface(SymbolicInterface<E> _interface);
	
	public void compileStruct(SymbolicStruct<E> struct);
	
	public void compileEnum(SymbolicEnum<E> _enum);
}
