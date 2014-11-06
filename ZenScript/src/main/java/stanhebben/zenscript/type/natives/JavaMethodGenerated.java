/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenTypeFunction;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.symbolic.method.AbstractMethod;
import org.openzen.zencode.symbolic.method.MethodArgument;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 *
 * @author Stan
 */
public class JavaMethodGenerated extends AbstractMethod {
	private final boolean isStatic;
	private final boolean isInterface;
	private final String owner;
	private final String name;
	
	private final String descriptor;
	private final ZenTypeFunction functionType;

	public JavaMethodGenerated(
			boolean isStatic,
			boolean isInterface,
			String owner,
			String name,
			MethodHeader header) {
		this.isStatic = isStatic;
		this.isInterface = isInterface;
		this.owner = owner;
		this.name = name;
		
		StringBuilder descriptorString = new StringBuilder();
		descriptorString.append('(');
		for (MethodArgument argument : header.getArguments()) {
			descriptorString.append(argument.getType().getSignature());
		}
		descriptorString.append(')');
		descriptorString.append(header.getReturnType().getSignature());
		descriptor = descriptorString.toString();
		
		functionType = new ZenTypeFunction(header);
	}
	
	@Override
	public boolean isStatic() {
		return isStatic;
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
	public ZenTypeFunction getFunctionType()
	{
		return functionType;
	}
}
