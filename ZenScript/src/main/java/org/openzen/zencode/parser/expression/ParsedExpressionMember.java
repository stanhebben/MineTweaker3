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
public class ParsedExpressionMember extends ParsedExpression {
	private final ParsedExpression value;
	private final String member;
	
	public ParsedExpressionMember(CodePosition position, ParsedExpression value, String member) {
		super(position);
		
		this.value = value;
		this.member = member;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return value.compilePartial(environment, null).getMember(getPosition(), member);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny valueValue = value.eval(environment);
		if (valueValue == null)
			return null;
		
		return valueValue.memberGet(member);
	}
}
