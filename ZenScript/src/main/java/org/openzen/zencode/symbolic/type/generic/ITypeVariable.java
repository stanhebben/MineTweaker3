/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.generic;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ITypeVariable<E extends IPartialExpression<E>> 
{
	public List<IGenericParameterBound<E>> getBounds();
}
