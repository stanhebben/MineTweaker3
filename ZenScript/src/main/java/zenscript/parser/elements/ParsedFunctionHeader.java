/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.elements;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.JavaMethodArgument;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.parser.expression.ParsedExpression;
import zenscript.parser.type.IParsedType;
import zenscript.parser.type.ParsedTypeBasic;
import zenscript.parser.type.TypeParser;

/**
 *
 * @author Stan
 */
public class ParsedFunctionHeader {
	public static ParsedFunctionHeader parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
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
		
		return new ParsedFunctionHeader(arguments, returnType, isVararg);
	}
	
	private final List<ParsedFunctionArgument> arguments;
	private final IParsedType returnType;
	private final boolean isVararg;
	
	public ParsedFunctionHeader(List<ParsedFunctionArgument> arguments, IParsedType returnType, boolean isVararg) {
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
	
	public List<JavaMethodArgument> getCompiledArguments(IScopeGlobal environment) {
		List<JavaMethodArgument> result = new ArrayList<JavaMethodArgument>();
		
		for (int i = 0; i < arguments.size(); i++) {
			ZenType type = arguments.get(i).getType().compile(environment);
			ParsedExpression defaultValue = arguments.get(i).getDefaultValue();
			Expression compiledDefaultValue;
			if (defaultValue == null) {
				compiledDefaultValue = null;
			} else {
				compiledDefaultValue = defaultValue.compile(environment.getTypes().getStaticGlobalEnvironment(), type).eval();
			}
			result.add(new JavaMethodArgument(arguments.get(i).getName(), type, compiledDefaultValue));
		}
		
		return result;
	}
}
