/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.member.IIteratorMember;
import org.openzen.zencode.symbolic.member.IIteratorVisitor;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IOperatorMember;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.generic.BasicTypeParameter;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ArrayTypeDefinition<E extends IPartialExpression<E>> extends TypeDefinition<E>
{
	private ITypeVariable<E> typeVariable;
	private ITypeDefinition<E> typeDefinition;
	
	public ArrayTypeDefinition()
	{
		super(Collections.singletonList(new BasicTypeParameter<E>()), false, true);
	}
	
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		typeVariable = getGenericParameters().get(0);
		typeDefinition = new ParameterType<>(scope, typeVariable);
		
		IGenericType<E> intType = typeRegistry.int_;
		IGenericType<E> valueType = new TypeInstance<>(typeDefinition, Collections.emptyList(), false);
		IGenericType<E> voidType = typeRegistry.void_;
		
		addMember(new ArrayLengthMember(intType));
		addMember(new IndexedGetterMember(intType, valueType));
		addMember(new IndexedSetterMember(intType, valueType, voidType));
		addMember(new ValueIteratorMember(Collections.singletonList(valueType)));
		addMember(new IndexValueIteratorMember(Arrays.asList(intType, valueType)));
	}
	
	@Override
	public IGenericType<E> getArrayBaseType(TypeInstance<E> typeInstance)
	{
		return typeInstance.getTypeCapture().get(typeVariable);
	}
	
	private class ArrayLengthMember implements IGetterMember<E>
	{
		private final IGenericType<E> intType;
		
		public ArrayLengthMember(IGenericType<E> intType)
		{
			this.intType = intType;
		}
		
		@Override
		public IGenericType<E> getType()
		{
			return intType;
		}

		@Override
		public String getName()
		{
			return "length";
		}

		@Override
		public E getStatic(CodePosition position, IMethodScope<E> scope)
		{
			scope.getErrorLogger().errorNotAStaticMember(position, this);
			return scope.getExpressionCompiler().invalid(position, scope, intType);
		}

		@Override
		public E getVirtual(CodePosition position, IMethodScope<E> scope, E instance)
		{
			return scope.getExpressionCompiler().getArrayLength(position, scope, instance);
		}

		@Override
		public int getModifiers()
		{
			return Modifier.EXPORT.getCode();
		}

		@Override
		public List<SymbolicAnnotation<E>> getAnnotations()
		{
			return Collections.emptyList();
		}

		@Override
		public void completeContents()
		{
			
		}

		@Override
		public void validate()
		{
			
		}

		@Override
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onGetter(this);
		}
	}
	
	private class IndexedGetterMember implements IOperatorMember<E>
	{
		private final MethodHeader<E> header;
		
		private IndexedGetterMember(IGenericType<E> intType, IGenericType<E> valueType)
		{
			header = MethodHeader.singleParameter(
					valueType,
					"index",
					intType);
		}
		
		@Override
		public MethodHeader<E> getHeader()
		{
			return header;
		}

		@Override
		public OperatorType getOperator()
		{
			return OperatorType.INDEXGET;
		}

		@Override
		public IVirtualCallable<E> instance(TypeInstance<E> instance)
		{
			return new IndexedGetterCallable(header.instance(instance));
		}

		@Override
		public int getModifiers()
		{
			return Modifier.EXPORT.getCode();
		}

		@Override
		public List<SymbolicAnnotation<E>> getAnnotations()
		{
			return Collections.emptyList();
		}

		@Override
		public void completeContents()
		{
			
		}

		@Override
		public void validate()
		{
			
		}

		@Override
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onOperator(this);
		}
	}
	
	private class IndexedSetterMember implements IOperatorMember<E>
	{
		private final MethodHeader<E> header;
		
		private IndexedSetterMember(IGenericType<E> intType, IGenericType<E> valueType, IGenericType<E> voidType)
		{
			List<MethodParameter<E>> parameters = new ArrayList<>();
			parameters.add(new MethodParameter<>(CodePosition.SYSTEM, "index", intType, null));
			parameters.add(new MethodParameter<>(CodePosition.SYSTEM, "value", valueType, null));
			header = new MethodHeader<>(CodePosition.SYSTEM, Collections.emptyList(), voidType, parameters, false);
		}
		
		@Override
		public MethodHeader<E> getHeader()
		{
			return header;
		}

		@Override
		public OperatorType getOperator()
		{
			return OperatorType.INDEXSET;
		}

		@Override
		public IVirtualCallable<E> instance(TypeInstance<E> instance)
		{
			return new IndexedSetterCallable(header.instance(instance));
		}

		@Override
		public int getModifiers()
		{
			return Modifier.EXPORT.getCode();
		}

		@Override
		public List<SymbolicAnnotation<E>> getAnnotations()
		{
			return Collections.emptyList();
		}

		@Override
		public void completeContents()
		{
			
		}

		@Override
		public void validate()
		{
			
		}

		@Override
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onOperator(this);
		}
	}
	
	private class IndexedGetterCallable implements IVirtualCallable<E>
	{
		private final InstancedMethodHeader<E> instancedHeader;
		
		public IndexedGetterCallable(InstancedMethodHeader<E> instancedHeader)
		{
			this.instancedHeader = instancedHeader;
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().getArrayElement(position, scope, instance, arguments.get(0));
		}

		@Override
		public String getFullName()
		{
			return "[]";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedHeader;
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
	
	private class IndexedSetterCallable implements IVirtualCallable<E>
	{
		private final InstancedMethodHeader<E> instancedHeader;
		
		public IndexedSetterCallable(InstancedMethodHeader<E> instancedHeader)
		{
			this.instancedHeader = instancedHeader;
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().setArrayElement(position, scope, instance, arguments.get(0), arguments.get(1));
		}

		@Override
		public String getFullName()
		{
			return "[]=";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedHeader;
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
	
	private class ValueIteratorMember implements IIteratorMember<E>
	{
		private List<IGenericType<E>> valueTypes;
		
		public ValueIteratorMember(List<IGenericType<E>> valueTypes)
		{
			this.valueTypes = valueTypes;
		}
		
		@Override
		public List<IGenericType<E>> getValueTypes()
		{
			return valueTypes;
		}

		@Override
		public <R> R visit(IIteratorVisitor<E, R> visitor)
		{
			return visitor.onArrayValueIterator();
		}

		@Override
		public int getModifiers()
		{
			return Modifier.EXPORT.getCode() | Modifier.ABSTRACT.getCode();
		}

		@Override
		public List<SymbolicAnnotation<E>> getAnnotations()
		{
			return Collections.emptyList();
		}

		@Override
		public void completeContents()
		{
			
		}

		@Override
		public void validate()
		{
			
		}

		@Override
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onIterator(this);
		}
	}
	
	private class IndexValueIteratorMember implements IIteratorMember<E>
	{
		private final List<IGenericType<E>> valueTypes;
		
		public IndexValueIteratorMember(List<IGenericType<E>> valueTypes)
		{
			this.valueTypes = valueTypes;
		}

		@Override
		public List<IGenericType<E>> getValueTypes()
		{
			return valueTypes;
		}

		@Override
		public <R> R visit(IIteratorVisitor<E, R> visitor)
		{
			return visitor.onArrayKeyValueIterator();
		}

		@Override
		public int getModifiers()
		{
			return Modifier.EXPORT.getCode() + Modifier.ABSTRACT.getCode();
		}

		@Override
		public List<SymbolicAnnotation<E>> getAnnotations()
		{
			return Collections.emptyList();
		}

		@Override
		public void completeContents()
		{
			
		}

		@Override
		public void validate()
		{
			
		}

		@Override
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onIterator(this);
		}
	}
}
