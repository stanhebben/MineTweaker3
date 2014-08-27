/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.iterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import stanhebben.zenscript.type.IZenIterator;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class IteratorMapKeys implements IZenIterator {
	private final ZenTypeAssociative type;
	private int iterator;

	public IteratorMapKeys(ZenTypeAssociative type) {
		this.type = type;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals) {
		output.invokeInterface(Map.class, "keySet", Set.class);

		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Set.class, "iterator", Iterator.class);
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
		output.store(type.getKeyType().toASMType(), locals[0]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat) {
		output.goTo(repeat);
	}

	@Override
	public void compileEnd(MethodOutput output) {
		
	}

	@Override
	public ZenType getType(int i) {
		return type.getKeyType();
	}
}
