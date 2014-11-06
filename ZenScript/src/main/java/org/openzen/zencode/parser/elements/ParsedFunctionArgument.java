package org.openzen.zencode.parser.elements;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_AS;
import static org.openzen.zencode.lexer.ZenLexer.T_ASSIGN;
import static org.openzen.zencode.lexer.ZenLexer.T_BRCLOSE;
import static org.openzen.zencode.lexer.ZenLexer.T_DOT3;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.ParsedTypeBasic;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.method.MethodArgument;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunctionArgument
{
	public static ParsedFunctionArgument parse(ZenLexer tokener, ICodeErrorLogger errorLogger)
	{
		CodePosition position = tokener.getPosition();
		String argName = tokener.requiredIdentifier();
		IParsedType argType = ParsedTypeBasic.ANY;
		ParsedExpression defaultValue = null;
		boolean isVararg = false;

		if (tokener.optional(T_AS) != null) {
			argType = TypeParser.parse(tokener, errorLogger);
		}

		if (tokener.optional(T_ASSIGN) != null) {
			defaultValue = ParsedExpression.parse(tokener, errorLogger);
		}

		if (tokener.optional(T_DOT3) != null) {
			isVararg = true;
			tokener.required(T_BRCLOSE, ") expected");
		}

		return new ParsedFunctionArgument(position, argName, argType, defaultValue, isVararg);
	}

	private final CodePosition position;
	private final String name;
	private final IParsedType type;
	private final ParsedExpression defaultValue;
	private final boolean vararg;

	public ParsedFunctionArgument(CodePosition position, String name, IParsedType type, ParsedExpression defaultValue, boolean vararg)
	{
		this.position = position;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.vararg = vararg;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public String getName()
	{
		return name;
	}

	public IParsedType getType()
	{
		return type;
	}

	public ParsedExpression getDefaultValue()
	{
		return defaultValue;
	}

	public boolean isVarArg()
	{
		return vararg;
	}
	
	public MethodArgument compile(IScopeGlobal scope)
	{
		ZenType cType = type.compile(scope);
		Expression compiledDefaultValue = defaultValue == null ? null : defaultValue.compile(scope.getTypes().getStaticGlobalEnvironment(), cType);
		return new MethodArgument(name, cType, compiledDefaultValue);
	}
}
