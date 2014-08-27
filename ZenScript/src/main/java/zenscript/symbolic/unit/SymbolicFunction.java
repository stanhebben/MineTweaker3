/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.JavaMethodArgument;

/**
 *
 * @author Stan
 */
public class SymbolicFunction {
	private final ZenType returnType;
	private final List<JavaMethodArgument> arguments;
	
	private List<SymbolLocal> captured = new ArrayList<SymbolLocal>();
	
	public SymbolicFunction(ZenType returnType, List<JavaMethodArgument> arguments) {
		this.returnType = returnType;
		this.arguments = arguments;
	}
	
	public IPartialExpression addCapture(SymbolLocal local) {
		captured.add(local);
		
	}
}
