/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.IAny;
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
