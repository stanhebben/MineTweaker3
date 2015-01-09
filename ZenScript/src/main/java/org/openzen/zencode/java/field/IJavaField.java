/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.field;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.field.IField;

/**
 *
 * @author Stan
 */
public interface IJavaField extends IField<IJavaExpression>
{
	public String getInternalClassName();
	
	public String getFieldName();
}
