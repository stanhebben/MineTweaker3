/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementVar;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ParseException;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.parser.expression.ParsedExpression;
import zenscript.parser.type.IParsedType;
import zenscript.parser.type.TypeParser;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementVar extends ParsedStatement {
	public static ParsedStatementVar parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		Token token = tokener.next();
		if (token.getType() != T_VAR && token.getType() != T_VAL) {
			throw new ParseException(token, "var or val expected");
		}
		
		String name = tokener.required(TOKEN_ID, "identifier expected").getValue();

		IParsedType type = null;
		ParsedExpression initializer = null;
		if (tokener.optional(T_AS) != null) {
			type = TypeParser.parse(tokener, errorLogger);
		}

		if (tokener.optional(T_ASSIGN) != null) {
			initializer = ParsedExpression.parse(tokener, errorLogger);
		}

		tokener.required(T_SEMICOLON, "; expected");
		return new ParsedStatementVar(token.getPosition(), name, type, initializer, token.getType() == T_VAL);
	}
	
	private final String name;
	private final boolean isFinal;
	private final IParsedType asType;
	private final ParsedExpression initializer;
	
	public ParsedStatementVar(ZenPosition position, String name, IParsedType asType, ParsedExpression initializer, boolean isFinal) {
		super(position);
		
		this.name = name;
		this.asType = asType;
		this.initializer = initializer;
		this.isFinal = isFinal;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		ZenType cType = scope.getTypes().ANY;
		Expression cInitializer = null;
		if (asType != null) {
			cType = asType.compile(scope);
		}
		
		SymbolLocal symbol = new SymbolLocal(cType, isFinal);
		scope.putValue(name, symbol, getPosition());
		return new StatementVar(getPosition(), scope, symbol, cInitializer);
	}
}
