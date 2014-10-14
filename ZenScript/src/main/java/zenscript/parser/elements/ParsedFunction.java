/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.elements;

import java.util.List;
import zenscript.lexer.ZenTokener;
import zenscript.IZenErrorLogger;
import zenscript.parser.statement.ParsedStatement;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunction {
	public static ParsedFunction parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		tokener.next();
		
		List<ParsedGenericParameter> genericParameters
				= ParsedGenericParameters.parse(tokener, errorLogger);
		ZenPosition position = tokener.getPosition();
		String name = tokener.requiredIdentifier();
		
		ParsedFunctionSignature header
				= ParsedFunctionSignature.parse(tokener, errorLogger, genericParameters);
		List<ParsedStatement> statements
				= ParsedStatement.parseBlock(tokener, errorLogger);
		
		return new ParsedFunction(position, name, header, statements);
	}
	
	private final ZenPosition position;
	private final String name;
	private final ParsedFunctionSignature header;
	private final List<ParsedStatement> statements;
	
	//private final String signature;
	
	private ParsedFunction(ZenPosition position, String name, ParsedFunctionSignature header, List<ParsedStatement> statements) {
		this.position = position;
		this.name = name;
		this.header = header;
		this.statements = statements;
		
		/*StringBuilder sig = new StringBuilder();
		sig.append("(");
		for (ParsedFunctionArgument argument : arguments) {
			sig.append(argument.getType().getSignature());
		}
		sig.append(")");
		sig.append(returnType.getSignature());
		signature = sig.toString();*/
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public String getName() {
		return name;
	}
	
	/*public String getSignature() {
		return signature;
	}*/
	
	public ParsedFunctionSignature getHeader() {
		return header;
	}
	
	public List<ParsedStatement> getStatements() {
		return statements;
	}
}
