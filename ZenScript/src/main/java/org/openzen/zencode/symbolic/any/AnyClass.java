/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.any;

import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.member.ImplementationMember;
import org.openzen.zencode.symbolic.member.ConstructorMember;
import org.openzen.zencode.symbolic.member.MethodMember;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.scope.ScopeMethod;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Modifiers;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.expression.ExpressionSetInstanceField;
import stanhebben.zenscript.statements.StatementExpression;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public abstract class AnyClass
{
	private final ZenType forType;
	private final CodePosition position;
	
	public AnyClass(ZenType forType)
	{
		this.forType = forType;
		position = new CodePosition(null, 1, 1);
	}
	
	public SymbolicClass generate()
	{
		TypeRegistry types = forType.getTypeRegistry();
		SymbolicClass result = new SymbolicClass(
				types.scopeForAny,
				Modifiers.FINAL | Modifiers.ACCESS_EXPORT,
				forType.getAnyClassName(),
				null);
		
		AnyClassMembers members = new AnyClassMembers(result);
		
		constructField(members);
		constructConstructor(members);
		constructValueOf(members);
		constructAnyImplementation(members);
		
		return result;
	}
	
	private void constructField(AnyClassMembers members)
	{
		members.valueField = new FieldMember(
				members.forClass,
				Modifiers.ACCESS_PRIVATE | Modifiers.FINAL,
				"value",
				forType);
		
		members.forClass.addMember(members.valueField);
	}
	
	private void constructConstructor(AnyClassMembers members)
	{
		TypeRegistry types = forType.getTypeRegistry();
		
		MethodHeader initializerHeader = MethodHeader.singleArgumentVoid("value", forType);
		ScopeMethod initializerScope = new ScopeMethod(members.forClass.getScope(), types.VOID);
		
		Expression expressionThis = new ExpressionLocalGet(
				position,
				initializerScope,
				members.forClass.getThis());
		Expression expressionValue = initializerHeader.makeArgumentGetExpression(
				0,
				position,
				initializerScope);
		Expression expressionInit = new ExpressionSetInstanceField(
				position,
				initializerScope,
				expressionThis,
				members.valueField,
				expressionValue);
		
		members.constructor = new ConstructorMember(
						members.forClass,
						initializerHeader);
		
		members.forClass.addMember(members.constructor);
		members.constructor.addStatement(new StatementExpression(
				position,
				initializerScope,
				expressionInit));
	}
	
	private void constructValueOf(AnyClassMembers members)
	{
		MethodHeader valueOfHeader = MethodHeader.singleArgument(members.anyType, "value", forType);
		ScopeMethod valueOfScope = new ScopeMethod(members.forClass.getScope(), members.anyType);
		MethodMember method = new MethodMember(
				members.forClass,
				Modifiers.STATIC | Modifiers.ACCESS_EXPORT,
				"valueOf",
				valueOfHeader);
		Expression expressionValue = valueOfHeader.makeArgumentGetExpression(
				0,
				position,
				valueOfScope);
		Expression construct = members.constructor.call(position, valueOfScope, expressionValue);
		method.addStatement(construct.asStatement());
	}
	
	private void constructAnyImplementation(AnyClassMembers members)
	{
		TypeRegistry types = forType.getTypeRegistry();
		
		ImplementationMember anyImplementation = new ImplementationMember(members.forClass, types.ANY);
		constructAnyAdd(members);
		
		members.forClass.addMember(anyImplementation);
	}
	
	private void constructAnyNot(AnyClassMembers members)
	{
		TypeRegistry types = forType.getTypeRegistry();
		MethodHeader notHeader = MethodHeader.noArguments(types.ANY);
		ScopeMethod scope = new ScopeMethod(members.forClass.getScope(), types.ANY);
		MethodMember method = new MethodMember(members.forClass, Modifiers.ACCESS_EXPORT | Modifiers.OVERRIDE, "not", notHeader);
		implementAnyNot(members, method, scope);
	}
	
	protected abstract void implementAnyNot(AnyClassMembers members, MethodMember method, ScopeMethod scope);
	
	private void constructAnyAdd(AnyClassMembers members)
	{
		TypeRegistry types = forType.getTypeRegistry();
		MethodHeader addHeader = MethodHeader.singleArgument(types.ANY, "value", types.ANY);
		ScopeMethod scope = new ScopeMethod(members.forClass.getScope(), types.ANY);
		MethodMember method = new MethodMember(members.forClass, Modifiers.ACCESS_EXPORT | Modifiers.OVERRIDE, "add", addHeader);
		implementAnyAdd(members, method, scope);
	}
	
	protected abstract void implementAnyAdd(AnyClassMembers members, MethodMember method, ScopeMethod scope);
	
	protected void implementAnyAddExpansions(AnyClassMembers members, MethodMember method, ScopeMethod scope)
	{
		
	}
	
	protected void implementAnyOperationExpansions(AnyClassMembers members, MethodMember method, ScopeMethod scope, OperatorType operator)
	{
		for (TypeExpansion expansion : forType.getExpansions()) {
			for (IMethod operatorMethod : expansion.getOperators(operator)) {
				Expression call = operatorMethod.call(
						members.position,
						scope,
						members.makeThisExpression(scope),
						method.getHeader().makeArgumentGetExpression(0, members.position, scope));
				
				// TODO: finish
			}
		}
	}
	
	public class AnyClassMembers
	{
		public final CodePosition position;
		public final SymbolicClass forClass;
		public final ZenType anyType;
		
		public FieldMember valueField;
		public ConstructorMember constructor;
		public ImplementationMember anyImplementation;
		
		private AnyClassMembers(SymbolicClass forClass)
		{
			this.position = AnyClass.this.position;
			this.forClass = forClass;
			anyType = null;
			//anyType = new ZenTypeCompiledClass(forType.getScope(), forClass);
		}
		
		public Expression makeThisExpression(IScopeMethod scope)
		{
			return new ExpressionLocalGet(position, scope, forClass.getThis());
		}
		
		public Expression makeValueExpression(IScopeMethod scope)
		{
			return valueField.makeInstanceGetExpression(
					position,
					scope,
					makeThisExpression(scope));
		}
	}
}
