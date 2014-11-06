/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.symbolic.unit.SymbolicEnum;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.symbolic.unit.SymbolicInterface;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;
import stanhebben.zenscript.statements.Statement;

/**
 *
 * @author Stan
 */
public interface IModuleCompiler
{
	public void compileScript(List<Statement> statements);
	
	public void compileFunction(SymbolicFunction function);
	
	public void compileClass(SymbolicClass _class);
	
	public void compileInterface(SymbolicInterface _interface);
	
	public void compileStruct(SymbolicStruct struct);
	
	public void compileEnum(SymbolicEnum _enum);
}
