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
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionAssign extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	
	public ParsedExpressionAssign(CodePosition position, ParsedExpression left, ParsedExpression right) {
		super(position);
		
		this.left = left;
		this.right = right;
	}
	
	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		IPartialExpression cLeft = left.compilePartial(environment, predictedType);
		
		return cLeft.assign(
				getPosition(),
				right.compile(environment, cLeft.getType())
		);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
