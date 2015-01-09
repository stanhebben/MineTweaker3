/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.parser.member.ParsedAnyCaller;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class AnyCallerMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final ParsedAnyCaller source;
	private final int modifiers;
	private final IMethodScope<E> methodScope;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public AnyCallerMember(ParsedAnyCaller source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		
		MethodHeader<E> methodHeader = source.getSignature().compile(unitScope);
		methodScope = new MethodScope<E>(unitScope, methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return methodScope.getDefinition();
	}

	@Override
	public void completeContents()
	{
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
		contents = source.getContent().compile(methodScope);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate()
	{
		ITypeCompiler<E> types = methodScope.getTypeCompiler();
		
		MethodHeader<E> methodHeader = methodScope.getMethodHeader();
		if (!methodHeader.accepts(
				methodScope,
				types.getString(methodScope),
				types.getAnyArray(methodScope),
				types.getMap(methodScope, types.getString(methodScope), types.getAny(methodScope))))
			methodScope.getErrorLogger().errorInvalidOperatorArguments(
					source.getPosition(),
					OperatorType.MEMBERCALLER,
					methodHeader,
					types.getString(methodScope),
					types.getAnyArray(methodScope),
					types.getMap(methodScope, types.getString(methodScope), types.getAny(methodScope)));
		
		methodHeader.validate(methodScope);
		contents.validate();
	}

	@Override
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return annotations;
	}
}
