package org.openzen.zencode.parser.elements;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.MethodArgument;
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
public class ParsedFunctionSignature {
	public static ParsedFunctionSignature parse(ZenLexer tokener, ICodeErrorLogger errorLogger, List<ParsedGenericParameter> generics) {
		List<ParsedFunctionArgument> arguments = new ArrayList<ParsedFunctionArgument>();
		
		tokener.required(T_BROPEN, "( expected");
		
		if (tokener.optional(T_BRCLOSE) == null) {
			ParsedFunctionArgument argument;
			do {
				argument = ParsedFunctionArgument.parse(tokener, errorLogger);
				arguments.add(argument);
			} while (!argument.isVarArg() && tokener.optional(T_COMMA) != null);
			
			tokener.required(T_BRCLOSE, ") or ; expected");
		}
		
		IParsedType returnType = ParsedTypeBasic.ANY;
		
		if (tokener.optional(T_AS) != null)
			returnType = TypeParser.parse(tokener, errorLogger);
		
		return new ParsedFunctionSignature(generics, arguments, returnType);
	}
	
	private final List<ParsedGenericParameter> generics;
	private final List<ParsedFunctionArgument> arguments;
	private final IParsedType returnType;
	
	public ParsedFunctionSignature(List<ParsedGenericParameter> generics, List<ParsedFunctionArgument> arguments, IParsedType returnType) {
		this.generics = generics;
		this.arguments = arguments;
		this.returnType = returnType;
	}
	
	public MethodHeader compile(IScopeGlobal scope) {
		ZenType compiledReturnType = this.returnType.compile(scope);
		List<MethodArgument> compiledArguments = new ArrayList<MethodArgument>();
		
		for (ParsedFunctionArgument argument : arguments) {
			compiledArguments.add(argument.compile(scope));
		}
		
		boolean isVararg = !arguments.isEmpty() && arguments.get(arguments.size() - 1).isVarArg();
		return new MethodHeader(compiledReturnType, compiledArguments, isVararg);
	}
	
	public List<ParsedFunctionArgument> getArguments() {
		return arguments;
	}
	
	public IParsedType getReturnType() {
		return returnType;
	}
	
	public boolean isVararg() {
		return !arguments.isEmpty() && arguments.get(arguments.size() - 1).isVarArg();
	}
	
	public List<MethodArgument> getCompiledArguments(IScopeGlobal environment) {
		List<MethodArgument> result = new ArrayList<MethodArgument>();
		
		for (ParsedFunctionArgument argument : arguments) {
			ZenType type = argument.getType().compile(environment);
			ParsedExpression defaultValue = argument.getDefaultValue();
			Expression compiledDefaultValue = null;
			
			if (defaultValue != null) {
				compiledDefaultValue = defaultValue.compile(environment.getTypes().getStaticGlobalEnvironment(), type);
			}
			
			result.add(new MethodArgument(argument.getName(), type, compiledDefaultValue));
		}
		
		return result;
	}
}
