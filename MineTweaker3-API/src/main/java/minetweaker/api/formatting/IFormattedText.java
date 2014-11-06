/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.formatting;

import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenOperator;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.formatting.IFormattedText")
public interface IFormattedText {
	@ZenOperator(OperatorType.ADD)
	public IFormattedText add(IFormattedText other);
	
	@ZenOperator(OperatorType.CAT)
	public IFormattedText cat(IFormattedText other);
}
