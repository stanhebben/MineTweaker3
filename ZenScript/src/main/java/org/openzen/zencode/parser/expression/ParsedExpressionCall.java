/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.parser.expression.ParsedCallArguments.MatchedArguments;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCall extends ParsedExpression
{
	private final ParsedExpression receiver;
	private final ParsedCallArguments arguments;

	public ParsedExpressionCall(CodePosition position, ParsedExpression receiver, ParsedCallArguments arguments)
	{
		super(position);

		this.receiver = receiver;
		this.arguments = arguments;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, TypeInstance<E> asType)
	{
		IPartialExpression<E> cReceiver = receiver.compilePartial(scope, null);

		MatchedArguments<E> matchedArguments = arguments.compile(cReceiver.getMethods(), scope);
		if (matchedArguments == null) {
			if (cReceiver.getMethods().isEmpty())
				scope.getErrorLogger().errorNotAValidMethod(getPosition());
			else
				scope.getErrorLogger().errorNoMatchingMethod(getPosition(), cReceiver.getMethods(), arguments);
			
			return scope.getExpressionCompiler().invalid(getPosition(), scope);
		}

		IPartialExpression<E> result = cReceiver.call(getPosition(), matchedArguments.method, matchedArguments.arguments);
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		IAny receiverValue = receiver.eval(environment);
		if (receiverValue == null)
			return null;

		IAny[] argumentValues = arguments.compileConstants(environment);
		if (argumentValues == null)
			return null;

		return receiverValue.call(argumentValues);
	}
}
