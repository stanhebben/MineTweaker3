package zenscript.parser.elements;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_AS;
import static zenscript.lexer.ZenTokener.T_ASSIGN;
import static zenscript.lexer.ZenTokener.T_BRCLOSE;
import static zenscript.lexer.ZenTokener.T_DOT3;
import zenscript.parser.expression.ParsedExpression;
import zenscript.parser.type.IParsedType;
import zenscript.parser.type.ParsedTypeBasic;
import zenscript.parser.type.TypeParser;
import zenscript.symbolic.method.MethodArgument;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunctionArgument
{
	public static ParsedFunctionArgument parse(ZenTokener tokener, IZenErrorLogger errorLogger)
	{
		ZenPosition position = tokener.getPosition();
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

	private final ZenPosition position;
	private final String name;
	private final IParsedType type;
	private final ParsedExpression defaultValue;
	private final boolean vararg;

	public ParsedFunctionArgument(ZenPosition position, String name, IParsedType type, ParsedExpression defaultValue, boolean vararg)
	{
		this.position = position;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.vararg = vararg;
	}

	public ZenPosition getPosition()
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
