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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.casting.CastingRuleMethod;
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
	
	private final Map<OperatorType, List<IVirtualCallable<E>>> operators;
	private final Map<String, IVirtualCallable<E>> getters;
	private final Map<String, IVirtualCallable<E>> setters;
	private final Map<String, List<IVirtualCallable<E>>> methods;
	private final List<IVirtualCallable<E>> casters;
	
	private final Map<String, ICallable<E>> staticGetters;
	private final Map<String, ICallable<E>> staticSetters;
	private final Map<String, List<ICallable<E>>> staticMethods;
	
	public TypeExpansion(IModuleScope<E> scope, AccessType accessType)
	{
		this.scope = scope;
		this.accessType = accessType;
		
		operators = new EnumMap<OperatorType, List<IVirtualCallable<E>>>(OperatorType.class);
		getters = new HashMap<String, IVirtualCallable<E>>();
		setters = new HashMap<String, IVirtualCallable<E>>();
		methods = new HashMap<String, List<IVirtualCallable<E>>>();
		casters = new ArrayList<IVirtualCallable<E>>();
		
		staticGetters = new HashMap<String, ICallable<E>>();
		staticSetters = new HashMap<String, ICallable<E>>();
		staticMethods = new HashMap<String, List<ICallable<E>>>();
	}
	
	public IModuleScope<E> getScope()
	{
		return scope;
	}
	
	public boolean isVisibleTo(AccessScope usingScope)
	{
		return accessType.isVisible(usingScope, scope.getAccessScope());
	}
	
	public void addCaster(IVirtualCallable<E> caster)
	{
		casters.add(caster);
	}
	
	public void addGetter(String name, IVirtualCallable<E> getter)
	{
		getters.put(name, getter);
	}
	
	public void addSetter(String name, IVirtualCallable<E> setter)
	{
		setters.put(name, setter);
	}
	
	public void addMethod(String name, IVirtualCallable<E> method)
	{
		if (!methods.containsKey(name))
			methods.put(name, new ArrayList<IVirtualCallable<E>>());
		
		methods.get(name).add(method);
	}
	
	public void addOperator(OperatorType operator, IVirtualCallable<E> method)
	{
		if (!operators.containsKey(operator))
			operators.put(operator, new ArrayList<IVirtualCallable<E>>());
		
		operators.get(operator).add(method);
	}
	
	public void addStaticGetter(String name, ICallable<E> getter)
	{
		staticGetters.put(name, getter);
	}
	
	public void addStaticSetter(String name, ICallable<E> setter)
	{
		staticSetters.put(name, setter);
	}
	
	public void addStaticMethod(String name, ICallable<E> method)
	{
		if (!staticMethods.containsKey(name))
			staticMethods.put(name, new ArrayList<ICallable<E>>());
		
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
		
		for (IVirtualCallable<E> method : operators.get(operator)) {
			if (method.getMethodHeader().acceptsWithExactTypes(values))
				return method.bind(values.get(0)).call(position, scope, values.subList(1, values.size()));
		}
		
		return null;
	}
	
	public E operator(CodePosition position, IMethodScope<E> scope, OperatorType operator, List<E> values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IVirtualCallable<E> method : operators.get(operator)) {
			if (method.getMethodHeader().accepts(scope, values))
				return method.bind(values.get(0)).call(position, scope, values);
		}
		
		return null;
	}
	
	public List<IVirtualCallable<E>> getOperators(OperatorType operator)
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
			for (IVirtualCallable<E> method : methods.get(member.getName())) {
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
			for (ICallable<E> method : staticMethods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandCastingRules(ICastingRuleDelegate<E> rules)
	{
		for (IVirtualCallable<E> caster : casters) {
			rules.registerCastingRule(caster.getMethodHeader().getReturnType(), new CastingRuleMethod<E>(caster));
		}
	}
}
