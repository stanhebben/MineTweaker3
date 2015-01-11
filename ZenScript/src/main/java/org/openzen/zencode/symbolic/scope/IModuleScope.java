/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IModuleScope<E extends IPartialExpression<E>>
{
	public IZenCompileEnvironment<E> getEnvironment();

	public ITypeCompiler<E> getTypeCompiler();

	public IExpressionCompiler<E> getExpressionCompiler();

	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment);

	public IZenSymbol<E> getSymbol(String name);
	
	public void putValue(String name, IZenSymbol<E> value, CodePosition position);
	
	public boolean contains(String name);
	
	public ICodeErrorLogger<E> getErrorLogger();
	
	public ZenPackage<E> getRootPackage();
	
	public IMethodScope<E> getConstantScope();
	
	public AccessScope getAccessScope();
	
	public TypeCapture<E> getTypeCapture();
	
	public void putImport(String name, IZenSymbol<E> symbol, CodePosition position);
}
