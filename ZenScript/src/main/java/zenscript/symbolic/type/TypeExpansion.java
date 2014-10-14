/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.type;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import zenscript.annotations.OperatorType;
import zenscript.symbolic.AccessScope;
import zenscript.symbolic.MemberVirtual;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class TypeExpansion
{
	private final AccessScope scope;
	private final Map<OperatorType, List<IMethod>> operators;
	private final Map<String, IMethod> getters;
	private final Map<String, IMethod> setters;
	private final Map<String, List<IMethod>> methods;
	
	public TypeExpansion(AccessScope scope)
	{
		this.scope = scope;
		
		operators = new EnumMap<OperatorType, List<IMethod>>(OperatorType.class);
	}
	
	public AccessScope getScope()
	{
		return scope;
	}
	
	public boolean hasOperator(OperatorType operator)
	{
		return operators.containsKey(operator);
	}
	
	public Expression operatorExact(ZenPosition position, IScopeMethod scope, OperatorType operator, Expression... values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod method : operators.get(operator)) {
			if (method.getMethodHeader().acceptsWithExactTypes(values))
				return new ExpressionCallStatic(position, scope, method, values);
		}
		
		return null;
	}
	
	public Expression operator(ZenPosition position, IScopeMethod scope, OperatorType operator, Expression... values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod method : operators.get(operator)) {
			if (method.getMethodHeader().accepts(values))
				return new ExpressionCallStatic(position, scope, method, values);
		}
		
		return null;
	}
	
	public void expandMember(MemberVirtual member)
	{
		// TODO: complete
	}
	
	public void expandStaticMember(MemberStatic member)
	{
		
	}
}
