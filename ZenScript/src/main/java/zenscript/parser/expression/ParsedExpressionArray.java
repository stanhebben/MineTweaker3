/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArray;
import stanhebben.zenscript.expression.ExpressionAs;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionArray extends ParsedExpression {
	private final List<ParsedExpression> contents;
	
	public ParsedExpressionArray(ZenPosition position, List<ParsedExpression> contents) {
		super(position);
		
		this.contents = contents;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		ZenType predictedBaseType = null;
		ZenTypeArrayBasic arrayType = environment.getTypes().ANYARRAY;
		ICastingRule castingRule = null;
		
		if (predictedType instanceof ZenTypeArray) {
			predictedBaseType = ((ZenTypeArray) predictedType).getBaseType();
			if (predictedType instanceof ZenTypeArrayBasic) {
				// TODO: allow any kind of array type
				arrayType = (ZenTypeArrayBasic) predictedType;
			}
		} else {
			// find any[] caster that casts to the given type
			castingRule = environment.getTypes().ANYARRAY.getCastingRule(predictedType);
			if (castingRule != null) {
				if (castingRule.getInputType() instanceof ZenTypeArray) {
					predictedBaseType = ((ZenTypeArray) castingRule.getInputType()).getBaseType();
					if (castingRule.getInputType() instanceof ZenTypeArrayBasic) {
						// TODO: allow any kind of array type
						arrayType = (ZenTypeArrayBasic) castingRule.getInputType();
					}
				} else {
					environment.error(getPosition(), "Invalid caster - any[] caster but input type is not an array");
					castingRule = null;
				}
			}
		}
		
		System.out.println("Predicted array base type " + (predictedBaseType == null ? "none" : predictedBaseType.getName()));
		System.out.println("Contents are: " + contents);
		
		Expression[] cContents = new Expression[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			cContents[i] = contents.get(i).compile(environment, predictedBaseType).eval();
		}
		Expression result = new ExpressionArray(getPosition(), environment, arrayType, cContents);
		if (castingRule != null) {
			return new ExpressionAs(getPosition(), environment, result, castingRule);
		} else {
			return result;
		}
	}
	
	@Override
	public Expression compileKey(IScopeMethod environment, ZenType predictedType) {
		if (contents.size() == 1 && contents.get(0) instanceof ParsedExpressionVariable) {
			return contents.get(0).compile(environment, predictedType).eval();
		} else {
			return compile(environment, predictedType).eval();
		}
	}
}
