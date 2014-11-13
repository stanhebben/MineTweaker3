/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionFunction;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.statements.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.parser.elements.ParsedFunctionParameter;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 * TODO: review code quality and possible bugs
 * 
 * @author Stan
 */
public class ParsedExpressionFunction extends ParsedExpression {
	private final ParsedFunctionSignature header;
	private final List<ParsedStatement> statements;
	
	public ParsedExpressionFunction(CodePosition position, ParsedFunctionSignature header, List<ParsedStatement> statements) {
		super(position);
		
		this.header = header;
		this.statements = statements;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod scope, ZenType predictedType) {
		MethodHeader compiledHeader = header.compile(scope);
		
		if (predictedType != null) {
			IMethod function = predictedType.getFunction();
			if (function != null) {
				MethodHeader predictedHeader = function.getMethodHeader();

				List<ZenType> argumentTypes = new ArrayList<ZenType>();
				for (ParsedFunctionParameter argument : header.getArguments()) {
					argumentTypes.add(argument.getType().compile(scope));
				}

				ZenType newReturnType = compiledHeader.getReturnType();
				if (compiledHeader.getReturnType() == scope.getTypes().ANY)
					newReturnType = predictedHeader.getReturnType();

				for (int i = 0; i < argumentTypes.size(); i++) {
					if (i < predictedHeader.getArguments().size() && argumentTypes.get(i) == scope.getTypes().ANY) {
						argumentTypes.set(i, predictedHeader.getArguments().get(i).getType());
					}
				}

				List<MethodParameter> newArguments = new ArrayList<MethodParameter>();
				for (int i = 0; i < header.getArguments().size(); i++) {
					ParsedFunctionParameter argument = header.getArguments().get(i);

					String name = argument.getName();
					ZenType type = argumentTypes.get(i);
					Expression defaultValue = null;
					if (argument.getDefaultValue() != null) {
						defaultValue = argument.getDefaultValue().compile(scope, type);
					}

					newArguments.add(new MethodParameter(name, type, defaultValue));
				}
				
				compiledHeader = new MethodHeader(newReturnType, newArguments, predictedHeader.isVarargs());
			}
		}
		
		SymbolicFunction functionUnit = new SymbolicFunction(getPosition(), compiledHeader, scope);
		EnvironmentFunctionLiteral functionScope = new EnvironmentFunctionLiteral(scope, functionUnit);
		
		for (int i = 0; i < compiledHeader.getArguments().size(); i++) {
			MethodParameter argument = compiledHeader.getArguments().get(i);
			SymbolLocal symbol = argument.getLocal();
			
			functionScope.putValue(
					argument.getName(),
					symbol,
					header.getArguments().get(i).getPosition());
		}
		
		List<Statement> cStatements = new ArrayList<Statement>();
		for (ParsedStatement statement : statements) {
			cStatements.add(statement.compile(functionScope));
		}
		
		Expression result = new ExpressionFunction(getPosition(), scope, compiledHeader, cStatements);
		return result;
	}
	
	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
	
	private static class EnvironmentFunctionLiteral implements IScopeMethod {
		private final IScopeMethod outer;
		private final SymbolicFunction functionUnit;
		private final Map<String, IZenSymbol> locals;
		
		public EnvironmentFunctionLiteral(IScopeMethod outer, SymbolicFunction functionUnit) {
			this.outer = outer;
			this.functionUnit = functionUnit;
			locals = new HashMap<String, IZenSymbol>();
		}
		
		@Override
		public AccessScope getAccessScope()
		{
			return outer.getAccessScope();
		}

		@Override
		public TypeRegistry getTypes() {
			return outer.getTypes();
		}

		@Override
		public IZenCompileEnvironment getEnvironment() {
			return outer.getEnvironment();
		}

		@Override
		public String makeClassName() {
			return outer.makeClassName();
		}

		@Override
		public boolean containsClass(String name) {
			return outer.containsClass(name);
		}

		@Override
		public Set<String> getClassNames() {
			return outer.getClassNames();
		}

		@Override
		public byte[] getClass(String name) {
			return outer.getClass(name);
		}
		
		@Override
		public Map<String, byte[]> getClasses()
		{
			return outer.getClasses();
		}

		@Override
		public void putClass(String name, byte[] data) {
			outer.putClass(name, data);
		}

		@Override
		public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment) {
			if (locals.containsKey(name)) {
				return locals.get(name).instance(position, environment);
			} else {
				return outer.getValue(name, position, environment).via(functionUnit);
			}
		}

		@Override
		public void putValue(String name, IZenSymbol value, CodePosition position) {
			locals.put(name, value);
		}
		
		@Override
		public boolean hasErrors() {
			return outer.hasErrors();
		}

		@Override
		public void error(CodePosition position, String message) {
			outer.error(position, message);
		}

		@Override
		public void warning(CodePosition position, String message) {
			outer.warning(position, message);
		}

		@Override
		public Statement getControlStatement(String label) {
			return null;
		}

		@Override
		public ZenType getReturnType() {
			return functionUnit.getHeader().getReturnType();
		}
	}
}
