/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class JavaMethodArgument {
	private final String name;
	private final ZenType type;
	private final Expression defaultValue;
	
	public JavaMethodArgument(String name, ZenType type, Expression defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	public ZenType getType() {
		return type;
	}
	
	public Expression getDefaultValue() {
		return defaultValue;
	}
}
