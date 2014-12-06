/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.iterator.IJavaIterator;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public interface IJavaType extends IZenType<IJavaExpression, IJavaType>
{
	public String getSignature();
	
	public boolean isLarge();
	
	public Type toASMType();
	
	public IJavaIterator getIterator(int variables);
}
