/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.generic;

import java.lang.reflect.TypeVariable;

/**
 *
 * @author Stan
 */
public class TypeVariableNative implements ITypeVariable {
	private final TypeVariable typeVariable;
	
	public TypeVariableNative(TypeVariable typeVariable) {
		this.typeVariable = typeVariable;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + (this.typeVariable != null ? this.typeVariable.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
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
