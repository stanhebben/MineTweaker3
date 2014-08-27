/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.JavaMethodArgument;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionFunction extends Expression {
	private final ZenTypeFunction functionType;
	private final List<Statement> statements;
	
	public ExpressionFunction(ZenPosition position, IScopeMethod environment, List<JavaMethodArgument> arguments, ZenType returnType, List<Statement> statements) {
		super(position, environment);
		
		System.out.println("Function expression: " + arguments.size() + " arguments");
		
		this.statements = statements;
		
		functionType = new ZenTypeFunction(environment, returnType, arguments);
	}

	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		if (type instanceof ZenTypeNative) {
			ZenTypeNative nativeType = (ZenTypeNative) type;
			Class nativeClass = nativeType.getNativeClass();
			if (nativeClass.isInterface() && nativeClass.getMethods().length == 1) {
				// functional interface
				Method method = nativeClass.getMethods()[0];
				if (returnType != ZenTypeAny.INSTANCE
						&& !returnType.canCastImplicit(environment.getType(method.getGenericReturnType()), environment)) {
					environment.error(position, "return type not compatible");
					return new ExpressionInvalid(position);
				}
				if (arguments.size() != method.getParameterTypes().length) {
					environment.error(position, "number of arguments incorrect");
					return new ExpressionInvalid(position);
				}
				for (int i = 0; i < arguments.size(); i++) {
					ZenType argumentType = environment.getType(method.getGenericParameterTypes()[i]);
					if (arguments.get(i).getType() != ZenTypeAny.INSTANCE
							&& !argumentType.canCastImplicit(arguments.get(i).getType(), environment)) {
						environment.error(position, "argument " + i + " doesn't match");
						return new ExpressionInvalid(position);
					}
				}
				
				return new ExpressionJavaLambda(
						position,
						nativeClass,
						arguments,
						statements,
						environment.getType(nativeClass));
			} else {
				environment.error(position, type.toString() + " is not a functional interface");
				return new ExpressionInvalid(position);
			}
		} else {
			environment.error(position, "Cannot cast a function literal to " + type.toString());
			return new ExpressionInvalid(position);
		}
	}

	@Override
	public ZenType getType() {
		return functionType;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		// TODO: implement
		// TODO: make sure the function is compiled properly
		throw new UnsupportedOperationException("not yet implemented");
	}
}
