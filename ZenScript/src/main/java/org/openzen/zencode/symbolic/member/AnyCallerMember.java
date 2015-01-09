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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class AnyCallerMember<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IMember<E, T>
{
	private final ParsedAnyCaller source;
	private final int modifiers;
	private final IMethodScope<E, T> methodScope;
	
	private Statement<E, T> contents;
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public AnyCallerMember(ParsedAnyCaller source, IDefinitionScope<E, T> unitScope)
	{
		this.source = source;
		
		MethodHeader<E, T> methodHeader = source.getSignature().compile(unitScope);
		methodScope = new MethodScope<E, T>(unitScope, methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E, T> getUnit()
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
		ITypeCompiler<E, T> types = methodScope.getTypeCompiler();
		
		MethodHeader<E, T> methodHeader = methodScope.getMethodHeader();
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
	public List<SymbolicAnnotation<E, T>> getAnnotations()
	{
		return annotations;
	}
}
