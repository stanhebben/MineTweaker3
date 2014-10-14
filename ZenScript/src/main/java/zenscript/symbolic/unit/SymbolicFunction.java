/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.unit;

import java.util.HashMap;
import java.util.Map;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionGetInstanceField;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenTypeFunction;
import zenscript.symbolic.field.GeneratedField;
import zenscript.symbolic.method.MethodHeader;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class SymbolicFunction {
	private final SymbolLocal localThis;
	private final ZenTypeFunction type;
	private final String generatedClassName;
	
	private final Map<SymbolLocal, Capture> captured = new HashMap<SymbolLocal, Capture>();
	
	public SymbolicFunction(MethodHeader header) {
		this.type = new ZenTypeFunction(header);
		generatedClassName = header.getReturnType().getScope().makeClassName();
		
		localThis = new SymbolLocal(type, true);
	}
	
	public ZenTypeFunction getType() {
		return type;
	}
	
	public MethodHeader getHeader() {
		return type.getHeader();
	}
	
	public IPartialExpression addCapture(ZenPosition position, IScopeMethod scope, SymbolLocal local) {
		if (!captured.containsKey(local)) {
			GeneratedField field = new GeneratedField(
					local.getType(),
					generatedClassName,
					"__capture" + captured.size(),
					true,
					false);
			
			captured.put(local, new Capture(local, field));
		}
		
		return new ExpressionGetInstanceField(position, scope, new ExpressionLocalGet(position, scope, localThis), captured.get(local).field);
	}
	
	private static class Capture {
		private final SymbolLocal local;
		private final GeneratedField field;
		
		public Capture(SymbolLocal local, GeneratedField field) {
			this.local = local;
			this.field = field;
		}
	}
}
