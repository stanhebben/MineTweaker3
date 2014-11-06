/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionNew;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.parser.expression.ParsedCallArguments.MatchedArguments;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionNew extends ParsedExpression {
	private final IParsedType type;
	private final ParsedCallArguments callArguments;
	
	public ParsedExpressionNew(CodePosition position, IParsedType type, ParsedCallArguments callArguments) {
		super(position);
		
		this.type = type;
		this.callArguments = callArguments;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		ZenType cType = type.compile(environment);
		MatchedArguments compiledArguments = callArguments.compile(cType.getConstructors(), environment);
		return new ExpressionNew(getPosition(), environment, cType, compiledArguments.method, compiledArguments.arguments);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
