/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.IZenCompiler;
import org.openzen.zencode.ZenClassLoader;
import org.openzen.zencode.annotations.Named;
import org.openzen.zencode.annotations.NotNull;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.Optional;
import org.openzen.zencode.annotations.OptionalDouble;
import org.openzen.zencode.annotations.OptionalFloat;
import org.openzen.zencode.annotations.OptionalInt;
import org.openzen.zencode.annotations.OptionalLong;
import org.openzen.zencode.annotations.OptionalString;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenExpansion;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenMethodStatic;
import org.openzen.zencode.annotations.ZenOperator;
import org.openzen.zencode.annotations.ZenSetter;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.java.field.StaticJavaFieldSymbol;
import org.openzen.zencode.java.member.JavaExpansionCaster;
import org.openzen.zencode.java.member.JavaExpansionGetterMember;
import org.openzen.zencode.java.member.JavaExpansionMethod;
import org.openzen.zencode.java.member.JavaExpansionOperator;
import org.openzen.zencode.java.member.JavaExpansionSetterMember;
import org.openzen.zencode.java.member.JavaExpansionStaticMethod;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.method.JavaMethod;
import org.openzen.zencode.java.method.JavaStaticMethodCallable;
import org.openzen.zencode.java.type.JavaTypeCompiler;
import org.openzen.zencode.parser.IFileLoader;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.symbolic.SymbolicModule;
import org.openzen.zencode.java.util.MethodOutput;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.ModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.StaticMethodSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.generic.ExtendsGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ImplementsGenericParameterBound;
import org.openzen.zencode.util.CodePosition;

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
	private final JavaCompileState compileState;
	private final JavaExpressionCompiler expressionCompiler;
	private final JavaBytecodeCompiler bytecodeCompiler;
	private final TypeRegistry<IJavaExpression> typeRegistry;
	private final IModuleScope<IJavaExpression> systemScope;

	private Map<String, List<IMember<IJavaExpression>>> typeExpansions = new HashMap<>();
	
	public JavaCompiler(IZenCompileEnvironment<IJavaExpression> environment)
	{
		this.environment = environment;
		
		modules = new ArrayList<>();
		typeCompiler = new JavaTypeCompiler();
		bytecodeCompiler = new JavaBytecodeCompiler();
		compileState = new JavaCompileState();
		expressionCompiler = new JavaExpressionCompiler(compileState);
		
		typeRegistry = new TypeRegistry<>();
		systemScope = new ModuleScope<>(environment, expressionCompiler, typeRegistry);
		
		typeRegistry.init(systemScope);
		typeCompiler.init(systemScope);
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
	
	public void addAnnotatedClass(Class<?> cls)
	{
		try {
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof ZenExpansion) {
					String type = ((ZenExpansion) annotation).value();
					
					if (!typeExpansions.containsKey(type))
						typeExpansions.put(type, new ArrayList<>());
					
					List<IMember<IJavaExpression>> expansionMembers = getExpansions(cls);
					typeExpansions.get(type).addAll(expansionMembers);
				} else if (annotation instanceof ZenClass) {
					ITypeDefinition<IJavaExpression> typeDefinition = typeCompiler.getNativeType(CodePosition.NATIVE, cls);
					environment.getRootPackage().put(
							CodePosition.NATIVE,
							environment.getErrorLogger(),
							((ZenClass) annotation).value(),
							typeDefinition);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
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
	public IExpressionCompiler<IJavaExpression> getExpressionCompiler()
	{
		return expressionCompiler;
	}

	public Runnable compile()
	{
		for (String extendedType : typeExpansions.keySet()) {
			IGenericType<IJavaExpression> type = TypeParser.parseDirect(extendedType, systemScope);
			
			for (IMember<IJavaExpression> member : typeExpansions.get(extendedType)) {
				type.addMember(member);
			}
		}
		
		List<SymbolicModule<IJavaExpression>> symbolicModules = new ArrayList<SymbolicModule<IJavaExpression>>();
		for (ParsedModule module : modules) {
			symbolicModules.add(module.compileDefinitions(environment, expressionCompiler, typeRegistry));
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

		MethodOutput constructor = new MethodOutput(compileState, clsMain, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		constructor.start();
		constructor.loadObject(0);
		constructor.invokeSpecial(Object.class, "<init>", void.class);
		constructor.ret();
		constructor.end();

		MethodOutput mainScript = new MethodOutput(compileState, clsMain, Opcodes.ACC_PUBLIC, "run", "()V", null, null);
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

	private List<IMember<IJavaExpression>> getExpansions(Class<?> annotatedClass)
	{
		List<IMember<IJavaExpression>> result = new ArrayList<>();
		
		for (Method method : annotatedClass.getMethods()) {
			String methodName = method.getName();

			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof ZenCaster) {
					checkStatic(method);
					result.add(new JavaExpansionCaster(new JavaMethod(method, this)));
				} else if (annotation instanceof ZenGetter) {
					checkStatic(method);
					ZenGetter getterAnnotation = (ZenGetter) annotation;
					String name = getterAnnotation.value().length() == 0 ? method.getName() : getterAnnotation.value();

					if (method.getParameterTypes().length > 1)
						throw new IllegalArgumentException("Not a valid getter - too many parameters");
					
					result.add(new JavaExpansionGetterMember(name, new JavaMethod(method, this)));
				} else if (annotation instanceof ZenSetter) {
					checkStatic(method);
					ZenSetter setterAnnotation = (ZenSetter) annotation;
					String name = setterAnnotation.value().length() == 0 ? method.getName() : setterAnnotation.value();

					if (method.getParameterTypes().length < 1 || method.getParameterTypes().length > 2)
						throw new IllegalArgumentException("Not a valid setter - must have 1 or 2 parameters");
					
					result.add(new JavaExpansionSetterMember(name, new JavaMethod(method, this)));
				} else if (annotation instanceof ZenOperator) {
					checkStatic(method);
					ZenOperator operatorAnnotation = (ZenOperator) annotation;

					OperatorType operator = operatorAnnotation.value();
					if (operator.getArgumentCount() != method.getParameterTypes().length)
						throw new RuntimeException("Numbor of operator arguments is incorrect");

					result.add(new JavaExpansionOperator(operator, new JavaMethod(method, this)));
				} else if (annotation instanceof ZenMethod) {
					checkStatic(method);
					ZenMethod methodAnnotation = (ZenMethod) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();

					result.add(new JavaExpansionMethod(methodName, new JavaMethod(method, this)));
				} else if (annotation instanceof ZenMethodStatic) {
					checkStatic(method);
					ZenMethodStatic methodAnnotation = (ZenMethodStatic) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();

					result.add(new JavaExpansionStaticMethod(methodName, new JavaMethod(method, this)));
				}
			}
		}
		
		return result;
	}
	
	public MethodHeader<IJavaExpression> getMethodHeader(Method method, String[] knownArgumentNames)
	{
		IGenericType<IJavaExpression> returnType = typeCompiler
						.getNativeType(CodePosition.NATIVE, method.getGenericReturnType());
		
		Type[] genericParameters = method.getGenericParameterTypes();
		AnnotatedType[] annotatedParameters = method.getAnnotatedParameterTypes();
		List<MethodParameter<IJavaExpression>> arguments = new ArrayList<MethodParameter<IJavaExpression>>();
		for (int i = 0; i < genericParameters.length; i++) {
			IGenericType<IJavaExpression> type = typeCompiler
					.getNativeType(CodePosition.NATIVE, genericParameters[i]);
			
			String name = knownArgumentNames == null ? null : knownArgumentNames[i];
			IJavaExpression defaultValue = null;

			IMethodScope<IJavaExpression> staticMethodScope = systemScope.getConstantScope();
			for (Annotation annotation : annotatedParameters[i].getAnnotations()) {
				if (annotation instanceof Optional)
					defaultValue = type.createDefaultValue(null, staticMethodScope);
				else if (annotation instanceof OptionalInt)
				{
					int value = ((OptionalInt) annotation).value();
					
					if (type == typeRegistry.byte_)
						defaultValue = expressionCompiler.constantByte(null, staticMethodScope, (byte) value);
					else if (type == typeRegistry.ubyte)
						defaultValue = expressionCompiler.constantUByte(null, staticMethodScope, value);
					else if (type == typeRegistry.short_)
						defaultValue = expressionCompiler.constantShort(null, staticMethodScope, (short) value);
					else if (type == typeRegistry.ushort)
						defaultValue = expressionCompiler.constantUShort(null, staticMethodScope, value);
					else if (type == typeRegistry.int_)
						defaultValue = expressionCompiler.constantInt(null, staticMethodScope, value);
					else if (type == typeRegistry.uint)
						defaultValue = expressionCompiler.constantUInt(null, staticMethodScope, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalLong)
				{
					long value = ((OptionalLong) annotation).value();
					
					if (type == typeRegistry.long_)
						defaultValue = expressionCompiler.constantLong(null, staticMethodScope, value);
					else if (type == typeRegistry.ulong)
						defaultValue = expressionCompiler.constantULong(null, staticMethodScope, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalFloat)
				{
					float value = ((OptionalFloat) annotation).value();
					
					if (type == typeRegistry.float_)
						defaultValue = expressionCompiler.constantFloat(null, staticMethodScope, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalDouble)
				{
					double value = ((OptionalDouble) annotation).value();
					
					if (type == typeRegistry.double_)
						defaultValue = expressionCompiler.constantDouble(null, staticMethodScope, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalString)
				{
					String value = ((OptionalString) annotation).value();
					
					if (type == typeRegistry.string)
						defaultValue = expressionCompiler.constantString(null, staticMethodScope, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof NotNull)
					type = type.nonNull();
				else if (annotation instanceof Named)
					name = ((Named) annotation).value();
			}

			arguments.add(new MethodParameter<IJavaExpression>(
					new CodePosition(method.getDeclaringClass().getSimpleName() + ":" + method.getName(), 0, 0),
					name,
					type,
					defaultValue));
		}
		
		
		TypeVariable<Method>[] typeVariables = method.getTypeParameters();
		List<GenericParameter<IJavaExpression>> genericMethodParameters = Collections.emptyList();
		if (typeVariables.length > 0) {
			genericMethodParameters = new ArrayList<GenericParameter<IJavaExpression>>();
			
			for (TypeVariable<Method> typeVariable : typeVariables) {
				List<IGenericParameterBound<IJavaExpression>> bounds = new ArrayList<IGenericParameterBound<IJavaExpression>>();
				for (Type typeVariableBound : typeVariable.getBounds()) {
					IGenericType<IJavaExpression> type = typeCompiler
							.getNativeType(CodePosition.SYSTEM, typeVariableBound);
					
					if (type.isInterface())
						bounds.add(new ImplementsGenericParameterBound<>(type));
					else
						bounds.add(new ExtendsGenericParameterBound<>(type));
				}

				GenericParameter<IJavaExpression> parameter
						= new GenericParameter<IJavaExpression>(
								CodePosition.SYSTEM,
								typeVariable.getName(),
								bounds);
				genericMethodParameters.add(parameter);
			}
		}

		return new MethodHeader<>(
				CodePosition.SYSTEM,
				genericMethodParameters,
				returnType,
				arguments,
				method.isVarArgs());
	}

	public IZenSymbol<IJavaExpression> getStaticMethodSymbol(Class<?> cls, String name, Class<?>... parameterTypes)
	{
		IJavaMethod method = JavaMethod.get(this, cls, name, parameterTypes);
		return new StaticMethodSymbol<IJavaExpression>(new JavaStaticMethodCallable(method, method.getHeader().instance()));
	}

	public IZenSymbol<IJavaExpression> getStaticMethodSymbol(Method method)
	{
		IJavaMethod javaMethod = getStaticMethod(method);
		return new StaticMethodSymbol<IJavaExpression>(new JavaStaticMethodCallable(javaMethod, javaMethod.getHeader().instance()));
	}

	public JavaField getStaticField(Class<?> cls, String name)
	{
		try {
			Field field = cls.getField(name);
			return new JavaField(field, typeCompiler.getNativeType(CodePosition.NATIVE, field.getGenericType()));
		} catch (NoSuchFieldException ex) {
			Logger.getLogger(JavaCompiler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} catch (SecurityException ex) {
			Logger.getLogger(JavaCompiler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	public IJavaMethod getStaticMethod(Method method)
	{
		return new JavaMethod(method, this);
	}

	public IZenSymbol<IJavaExpression> getStaticField(Field field)
	{
		return new StaticJavaFieldSymbol(field, typeCompiler.getNativeType(CodePosition.SYSTEM, field.getGenericType()));
	}

	public IZenSymbol<IJavaExpression> getStaticFieldSymbol(Class<?> cls, String name)
	{
		try {
			Field field = cls.getField(name);
			return getStaticField(field);
		} catch (NoSuchFieldException ex) {
			Logger.getLogger(JavaCompiler.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(JavaCompiler.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return null;
	}

	public IZenSymbol<IJavaExpression> getStaticFieldSymbol(Field field)
	{
		return new StaticJavaFieldSymbol(field, typeCompiler.getNativeType(CodePosition.SYSTEM, field.getGenericType()));
	}

	/**
	 * Checks if the given method is static. Throws an exception if not.
	 *
	 * @param method metod to validate
	 */
	private static void checkStatic(Method method)
	{
		if ((method.getModifiers() & Modifier.STATIC) == 0)
			throw new RuntimeException("Expansion method " + method.getName() + " must be static");
	}
}
