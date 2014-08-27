package zenscript.parser.elements;

import zenscript.parser.expression.ParsedExpression;
import zenscript.parser.type.IParsedType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunctionArgument {
	private final ZenPosition position;
	private final String name;
	private final IParsedType type;
	private final ParsedExpression defaultValue;
	
	public ParsedFunctionArgument(ZenPosition position, String name, IParsedType type, ParsedExpression defaultValue) {
		this.position = position;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public String getName() {
		return name;
	}
	
	public IParsedType getType() {
		return type;
	}
	
	public ParsedExpression getDefaultValue() {
		return defaultValue;
	}
}
