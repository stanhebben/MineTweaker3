/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementSwitch;
import stanhebben.zenscript.statements.StatementVar;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementVar extends ParsedStatement {
	public static ParsedStatementVar parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
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
	
	public ParsedStatementVar(CodePosition position, String name, IParsedType asType, ParsedExpression initializer, boolean isFinal) {
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

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
