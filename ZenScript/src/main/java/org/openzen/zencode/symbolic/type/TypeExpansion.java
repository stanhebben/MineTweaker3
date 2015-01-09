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
import org.openzen.zencode.symbolic.scope.IGlobalScope;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class TypeExpansion<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final AccessType accessType;
	private final AccessScope accessScope;
	private final IGlobalScope<E, T> scope;
	
	private final Map<OperatorType, List<IMethod<E, T>>> operators;
	private final Map<String, IMethod<E, T>> getters;
	private final Map<String, IMethod<E, T>> setters;
	private final Map<String, List<IMethod<E, T>>> methods;
	private final List<IMethod<E, T>> casters;
	
	private final Map<String, IMethod<E, T>> staticGetters;
	private final Map<String, IMethod<E, T>> staticSetters;
	private final Map<String, List<IMethod<E, T>>> staticMethods;
	
	public TypeExpansion(IGlobalScope<E, T> scope, AccessType accessType, AccessScope accessScope)
	{
		this.scope = scope;
		this.accessType = accessType;
		this.accessScope = accessScope;
		
		operators = new EnumMap<OperatorType, List<IMethod<E, T>>>(OperatorType.class);
		getters = new HashMap<String, IMethod<E, T>>();
		setters = new HashMap<String, IMethod<E, T>>();
		methods = new HashMap<String, List<IMethod<E, T>>>();
		casters = new ArrayList<IMethod<E, T>>();
		
		staticGetters = new HashMap<String, IMethod<E, T>>();
		staticSetters = new HashMap<String, IMethod<E, T>>();
		staticMethods = new HashMap<String, List<IMethod<E, T>>>();
	}
	
	public AccessScope getScope()
	{
		return accessScope;
	}
	
	public boolean isVisibleTo(AccessScope usingScope)
	{
		return accessType.isVisible(usingScope, accessScope);
	}
	
	public void addCaster(IMethod<E, T> caster)
	{
		casters.add(caster);
	}
	
	public void addGetter(String name, IMethod<E, T> getter)
	{
		getters.put(name, getter);
	}
	
	public void addSetter(String name, IMethod<E, T> setter)
	{
		setters.put(name, setter);
	}
	
	public void addMethod(String name, IMethod<E, T> method)
	{
		if (!methods.containsKey(name))
			methods.put(name, new ArrayList<IMethod<E, T>>());
		
		methods.get(name).add(method);
	}
	
	public void addOperator(OperatorType operator, IMethod<E, T> method)
	{
		if (!operators.containsKey(operator))
			operators.put(operator, new ArrayList<IMethod<E, T>>());
		
		operators.get(operator).add(method);
	}
	
	public void addStaticGetter(String name, IMethod<E, T> getter)
	{
		staticGetters.put(name, getter);
	}
	
	public void addStaticSetter(String name, IMethod<E, T> setter)
	{
		staticSetters.put(name, setter);
	}
	
	public void addStaticMethod(String name, IMethod<E, T> method)
	{
		if (!staticMethods.containsKey(name))
			staticMethods.put(name, new ArrayList<IMethod<E, T>>());
		
		staticMethods.get(name).add(method);
	}
	
	public boolean hasOperator(OperatorType operator)
	{
		return operators.containsKey(operator);
	}
	
	public E operatorExact(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, List<E> values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod<E, T> method : operators.get(operator)) {
			if (method.getMethodHeader().acceptsWithExactTypes(values))
				return method.callStatic(position, scope, values);
		}
		
		return null;
	}
	
	public E operator(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, List<E> values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod<E, T> method : operators.get(operator)) {
			if (method.getMethodHeader().accepts(values))
				return method.callStatic(position, scope, values);
		}
		
		return null;
	}
	
	public List<IMethod<E, T>> getOperators(OperatorType operator)
	{
		return operators.get(operator);
	}
	
	public void expandMember(PartialVirtualMember<E, T> member, T type)
	{
		if (getters.containsKey(member.getName()))
			member.setGetter(new ExpansionGetter<E, T>(member, getters.get(member.getName())));
		
		if (setters.containsKey(member.getName()))
			member.setSetter(new ExpansionSetter<E, T>(member, setters.get(member.getName())));
		
		if (methods.containsKey(member.getName())) {
			for (IMethod<E, T> method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandStaticMember(PartialStaticMember<E, T> member)
	{
		if (staticGetters.containsKey(member.getName()))
			member.setGetter(new ExpansionStaticGetter<E, T>(member, staticGetters.get(member.getName())));
		
		if (staticSetters.containsKey(member.getName()))
			member.setSetter(new ExpansionStaticSetter<E, T>(member, staticSetters.get(member.getName())));
		
		if (staticMethods.containsKey(member.getName())) {
			for (IMethod<E, T> method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandCastingRules(ICastingRuleDelegate<E, T> rules)
	{
		for (IMethod<E, T> caster : casters) {
			rules.registerCastingRule(caster.getReturnType(), new CastingRuleStaticMethod<E, T>(caster));
		}
	}
}
