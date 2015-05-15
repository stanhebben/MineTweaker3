/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyArray;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 */
public class ParsedExpressionArray extends ParsedExpression
{
	private final List<ParsedExpression> contents;

	public ParsedExpressionArray(CodePosition position, List<ParsedExpression> contents)
	{
		super(position);

		this.contents = contents;
	}
	
	// #######################################
	// ### ParsedExpression implementation ###
	// #######################################

	@Override
	public <E extends IPartialExpression<E>>
			IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> asType)
	{
		IGenericType<E> arrayType;
		ICastingRule<E> castingRule = null;
		
		if (asType != null && asType.getArrayBaseType() != null)
			arrayType = asType;
		else if (asType != null) {
			castingRule = scope.getTypeCompiler().anyArray.getCastingRule(scope, asType);
			if (castingRule == null) {
				scope.getErrorLogger().errorCannotCastArrayTo(getPosition(), asType);
				return scope.getExpressionCompiler().invalid(getPosition(), scope, asType);
			}
			
			arrayType = castingRule.getInputType();
		} else
			arrayType = scope.getTypeCompiler().anyArray;
		
		List<E> cContents = new ArrayList<>();
		for (ParsedExpression content : contents) {
			cContents.add(content.compile(scope, arrayType.getArrayBaseType()));
		}

		E result = scope.getExpressionCompiler().array(getPosition(), scope, arrayType, cContents);
		if (castingRule != null)
			result = castingRule.cast(getPosition(), scope, result);
		
		return result;
	}

	@Override
	public <E extends IPartialExpression<E>>
			E compileKey(IMethodScope<E> environment, IGenericType<E> predictedType)
	{
		if (contents.size() == 1 && contents.get(0) instanceof ParsedExpressionVariable)
			return contents.get(0).compile(environment, predictedType);
		else
			return compile(environment, predictedType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		List<IAny> values = new ArrayList<>();
		for (ParsedExpression content : contents) {
			IAny value = content.eval(environment);
			if (value == null)
				return null;
			
			values.add(value);
		}

		return new AnyArray(values);
	}
}
