/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.symbolic.unit.SymbolicInterface;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.unit.SymbolicEnum;

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
