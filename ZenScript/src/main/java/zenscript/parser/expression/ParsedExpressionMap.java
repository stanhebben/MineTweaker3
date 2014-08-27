/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionAs;
import stanhebben.zenscript.expression.ExpressionMap;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionMap extends ParsedExpression {
	private final List<ParsedExpression> keys;
	private final List<ParsedExpression> values;
	
	public ParsedExpressionMap(
			ZenPosition position,
			List<ParsedExpression> keys,
			List<ParsedExpression> values) {
		super(position);
		
		this.keys = keys;
		this.values = values;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		ZenType predictedKeyType = null;
		ZenType predictedValueType = null;
		ICastingRule castingRule = null;
		ZenTypeAssociative mapType = environment.getTypes().ANYMAP;
		
		if (predictedType instanceof ZenTypeAssociative) {
			ZenTypeAssociative inputType = (ZenTypeAssociative) predictedType;
			predictedKeyType = inputType.getKeyType();
			predictedValueType = inputType.getValueType();
			mapType = inputType;
		} else {
			castingRule = environment.getTypes().ANYMAP.getCastingRule(predictedType);
			if (castingRule != null) {
				if (castingRule.getInputType() instanceof ZenTypeAssociative) {
					ZenTypeAssociative inputType = (ZenTypeAssociative) castingRule.getInputType();
					predictedKeyType = inputType.getKeyType();
					predictedValueType = inputType.getValueType();
					mapType = inputType;
				} else {
					environment.error(getPosition(), "Caster found for any[any] but its input is not an associative array");
				}
			}
		}
		
		Expression[] cKeys = new Expression[keys.size()];
		Expression[] cValues = new Expression[values.size()];
		
		for (int i = 0; i < keys.size(); i++) {
			cKeys[i] = keys.get(i).compileKey(environment, predictedKeyType);
			cValues[i] = values.get(i).compile(environment, predictedValueType).eval();
		}
		
		Expression result = new ExpressionMap(getPosition(), environment, cKeys, cValues, mapType);
		if (castingRule != null) {
			return new ExpressionAs(getPosition(), environment, result, castingRule);
		} else {
			return result;
		}
	}
}
