/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.formatting;

import zenscript.annotations.OperatorType;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenOperator;

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
