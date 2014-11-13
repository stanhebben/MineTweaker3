/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArray;
import stanhebben.zenscript.expression.ExpressionAs;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import org.openzen.zencode.runtime.AnyArray;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionArray extends ParsedExpression {
	private final List<ParsedExpression> contents;
	
	public ParsedExpressionArray(CodePosition position, List<ParsedExpression> contents) {
		super(position);
		
		this.contents = contents;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod scope, ZenType predictedType) {
		ZenType predictedBaseType = null;
		ZenTypeArrayBasic arrayType = scope.getTypes().ANYARRAY;
		ICastingRule castingRule = null;
		
		if (predictedType instanceof ZenTypeArray) {
			predictedBaseType = ((ZenTypeArray) predictedType).getBaseType();
			if (predictedType instanceof ZenTypeArrayBasic) {
				// TODO: allow any kind of array type
				arrayType = (ZenTypeArrayBasic) predictedType;
			}
		} else {
			// find any[] caster that casts to the given type
			castingRule = scope.getTypes().ANYARRAY.getCastingRule(scope.getAccessScope(), predictedType);
			if (castingRule != null) {
				if (castingRule.getInputType() instanceof ZenTypeArray) {
					predictedBaseType = ((ZenTypeArray) castingRule.getInputType()).getBaseType();
					if (castingRule.getInputType() instanceof ZenTypeArrayBasic) {
						// TODO: allow any kind of array type
						arrayType = (ZenTypeArrayBasic) castingRule.getInputType();
					}
				} else {
					scope.error(getPosition(), "Invalid caster - any[] caster but input type is not an array");
					castingRule = null;
				}
			}
		}
		
		Expression[] cContents = new Expression[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			cContents[i] = contents.get(i).compile(scope, predictedBaseType);
		}
		Expression result = new ExpressionArray(getPosition(), scope, arrayType, cContents);
		if (castingRule != null) {
			return new ExpressionAs(getPosition(), scope, result, castingRule);
		} else {
			return result;
		}
	}
	
	@Override
	public Expression compileKey(IScopeMethod environment, ZenType predictedType) {
		if (contents.size() == 1 && contents.get(0) instanceof ParsedExpressionVariable) {
			return contents.get(0).compile(environment, predictedType);
		} else {
			return compile(environment, predictedType);
		}
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny[] values = new IAny[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			values[i] = contents.get(i).eval(environment);
			if (values[i] == null)
				return null;
		}
		
		return new AnyArray(values);
	}
}
