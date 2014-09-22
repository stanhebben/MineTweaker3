package zenscript.parser.elements;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.MethodArgument;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.parser.expression.ParsedExpression;
import zenscript.parser.type.IParsedType;
import zenscript.parser.type.ParsedTypeBasic;
import zenscript.parser.type.TypeParser;

/**
 * Contains a parsed function header. A function header is the combination of
 * return type, argument types and names (and default values) as well as generic
 * parameters, if any.
 * 
 * @author Stan Hebben
 */
public class ParsedFunctionSignature {
	public static ParsedFunctionSignature parse(ZenTokener tokener, IZenErrorLogger errorLogger, List<ParsedGenericParameter> generics) {
		List<ParsedFunctionArgument> arguments = new ArrayList<ParsedFunctionArgument>();
		boolean isVararg = false;
		
		tokener.required(T_BROPEN, "( expected");
		
		boolean canHaveMore = tokener.optional(T_BRCLOSE) == null;
		
		while (canHaveMore) {
			Token argName = tokener.required(TOKEN_ID, "identifier expected");
			IParsedType argType = ParsedTypeBasic.ANY;
			ParsedExpression defaultValue = null;
			
			if (tokener.optional(T_AS) != null) {
				argType = TypeParser.parse(tokener, errorLogger);
			}
			
			if (tokener.optional(T_ASSIGN) != null) {
				defaultValue = ParsedExpression.parse(tokener, errorLogger);
			}
			
			if (tokener.optional(T_DOT3) != null) {
				isVararg = true;
				tokener.required(T_BRCLOSE, ") expected");
				
				canHaveMore = false;
			} else if (tokener.optional(T_COMMA) == null) {
				tokener.required(T_BRCLOSE, ") expected");
				
				canHaveMore = false;
			}
			
			arguments.add(new ParsedFunctionArgument(argName.getPosition(), argName.getValue(), argType, defaultValue));
		}
		
		IParsedType returnType = ParsedTypeBasic.ANY;
		if (tokener.optional(T_AS) != null) {
			returnType = TypeParser.parse(tokener, errorLogger);
		}
		
		return new ParsedFunctionSignature(generics, arguments, returnType, isVararg);
	}
	
	private final List<ParsedGenericParameter> generics;
	private final List<ParsedFunctionArgument> arguments;
	private final IParsedType returnType;
	private final boolean isVararg;
	
	public ParsedFunctionSignature(List<ParsedGenericParameter> generics, List<ParsedFunctionArgument> arguments, IParsedType returnType, boolean isVararg) {
		this.generics = generics;
		this.arguments = arguments;
		this.returnType = returnType;
		this.isVararg = isVararg;
	}
	
	public List<ParsedFunctionArgument> getArguments() {
		return arguments;
	}
	
	public IParsedType getReturnType() {
		return returnType;
	}
	
	public boolean isVararg() {
		return isVararg;
	}
	
	public List<MethodArgument> getCompiledArguments(IScopeGlobal environment) {
		List<MethodArgument> result = new ArrayList<MethodArgument>();
		
		for (int i = 0; i < arguments.size(); i++) {
			ZenType type = arguments.get(i).getType().compile(environment);
			ParsedExpression defaultValue = arguments.get(i).getDefaultValue();
			Expression compiledDefaultValue;
			if (defaultValue == null) {
				compiledDefaultValue = null;
			} else {
				compiledDefaultValue = defaultValue.compile(environment.getTypes().getStaticGlobalEnvironment(), type).eval();
			}
			result.add(new MethodArgument(arguments.get(i).getName(), type, compiledDefaultValue));
		}
		
		return result;
	}
}
