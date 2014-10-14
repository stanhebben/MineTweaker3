/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionFunction;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import zenscript.parser.elements.ParsedFunctionArgument;
import zenscript.parser.elements.ParsedFunctionSignature;
import zenscript.parser.statement.ParsedStatement;
import zenscript.runtime.IAny;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.method.IMethod;
import zenscript.symbolic.method.MethodArgument;
import zenscript.symbolic.method.MethodHeader;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 * TODO: review code quality and possible bugs
 * 
 * @author Stan
 */
public class ParsedExpressionFunction extends ParsedExpression {
	private final ParsedFunctionSignature header;
	private final List<ParsedStatement> statements;
	
	public ParsedExpressionFunction(ZenPosition position, ParsedFunctionSignature header, List<ParsedStatement> statements) {
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
				for (ParsedFunctionArgument argument : header.getArguments()) {
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

				List<MethodArgument> newArguments = new ArrayList<MethodArgument>();
				for (int i = 0; i < header.getArguments().size(); i++) {
					ParsedFunctionArgument argument = header.getArguments().get(i);

					String name = argument.getName();
					ZenType type = argumentTypes.get(i);
					Expression defaultValue = null;
					if (argument.getDefaultValue() != null) {
						defaultValue = argument.getDefaultValue().compile(scope, type);
					}

					newArguments.add(new MethodArgument(name, type, defaultValue));
				}
				
				compiledHeader = new MethodHeader(newReturnType, newArguments, predictedHeader.isVarargs());
			}
		}
		
		SymbolicFunction functionUnit = new SymbolicFunction(compiledHeader);
		EnvironmentFunctionLiteral functionScope = new EnvironmentFunctionLiteral(scope, functionUnit);
		
		for (int i = 0; i < compiledHeader.getArguments().size(); i++) {
			MethodArgument argument = compiledHeader.getArguments().get(i);
			SymbolLocal symbol = new SymbolLocal(argument.getType(), false);
			
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
		public void putClass(String name, byte[] data) {
			outer.putClass(name, data);
		}

		@Override
		public IPartialExpression getValue(String name, ZenPosition position, IScopeMethod environment) {
			if (locals.containsKey(name)) {
				return locals.get(name).instance(position, environment);
			} else {
				return outer.getValue(name, position, environment).via(functionUnit);
			}
		}

		@Override
		public void putValue(String name, IZenSymbol value, ZenPosition position) {
			locals.put(name, value);
		}

		@Override
		public void error(ZenPosition position, String message) {
			outer.error(position, message);
		}

		@Override
		public void warning(ZenPosition position, String message) {
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
