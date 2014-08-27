/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionMember extends ParsedExpression {
	private final ParsedExpression value;
	private final String member;
	
	public ParsedExpressionMember(ZenPosition position, ParsedExpression value, String member) {
		super(position);
		
		this.value = value;
		this.member = member;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		return value.compile(environment, null).getMember(getPosition(), member);
	}
}
