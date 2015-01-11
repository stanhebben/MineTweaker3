/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.IZenCompiler;
import org.openzen.zencode.ZenClassLoader;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.JavaTypeCompiler;
import org.openzen.zencode.parser.IFileLoader;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.symbolic.SymbolicModule;
import org.openzen.zencode.java.util.MethodOutput;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;

/**
 *
 * @author Stan
 */
public class JavaCompiler implements IZenCompiler<IJavaExpression>
{
	private final IZenCompileEnvironment<IJavaExpression> environment;
	
	private final List<ParsedModule> modules;
	private File debugOutputDirectory;
	private final JavaTypeCompiler typeCompiler;
	private final JavaExpressionCompiler expressionCompiler;
	private final JavaBytecodeCompiler bytecodeCompiler;

	public JavaCompiler(IZenCompileEnvironment<IJavaExpression> environment)
	{
		this.environment = environment;
		
		modules = new ArrayList<ParsedModule>();
		typeCompiler = new JavaTypeCompiler(environment);
		bytecodeCompiler = new JavaBytecodeCompiler();
		
		expressionCompiler = new JavaExpressionCompiler();
	}

	public void setDebugOutputDirectory(File file)
	{
		if (!file.exists())
			file.mkdirs();

		debugOutputDirectory = file;
	}

	public ParsedModule createAndAddModule(String name, IFileLoader fileLoader)
	{
		ParsedModule result = new ParsedModule(environment.getErrorLogger(), fileLoader, name);
		modules.add(result);
		return result;
	}

	public void addModule(ParsedModule module)
	{
		modules.add(module);
	}

	public IZenCompileEnvironment<IJavaExpression> getCompileEnvironment()
	{
		return environment;
	}
	
	@Override
	public ITypeCompiler<IJavaExpression> getTypeCompiler()
	{
		return null; // TODO: phase this out?
	}
	
	@Override
	public IExpressionCompiler<IJavaExpression> getExpressionCompiler()
	{
		return expressionCompiler;
	}

	public Runnable compile()
	{
		List<SymbolicModule<IJavaExpression>> symbolicModules = new ArrayList<SymbolicModule<IJavaExpression>>();
		for (ParsedModule module : modules) {
			symbolicModules.add(module.compileDefinitions(environment, this));
		}

		for (SymbolicModule<IJavaExpression> symbolicModule : symbolicModules) {
			symbolicModule.compileMembers();
		}

		for (SymbolicModule<IJavaExpression> symbolicModule : symbolicModules) {
			symbolicModule.compileMembers();
		}
		
		for (SymbolicModule<IJavaExpression> symbolicModule : symbolicModules) {
			symbolicModule.validate();
		}
		
		// everything is compiled to symbolic level and validated... now to compile it to java bytecode!
		
		for (SymbolicModule<IJavaExpression> symbolicModule : symbolicModules) {
			for (ISymbolicDefinition<IJavaExpression> definition : symbolicModule.getDefinitions()) {
				// TODO: complete this
			}
		}

		ClassWriter clsMain = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		clsMain.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "__ZenMain__", null, internal(Object.class), new String[]{internal(Runnable.class)});

		MethodOutput constructor = new MethodOutput(typeCompiler, clsMain, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		constructor.start();
		constructor.loadObject(0);
		constructor.invokeSpecial(Object.class, "<init>", void.class);
		constructor.ret();
		constructor.end();

		MethodOutput mainScript = new MethodOutput(typeCompiler, clsMain, Opcodes.ACC_PUBLIC, "run", "()V", null, null);
		mainScript.start();

		for (SymbolicModule<IJavaExpression> module : symbolicModules) {
			throw new UnsupportedOperationException("TODO");
			// TODO
			//module.compileDefinitions(mainScript);
		}

		mainScript.ret();
		mainScript.end();
		clsMain.visitEnd();
		bytecodeCompiler.putClass("__ZenMain__", clsMain.toByteArray());

		if (debugOutputDirectory != null)
			bytecodeCompiler.writeClassesToFolder(debugOutputDirectory);

		return getMain();
	}

	/**
	 * Retrieves the main runnable. Running this runnable will execute the
	 * content of the given module.
	 *
	 * @return main runnable
	 */
	private Runnable getMain()
	{
		ZenClassLoader classLoader = new ZenClassLoader(getClass().getClassLoader(), bytecodeCompiler.getClasses());
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
