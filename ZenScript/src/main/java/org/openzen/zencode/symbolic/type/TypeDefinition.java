/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialImportable;
import org.openzen.zencode.symbolic.member.ICallerMember;
import org.openzen.zencode.symbolic.member.ICasterMember;
import org.openzen.zencode.symbolic.member.IConstructorMember;
import org.openzen.zencode.symbolic.member.IFieldMember;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.member.IImplementationMember;
import org.openzen.zencode.symbolic.member.IIteratorMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IMethodMember;
import org.openzen.zencode.symbolic.member.IOperatorMember;
import org.openzen.zencode.symbolic.member.ISetterMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.util.CodePosition;

/**
 * 
 * @author Stan
 * @param <E>
 */
public class TypeDefinition<E extends IPartialExpression<E>>
	implements ITypeDefinition<E>, IImportable<E>
{
	private final List<? extends ITypeVariable<E>> typeVariables;
	private final boolean isStruct;
	private final boolean isInterface;
	private final List<IGenericType<E>> superclasses;
	private final List<IConstructorMember<E>> constructors;
	private final Map<OperatorType, List<IOperatorMember<E>>> operators;
	private final Map<String, Member> virtualMembers;
	private final Map<String, Member> staticMembers;
	private final List<ICasterMember<E>> casters;
	private final List<ICallerMember<E>> instanceCallers;
	private final List<ICallerMember<E>> staticCallers;
	private final List<IImplementationMember<E>> implementations;
	private final Map<String, TypeDefinition<E>> innerTypes;
	private final List<IIteratorMember<E>> iterators;
	
	private final IMemberVisitor<E, Void> addMemberVisitor;
	
	public TypeDefinition()
	{
		this(Collections.<ITypeVariable<E>>emptyList(), false, false);
	}
	
	public TypeDefinition(List<? extends ITypeVariable<E>> typeVariables, boolean isStruct, boolean isInterface)
	{
		this.typeVariables = typeVariables;
		this.isStruct = isStruct;
		this.isInterface = isInterface;
		
		superclasses = new ArrayList<>();
		constructors = new ArrayList<>();
		operators = new EnumMap<>(OperatorType.class);
		virtualMembers = new HashMap<>();
		staticMembers = new HashMap<>();
		casters = new ArrayList<>();
		instanceCallers = new ArrayList<>();
		staticCallers = new ArrayList<>();
		implementations = new ArrayList<>();
		innerTypes = new HashMap<>();
		iterators = new ArrayList<>();
		
		addMemberVisitor = new AddMemberVisitor();
	}
	
	@Override
	public void addMember(IMember<E> member)
	{
		member.accept(addMemberVisitor);
	}
	
	public void addSuperclass(IGenericType<E> superclass)
	{
		superclasses.add(superclass);
	}

	@Override
	public List<? extends ITypeVariable<E>> getGenericParameters()
	{
		return typeVariables;
	}

	@Override
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope, TypeInstance<E> forType)
	{
		List<ICallable<E>> methods = new ArrayList<>();
		for (IConstructorMember<E> constructor : constructors) {
			if (constructor.isAccessibleFrom(scope))
				methods.add(constructor.instance(forType));
		}
		return methods;
	}
	
	@Override
	public List<ICallable<E>> getInstanceCallers(IModuleScope<E> scope, E instance, TypeInstance<E> forType)
	{
		List<ICallable<E>> methods = new ArrayList<>();
		for (ICallerMember<E> caller : instanceCallers) {
			if (caller.isAccessibleFrom(scope))
				methods.add(caller.getVirtualInstance(forType).bind(instance));
		}
		
		for (IGenericType<E> superclass : superclasses) {
			methods.addAll(superclass.getVirtualCallers(scope, instance));
		}
		
		return methods;
	}
	
	@Override
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope, TypeInstance<E> forType)
	{
		List<ICallable<E>> methods = new ArrayList<>();
		for (ICallerMember<E> caller : staticCallers) {
			if (caller.isAccessibleFrom(scope))
				methods.add(caller.getStaticInstance(forType));
		}
		
		for (IGenericType<E> superclass : superclasses) {
			methods.addAll(superclass.getStaticCallers(scope));
		}
		
		return methods;
	}

	@Override
	public IPartialExpression<E> getInstanceMember(
			CodePosition position,
			IMethodScope<E> scope,
			TypeInstance<E> forType,
			String name,
			E instance)
	{
		if (virtualMembers.containsKey(name))
			return new VirtualMemberExpression(position, scope, instance, virtualMembers.get(name), forType);
		else
			return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public IPartialExpression<E> getStaticMember(
			CodePosition position,
			IMethodScope<E> scope,
			TypeInstance<E> forType,
			String name)
	{
		if (staticMembers.containsKey(name))
			return new StaticMemberExpression(position, scope, staticMembers.get(name), forType);
		else
			return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E getOperator(
			CodePosition position,
			IMethodScope<E> scope,
			TypeInstance<E> forType,
			OperatorType operator,
			E instance,
			List<E> operands)
	{
		if (!operators.containsKey(operator)) {
			scope.getErrorLogger().errorNoSuchOperator(position, forType, operator);
			return scope.getExpressionCompiler().invalid(position, scope);
		} else {
			IVirtualCallable<E> acceptedMethod = null;
			boolean acceptedMethodAmbiguous = false;
			
			for (IOperatorMember<E> operatorMember : operators.get(operator)) {
				if (operatorMember.isAccessibleFrom(scope))
				{
					IVirtualCallable<E> method = operatorMember.instance(forType);
					if (method.getMethodHeader().acceptsWithExactTypes(operands))
						return method.call(position, scope, instance, operands);
					else if (method.getMethodHeader().accepts(scope, operands)) {
						if (acceptedMethod == null)
							acceptedMethod = method;
						else
							acceptedMethodAmbiguous = true;
					}
				}
			}
			
			if (acceptedMethod == null) {
				scope.getErrorLogger().errorNoSuchOperator(position, forType, operator);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else if (acceptedMethodAmbiguous) {
				scope.getErrorLogger().errorAmbiguousMethodCall(position);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else {
				return acceptedMethod.call(position, scope, operands.get(0), operands.subList(1, operands.size()));
			}
		}
	}

	@Override
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, TypeInstance<E> fromType, IGenericType<E> toType)
	{
		ICastingRule<E> result = null;
		
		for (IGenericType<E> superclass : superclasses) {
			// TODO: can be incorrect if scopes differ
			ICastingRule<E> castingRule = superclass.getCastingRule(scope, toType);
			result = selectCastingRule(result, castingRule);
		}
		
		for (IImplementationMember<E> implementation : implementations) {
			ICastingRule<E> castingRule = implementation.getType().getCastingRule(scope, toType);
			result = selectCastingRule(result, castingRule);
		}
		
		return result;
	}
	
	private ICastingRule<E> selectCastingRule(ICastingRule<E> current, ICastingRule<E> other)
	{
		if (current == null)
			return other;
		
		if (other == null)
			return current;
		
		if (current.isExplicit() && !other.isExplicit())
			return other;
		
		if (current.isExplicit() == other.isExplicit()) {
			// TODO: ambiguous casting rule
			return other;
		}
		
		return current;
	}

	@Override
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType)
	{
		if (forType.isNullable())
			return scope.getExpressionCompiler().constantNull(position, scope);
		else
			return null;
	}

	@Override
	public IGenericType<E> getArrayBaseType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public IGenericType<E> getMapKeyType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public IGenericType<E> getMapValueType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> predictOperatorArgumentType(TypeInstance<E> forType, OperatorType operator)
	{
		if (!operators.containsKey(operator) || operators.get(operator).isEmpty())
			return null;
		
		if (operators.get(operator).size() == 1) {
			InstancedMethodHeader<E> header = operators.get(operator).get(0).instance(forType).getMethodHeader();
			List<IGenericType<E>> types = new ArrayList<>();
			for (int i = 0; i < operator.getArgumentCount(); i++) {
				types.add(header.getArgumentType(i));
			}
			return types;
		} else {
			return null;
		}
	}

	@Override
	public E compare(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, E left, E right, CompareType comparator)
	{
		if (operators.containsKey(comparator.operator))
			return getOperator(position, scope, forType, comparator.operator, left, Collections.singletonList(right));
		
		if (operators.containsKey(comparator.inverseOperator))
			return scope.getExpressionCompiler().notBool(
					position, 
					scope,
					getOperator(position, scope, forType, comparator.inverseOperator, left, Collections.singletonList(right)));
		
		return scope.getExpressionCompiler().compareGeneric(
				position,
				scope,
				getOperator(position, scope, forType, OperatorType.COMPARE, left, Collections.singletonList(right)),
				comparator);
	}

	@Override
	public MethodHeader<E> getFunctionHeader(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> getForeachTypes(IModuleScope<E> scope, TypeInstance<E> forType, int numArguments)
	{
		for (IIteratorMember<E> iterator : iterators)
		{
			if (iterator.getValueTypes().size() == numArguments && iterator.isAccessibleFrom(scope))
			{
				List<IGenericType<E>> instanced = new ArrayList<>();
				for (IGenericType<E> type : iterator.getValueTypes())
					instanced.add(type.instance(forType.getTypeCapture()));
				
				return instanced;
			}
		}
		
		return null;
	}

	@Override
	public boolean isValidSwitchType()
	{
		return false;
	}

	@Override
	public boolean isStruct()
	{
		return isStruct;
	}
	
	@Override
	public boolean isInterface()
	{
		return isInterface;
	}
	
	@Override
	public List<IGenericType<E>> getStructMemberTypes(TypeInstance<E> instance)
	{
		if (isStruct) {
			List<IGenericType<E>> types = new ArrayList<>();
			for (Member member : virtualMembers.values()) {
				if (member.field != null)
					types.add(member.getter.getType().instance(instance.getTypeCapture()));
			}
			return types;
		} else {
			return null;
		}
	}

	@Override
	public IImportable<E> getSubDefinition(String name)
	{
		return innerTypes.get(name);
	}

	@Override
	public Collection<String> getSubDefinitionNames()
	{
		return innerTypes.keySet();
	}

	@Override
	public IZenSymbol<E> getMember(String name)
	{
		return new TypeMemberSymbol(name);
	}

	@Override
	public TypeInstance<E> toType(IModuleScope<E> scope, List<IGenericType<E>> genericTypes)
	{
		return new TypeInstance<>(this, genericTypes, false);
	}

	@Override
	public IPartialExpression<E> toPartialExpression(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialImportable<>(position, scope, this);
	}
	
	private class AddMemberVisitor implements IMemberVisitor<E, Void>
	{
		private Member getVirtualMember(String name)
		{
			if (!virtualMembers.containsKey(name))
				virtualMembers.put(name, new Member(name));
			
			return virtualMembers.get(name);
		}
		
		private Member getStaticMember(String name)
		{
			if (!staticMembers.containsKey(name))
				staticMembers.put(name, new Member(name));
			
			return staticMembers.get(name);
		}
		
		private Member getMember(int modifiers, String name)
		{
			if ((modifiers & Modifier.STATIC) > 0)
				return getStaticMember(name);
			else
				return getVirtualMember(name);
		}
		
		@Override
		public Void onField(IFieldMember<E> member)
		{
			getMember(member.getModifiers(), member.getName()).field = member;
			return null;
		}

		@Override
		public Void onConstructor(IConstructorMember<E> member)
		{
			constructors.add(member);
			return null;
		}

		@Override
		public Void onMethod(IMethodMember<E> member)
		{
			getMember(member.getModifiers(), member.getName()).methods.add(member);
			return null;
		}

		@Override
		public Void onGetter(IGetterMember<E> getter)
		{
			getMember(getter.getModifiers(), getter.getName()).getter = getter;
			return null;
		}

		@Override
		public Void onSetter(ISetterMember<E> setter)
		{
			getMember(setter.getModifiers(), setter.getName()).setter = setter;
			return null;
		}

		@Override
		public Void onOperator(IOperatorMember<E> operator)
		{
			if (!operators.containsKey(operator.getOperator()))
				operators.put(operator.getOperator(), new ArrayList<>());
			
			operators.get(operator.getOperator()).add(operator);
			return null;
		}

		@Override
		public Void onCaller(ICallerMember<E> caller)
		{
			if ((caller.getModifiers() & Modifier.STATIC) > 0)
				staticCallers.add(caller);
			else
				instanceCallers.add(caller);
			
			return null;
		}

		@Override
		public Void onImplementation(IImplementationMember<E> implementation)
		{
			implementations.add(implementation);
			return null;
		}

		@Override
		public Void onCaster(ICasterMember<E> caster)
		{
			casters.add(caster);
			return null;
		}
		
		@Override
		public Void onIterator(IIteratorMember<E> iterator)
		{
			iterators.add(iterator);
			return null;
		}
	}
	
	private class Member
	{
		private final String name;
		private IFieldMember<E> field;
		private IGetterMember<E> getter;
		private ISetterMember<E> setter;
		private List<IMethodMember<E>> methods;
		
		public Member(String name)
		{
			this.name = name;
			methods = new ArrayList<>();
		}
	}
	
	private class VirtualMemberExpression implements IPartialExpression<E>
	{
		private final CodePosition position;
		private final IMethodScope<E> scope;
		private final E instance;
		private final Member member;
		private final TypeInstance<E> typeInstance;
		
		public VirtualMemberExpression(
				CodePosition position,
				IMethodScope<E> scope,
				E instance,
				Member member,
				TypeInstance<E> typeInstance)
		{
			this.position = position;
			this.scope = scope;
			this.member = member;
			this.instance = instance;
			this.typeInstance = typeInstance;
		}

		@Override
		public CodePosition getPosition()
		{
			return position;
		}

		@Override
		public IMethodScope<E> getScope()
		{
			return scope;
		}

		@Override
		public E eval()
		{
			if (member.getter == null) {
				scope.getErrorLogger().errorNoGetterForMember(position, member.name);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else {
				return member.getter.getVirtual(position, scope, instance);
			}
		}

		@Override
		public E assign(CodePosition position, E value)
		{
			if (member.setter == null) {
				scope.getErrorLogger().errorNoSetterForMember(position, member.name);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else {
				return member.setter.setVirtual(position, scope, instance, value);
			}
		}

		@Override
		public IPartialExpression<E> getMember(CodePosition position, String name)
		{
			return eval().getMember(position, name);
		}

		@Override
		public List<ICallable<E>> getMethods()
		{
			List<ICallable<E>> methods = new ArrayList<>();
			for (IMethodMember<E> method : member.methods) {
				if (method.isAccessibleFrom(scope))
					methods.add(method.getVirtualInstance(typeInstance).bind(instance));
			}
			
			if (member.getter != null) {
				methods.addAll(member.getter.getType().getVirtualCallers(scope, instance));
			}
			
			return methods;
		}

		@Override
		public E cast(CodePosition position, IGenericType<E> type)
		{
			E value = eval();
			ICastingRule<E> castingRule = value.getType().getCastingRule(scope, type);
			if (castingRule == null) {
				scope.getErrorLogger().errorCannotCastExplicit(position, value.getType(), type);
				return scope.getExpressionCompiler().invalid(position, scope, type);
			} else {
				return castingRule.cast(position, scope, value);
			}
		}

		@Override
		public IGenericType<E> getType()
		{
			return member.getter == null ? scope.getTypeCompiler().any : member.getter.getType();
		}

		@Override
		public IPartialExpression<E> via(SymbolicFunction<E> function)
		{
			return this;
		}

		@Override
		public IAny getCompileTimeValue()
		{
			IAny instanceValue = instance.getCompileTimeValue();
			if (instanceValue == null)
				return null;
			
			return instanceValue.memberGet(member.name);
		}

		@Override
		public void validate()
		{
			instance.validate();
		}
	}
	
	private class StaticMemberExpression implements IPartialExpression<E>
	{
		private final CodePosition position;
		private final IMethodScope<E> scope;
		private final Member member;
		private final TypeInstance<E> typeInstance;
		
		public StaticMemberExpression(CodePosition position, IMethodScope<E> scope, Member member, TypeInstance<E> typeInstance)
		{
			this.position = position;
			this.scope = scope;
			this.member = member;
			this.typeInstance = typeInstance;
		}

		@Override
		public CodePosition getPosition()
		{
			return position;
		}

		@Override
		public IMethodScope<E> getScope()
		{
			return scope;
		}

		@Override
		public E eval()
		{
			if (member.getter == null) {
				scope.getErrorLogger().errorNoGetterForMember(position, member.name);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else {
				return member.getter.getStatic(position, scope);
			}
		}

		@Override
		public E assign(CodePosition position, E value)
		{
			if (member.setter == null) {
				scope.getErrorLogger().errorNoSetterForMember(position, member.name);
				return scope.getExpressionCompiler().invalid(position, scope);
			} else {
				return member.setter.setStatic(position, scope, value);
			}
		}

		@Override
		public IPartialExpression<E> getMember(CodePosition position, String name)
		{
			return eval().getMember(position, name);
		}

		@Override
		public List<ICallable<E>> getMethods()
		{
			List<ICallable<E>> methods = new ArrayList<>();
			for (IMethodMember<E> method : member.methods) {
				if (method.isAccessibleFrom(scope))
					methods.add(method.getStaticInstance(typeInstance));
			}
			
			if (member.getter != null) {
				E getterExpression = member.getter.getStatic(position, scope);
				methods.addAll(member.getter.getType().getVirtualCallers(scope, getterExpression));
			}
			
			return methods;
		}

		@Override
		public E cast(CodePosition position, IGenericType<E> type)
		{
			E value = eval();
			ICastingRule<E> castingRule = value.getType().getCastingRule(scope, type);
			if (castingRule == null) {
				scope.getErrorLogger().errorCannotCastExplicit(position, value.getType(), type);
				return scope.getExpressionCompiler().invalid(position, scope, type);
			} else {
				return castingRule.cast(position, scope, value);
			}
		}

		@Override
		public IGenericType<E> getType()
		{
			return member.getter == null ? scope.getTypeCompiler().any : member.getter.getType();
		}

		@Override
		public IPartialExpression<E> via(SymbolicFunction<E> function)
		{
			return this;
		}

		@Override
		public IAny getCompileTimeValue()
		{
			// TODO: evaluate static values
			return null;
		}

		@Override
		public void validate()
		{
			// nothing to do
		}
	}
	
	private class TypeMemberSymbol implements IZenSymbol<E>
	{
		private final String name;
		
		public TypeMemberSymbol(String name)
		{
			this.name = name;
		}

		@Override
		public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> scope)
		{
			if (staticMembers.containsKey(name))
				return new StaticMemberExpression(
						position,
						scope,
						staticMembers.get(name),
						new TypeInstance<>(TypeDefinition.this, Collections.<IGenericType<E>>emptyList(), false));
			else
				return new PartialImportable<>(position, scope, innerTypes.get(name));
		}

		@Override
		public IImportable<E> asImportable()
		{
			return innerTypes.get(name);
		}
	}
}
