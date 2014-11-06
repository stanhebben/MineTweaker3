/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.expand;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.type.casting.CastingRuleDelegateStaticMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ZenExpandCaster {
	private final IMethod method;
	
	public ZenExpandCaster(IMethod method) {
		this.method = method;
	}
	
	public ZenType getTarget() {
		return method.getReturnType();
	}
	
	public void constructCastingRules(ICastingRuleDelegate rules) {
		ZenType type = method.getReturnType();
		rules.registerCastingRule(type, new CastingRuleStaticMethod(method));
		
		type.constructCastingRules(new CastingRuleDelegateStaticMethod(rules, method), false);
	}
	
	public Expression cast(CodePosition position, IScopeMethod environment, Expression expression) {
		return new ExpressionCallStatic(position, environment, method, expression);
	}
	
	public void compile(MethodOutput output) {
		method.invokeStatic(output);
	}
}
