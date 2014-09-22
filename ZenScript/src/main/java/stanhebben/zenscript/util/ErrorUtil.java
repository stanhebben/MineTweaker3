/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.util;

import java.util.List;
import stanhebben.zenscript.expression.Expression;
import zenscript.symbolic.method.IMethod;

/**
 *
 * @author Stan
 */
public class ErrorUtil {
	private ErrorUtil() {}
	
	/**
	 * If a set of methods is available and none matches, this method creates
	 * a suitable message.
	 * 
	 * @param methods matching methods
	 * @param arguments calling arguments
	 * @return return value
	 */
	public static String methodMatchingError(List<IMethod> methods, Expression... arguments) {
		if (methods.isEmpty()) {
			return "no method with that name available";
		} else {
			StringBuilder message = new StringBuilder();
			if (methods.size() == 1) {
				message.append("a method ");
			} else {
				message.append(methods.size()).append(" methods ");
			}
			message.append("available but none matches the parameters (");
			boolean first = true;
			for (Expression value : arguments) {
				if (first) {
					first = false;
				} else {
					message.append(", ");
				}
				message.append(value.getType().toString());
			}
			message.append(")");
			return message.toString();
		}
	}
}
