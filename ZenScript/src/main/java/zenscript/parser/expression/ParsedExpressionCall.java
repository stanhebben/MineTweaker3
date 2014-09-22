/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.parser.expression.ParsedCallArguments.MatchedArguments;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCall extends ParsedExpression {
	private final ParsedExpression receiver;
	private final ParsedCallArguments arguments;
	
	public ParsedExpressionCall(ZenPosition position, ParsedExpression receiver, ParsedCallArguments arguments) {
		super(position);
		
		this.receiver = receiver;
		this.arguments = arguments;
	}
	
	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		IPartialExpression cReceiver = receiver.compile(environment, null);
		
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
		
		IAny[] argumentValues = arguments.compileValues(environment);
		if (argumentValues == null)
			return null;
		
		return receiverValue.call(argumentValues);
	}
}
