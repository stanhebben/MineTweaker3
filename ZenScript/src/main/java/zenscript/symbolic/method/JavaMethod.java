/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zenscript.symbolic.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionFloat;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import stanhebben.zenscript.type.natives.JavaMethodGenerated;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.annotations.Named;
import zenscript.annotations.NotNull;
import zenscript.annotations.Optional;
import zenscript.annotations.OptionalDouble;
import zenscript.annotations.OptionalFloat;
import zenscript.annotations.OptionalInt;
import zenscript.annotations.OptionalLong;
import zenscript.annotations.OptionalString;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 */
public class JavaMethod extends AbstractMethod
{
	public static final int PRIORITY_INVALID = -1;
	public static final int PRIORITY_LOW = 1;
	public static final int PRIORITY_MEDIUM = 2;
	public static final int PRIORITY_HIGH = 3;

	public static IMethod get(TypeRegistry types, Class cls, String name, Class... parameterTypes)
	{
		try {
			Method method = cls.getMethod(name, parameterTypes);
			if (method == null)
				throw new RuntimeException("method " + name + " not found in class " + cls.getName());
			return new JavaMethod(method, types, TypeCapture.EMPTY);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("method " + name + " not found in class " + cls.getName(), ex);
		} catch (SecurityException ex) {
			throw new RuntimeException("method retrieval not permitted", ex);
		}
	}

	public static IMethod getStatic(String owner, String name, ZenType returnType, MethodArgument... arguments)
	{
		return new JavaMethodGenerated(true, false, owner, name, new MethodHeader(returnType, Arrays.asList(arguments), false));
	}

	public static IMethod getStatic(String owner, String name, ZenType returnType, ZenType... arguments)
	{
		MethodArgument[] convertedArguments = new MethodArgument[arguments.length];
		for (int i = 0; i < convertedArguments.length; i++) {
			convertedArguments[i] = new MethodArgument(null, arguments[i], null);
		}
		return getStatic(owner, name, returnType, convertedArguments);
	}

	public static IMethod get(TypeRegistry types, Method method)
	{
		return get(types, method, TypeCapture.EMPTY);
	}

	public static IMethod get(TypeRegistry types, Method method, TypeCapture capture)
	{
		return new JavaMethod(method, types, capture);
	}

	
	private final Method method;
	private final ZenTypeFunction functionType;

	public JavaMethod(Method method, TypeRegistry types, TypeCapture capture)
	{
		this(method, types, null, capture);
	}

	public JavaMethod(Method method, TypeRegistry types, String[] argumentNames, TypeCapture capture)
	{
		this.method = method;

		ZenType returnType = types.getNativeType(null, method.getGenericReturnType(), capture);

		Type[] genericParameters = method.getGenericParameterTypes();
		List<MethodArgument> arguments = new ArrayList<MethodArgument>();
		for (int i = 0; i < genericParameters.length; i++) {
			ZenType type = types.getNativeType(null, genericParameters[i], capture);
			String name = argumentNames == null || i >= argumentNames.length ? null : argumentNames[i];
			Expression defaultValue = null;

			IScopeMethod environment = types.getStaticGlobalEnvironment();
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof Optional)
					defaultValue = type.defaultValue(null, environment);
				else if (annotation instanceof OptionalInt)
					if (type == types.BYTE || type == types.SHORT || type == types.INT)
						defaultValue = new ExpressionInt(null, environment, ((OptionalInt) annotation).value(), type);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				else if (annotation instanceof OptionalLong)
					if (type == types.LONG)
						defaultValue = new ExpressionInt(null, environment, ((OptionalLong) annotation).value(), types.LONG);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				else if (annotation instanceof OptionalFloat)
					if (type == types.FLOAT)
						defaultValue = new ExpressionFloat(null, environment, ((OptionalFloat) annotation).value(), types.FLOAT);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				else if (annotation instanceof OptionalDouble)
					if (type == types.DOUBLE)
						defaultValue = new ExpressionFloat(null, environment, ((OptionalDouble) annotation).value(), types.DOUBLE);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				else if (annotation instanceof OptionalString)
					if (type == types.STRING)
						defaultValue = new ExpressionString(null, environment, ((OptionalString) annotation).value());
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				else if (annotation instanceof NotNull)
					type = type.nonNull();
				else if (annotation instanceof Named)
					name = ((Named) annotation).value();
			}

			arguments.add(new MethodArgument(name, type, defaultValue));
		}

		functionType = new ZenTypeFunction(new MethodHeader(returnType, arguments, method.isVarArgs()));
	}

	@Override
	public boolean isStatic()
	{
		return (method.getModifiers() & Modifier.STATIC) > 0;
	}

	@Override
	public void invokeVirtual(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Method is static");
		else
			if (method.getDeclaringClass().isInterface())
				output.invokeInterface(
						method.getDeclaringClass(),
						method.getName(),
						method.getReturnType(),
						method.getParameterTypes());
			else
				output.invokeVirtual(
						method.getDeclaringClass(),
						method.getName(),
						method.getReturnType(),
						method.getParameterTypes());
	}

	@Override
	public void invokeStatic(MethodOutput output)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Method is not static");
		else
			output.invokeStatic(
					method.getDeclaringClass(),
					method.getName(),
					method.getReturnType(),
					method.getParameterTypes());
	}

	@Override
	public void invokeSpecial(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Method is static");
		else
			output.invokeSpecial(
					method.getDeclaringClass(),
					method.getName(),
					method.getReturnType(),
					method.getParameterTypes());
	}

	@Override
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Method is static");
		else {
			receiver.compile(true, output);

			for (Expression argument : arguments) {
				argument.compile(true, output);
			}

			if (method.getDeclaringClass().isInterface())
				output.invokeInterface(
						method.getDeclaringClass(),
						method.getName(),
						method.getReturnType(),
						method.getParameterTypes());
			else
				output.invokeVirtual(
						method.getDeclaringClass(),
						method.getName(),
						method.getReturnType(),
						method.getParameterTypes());
		}
	}

	@Override
	public void invokeStatic(MethodOutput output, Expression[] arguments)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Method is not static");
		else {
			for (Expression argument : arguments) {
				argument.compile(true, output);
			}

			output.invokeStatic(
					method.getDeclaringClass(),
					method.getName(),
					method.getReturnType(),
					method.getParameterTypes());
		}
	}

	@Override
	public ZenTypeFunction getFunctionType()
	{
		return functionType;
	}
}
