/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.unit;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionGetInstanceField;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Modifiers;

/**
 *
 * @author Stan
 */
public class SymbolicFunction implements ISymbolicUnit
{
	private final SymbolLocal localThis;
	private final ZenTypeFunction type;
	private final String generatedClassName;

	private final Map<SymbolLocal, Capture> captured = new HashMap<SymbolLocal, Capture>();

	public SymbolicFunction(MethodHeader header)
	{
		this.type = new ZenTypeFunction(header);
		generatedClassName = header.getReturnType().getScope().makeClassName();

		localThis = new SymbolLocal(type, true);
	}

	@Override
	public ZenTypeFunction getType()
	{
		return type;
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
