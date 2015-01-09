package org.openzen.zencode.parser.elements;

import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.generic.ParsedGenericParameters;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.ParsedTypeBasic;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

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
		CodePosition position = lexer.getPosition();
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

		return new ParsedFunctionSignature(position, genericParameters, parameters, returnType);
	}
	
	private final CodePosition position;
	private final List<ParsedGenericParameter> generics;
	private final List<ParsedFunctionParameter> parameters;
	private final IParsedType returnType;

	public ParsedFunctionSignature(
			CodePosition position,
			List<ParsedGenericParameter> generics,
			List<ParsedFunctionParameter> parameters,
			IParsedType returnType)
	{
		this.position = position;
		this.generics = generics;
		this.parameters = parameters;
		this.returnType = returnType;
	}

	public <E extends IPartialExpression<E>>
		 MethodHeader<E> compile(IModuleScope<E> scope)
	{
		TypeInstance<E> compiledReturnType = this.returnType.compile(scope);
		List<MethodParameter<E>> compiledArguments = new ArrayList<MethodParameter<E>>();

		for (ParsedFunctionParameter parameter : parameters) {
			compiledArguments.add(parameter.compile(scope));
		}
		
		List<GenericParameter<E>> genericParameters = GenericParameter.compile(generics, scope);

		boolean isVararg = !parameters.isEmpty() && parameters.get(parameters.size() - 1).isVarArg();
		MethodHeader<E> result = new MethodHeader<E>(position, genericParameters, compiledReturnType, compiledArguments, isVararg);
		result.completeMembers(scope);
		return result;
	}
		 
	public List<ParsedGenericParameter> getGenericParameters()
	{
		return generics;
	}

	public List<ParsedFunctionParameter> getParameters()
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

	public <E extends IPartialExpression<E>>
		 List<MethodParameter<E>> getCompiledArguments(IModuleScope<E> scope)
	{
		List<MethodParameter<E>> result = new ArrayList<MethodParameter<E>>();

		for (ParsedFunctionParameter parameter : parameters) {
			result.add(parameter.compile(scope));
		}

		return result;
	}
}
