/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.modifier;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;

/**
 *
 * @author Stan
 */
public class ModifierParser
{
	private ModifierParser() {}
	
	public static List<IParsedModifier> parseMemberModifiers(ZenLexer lexer)
	{
		List<IParsedModifier> results = new ArrayList<IParsedModifier>();
		loop: while (true) {
			switch (lexer.peek().getType()) {
				case T_PRIVATE:
					results.add(new ParsedModifierPrivate(lexer.next().getPosition()));
					break;
				case T_PUBLIC:
					results.add(new ParsedModifierPublic(lexer.next().getPosition()));
					break;
				case T_EXPORT:
					results.add(new ParsedModifierExport(lexer.next().getPosition()));
					break;
				case T_SYNCHRONIZED:
					results.add(new ParsedModifierSynchronized(lexer.next().getPosition()));
					break;
				case T_OVERRIDE:
					results.add(new ParsedModifierOverride(lexer.next().getPosition()));
					break;
				case T_OPEN:
					results.add(new ParsedModifierOpen(lexer.next().getPosition()));
					break;
				case T_GENERATED:
					results.add(new ParsedModifierGenerated(lexer.next().getPosition()));
					break;
				case T_NATIVE:
					results.add(new ParsedModifierNative(lexer.next().getPosition()));
					break;
				case T_ABSTRACT:
					results.add(new ParsedModifierAbstract(lexer.next().getPosition()));
					break;
				case T_STATIC:
					results.add(new ParsedModifierStatic(lexer.next().getPosition()));
					break;
				case T_FINAL:
					results.add(new ParsedModifierFinal(lexer.next().getPosition()));
					break;
				default:
					break loop;
			}
		}
		
		return results;
	}
	
	public static List<IParsedModifier> parseUnitModifiers(ZenLexer lexer)
	{
		List<IParsedModifier> results = new ArrayList<IParsedModifier>();
		loop: while (true) {
			switch (lexer.peek().getType()) {
				case T_PRIVATE:
					results.add(new ParsedModifierPrivate(lexer.next().getPosition()));
					break;
				case T_PUBLIC:
					results.add(new ParsedModifierPublic(lexer.next().getPosition()));
					break;
				case T_EXPORT:
					results.add(new ParsedModifierExport(lexer.next().getPosition()));
					break;
				case T_ABSTRACT:
					results.add(new ParsedModifierAbstract(lexer.next().getPosition()));
					break;
				case T_FINAL:
					results.add(new ParsedModifierFinal(lexer.next().getPosition()));
					break;
				default:
					break loop;
			}
		}
		
		return results;
	}
}
