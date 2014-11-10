/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenClassLoader;
import org.openzen.zencode.symbolic.SymbolicModule;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.util.MethodOutput;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;

/**
 *
 * @author Stan
 */
public class ParserEnvironment {
	private final List<ParsedModule> modules;
	private final IScopeGlobal global;
	
	public ParserEnvironment(IScopeGlobal global)
	{
		modules = new ArrayList<ParsedModule>();
		this.global = global;
	}
	
	public ParsedModule createAndAddModule(String name, IFileLoader fileLoader) {
		ParsedModule result = new ParsedModule(this, fileLoader, name);
		modules.add(result);
		return result;
	}
	
	public void addModule(ParsedModule module)
	{
		modules.add(module);
	}
	
	public IZenCompileEnvironment getCompileEnvironment() {
		return global.getEnvironment();
	}
	
	public Runnable compile()
	{
		List<SymbolicModule> symbolicModules = new ArrayList<SymbolicModule>();
		for (ParsedModule module : modules)
		{
			SymbolicModule symbolicModule = new SymbolicModule(global);
			module.compileUnits(symbolicModule);
			symbolicModules.add(symbolicModule);
		}
		
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).compileFunctions(symbolicModules.get(i));
		}
		
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).compileContents(symbolicModules.get(i));
		}
		
		ClassWriter clsMain = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		clsMain.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "__ZenMain__", null, internal(Object.class), new String[] {internal(Runnable.class)});
		
		MethodOutput constructor = new MethodOutput(clsMain, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		constructor.start();
		constructor.loadObject(0);
		constructor.invokeSpecial(Object.class, "<init>", void.class);
		constructor.ret();
		constructor.end();
		
		MethodOutput mainScript = new MethodOutput(clsMain, Opcodes.ACC_PUBLIC, "run", "()V", null, null);
		mainScript.start();
		
		for (SymbolicModule module : symbolicModules) {
			module.compile(mainScript);
		}
		
		mainScript.ret();
		mainScript.end();
		clsMain.visitEnd();
		global.putClass("__ZenMain__", clsMain.toByteArray());
		
		return getMain();
	}
	
	/**
	 * Retrieves the main runnable. Running this runnable will execute the content
	 * of the given module.
	 * 
	 * @return main runnable
	 */
	private Runnable getMain() {
		ZenClassLoader classLoader = new ZenClassLoader(getClass().getClassLoader(), global.getClasses());
		try {
			return (Runnable) classLoader.loadClass("__ZenMain__").newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Could not load scripts", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not load scripts", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not load scripts", e);
		}
	}
}
