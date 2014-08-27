/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.elements;

import static zenscript.lexer.ZenTokener.*;

import java.util.ArrayList;
import java.util.List;
import zenscript.lexer.ZenTokener;
import zenscript.lexer.Token;
import zenscript.IZenErrorLogger;
import zenscript.parser.statement.ParsedStatement;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunction {
	public static ParsedFunction parse(ZenTokener parser, IZenErrorLogger errorLogger) {
		parser.next();
		Token tName = parser.required(ZenTokener.TOKEN_ID, "identifier expected");

		ParsedFunctionHeader header = ParsedFunctionHeader.parse(parser, errorLogger);
		
		parser.required(T_AOPEN, "{ expected");
		
		ArrayList<ParsedStatement> statements = new ArrayList<ParsedStatement>();
		if (parser.optional(T_ACLOSE) == null) {
			while (parser.optional(T_ACLOSE) == null) {
				statements.add(ParsedStatement.parse(parser, errorLogger));
			}
		}
		
		return new ParsedFunction(tName.getPosition(), tName.getValue(), header, statements);
	}
	
	private final ZenPosition position;
	private final String name;
	private final ParsedFunctionHeader header;
	private final List<ParsedStatement> statements;
	
	//private final String signature;
	
	private ParsedFunction(ZenPosition position, String name, ParsedFunctionHeader header, List<ParsedStatement> statements) {
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
	
	public ParsedFunctionHeader getHeader() {
		return header;
	}
	
	public List<ParsedStatement> getStatements() {
		return statements;
	}
}
