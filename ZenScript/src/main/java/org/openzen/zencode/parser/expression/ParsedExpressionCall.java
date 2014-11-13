/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.parser.expression.ParsedCallArguments.MatchedArguments;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.ExpressionInvalid;

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
	public IPartialExpression compilePartial(IScopeMethod scope, ZenType predictedType)
	{
		IPartialExpression cReceiver = receiver.compilePartial(scope, null);

		MatchedArguments matchedArguments = arguments.compile(cReceiver.getMethods(), scope);
		if (matchedArguments == null) {
			if (cReceiver.getMethods().isEmpty())
				scope.error(getPosition(), "Trying to call a non-method");
			else
				scope.error(getPosition(), "No method matched the given arguments");
			return new ExpressionInvalid(getPosition(), scope);
		}

		return cReceiver.call(getPosition(), matchedArguments.method, matchedArguments.arguments);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment)
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
