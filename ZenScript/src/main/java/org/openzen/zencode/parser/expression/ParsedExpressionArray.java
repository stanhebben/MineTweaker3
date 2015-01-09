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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionArray extends ParsedExpression
{
	private final List<ParsedExpression> contents;

	public ParsedExpressionArray(CodePosition position, List<ParsedExpression> contents)
	{
		super(position);

		this.contents = contents;
	}

	@Override
	public <E extends IPartialExpression<E>>
			IPartialExpression<E> compilePartial(IMethodScope<E> scope, TypeInstance<E> asType)
	{
		TypeInstance<E> arrayType;
		ICastingRule<E> castingRule = null;
		
		if (asType != null && asType.getArrayBaseType() != null)
			arrayType = asType;
		else if (asType != null) {
			castingRule = scope.getTypeCompiler().getAnyArray(scope).getCastingRule(asType);
			if (castingRule == null) {
				scope.getErrorLogger().errorCannotCastArrayTo(getPosition(), asType);
				return scope.getExpressionCompiler().invalid(getPosition(), scope, asType);
			}
			
			arrayType = castingRule.getInputType();
		} else
			arrayType = scope.getTypeCompiler().getAnyArray(scope);
		
		List<E> cContents = new ArrayList<E>();
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
			E compileKey(IMethodScope<E> environment, TypeInstance<E> predictedType)
	{
		if (contents.size() == 1 && contents.get(0) instanceof ParsedExpressionVariable)
			return contents.get(0).compile(environment, predictedType);
		else
			return compile(environment, predictedType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		IAny[] values = new IAny[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			values[i] = contents.get(i).eval(environment);
			if (values[i] == null)
				return null;
		}

		return new AnyArray(values);
	}
}
