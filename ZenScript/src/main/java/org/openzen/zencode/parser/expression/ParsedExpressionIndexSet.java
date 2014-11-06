/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionIndexSet extends ParsedExpression {
	private final ParsedExpression value;
	private final ParsedExpression index;
	private final ParsedExpression setValue;
	
	public ParsedExpressionIndexSet(CodePosition position, ParsedExpression value, ParsedExpression index, ParsedExpression setValue) {
		super(position);
		
		this.value = value;
		this.index = index;
		this.setValue = setValue;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve prediction in this expression
		Expression cValue = value.compile(environment, null);
		Expression cIndex = index.compile(environment, null);
		Expression cSetValue = setValue.compile(environment, null);
		return cValue.getType().operator(getPosition(), environment, OperatorType.INDEXSET, cValue, cIndex, cSetValue);
	} 

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
