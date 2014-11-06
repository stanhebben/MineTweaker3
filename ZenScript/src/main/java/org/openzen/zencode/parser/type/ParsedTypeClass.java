/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.type;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedTypeClass implements IParsedType {
	private final CodePosition position;
	private final List<String> name;
	private final List<IParsedType> genericType;
	
	public ParsedTypeClass(ICodeErrorLogger errors, ZenLexer tokener) {
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
				environment.error(position, "Could not find package or class " + Strings.join(name.subList(0, i), "."));
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
			environment.error(position, Strings.join(name, ".") + " is not a valid type");
			type = environment.getTypes().ANY;
		}
		
		return type;
	}
}
