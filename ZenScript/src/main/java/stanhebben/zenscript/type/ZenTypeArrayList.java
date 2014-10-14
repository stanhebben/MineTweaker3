/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import java.util.List;
import org.objectweb.asm.Type;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionListGet;
import stanhebben.zenscript.expression.ExpressionListLength;
import stanhebben.zenscript.expression.ExpressionListSet;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.iterator.IteratorIterable;
import stanhebben.zenscript.type.iterator.IteratorList;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import zenscript.symbolic.type.casting.CastingRuleDelegateList;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ZenTypeArrayList extends ZenTypeArray {
	private final Type type;
	
	public ZenTypeArrayList(ZenType baseType) {
		super(baseType);
		
		type = Type.getType(List.class);
	}

	@Override
	public IPartialExpression getMemberLength(ZenPosition position, IScopeMethod environment, IPartialExpression value) {
		return new ExpressionListLength(position, environment, value.eval());
	}
	
	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		ICastingRuleDelegate arrayRules = new CastingRuleDelegateList(getScope(), rules, this);
		getBaseType().constructCastingRules(arrayRules, followCasters);
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}
	
	@Override
	public IZenIterator makeIterator(int numValues) {
		if (numValues == 1) {
			return new IteratorIterable(getBaseType());
		} else if (numValues == 2) {
			return new IteratorList(getScope(), getBaseType());
		} else {
			return null;
		}
	}

	@Override
	public Class toJavaClass() {
		return List.class;
	}
	
	@Override
	public String getAnyClassName() {
		return null;
	}

	@Override
	public Type toASMType() {
		return Type.getType(List.class);
	}

	@Override
	public String getSignature() {
		return signature(List.class);
	}
	
	@Override
	public Expression indexGet(ZenPosition position, IScopeMethod environment, Expression array, Expression index) {
		return new ExpressionListGet(position, environment, array, index);
	}

	@Override
	public Expression indexSet(ZenPosition position, IScopeMethod environment, Expression array, Expression index, Expression value) {
		return new ExpressionListSet(position, environment, array, index, value);
	}
}
