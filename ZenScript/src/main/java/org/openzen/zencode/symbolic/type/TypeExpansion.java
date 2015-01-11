/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.AccessType;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.expression.partial.PartialVirtualMember;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class TypeExpansion<E extends IPartialExpression<E>>
{
	private final AccessType accessType;
	private final IModuleScope<E> scope;
	
	private final Map<OperatorType, List<IMethod<E>>> operators;
	private final Map<String, IMethod<E>> getters;
	private final Map<String, IMethod<E>> setters;
	private final Map<String, List<IMethod<E>>> methods;
	private final List<IMethod<E>> casters;
	
	private final Map<String, IMethod<E>> staticGetters;
	private final Map<String, IMethod<E>> staticSetters;
	private final Map<String, List<IMethod<E>>> staticMethods;
	
	public TypeExpansion(IModuleScope<E> scope, AccessType accessType)
	{
		this.scope = scope;
		this.accessType = accessType;
		
		operators = new EnumMap<OperatorType, List<IMethod<E>>>(OperatorType.class);
		getters = new HashMap<String, IMethod<E>>();
		setters = new HashMap<String, IMethod<E>>();
		methods = new HashMap<String, List<IMethod<E>>>();
		casters = new ArrayList<IMethod<E>>();
		
		staticGetters = new HashMap<String, IMethod<E>>();
		staticSetters = new HashMap<String, IMethod<E>>();
		staticMethods = new HashMap<String, List<IMethod<E>>>();
	}
	
	public IModuleScope<E> getScope()
	{
		return scope;
	}
	
	public boolean isVisibleTo(AccessScope usingScope)
	{
		return accessType.isVisible(usingScope, scope.getAccessScope());
	}
	
	public void addCaster(IMethod<E> caster)
	{
		casters.add(caster);
	}
	
	public void addGetter(String name, IMethod<E> getter)
	{
		getters.put(name, getter);
	}
	
	public void addSetter(String name, IMethod<E> setter)
	{
		setters.put(name, setter);
	}
	
	public void addMethod(String name, IMethod<E> method)
	{
		if (!methods.containsKey(name))
			methods.put(name, new ArrayList<IMethod<E>>());
		
		methods.get(name).add(method);
	}
	
	public void addOperator(OperatorType operator, IMethod<E> method)
	{
		if (!operators.containsKey(operator))
			operators.put(operator, new ArrayList<IMethod<E>>());
		
		operators.get(operator).add(method);
	}
	
	public void addStaticGetter(String name, IMethod<E> getter)
	{
		staticGetters.put(name, getter);
	}
	
	public void addStaticSetter(String name, IMethod<E> setter)
	{
		staticSetters.put(name, setter);
	}
	
	public void addStaticMethod(String name, IMethod<E> method)
	{
		if (!staticMethods.containsKey(name))
			staticMethods.put(name, new ArrayList<IMethod<E>>());
		
		staticMethods.get(name).add(method);
	}
	
	public boolean hasOperator(OperatorType operator)
	{
		return operators.containsKey(operator);
	}
	
	public E operatorExact(CodePosition position, IMethodScope<E> scope, OperatorType operator, List<E> values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod<E> method : operators.get(operator)) {
			if (method.getMethodHeader().acceptsWithExactTypes(values))
				return method.callStatic(position, scope, values);
		}
		
		return null;
	}
	
	public E operator(CodePosition position, IMethodScope<E> scope, OperatorType operator, List<E> values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod<E> method : operators.get(operator)) {
			if (method.getMethodHeader().accepts(values))
				return method.callStatic(position, scope, values);
		}
		
		return null;
	}
	
	public List<IMethod<E>> getOperators(OperatorType operator)
	{
		return operators.get(operator);
	}
	
	public void expandMember(PartialVirtualMember<E> member, TypeInstance<E> type)
	{
		if (getters.containsKey(member.getName()))
			member.setGetter(new ExpansionGetter<E>(member, getters.get(member.getName())));
		
		if (setters.containsKey(member.getName()))
			member.setSetter(new ExpansionSetter<E>(member, setters.get(member.getName())));
		
		if (methods.containsKey(member.getName())) {
			for (IMethod<E> method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandStaticMember(PartialStaticMember<E> member)
	{
		if (staticGetters.containsKey(member.getName()))
			member.setGetter(new ExpansionStaticGetter<E>(member, staticGetters.get(member.getName())));
		
		if (staticSetters.containsKey(member.getName()))
			member.setSetter(new ExpansionStaticSetter<E>(member, staticSetters.get(member.getName())));
		
		if (staticMethods.containsKey(member.getName())) {
			for (IMethod<E> method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandCastingRules(ICastingRuleDelegate<E> rules)
	{
		for (IMethod<E> caster : casters) {
			rules.registerCastingRule(caster.getReturnType(), new CastingRuleStaticMethod<E>(caster));
		}
	}
}
