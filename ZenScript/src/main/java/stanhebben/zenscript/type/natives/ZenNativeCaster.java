/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import org.openzen.zencode.symbolic.method.IMethod;
import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stanneke
 */
public class ZenNativeCaster {
	private final IMethod method;
	
	public ZenNativeCaster(IMethod method) {
		this.method = method;
	}
	
	public ZenType getReturnType() {
		return method.getMethodHeader().getReturnType();
	}
	
	public void compile(MethodOutput output) {
		if (method.isStatic()) {
			method.invokeStatic(output);
		} else {
			method.invokeVirtual(output);
		}
	}
	
	public void compileAnyCanCastImplicit(ZenType type, MethodOutput output, IScopeGlobal environment, int localClass) {
		String casterAny = method.getMethodHeader().getReturnType().getAnyClassName();
		if (casterAny == null) {
			// TODO: make sure no type ever does this
			return;
		}

		Label skip = new Label();
		output.loadObject(localClass);
		output.invokeStatic(casterAny, "rtCanCastImplicit", "(Ljava/lang/Class;)Z");
		output.ifEQ(skip);
		output.iConst1();
		output.returnInt();
		output.label(skip);
	}
	
	public void compileAnyCast(ZenType type, MethodOutput output, IScopeGlobal environment, int localValue, int localClass) {
		Label skip = new Label();
		output.loadObject(localClass);
		output.constant(method.getReturnType().toASMType());
		output.ifACmpNe(skip);
		output.load(type.toASMType(), localValue);
		
		compile(output);
		
		output.returnType(method.getReturnType().toASMType());
		output.label(skip);
		
		String casterAny = method.getReturnType().getAnyClassName();
		if (casterAny == null)
			// TODO: make sure this isn't necessary
			return;

		Label skip2 = new Label();
		output.loadObject(localClass);
		output.invokeStatic(casterAny, "rtCanCastImplicit", "(Ljava/lang/Class;)Z");
		output.ifEQ(skip2);
		output.load(type.toASMType(), localValue);
		
		compile(output);
		
		output.returnType(method.getReturnType().toASMType());
		output.label(skip2);
	}
}
