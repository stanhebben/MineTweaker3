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
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionGetInstanceField;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeClass;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.scope.ScopeClass;
import org.openzen.zencode.symbolic.scope.ScopeMethod;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Modifiers;
import stanhebben.zenscript.statements.Statement;
import org.openzen.zencode.util.MethodOutput;
import static org.openzen.zencode.util.ZenTypeUtil.internal;

/**
 *
 * @author Stan
 */
public class SymbolicFunction implements ISymbolicUnit
{
	private final CodePosition position;
	private final SymbolLocal localThis;
	private final ZenTypeFunction type;
	private final String generatedClassName;
	private final List<Statement> statements = new ArrayList<Statement>();
	
	private final IScopeClass classScope;
	private final IScopeMethod methodScope;

	private final Map<SymbolLocal, Capture> captured = new HashMap<SymbolLocal, Capture>();

	public SymbolicFunction(CodePosition position, MethodHeader header, IScopeModule scope)
	{
		classScope = new ScopeClass(scope);
		methodScope = new ScopeMethod(classScope, header.getReturnType());
		
		this.position = position;
		this.type = new ZenTypeFunction(header);
		generatedClassName = header.getReturnType().getScope().makeClassName();

		localThis = new SymbolLocal(type, true);
	}
	
	public IScopeMethod getScope()
	{
		return methodScope;
	}
	
	public void addStatement(Statement statement)
	{
		statements.add(statement);
	}
	
	public String getClassName()
	{
		return generatedClassName;
	}

	@Override
	public ZenTypeFunction getType()
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
		
		for (Statement statement : statements) {
			statement.compile(methodOutput);
		}
		
		methodOutput.ret();
		methodOutput.end();
		
		classWriter.visitEnd();
		classScope.putClass(generatedClassName, classWriter.toByteArray());
	}

	public MethodHeader getHeader()
	{
		return type.getHeader();
	}

	public IPartialExpression addCapture(CodePosition position, IScopeMethod scope, SymbolLocal local)
	{
		if (!captured.containsKey(local)) {
			FieldMember field = new FieldMember(
					this,
					Modifiers.ACCESS_PRIVATE | Modifiers.FINAL,
					"__capture" + captured.size(),
					local.getType());
			captured.put(local, new Capture(local, field));
		}

		return new ExpressionGetInstanceField(position, scope, new ExpressionLocalGet(position, scope, localThis), captured.get(local).field);
	}
	
	private static class Capture
	{
		private final SymbolLocal local;
		private final FieldMember field;

		public Capture(SymbolLocal local, FieldMember field)
		{
			this.local = local;
			this.field = field;
		}
	}
}
