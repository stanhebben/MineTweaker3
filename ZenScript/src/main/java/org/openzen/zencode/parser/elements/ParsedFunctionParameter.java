package org.openzen.zencode.parser.elements;

import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_AS;
import static org.openzen.zencode.lexer.ZenLexer.T_ASSIGN;
import static org.openzen.zencode.lexer.ZenLexer.T_DOT3;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.ParsedTypeBasic;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunctionParameter
{
	public static ParsedFunctionParameter parse(ZenLexer lexer)
	{
		CodePosition position = lexer.getPosition();
		String argName = lexer.requiredIdentifier();
		IParsedType argType = ParsedTypeBasic.ANY;
		ParsedExpression defaultValue = null;
		boolean isVararg = false;

		if (lexer.optional(T_AS) != null)
			argType = TypeParser.parse(lexer);

		if (lexer.optional(T_ASSIGN) != null)
			defaultValue = ParsedExpression.parse(lexer);

		if (lexer.optional(T_DOT3) != null)
			isVararg = true;

		return new ParsedFunctionParameter(position, argName, argType, defaultValue, isVararg);
	}

	private final CodePosition position;
	private final String name;
	private final IParsedType type;
	private final ParsedExpression defaultValue;
	private final boolean vararg;

	public ParsedFunctionParameter(CodePosition position, String name, IParsedType type, ParsedExpression defaultValue, boolean vararg)
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

	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 MethodParameter<E, T> compile(IModuleScope<E, T> scope)
	{
		T cType = type.compile(scope);
		E compiledDefaultValue = defaultValue == null ? null : defaultValue.compile(scope.getConstantEnvironment(), cType);
		return new MethodParameter<E, T>(position, name, cType, compiledDefaultValue);
	}
}
