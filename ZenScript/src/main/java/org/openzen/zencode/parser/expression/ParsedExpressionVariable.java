/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionString;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionVariable extends ParsedExpression
{
	private final String name;

	public ParsedExpressionVariable(CodePosition position, String name)
	{
		super(position);

		this.name = name;
	}

	@Override
	public String asIdentifier()
	{
		return name;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod scope, ZenType predictedType)
	{
		IPartialExpression result = scope.getValue(name, getPosition(), scope);
		if (result == null) {
			if (predictedType == null) {
				scope.error(getPosition(), "could not find " + name);
				return new ExpressionInvalid(getPosition(), scope);
			}

			// enable usage of static members of the same type as the predicted type (eg. enum values)
			IPartialExpression member = predictedType.getStaticMember(getPosition(), scope, name);
			if (member == null || member.getType().getCastingRule(scope.getAccessScope(), predictedType) == null) {
				scope.error(getPosition(), "could not find " + name);
				return new ExpressionInvalid(getPosition(), scope);
			} else
				return member;
		} else
			return result;
	}

	@Override
	public Expression compileKey(IScopeMethod scope, ZenType predictedType)
	{
		return new ExpressionString(getPosition(), scope, name);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment)
	{
		return environment.evalGlobal(name);
	}
}
