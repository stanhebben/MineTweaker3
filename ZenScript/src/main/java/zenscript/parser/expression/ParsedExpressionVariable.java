/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionVariable extends ParsedExpression {
	private final String name;
	
	public ParsedExpressionVariable(ZenPosition position, String name) {
		super(position);
		
		this.name = name;
	}
	
	@Override
	public String asIdentifier() {
		return name;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		IPartialExpression result = environment.getValue(name, getPosition(), environment);
		if (result == null) {
			// enable usage of static members of the same type as the predicted type (eg. enum values)
			IPartialExpression member = predictedType.getStaticMember(getPosition(), environment, name);
			if (member == null || member.getType().getCastingRule(predictedType) == null) {
				environment.error(getPosition(), "could not find " + name);
				return new ExpressionInvalid(getPosition(), environment);
			} else {
				return member;
			}
		} else {
			return result;
		}
	}
	
	@Override
	public Expression compileKey(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionString(getPosition(), environment, name);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return environment.evalGlobal(name);
	}
}
