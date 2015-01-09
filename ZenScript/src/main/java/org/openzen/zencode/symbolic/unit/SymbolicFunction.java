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
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.DefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicFunction<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements ISymbolicDefinition<E, T>
{
	private final ParsedFunction source;
	
	private final CodePosition position;
	private final SymbolLocal<E, T> localThis;
	private final int modifiers;
	private List<SymbolicAnnotation<E, T>> annotations;
	private final T type;
	private final String generatedClassName;
	private Statement<E, T> content;
	
	private final IDefinitionScope<E, T> classScope;
	private final IMethodScope<E, T> methodScope;

	private final Map<SymbolLocal<E, T>, Capture<E, T>> captured = new HashMap<SymbolLocal<E, T>, Capture<E, T>>();

	public SymbolicFunction(CodePosition position, int modifiers, MethodHeader<E, T> header, IModuleScope<E, T> scope)
	{
		source = null;
		classScope = new DefinitionScope<E, T>(scope, this);
		methodScope = new MethodScope<E, T>(classScope, header);
		
		this.position = position;
		this.type = scope.getTypeCompiler().getFunction(methodScope, header);
		this.modifiers = modifiers;
		this.annotations = Collections.emptyList();
		generatedClassName = header.getReturnType().getScope().makeClassName();

		localThis = new SymbolLocal<E, T>(type, true);
	}
	
	public SymbolicFunction(ParsedFunction source, IModuleScope<E, T> scope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
		position = source.getPosition();
		classScope = new DefinitionScope<E, T>(scope, this);
		MethodHeader<E, T> header = source.getSignature().compile(classScope);
		methodScope = new MethodScope<E, T>(classScope, header);
		type = scope.getTypeCompiler().getFunction(classScope, header);
		generatedClassName = header.getReturnType().getScope().makeClassName();
		
		localThis = new SymbolLocal<E, T>(type, true);
	}
	
	public IMethodScope<E, T> getScope()
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

	public MethodHeader<E, T> getHeader()
	{
		return type.getFunctionHeader();
	}

	public IPartialExpression<E, T> addCapture(CodePosition position, IMethodScope<E, T> scope, SymbolLocal<E, T> local)
	{
		if (!captured.containsKey(local)) {
			FieldMember<E, T> field = new FieldMember<E, T>(
					classScope,
					Modifier.PRIVATE.getCode() | Modifier.FINAL.getCode(),
					"__capture" + captured.size(),
					local.getType());
			captured.put(local, new Capture<E, T>(local, field));
		}

		//return new ExpressionGetInstanceField(position, scope, scope.getExpressionCompiler().localGet(position, scope, localThis), captured.get(local).field);
		return null; // TODO: finish captures
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

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		
	}

	@Override
	public void compileMembers()
	{
		
	}

	@Override
	public void compileMemberContents()
	{
		if (source == null)
			return;
		
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), classScope);
		content = source.getContents().compile(methodScope);
	}
	
	private static class Capture<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	{
		private final SymbolLocal<E, T> local;
		private final FieldMember<E, T> field;

		public Capture(SymbolLocal<E, T> local, FieldMember<E, T> field)
		{
			this.local = local;
			this.field = field;
		}
	}
}
