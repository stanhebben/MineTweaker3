/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public interface IParsedType {
	public ZenType compile(IScopeGlobal environment);
}
