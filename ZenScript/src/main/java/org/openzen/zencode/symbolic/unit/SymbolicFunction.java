/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.unit;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.parser.unit.ParsedFunction;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicFunction<E extends IPartialExpression<E>> extends AbstractSymbolicDefinition<E>
{
	private final ParsedFunction source;
	
	private final CodePosition position;
	private final SymbolLocal<E> localThis;
	private final TypeInstance<E> type;
	private final String generatedClassName;
	private Statement<E> content;
	
	private final IMethodScope<E> methodScope;

	private final Map<SymbolLocal<E>, Capture<E>> captured = new HashMap<SymbolLocal<E>, Capture<E>>();

	public SymbolicFunction(CodePosition position, int modifiers, MethodHeader<E> header, IModuleScope<E> scope)
	{
		super(modifiers, Collections.<SymbolicAnnotation<E>>emptyList(), scope);
		
		source = null;
		methodScope = new MethodScope<E>(getScope(), header);
		
		this.position = position;
		this.type = scope.getTypeCompiler().getFunction(methodScope, header);
		generatedClassName = header.getReturnType().getScope().makeClassName();
		
		localThis = new SymbolLocal<E>(type, true);
	}
	
	public SymbolicFunction(ParsedFunction source, IModuleScope<E> scope)
	{
		super(source, scope);
		
		this.source = source;
		position = source.getPosition();
		MethodHeader<E> header = source.getSignature().compile(getScope());
		methodScope = new MethodScope<E>(getScope(), header);
		type = scope.getTypeCompiler().getFunction(getScope(), header);
		generatedClassName = header.getReturnType().getScope().makeClassName();
		
		localThis = new SymbolLocal<E>(type, true);
	}
	
	public final IMethodScope<E> getMethodScope()
	{
		return methodScope;
	}
	
	public String getClassName()
	{
		return generatedClassName;
	}

	/*@Override
	public T getType()
	{
		return type;
	}
	
	@Override
	public void compile()
	{
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classWriter.visitSource(position.getFile().getFileName(), null);
		
		classWriter.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, generatedClassName, null, internal(Object.class), null);
		MethodOutput methodOutput = new MethodOutput(classWriter, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "call", type.getMethodHeader().getSignature(), null, null);
		methodOutput.start();
		
		for (Statement<E, T> statement : statements) {
			statement.compile(methodOutput);
		}
		
		methodOutput.ret();
		methodOutput.end();
		
		classWriter.visitEnd();
		classScope.putClass(generatedClassName, classWriter.toByteArray());
	}*/

	public MethodHeader<E> getHeader()
	{
		return type.getFunctionHeader();
	}

	public IPartialExpression<E> addCapture(CodePosition position, IMethodScope<E> scope, SymbolLocal<E> local)
	{
		if (!captured.containsKey(local)) {
			FieldMember<E> field = new FieldMember<E>(
					getScope(),
					Modifier.PRIVATE.getCode() | Modifier.FINAL.getCode(),
					"__capture" + captured.size(),
					local.getType());
			captured.put(local, new Capture<E>(local, field));
		}

		//return new ExpressionGetInstanceField(position, scope, scope.getExpressionCompiler().localGet(position, scope, localThis), captured.get(local).field);
		return null; // TODO: finish captures
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
	{
		
	}

	@Override
	public void compileMembers()
	{
		super.compileMembers();
	}

	@Override
	public void compileMemberContents()
	{
		super.compileMemberContents();
		
		if (source == null)
			return;
		
		content = source.getContents().compile(methodScope);
	}

	@Override
	public List<? extends ITypeVariable<E>> getTypeVariables()
	{
		return methodScope.getMethodHeader().getGenericParameters();
	}
	
	private static class Capture<E extends IPartialExpression<E>>
	{
		private final SymbolLocal<E> local;
		private final FieldMember<E> field;

		public Capture(SymbolLocal<E> local, FieldMember<E> field)
		{
			this.local = local;
			this.field = field;
		}
	}
}
