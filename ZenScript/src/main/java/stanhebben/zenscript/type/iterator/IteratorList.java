/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.iterator;

import java.util.Iterator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.IZenIterator;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */

	
public class IteratorList implements IZenIterator {
	private final IScopeGlobal environment;
	private final ZenType iteratorType;
	private int iterator;

	public IteratorList(IScopeGlobal environment, ZenType iteratorType) {
		this.environment = environment;
		this.iteratorType = iteratorType;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals) {
		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Iterable.class, "iterator", Iterator.class);
		output.storeObject(iterator);
		output.iConst0();
		output.storeInt(locals[0]);
	}

	@Override
	public void compilePreIterate(MethodOutput output, int[] locals, Label exit) {
		output.dup();
		output.invokeInterface(
				Iterator.class,
				"hasNext",
				boolean.class);
		output.ifEQ(exit);

		output.dup();
		output.invokeInterface(Iterator.class, "next", Object.class);
		output.store(iteratorType.toASMType(), locals[1]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat) {
		output.iinc(locals[0]);
		output.goTo(repeat);
	}

	@Override
	public void compileEnd(MethodOutput output) {
		output.pop();
	}

	@Override
	public ZenType getType(int i) {
		return i == 0 ? environment.getTypes().INT : iteratorType;
	}
}
