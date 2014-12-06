/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeClass;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.scope.ScopeClass;
import org.openzen.zencode.symbolic.scope.ScopeMethod;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Modifiers;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicFunction<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements ISymbolicUnit<E, T>
{
	private final CodePosition position;
	private final SymbolLocal<E, T> localThis;
	private final T type;
	private final String generatedClassName;
	private final List<Statement<E, T>> statements = new ArrayList<Statement<E, T>>();
	
	private final IScopeClass<E, T> classScope;
	private final IScopeMethod<E, T> methodScope;

	private final Map<SymbolLocal<E, T>, Capture<E, T>> captured = new HashMap<SymbolLocal<E, T>, Capture<E, T>>();

	public SymbolicFunction(CodePosition position, MethodHeader<E, T> header, IScopeModule<E, T> scope)
	{
		classScope = new ScopeClass<E, T>(scope);
		methodScope = new ScopeMethod<E, T>(classScope, header.getReturnType());
		
		this.position = position;
		this.type = scope.getTypes().getFunction(header);
		generatedClassName = header.getReturnType().getScope().makeClassName();

		localThis = new SymbolLocal<E, T>(type, true);
	}
	
	public IScopeMethod<E, T> getScope()
	{
		return methodScope;
	}
	
	public void addStatement(Statement<E, T> statement)
	{
		statements.add(statement);
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
		MethodOutput methodOutput = new MethodOutput(classWriter, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "call", type.getHeader().getSignature(), null, null);
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

	public IPartialExpression<E, T> addCapture(CodePosition position, IScopeMethod<E, T> scope, SymbolLocal<E, T> local)
	{
		if (!captured.containsKey(local)) {
			FieldMember<E, T> field = new FieldMember<E, T>(
					this,
					Modifiers.ACCESS_PRIVATE | Modifiers.FINAL,
					"__capture" + captured.size(),
					local.getType());
			captured.put(local, new Capture<E, T>(local, field));
		}

		//return new ExpressionGetInstanceField(position, scope, scope.getExpressionCompiler().localGet(position, scope, localThis), captured.get(local).field);
		return null; // TODO
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
