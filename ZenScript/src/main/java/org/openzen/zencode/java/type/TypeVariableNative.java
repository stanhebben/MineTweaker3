/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.java.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class TypeVariableNative<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements ITypeVariable<E, T>
{
	private final TypeVariable<?> typeVariable;
	private List<IGenericParameterBound<E, T>> bounds;
	
	public TypeVariableNative(TypeVariable<?> typeVariable)
	{
		this.typeVariable = typeVariable;
		
		for (Type bound : typeVariable.getBounds()) {
			// TODO: add bounds
		}
	}

	@Override
	public List<IGenericParameterBound<E, T>> getBounds()
	{
		return bounds;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 67 * hash + (this.typeVariable != null ? this.typeVariable.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TypeVariableNative other = (TypeVariableNative) obj;
		if (this.typeVariable != other.typeVariable && (this.typeVariable == null || !this.typeVariable.equals(other.typeVariable))) {
			return false;
		}
		return true;
	}
}
