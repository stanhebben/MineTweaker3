/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCast extends ParsedExpression {
	private final ParsedExpression value;
	private final IParsedType type;
	
	public ParsedExpressionCast(CodePosition position, ParsedExpression value, IParsedType type) {
		super(position);
		
		this.value = value;
		this.type = type;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		ZenType compiledType = type.compile(environment);
		
		return value.compile(environment, compiledType).cast(getPosition(), compiledType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		// not (yet?) possible
		return null;
	}
}
