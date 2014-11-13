package org.openzen.zencode.parser.elements;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.ParsedTypeBasic;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 * Contains a parsed function header. A function header is the combination of
 * return type, argument types and names (and default values) as well as generic
 * parameters, if any.
 *
 * @author Stan Hebben
 */
public class ParsedFunctionSignature
{
	public static ParsedFunctionSignature parse(ZenLexer tokener, ICodeErrorLogger errorLogger, List<ParsedGenericParameter> generics)
	{
		List<ParsedFunctionParameter> parameters = new ArrayList<ParsedFunctionParameter>();

		tokener.required(T_BROPEN, "( expected");

		if (tokener.optional(T_BRCLOSE) == null) {
			ParsedFunctionParameter argument;
			do {
				argument = ParsedFunctionParameter.parse(tokener, errorLogger);
				parameters.add(argument);
			} while (!argument.isVarArg() && tokener.optional(T_COMMA) != null);

			tokener.required(T_BRCLOSE, ") or ; expected");
		}

		IParsedType returnType = ParsedTypeBasic.ANY;

		if (tokener.optional(T_AS) != null)
			returnType = TypeParser.parse(tokener, errorLogger);

		return new ParsedFunctionSignature(generics, parameters, returnType);
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

	public MethodHeader compile(IScopeGlobal scope)
	{
		ZenType compiledReturnType = this.returnType.compile(scope);
		List<MethodParameter> compiledArguments = new ArrayList<MethodParameter>();

		for (ParsedFunctionParameter parameter : parameters) {
			compiledArguments.add(parameter.compile(scope));
		}

		boolean isVararg = !parameters.isEmpty() && parameters.get(parameters.size() - 1).isVarArg();
		return new MethodHeader(compiledReturnType, compiledArguments, isVararg);
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

	public List<MethodParameter> getCompiledArguments(IScopeGlobal environment)
	{
		List<MethodParameter> result = new ArrayList<MethodParameter>();

		for (ParsedFunctionParameter parameter : parameters) {
			ZenType type = parameter.getType().compile(environment);
			ParsedExpression defaultValue = parameter.getDefaultValue();
			Expression compiledDefaultValue = null;

			if (defaultValue != null)
				compiledDefaultValue = defaultValue.compile(environment.getTypes().getStaticGlobalEnvironment(), type);

			result.add(new MethodParameter(parameter.getName(), type, compiledDefaultValue));
		}

		return result;
	}
}
