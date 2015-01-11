/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import java.io.IOException;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public abstract class ParsedStatement
{
	public static ParsedStatement parse(String value, ICodeErrorLogger<?> errorLogger)
	{
		try {
			return parse(new ZenLexer(errorLogger, value));
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse statement " + value, ex);
		}
	}
	
	public static ParsedStatement parse(ZenLexer lexer)
	{
		Token next = lexer.peek();
		switch (next.getType()) {
			case T_IMPORT:
				return ParsedImportStatement.parse(lexer);

			case T_AOPEN:
				return ParsedStatementBlock.parse(lexer);
				
			case T_RETURN:
				return ParsedStatementReturn.parse(lexer);

			case T_IF:
				return ParsedStatementIf.parse(lexer);

			case T_VAR:
			case T_VAL:
				return ParsedStatementVar.parse(lexer);

			case T_FOR:
				return ParsedStatementFor.parse(lexer);

			case T_WHILE:
				return ParsedStatementWhile.parse(lexer);

			case T_DO:
				return ParsedStatementDoWhile.parse(lexer);
			
			case T_TRY:
				return ParsedTryStatement.parse(lexer);

			case T_SWITCH:
				return ParsedStatementSwitch.parse(lexer);

			case T_CASE:
				return ParsedStatementCase.parse(lexer);

			case T_DEFAULT:
				return ParsedStatementDefault.parse(lexer);

			case T_BREAK:
				return ParsedStatementBreak.parse(lexer);

			case T_CONTINUE:
				return ParsedStatementContinue.parse(lexer);
				
			case T_THROW:
				return ParsedThrowStatement.parse(lexer);
				
			case T_SYNCHRONIZED:
				return ParsedSynchronizedStatement.parse(lexer);

			default:
				return ParsedStatementExpression.parse(lexer);
		}
	}

	private final CodePosition position;

	public ParsedStatement(CodePosition position)
	{
		this.position = position;
	}

	public CodePosition getPosition()
	{
		return position;
	}
	
	public <E extends IPartialExpression<E>>
		void processImports(IModuleScope<E> scriptScope)
	{
		// nothing do to, by default
	}
	
	public abstract <E extends IPartialExpression<E>>
		Statement<E> compile(IMethodScope<E> scope);

	public abstract <E extends IPartialExpression<E>>
		void compileSwitch(IMethodScope<E> scope, StatementSwitch<E> forSwitch);
}
