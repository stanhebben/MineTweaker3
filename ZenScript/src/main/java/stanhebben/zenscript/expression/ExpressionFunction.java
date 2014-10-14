/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.method.MethodHeader;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionFunction extends Expression {
	private final ZenTypeFunction functionType;
	private final List<Statement> statements;
	
	public ExpressionFunction(ZenPosition position, IScopeMethod environment, MethodHeader header, List<Statement> statements) {
		super(position, environment);
		
		this.statements = statements;
		
		functionType = new ZenTypeFunction(header);
	}

	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		ICastingRule castingRule = functionType.getCastingRule(type);
		if (castingRule != null) {
			// TODO: finish, but how?
			throw new UnsupportedOperationException("not yet implemented");
		} else {
			getEnvironment().error(position, "Cannot cast a function literal to " + type.toString());
			return new ExpressionInvalid(position, getEnvironment(), type);
		}
	}

	@Override
	public ZenType getType() {
		return functionType;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		// TODO: implement
		// TODO: make sure the function is compiled properly
		throw new UnsupportedOperationException("not yet implemented");
	}
}
