/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.parser.expression.ParsedCallArguments.MatchedArguments;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyAssociative;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionAssociative extends ParsedExpression
{
	private final List<ParsedExpression> keys;
	private final List<ParsedExpression> values;

	public ParsedExpressionAssociative(
			CodePosition position,
			List<ParsedExpression> keys,
			List<ParsedExpression> values)
	{
		super(position);

		this.keys = keys;
		this.values = values;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
			IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T asType)
	{
		if (asType != null && asType.isStruct())
			return compileAsStruct(scope, asType);
		
		ICastingRule<E, T> castingRule = null;
		T mapType;
		
		if (asType != null && asType.getMapKeyType() != null)
			mapType = asType;
		else if (asType != null) {
			castingRule = scope.getTypeCompiler().getAnyAnyMap(scope).getCastingRule(asType);
			if (castingRule == null) {
				scope.getErrorLogger().errorCannotCastMapTo(getPosition(), asType);
				return scope.getExpressionCompiler().invalid(getPosition(), scope, asType);
			}
			
			mapType = castingRule.getInputType();
		}
		else
			mapType = scope.getTypeCompiler().getAnyAnyMap(scope);

		List<E> cKeys = new ArrayList<E>();
		List<E> cValues = new ArrayList<E>();

		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i) == null) {
				scope.getErrorLogger().errorKeyRequired(values.get(i).getPosition());
				continue;
			}
			
			cKeys.add(keys.get(i).compileKey(scope, mapType.getMapKeyType()));
			cValues.add(values.get(i).compile(scope, mapType.getMapValueType()));
		}

		E result = scope.getExpressionCompiler().map(getPosition(), scope, mapType, cKeys, cValues);
		if (castingRule != null)
			return castingRule.cast(getPosition(), scope, result);
		else
			return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		Map<IAny, IAny> map = new HashMap<IAny, IAny>();
		for (int i = 0; i < keys.size(); i++) {
			IAny key = keys.get(i).eval(environment);
			if (key == null)
				return null;

			if (key == AnyNull.INSTANCE)
				key = null;

			IAny value = values.get(i).eval(environment);
			if (value == null)
				return null;

			if (value == AnyNull.INSTANCE)
				value = null;

			map.put(key, value);
		}

		return new AnyAssociative(map);
	}
	
	private <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
			IPartialExpression<E, T> compileAsStruct(IMethodScope<E, T> scope, T asType)
	{
		List<ParsedCallArgument> arguments = new ArrayList<ParsedCallArgument>();
		for (int i = 0; i < keys.size(); i++) {
			ParsedExpression key = keys.get(i);
			String argumentName = null;
			if (key != null) {
				argumentName = key.asIdentifier();
				if (argumentName == null)
					scope.getErrorLogger().errorNotAValidParameterName(getPosition());
			}
			
			arguments.add(new ParsedCallArgument(argumentName, values.get(i)));
		}
		
		ParsedCallArguments allArguments = new ParsedCallArguments(arguments);
		MatchedArguments<E, T> compiledArguments = allArguments.compile(asType.getConstructors(), scope);
		return compiledArguments.method.callStatic(getPosition(), scope, compiledArguments.arguments);
	}
}
