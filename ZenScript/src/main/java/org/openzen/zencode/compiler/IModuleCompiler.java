/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.symbolic.unit.SymbolicEnum;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.symbolic.unit.SymbolicInterface;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IModuleCompiler<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	public void compileScript(List<Statement<E, T>> statements);
	
	public void compileFunction(SymbolicFunction<E, T> function);
	
	public void compileClass(SymbolicClass _class);
	
	public void compileInterface(SymbolicInterface _interface);
	
	public void compileStruct(SymbolicStruct struct);
	
	public void compileEnum(SymbolicEnum _enum);
}
