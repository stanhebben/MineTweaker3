package org.openzen.zencode.parser.elements;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.ParsedTypeBasic;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 * Contains a parsed function header. A function header is the combination of
 * return type, argument types and names (and default values) as well as generic
 * parameters, if any.
 *
 * @author Stan Hebben
 */
public class ParsedFunctionSignature
{
	public static ParsedFunctionSignature parse(ZenLexer lexer)
	{
		List<ParsedGenericParameter> genericParameters
				= ParsedGenericParameters.parse(lexer);
		
		List<ParsedFunctionParameter> parameters = new ArrayList<ParsedFunctionParameter>();

		lexer.required(T_BROPEN, "( expected");

		if (lexer.optional(T_BRCLOSE) == null) {
			ParsedFunctionParameter argument;
			do {
				argument = ParsedFunctionParameter.parse(lexer);
				parameters.add(argument);
			} while (!argument.isVarArg() && lexer.optional(T_COMMA) != null);

			lexer.required(T_BRCLOSE, ") expected");
		}

		IParsedType returnType = ParsedTypeBasic.ANY;

		if (lexer.optional(T_AS) != null)
			returnType = TypeParser.parse(lexer);

		return new ParsedFunctionSignature(genericParameters, parameters, returnType);
	}

	private final List<ParsedGenericParameter> generics;
	private final List<ParsedFunctionParameter> parameters;
	private final IParsedType returnType;

	public ParsedFunctionSignature(List<ParsedGenericParameter> generics, List<ParsedFunctionParameter> parameters, IParsedType returnType)
	{
		this.generics = generics;
		this.parameters = parameters;
		this.returnType = returnType;
	}

	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 MethodHeader<E, T> compile(IScopeGlobal<E, T> scope)
	{
		T compiledReturnType = this.returnType.compile(scope);
		List<MethodParameter<E, T>> compiledArguments = new ArrayList<MethodParameter<E, T>>();

		for (ParsedFunctionParameter parameter : parameters) {
			compiledArguments.add(parameter.compile(scope));
		}

		boolean isVararg = !parameters.isEmpty() && parameters.get(parameters.size() - 1).isVarArg();
		return new MethodHeader<E, T>(compiledReturnType, compiledArguments, isVararg);
	}

	public List<ParsedFunctionParameter> getArguments()
	{
		return parameters;
	}

	public IParsedType getReturnType()
	{
		return returnType;
	}

	public boolean isVararg()
	{
		return !parameters.isEmpty() && parameters.get(parameters.size() - 1).isVarArg();
	}

	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 List<MethodParameter<E, T>> getCompiledArguments(IScopeGlobal<E, T> environment)
	{
		List<MethodParameter<E, T>> result = new ArrayList<MethodParameter<E, T>>();

		for (ParsedFunctionParameter parameter : parameters) {
			T type = parameter.getType().compile(environment);
			ParsedExpression defaultValue = parameter.getDefaultValue();
			E compiledDefaultValue = null;

			if (defaultValue != null)
				compiledDefaultValue = defaultValue.compile(environment.getConstantEnvironment(), type);

			result.add(new MethodParameter<E, T>(parameter.getName(), type, compiledDefaultValue));
		}

		return result;
	}
}
