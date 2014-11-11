/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.iterator;

import java.util.Iterator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import stanhebben.zenscript.type.IZenIterator;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class IteratorIterable implements IZenIterator {
	private final ZenType iteratorType;
	private int iterator;

	public IteratorIterable(ZenType iteratorType) {
		this.iteratorType = iteratorType;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals) {
		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Iterable.class, "iterator", Iterator.class);
		output.storeObject(iterator);
	}

	@Override
	public void compilePreIterate(MethodOutput output, int[] locals, Label exit) {
		output.loadObject(iterator);
		output.invokeInterface(
				Iterator.class,
				"hasNext",
				boolean.class);
		output.ifEQ(exit);

		output.loadObject(iterator);
		output.invokeInterface(Iterator.class, "next", Object.class);
		output.checkCast(iteratorType.toASMType().getInternalName());
		output.store(iteratorType.toASMType(), locals[0]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat) {
		output.goTo(repeat);
	}

	@Override
	public ZenType getType(int i) {
		return iteratorType;
	}

	@Override
	public void compileEnd(MethodOutput output) {
		output.pop();
	}
}
