/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import zenscript.parser.elements.ParsedFunctionArgument;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import stanhebben.zenscript.type.natives.JavaMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionJavaMethodStatic extends Expression {
	private final JavaMethod method;
	private final ZenType type;
	
	public ExpressionJavaMethodStatic(ZenPosition position, JavaMethod method, IScopeGlobal environment) {
		super(position);
		
		this.method = method;
		
		List<ParsedFunctionArgument> arguments = new ArrayList<ParsedFunctionArgument>();
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			arguments.add(new ParsedFunctionArgument("p" + i, environment.getType(method.getMethod().getGenericParameterTypes()[i])));
		}
		
		this.type = new ZenTypeFunction(environment.getType(method.getMethod().getGenericReturnType()), arguments);
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, IScopeMethod environment) {
		// TODO: compile
	}
}
