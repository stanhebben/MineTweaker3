/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenMethodStatic;
import org.openzen.zencode.annotations.ZenOperator;
import org.openzen.zencode.annotations.ZenSetter;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.AccessType;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.JavaMethod;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.type.casting.CastingRuleStaticMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TypeExpansion
{
	private final AccessType accessType;
	private final AccessScope accessScope;
	private final IScopeGlobal scope;
	
	private final Map<OperatorType, List<IMethod>> operators;
	private final Map<String, IMethod> getters;
	private final Map<String, IMethod> setters;
	private final Map<String, List<IMethod>> methods;
	private final List<IMethod> casters;
	
	private final Map<String, IMethod> staticGetters;
	private final Map<String, IMethod> staticSetters;
	private final Map<String, List<IMethod>> staticMethods;
	
	public TypeExpansion(IScopeGlobal scope, AccessType accessType, AccessScope accessScope)
	{
		this.scope = scope;
		this.accessType = accessType;
		this.accessScope = accessScope;
		
		operators = new EnumMap<OperatorType, List<IMethod>>(OperatorType.class);
		getters = new HashMap<String, IMethod>();
		setters = new HashMap<String, IMethod>();
		methods = new HashMap<String, List<IMethod>>();
		casters = new ArrayList<IMethod>();
		
		staticGetters = new HashMap<String, IMethod>();
		staticSetters = new HashMap<String, IMethod>();
		staticMethods = new HashMap<String, List<IMethod>>();
	}
	
	public void load(Class javaExpansionClass)
	{
		for (Method method : javaExpansionClass.getMethods()) {
			String methodName = method.getName();
			
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof ZenCaster) {
					checkStatic(method);
					casters.add(JavaMethod.get(scope.getTypes(), method));
				} else if (annotation instanceof ZenGetter) {
					checkStatic(method);
					ZenGetter getterAnnotation = (ZenGetter) annotation;
					String name = getterAnnotation.value().length() == 0 ? method.getName() : getterAnnotation.value();
					getters.put(name, JavaMethod.get(scope.getTypes(), method));
				} else if (annotation instanceof ZenSetter) {
					checkStatic(method);
					ZenSetter setterAnnotation = (ZenSetter) annotation;
					String name = setterAnnotation.value().length() == 0 ? method.getName() : setterAnnotation.value();
					setters.put(name, JavaMethod.get(scope.getTypes(), method));
				} else if (annotation instanceof ZenOperator) {
					checkStatic(method);
					ZenOperator operatorAnnotation = (ZenOperator) annotation;
					
					OperatorType operator = operatorAnnotation.value();
					if (operator.getArgumentCount() != method.getParameterTypes().length)
						throw new RuntimeException("Numbor of operator arguments is incorrect");
					
					if (!operators.containsKey(operator))
						operators.put(operator, new ArrayList<IMethod>());
					
					operators.get(operator).add(JavaMethod.get(scope.getTypes(), method));
				} else if (annotation instanceof ZenMethod) {
					checkStatic(method);
					ZenMethod methodAnnotation = (ZenMethod) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();
					
					if (!methods.containsKey(methodName))
						methods.put(methodName, new ArrayList<IMethod>());
					
					methods.get(methodName).add(JavaMethod.get(scope.getTypes(), method));
				} else if (annotation instanceof ZenMethodStatic) {
					checkStatic(method);
					ZenMethodStatic methodAnnotation = (ZenMethodStatic) annotation;
					if (methodAnnotation.value().length() > 0) {
						methodName = methodAnnotation.value();
					}
					
					if (!staticMethods.containsKey(methodName))
						staticMethods.put(methodName, new ArrayList<IMethod>());
					
					staticMethods.get(methodName).add(JavaMethod.get(scope.getTypes(), method));
				}
			}
		}
	}
	
	/**
	 * Checks if the given method is static. Throws an exception if not.
	 * 
	 * @param method metod to validate
	 */
	private void checkStatic(Method method) {
		if ((method.getModifiers() & Modifier.STATIC) == 0) {
			throw new RuntimeException("Expansion method " + method.getName() + " must be static");
		}
	}
	
	public AccessScope getScope()
	{
		return accessScope;
	}
	
	public boolean isVisibleTo(AccessScope usingScope)
	{
		return accessType.isVisible(usingScope, accessScope);
	}
	
	public boolean hasOperator(OperatorType operator)
	{
		return operators.containsKey(operator);
	}
	
	public Expression operatorExact(CodePosition position, IScopeMethod scope, OperatorType operator, Expression... values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod method : operators.get(operator)) {
			if (method.getMethodHeader().acceptsWithExactTypes(values))
				return new ExpressionCallStatic(position, scope, method, values);
		}
		
		return null;
	}
	
	public Expression operator(CodePosition position, IScopeMethod scope, OperatorType operator, Expression... values)
	{
		if (!operators.containsKey(operator))
			return null;
		
		for (IMethod method : operators.get(operator)) {
			if (method.getMethodHeader().accepts(values))
				return new ExpressionCallStatic(position, scope, method, values);
		}
		
		return null;
	}
	
	public List<IMethod> getOperators(OperatorType operator)
	{
		return operators.get(operator);
	}
	
	public void expandMember(MemberVirtual member)
	{
		if (getters.containsKey(member.getName()))
			member.setGetter(new ExpansionGetter(member, getters.get(member.getName())));
		
		if (setters.containsKey(member.getName()))
			member.setSetter(new ExpansionSetter(member, setters.get(member.getName())));
		
		if (methods.containsKey(member.getName())) {
			for (IMethod method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandStaticMember(MemberStatic member)
	{
		if (staticGetters.containsKey(member.getName()))
			member.setGetter(new ExpansionStaticGetter(member, staticGetters.get(member.getName())));
		
		if (staticSetters.containsKey(member.getName()))
			member.setSetter(new ExpansionStaticSetter(member, staticSetters.get(member.getName())));
		
		if (staticMethods.containsKey(member.getName())) {
			for (IMethod method : methods.get(member.getName())) {
				member.addMethod(method);
			}
		}
	}
	
	public void expandCastingRules(ICastingRuleDelegate rules)
	{
		for (IMethod caster : casters) {
			rules.registerCastingRule(caster.getReturnType(), new CastingRuleStaticMethod(caster));
		}
	}
}
