/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.parser.expression.ParsedCallArguments.MatchedArguments;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCall extends ParsedExpression {
	private final ParsedExpression receiver;
	private final ParsedCallArguments arguments;
	
	public ParsedExpressionCall(CodePosition position, ParsedExpression receiver, ParsedCallArguments arguments) {
		super(position);
		
		this.receiver = receiver;
		this.arguments = arguments;
	}
	
	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		IPartialExpression cReceiver = receiver.compilePartial(environment, null);
		
		MatchedArguments matchedArguments = arguments.compile(cReceiver.getMethods(), environment);
		if (matchedArguments.method.isStatic()) {
			return new ExpressionCallStatic(
					getPosition(),
					environment,
					matchedArguments.method,
					matchedArguments.arguments);
		} else {
			return new ExpressionCallVirtual(
					getPosition(),
					environment,
					matchedArguments.method,
					cReceiver.eval(),
					matchedArguments.arguments);
		}
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny receiverValue = receiver.eval(environment);
		if (receiverValue == null)
			return null;
		
		IAny[] argumentValues = arguments.compileConstants(environment);
		if (argumentValues == null)
			return null;
		
		return receiverValue.call(argumentValues);
	}
}
