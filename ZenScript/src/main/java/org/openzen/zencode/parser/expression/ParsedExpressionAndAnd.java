/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionAndAnd;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionAndAnd extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	
	public ParsedExpressionAndAnd(CodePosition position, ParsedExpression left, ParsedExpression right) {
		super(position);
		
		this.left = left;
		this.right = right;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionAndAnd(
				getPosition(),
				environment,
				left.compile(environment, predictedType),
				right.compile(environment, predictedType));
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny leftValue = left.eval(environment);
		if (leftValue == null)
			return null;
		
		if (!leftValue.asBool())
			return AnyBool.FALSE;
		
		return right.eval(environment);
	}
}
