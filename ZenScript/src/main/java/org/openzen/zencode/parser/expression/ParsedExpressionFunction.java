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
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.parser.elements.ParsedFunctionParameter;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 * TODO: review code quality and possible bugs
 *
 * @author Stan
 */
public class ParsedExpressionFunction extends ParsedExpression
{
	private final ParsedFunctionSignature header;
	private final List<ParsedStatement> statements;

	public ParsedExpressionFunction(CodePosition position, ParsedFunctionSignature header, List<ParsedStatement> statements)
	{
		super(position);

		this.header = header;
		this.statements = statements;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IScopeMethod<E, T> scope, T asType)
	{
		MethodHeader<E, T> compiledHeader = header.compile(scope);
		
		if (asType != null) {
			MethodHeader<E, T> function = asType.getFunctionHeader();
			if (function != null) {
				List<T> argumentTypes = new ArrayList<T>();
				for (ParsedFunctionParameter argument : header.getArguments()) {
					argumentTypes.add(argument.getType().compile(scope));
				}

				T newReturnType = compiledHeader.getReturnType();
				if (compiledHeader.getReturnType() == scope.getTypes().getAny())
					newReturnType = function.getReturnType();

				for (int i = 0; i < argumentTypes.size(); i++) {
					if (i < function.getParameters().size() && argumentTypes.get(i) == scope.getTypes().getAny())
						argumentTypes.set(i, function.getParameters().get(i).getType());
				}

				List<MethodParameter<E, T>> newArguments = new ArrayList<MethodParameter<E, T>>();
				for (int i = 0; i < header.getArguments().size(); i++) {
					ParsedFunctionParameter argument = header.getArguments().get(i);

					String name = argument.getName();
					T type = argumentTypes.get(i);
					E defaultValue = null;
					if (argument.getDefaultValue() != null)
						defaultValue = argument.getDefaultValue().compile(scope, type);

					newArguments.add(new MethodParameter<E, T>(name, type, defaultValue));
				}

				compiledHeader = new MethodHeader<E, T>(newReturnType, newArguments, function.isVarargs());
			}
		}

		SymbolicFunction<E, T> functionUnit = new SymbolicFunction<E, T>(getPosition(), compiledHeader, scope);
		EnvironmentFunctionLiteral<E, T> functionScope = new EnvironmentFunctionLiteral<E, T>(scope, functionUnit);

		for (int i = 0; i < compiledHeader.getParameters().size(); i++) {
			MethodParameter<E, T> argument = compiledHeader.getParameters().get(i);
			SymbolLocal<E, T> symbol = argument.getLocal();

			functionScope.putValue(
					argument.getName(),
					symbol,
					header.getArguments().get(i).getPosition());
		}

		List<Statement<E, T>> cStatements = new ArrayList<Statement<E, T>>();
		for (ParsedStatement statement : statements) {
			Statement<E, T> cStatement = statement.compile(functionScope);
			cStatements.add(cStatement);
		}

		return scope.getExpressionCompiler().functionExpression(getPosition(), scope, compiledHeader, cStatements);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return null;
	}

	private static class EnvironmentFunctionLiteral<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
			implements IScopeMethod<E, T>
	{
		private final IScopeMethod<E, T> outer;
		private final SymbolicFunction<E, T> functionUnit;
		private final Map<String, IZenSymbol<E, T>> locals;

		public EnvironmentFunctionLiteral(IScopeMethod<E, T> outer, SymbolicFunction<E, T> functionUnit)
		{
			this.outer = outer;
			this.functionUnit = functionUnit;
			locals = new HashMap<String, IZenSymbol<E, T>>();
		}

		@Override
		public AccessScope getAccessScope()
		{
			return outer.getAccessScope();
		}

		@Override
		public ITypeCompiler<E, T> getTypes()
		{
			return outer.getTypes();
		}

		@Override
		public IZenCompileEnvironment<E, T> getEnvironment()
		{
			return outer.getEnvironment();
		}

		@Override
		public IExpressionCompiler<E, T> getExpressionCompiler()
		{
			return outer.getExpressionCompiler();
		}
		
		@Override
		public IScopeMethod<E, T> getConstantEnvironment()
		{
			return outer.getConstantEnvironment();
		}

		@Override
		public String makeClassName()
		{
			return outer.makeClassName();
		}

		@Override
		public boolean containsClass(String name)
		{
			return outer.containsClass(name);
		}

		@Override
		public Set<String> getClassNames()
		{
			return outer.getClassNames();
		}

		@Override
		public byte[] getClass(String name)
		{
			return outer.getClass(name);
		}

		@Override
		public Map<String, byte[]> getClasses()
		{
			return outer.getClasses();
		}

		@Override
		public void putClass(String name, byte[] data)
		{
			outer.putClass(name, data);
		}

		@Override
		public IPartialExpression<E, T> getValue(String name, CodePosition position, IScopeMethod<E, T> environment)
		{
			if (locals.containsKey(name))
				return locals.get(name).instance(position, environment);
			else
				return outer.getValue(name, position, environment).via(functionUnit);
		}

		@Override
		public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
		{
			locals.put(name, value);
		}

		@Override
		public boolean hasErrors()
		{
			return outer.hasErrors();
		}

		@Override
		public void error(CodePosition position, String message)
		{
			outer.error(position, message);
		}

		@Override
		public void warning(CodePosition position, String message)
		{
			outer.warning(position, message);
		}

		@Override
		public Statement<E, T> getControlStatement(String label)
		{
			return null;
		}

		@Override
		public T getReturnType()
		{
			return functionUnit.getHeader().getReturnType();
		}
	}
}
