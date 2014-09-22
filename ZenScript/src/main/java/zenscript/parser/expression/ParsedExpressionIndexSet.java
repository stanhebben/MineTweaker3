/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionIndexSet extends ParsedExpression {
	private final ParsedExpression value;
	private final ParsedExpression index;
	private final ParsedExpression setValue;
	
	public ParsedExpressionIndexSet(ZenPosition position, ParsedExpression value, ParsedExpression index, ParsedExpression setValue) {
		super(position);
		
		this.value = value;
		this.index = index;
		this.setValue = setValue;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve prediction in this expression
		Expression cValue = value.compile(environment, null).eval();
		Expression cIndex = index.compile(environment, null).eval();
		Expression cSetValue = setValue.compile(environment, null).eval();
		return cValue.getType().trinary(getPosition(), environment, cValue, cIndex, cSetValue,OperatorType.INDEXSET);
	} 

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
