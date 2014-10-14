/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionNew;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.parser.expression.ParsedCallArguments.MatchedArguments;
import zenscript.parser.type.IParsedType;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionNew extends ParsedExpression {
	private final IParsedType type;
	private final ParsedCallArguments callArguments;
	
	public ParsedExpressionNew(ZenPosition position, IParsedType type, ParsedCallArguments callArguments) {
		super(position);
		
		this.type = type;
		this.callArguments = callArguments;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		ZenType cType = type.compile(environment);
		MatchedArguments compiledArguments = callArguments.compile(cType.getConstructors(), environment);
		return new ExpressionNew(getPosition(), environment, cType, compiledArguments.method, compiledArguments.arguments);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
