/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArray;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 *
 * @author Stan
 */
public class ParsedCallArguments
{
	public static ParsedCallArguments parse(ZenLexer tokener, ICodeErrorLogger errors)
	{
		tokener.required(T_BROPEN, "( expected");

		List<ParsedCallArgument> arguments = new ArrayList<ParsedCallArgument>();

		boolean canHaveMore = true;

		while (canHaveMore) {
			arguments.add(ParsedCallArgument.parse(tokener, errors));

			if (tokener.optional(T_COMMA) == null)
				canHaveMore = false;
		}

		tokener.required(T_BRCLOSE, ") expected");
		return new ParsedCallArguments(arguments);
	}

	private final List<ParsedCallArgument> arguments;
	private final int numUnkeyedValues;

	public ParsedCallArguments(List<ParsedCallArgument> arguments)
	{
		this.arguments = arguments;

		int numberOfUnkeyedValues = arguments.size();
		while (numberOfUnkeyedValues > 0 && arguments.get(numberOfUnkeyedValues - 1).getKey() != null)
			numberOfUnkeyedValues--;

		numUnkeyedValues = numberOfUnkeyedValues;
	}

	public MatchedArguments compile(List<IMethod> methods, IScopeMethod environment)
	{
		ZenType[] predictedTypes = predictArgumentTypes(methods);
		Expression[] compiledArguments = compileArguments(environment, predictedTypes);

		MatchedArguments matchedExactly = matchExactly(methods, environment, compiledArguments);
		if (matchedExactly != null)
			return matchedExactly;

		return matchWithImplicitConversion(methods, environment, compiledArguments);
	}

	private Expression[] compileArguments(IScopeMethod environment, ZenType[] predictedTypes)
	{
		Expression[] compiled = new Expression[arguments.size()];
		for (int i = 0; i < arguments.size(); i++) {
			compiled[i] = arguments.get(i).getValue().compile(environment, predictedTypes[i]);
		}

		return compiled;
	}

	private MatchedArguments matchExactly(List<IMethod> methods, IScopeMethod environment, Expression[] compiled)
	{
		for (IMethod method : methods) {
			Expression[] matched = matchArgumentsExactly(method.getMethodHeader(), environment, compiled);
			if (matched != null)
				return new MatchedArguments(method, matched);
		}

		return null;
	}

	private MatchedArguments matchWithImplicitConversion(List<IMethod> methods, IScopeMethod environment, Expression[] compiled)
	{
		for (IMethod method : methods) {
			Expression[] matched = matchArgumentsWithImplicitConversion(method.getMethodHeader(), environment, compiled);
			if (matched != null)
				return new MatchedArguments(method, matched);
		}

		return null;
	}

	public IAny[] compileConstants(IZenCompileEnvironment environment)
	{
		if (hasKeyedArguments())
			return null;

		IAny[] results = new IAny[arguments.size()];
		for (int i = 0; i < results.length; i++) {
			IAny value = arguments.get(i).getValue().eval(environment);
			if (value == null)
				return null;

			results[i] = value;
		}

		return results;
	}

	// #######################
	// ### Private methods ###
	// #######################
	private boolean hasKeyedArguments()
	{
		for (ParsedCallArgument argument : arguments) {
			if (argument.getKey() != null)
				return true;
		}

		return false;
	}

	private ZenType[] predictArgumentTypes(List<IMethod> methods)
	{
		ZenType[] predictedTypes = new ZenType[arguments.size()];
		boolean[] ambiguous = new boolean[arguments.size()];

		for (IMethod method : methods) {
			MethodHeader header = method.getMethodHeader();

			if (!header.accepts(arguments.size()))
				continue;

			if (!checkUnusedArgumentPositions(header))
				continue;

			predictArgumentTypesForMethod(header, predictedTypes, ambiguous);
		}

		return predictedTypes;
	}

	private boolean[] getUsedKeyedArgumentPositions(MethodHeader method)
	{
		boolean[] isUsed = new boolean[method.getArguments().size() - numUnkeyedValues];
		for (int i = numUnkeyedValues; i < arguments.size(); i++) {
			int parameterIndex = method.getArgumentIndex(getArgumentKey(i));
			if (parameterIndex < numUnkeyedValues)
				return null;
			else
				isUsed[parameterIndex - numUnkeyedValues] = true;
		}

		return isUsed;
	}

	private boolean checkUnusedArgumentPositions(MethodHeader method)
	{
		// this method assumes that number of arguments has already been checked
		// then only needs to check for unused keyed argument positions
		boolean[] isUsed = getUsedKeyedArgumentPositions(method);

		List<MethodParameter> methodArguments = method.getArguments();
		for (int i = 0; i < isUsed.length; i++) {
			if (!isUsed[i] && methodArguments.get(numUnkeyedValues + i).getDefaultValue() == null)
				return false;
		}

		return true;
	}

	private void predictArgumentTypesForMethod(MethodHeader method, ZenType[] predictedTypes, boolean[] ambiguous)
	{
		List<MethodParameter> methodArguments = method.getArguments();

		for (int i = 0; i < numUnkeyedValues; i++) {
			if (ambiguous[i])
				continue;

			if (predictedTypes[i] == null)
				predictedTypes[i] = methodArguments.get(i).getType();
			else if (!predictedTypes[i].equals(methodArguments.get(i).getType())) {
				predictedTypes[i] = null;
				ambiguous[i] = true;
			}
		}

		for (int i = numUnkeyedValues; i < arguments.size(); i++) {
			if (ambiguous[i])
				continue;

			ZenType argumentType = methodArguments.get(method.getArgumentIndex(getArgumentKey(i))).getType();

			if (predictedTypes[i] == null)
				predictedTypes[i] = argumentType;
			else if (!predictedTypes[i].equals(argumentType)) {
				predictedTypes[i] = null;
				ambiguous[i] = true;
			}
		}
	}

	private String getArgumentKey(int index)
	{
		return arguments.get(index).getKey();
	}

	private Expression[] matchArgumentsExactly(MethodHeader method, IScopeMethod environment, Expression[] compiled)
	{
		return matchArguments(method, environment, compiled, true);
	}

	private Expression[] matchArgumentsWithImplicitConversion(MethodHeader method, IScopeMethod environment, Expression[] compiled)
	{
		return matchArguments(method, environment, compiled, false);
	}

	/**
	 * Performs a matching of compiled expressions to the method arguments.
	 * Performs type casting where necessary.
	 *
	 * @param method
	 * @param compiled
	 * @param exactly
	 * @return
	 */
	private Expression[] matchArguments(MethodHeader method, IScopeMethod environment, Expression[] compiled, boolean exactly)
	{
		int numUnkeyed = numUnkeyedValues;

		List<MethodParameter> methodArguments = method.getArguments();

		// little optimization
		if (!method.accepts(numUnkeyed))
			return null;

		boolean[] isUsed = new boolean[methodArguments.size() - numUnkeyed];
		boolean isVarargCall = false;
		ZenType varargBaseType = null;

		if (method.isVarargs())
			varargBaseType = method.getVarArgBaseType();

		// check parameters without names
		for (int i = 0; i < numUnkeyed; i++) {
			if (method.isVarargs() && i >= methodArguments.size() - 1)
				if (matches(compiled[i], varargBaseType, exactly)) {
					isVarargCall = true;
					continue;
				}

			if (i >= methodArguments.size())
				return null;

			if (!matches(compiled[i], methodArguments.get(i).getType(), exactly))
				return null;
		}

		// is this a vararg callStatic with an empty array?
		if (method.isVarargs() && numUnkeyed == compiled.length && numUnkeyed == methodArguments.size() - 1)
			isVarargCall = true;

		// check parameters with names
		for (int i = numUnkeyed; i < this.arguments.size(); i++) {
			int parameterIndex = method.getArgumentIndex(arguments.get(i).getKey());
			if (parameterIndex < numUnkeyed)
				return null;
			else if (exactly && !methodArguments.get(parameterIndex).getType().equals(compiled[i].getType()))
				return null;
			else if (!exactly && !methodArguments.get(parameterIndex).getType().equals(compiled[i].getType()))
				return null;
			else
				isUsed[parameterIndex - numUnkeyed] = true;
		}

		// check if all non-optional arguments are filled
		if (!checkUnusedArgumentPositions(method))
			return null;

		Expression[] result = new Expression[methodArguments.size()];

		// fill arguments without name
		if (isVarargCall)
			numUnkeyed = methodArguments.size() - 1;

		for (int i = 0; i < numUnkeyed; i++) {
			result[i] = compiled[i].cast(compiled[i].getPosition(), methodArguments.get(i).getType());
		}

		if (isVarargCall)
			result[methodArguments.size() - 1] = assembleVararg(methodArguments.get(methodArguments.size() - 1), environment, compiled, numUnkeyed);
		else {
			// fill keyed arguments
			for (int i = numUnkeyed; i < arguments.size(); i++) {
				int parameterIndex = method.getArgumentIndex(arguments.get(i).getKey());
				result[parameterIndex] = compiled[i].cast(compiled[i].getPosition(), methodArguments.get(parameterIndex).getType());
			}

			// fill default values
			for (int i = 0; i < isUsed.length; i++) {
				if (!isUsed[i])
					result[i + numUnkeyed] = methodArguments.get(i + numUnkeyed).getDefaultValue();
			}
		}

		return result;
	}

	/**
	 * Check if this type matches another type. If exactly is true, the types
	 * must be equal. Otherwise, the expression type must be implicitly castable
	 * to the argument type.
	 *
	 * @param expression expression to check
	 * @param type argument type
	 * @param exactly
	 * @return
	 */
	private boolean matches(Expression expression, ZenType type, boolean exactly)
	{
		if (exactly)
			return expression.getType().equals(type);
		else
			return expression.getType().canCastImplicit(expression.getScope().getAccessScope(), type);
	}

	/**
	 * Assembles remaining arguments into a single array. Used to assemble
	 * varargs values. Also works if from == compiled.length .
	 *
	 * @param argument argument to assemble for
	 * @param compiled compiled expressions array
	 * @param fromIndex index in the expressions array to assemble from
	 * @return array expression with vararg values
	 */
	private Expression assembleVararg(MethodParameter argument, IScopeMethod environment, Expression[] compiled, int fromIndex)
	{
		ZenType varargBaseType = ((ZenTypeArray) argument.getType()).getBaseType();

		// combine varargs into an array expression
		Expression[] arrayMembers = new Expression[compiled.length - fromIndex];
		for (int i = fromIndex; i < compiled.length; i++) {
			arrayMembers[i - fromIndex] = compiled[i].cast(
					compiled[i].getPosition(),
					varargBaseType);
		}

		return new ExpressionArray(
				compiled[fromIndex].getPosition(),
				environment,
				(ZenTypeArrayBasic) argument.getType(),
				arrayMembers);
	}

	public class MatchedArguments
	{
		public final IMethod method;
		public final Expression[] arguments;

		public MatchedArguments(IMethod method, Expression[] arguments)
		{
			this.method = method;
			this.arguments = arguments;
		}
	}
}
