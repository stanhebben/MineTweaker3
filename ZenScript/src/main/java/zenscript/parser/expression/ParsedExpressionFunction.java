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
import org.objectweb.asm.ClassVisitor;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.TypeExpansion;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionFunction;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethodArgument;
import zenscript.parser.elements.ParsedFunctionArgument;
import zenscript.parser.elements.ParsedFunctionHeader;
import zenscript.parser.statement.ParsedStatement;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionFunction extends ParsedExpression {
	private final ParsedFunctionHeader header;
	private final List<ParsedStatement> statements;
	
	public ParsedExpressionFunction(ZenPosition position, ParsedFunctionHeader header, List<ParsedStatement> statements) {
		super(position);
		
		this.header = header;
		this.statements = statements;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		ZenType returnType = header.getReturnType().compile(environment);
		List<JavaMethodArgument> arguments;
		
		IJavaMethod function = predictedType.getFunction();
		if (function == null) {
			arguments = header.getCompiledArguments(environment);
		} else {
			List<ZenType> argumentTypes = new ArrayList<ZenType>();
			for (ParsedFunctionArgument argument : header.getArguments()) {
				argumentTypes.add(argument.getType().compile(environment));
			}
			
			if (returnType == environment.getTypes().ANY)
				returnType = function.getReturnType();
			
			for (int i = 0; i < argumentTypes.size(); i++) {
				if (i < function.getArguments().length && argumentTypes.get(i) == environment.getTypes().ANY) {
					argumentTypes.set(i, function.getArguments()[i].getType());
				}
			}
			
			arguments = new ArrayList<JavaMethodArgument>();
			for (int i = 0; i < header.getArguments().size(); i++) {
				ParsedFunctionArgument argument = header.getArguments().get(i);
				
				String name = argument.getName();
				ZenType type = argumentTypes.get(i);
				Expression defaultValue = null;
				if (argument.getDefaultValue() != null) {
					defaultValue = argument.getDefaultValue().compile(environment, type).eval();
				}
				
				arguments.add(new JavaMethodArgument(name, type, defaultValue));
			}
		}
		
		SymbolicFunction functionUnit = new SymbolicFunction(returnType, arguments);
		EnvironmentFunctionLiteral scope = new EnvironmentFunctionLiteral(environment, functionUnit);
		
		List<SymbolLocal> argumentLocals = new ArrayList<SymbolLocal>();
		for (int i = 0; i < arguments.size(); i++) {
			JavaMethodArgument argument = arguments.get(i);
			SymbolLocal symbol = new SymbolLocal(argument.getType(), false);
			argumentLocals.add(symbol);
			
			scope.putValue(
					argument.getName(),
					symbol,
					header.getArguments().get(i).getPosition());
		}
		
		List<Statement> cStatements = new ArrayList<Statement>();
		for (ParsedStatement statement : statements) {
			cStatements.add(statement.compile(scope));
		}
		
		// TODO: compile statements
		Expression result = new ExpressionFunction(getPosition(), environment, arguments, returnType, statements);
		
		
		
		return result;
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
		public ClassVisitor getClassOutput() {
			return outer.getClassOutput();
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
		public TypeExpansion getExpansion(String name) {
			return outer.getExpansion(name);
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
	}
}
