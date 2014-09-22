/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import zenscript.symbolic.method.MethodArgument;
import zenscript.symbolic.method.IMethod;
import java.util.HashMap;
import java.util.Map;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class JavaMethodGenerated implements IMethod {
	private final boolean isStatic;
	private final boolean isInterface;
	private final boolean isVarargs;
	private final String owner;
	private final String name;
	
	private final MethodArgument[] arguments;
	private final Map<String, Integer> argumentByName;
	private final boolean[] optional;
	private final ZenType returnType;
	
	private final String descriptor;

	public JavaMethodGenerated(
			boolean isStatic,
			boolean isInterface,
			boolean isVarargs,
			String owner,
			String name,
			ZenType returnType,
			MethodArgument[] arguments,
			boolean[] optional) {
		this.isStatic = isStatic;
		this.isInterface = isInterface;
		this.isVarargs = isVarargs;
		this.owner = owner;
		this.name = name;
		
		this.returnType = returnType;
		this.arguments = arguments;
		this.optional = optional;
		
		StringBuilder descriptorString = new StringBuilder();
		descriptorString.append('(');
		for (MethodArgument argument : arguments) {
			descriptorString.append(argument.getType().getSignature());
		}
		descriptorString.append(')');
		descriptorString.append(returnType.getSignature());
		descriptor = descriptorString.toString();
		
		argumentByName = new HashMap<String, Integer>();
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].getName() != null) {
				argumentByName.put(arguments[i].getName(), i);
			}
		}
	}
	
	@Override
	public boolean isStatic() {
		return isStatic;
	}
	
	@Override
	public boolean isVarargs() {
		return isVarargs;
	}
	
	@Override
	public boolean accepts(int numArguments) {
		if (numArguments > arguments.length) {
			return isVarargs;
		} if (numArguments == arguments.length) {
			return true;
		} else {
			for (int i = numArguments; i < arguments.length; i++) {
				if (!optional[i]) return false;
			}
			return true;
		}
	}
	
	@Override
	public void invokeVirtual(MethodOutput output) {
		if (isStatic) {
			throw new UnsupportedOperationException("Cannot call static methods as virtual");
		} else {
			if (isInterface) {
				output.invokeInterface(owner, name, descriptor);
			} else {
				output.invokeVirtual(owner, name, descriptor);
			}
		}
	}
	
	@Override
	public void invokeStatic(MethodOutput output) {
		if (!isStatic) {
			throw new UnsupportedOperationException("Cannot call virtual methods as static");
		} else {
			output.invokeStatic(owner, name, descriptor);
		}
	}
	
	@Override
	public void invokeSpecial(MethodOutput output) {
		if (isStatic) {
			throw new UnsupportedOperationException("Cannot call static methods as special");
		} else {
			output.invokeSpecial(owner, name, descriptor);
		}
	}
	
	@Override
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments) {
		if (isStatic) {
			throw new UnsupportedOperationException("Cannot call static methods as virtual");
		} else {
			receiver.compile(true, output);
			
			for (Expression argument : arguments) {
				argument.compile(true, output);
			}
			
			if (isInterface) {
				output.invokeInterface(owner, name, descriptor);
			} else {
				output.invokeVirtual(owner, name, descriptor);
			}
		}
	}

	@Override
	public void invokeStatic(MethodOutput output, Expression[] arguments) {
		if (!isStatic) {
			throw new UnsupportedOperationException("Cannot call virtual methods as static");
		} else {
			for (Expression argument : arguments) {
				argument.compile(true, output);
			}
			
			output.invokeStatic(owner, name, descriptor);
		}
	}

	@Override
	public ZenType getReturnType() {
		return returnType;
	}

	@Override
	public MethodArgument[] getArguments() {
		return arguments;
	}

	@Override
	public int getArgumentIndex(String name) {
		return argumentByName.containsKey(name) ? argumentByName.get(name) : -1;
	}
}
