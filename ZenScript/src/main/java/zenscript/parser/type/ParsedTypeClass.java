/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.util.StringUtil;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedTypeClass implements IParsedType {
	private final ZenPosition position;
	private final List<String> name;
	private final List<IParsedType> genericType;
	
	public ParsedTypeClass(IZenErrorLogger errors, ZenTokener tokener) {
		Token nameFirst = tokener.required(TOKEN_ID, "identifier expected");
		position = nameFirst.getPosition();
		
		name = new ArrayList<String>();
		name.add(nameFirst.getValue());
		
		while (tokener.optional(T_DOT) != null) {
			Token namePart = tokener.required(TOKEN_ID, "identifier expected");
			name.add(namePart.getValue());
		}
		
		if (tokener.optional(T_LT) == null) {
			genericType = null;
		} else {
			genericType = new ArrayList<IParsedType>();
			while (tokener.optional(T_GT) != null) {
				IParsedType type = TypeParser.parse(tokener, errors);
				genericType.add(type);
			}
		}
	}
	
	@Override
	public ZenType compile(IScopeGlobal environment) {
		IPartialExpression expression = environment.getValue(name.get(0), position, environment.getTypes().getStaticGlobalEnvironment());
		
		for (int i = 1; i < name.size(); i++) {
			expression = expression.getMember(position, name.get(i));
			if (expression == null) {
				environment.error(position, "Could not find package or class " + StringUtil.join(name.subList(0, i), "."));
				return environment.getTypes().ANY;
			}
		}
		
		List<ZenType> compiledGenericTypes;
		if (genericType == null) {
			compiledGenericTypes = null;
		} else {
			compiledGenericTypes = new ArrayList<ZenType>();
			for (IParsedType type : genericType) {
				compiledGenericTypes.add(type.compile(environment));
			}
		}
		
		ZenType type = expression.toType(compiledGenericTypes);
		if (type == null) {
			environment.error(position, StringUtil.join(name, ".") + " is not a valid type");
			type = environment.getTypes().ANY;
		}
		
		return type;
	}
}
