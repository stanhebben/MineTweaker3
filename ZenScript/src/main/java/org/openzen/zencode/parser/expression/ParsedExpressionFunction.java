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
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.parser.elements.ParsedFunctionParameter;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
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
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T asType)
	{
		MethodHeader<E, T> compiledHeader = header.compile(scope);
		
		if (asType != null) {
			MethodHeader<E, T> function = asType.getFunctionHeader();
			if (function != null) {
				List<T> argumentTypes = new ArrayList<T>();
				for (ParsedFunctionParameter argument : header.getParameters()) {
					argumentTypes.add(argument.getType().compile(scope));
				}

				T newReturnType = compiledHeader.getReturnType();
				if (compiledHeader.getReturnType() == scope.getTypeCompiler().getAny(scope))
					newReturnType = function.getReturnType();

				for (int i = 0; i < argumentTypes.size(); i++) {
					if (i < function.getParameters().size() && argumentTypes.get(i) == scope.getTypeCompiler().getAny(scope))
						argumentTypes.set(i, function.getParameters().get(i).getType());
				}

				List<MethodParameter<E, T>> newArguments = new ArrayList<MethodParameter<E, T>>();
				for (ParsedFunctionParameter parameter : header.getParameters()) {
					newArguments.add(parameter.compile(scope));
				}

				compiledHeader = new MethodHeader<E, T>(
						compiledHeader.getPosition(),
						function.getGenericParameters(),
						newReturnType,
						newArguments,
						function.isVarargs());
			}
		}

		SymbolicFunction<E, T> functionUnit = new SymbolicFunction<E, T>(getPosition(), Modifier.PRIVATE.getCode(), compiledHeader, scope);
		EnvironmentFunctionLiteral<E, T> functionScope = new EnvironmentFunctionLiteral<E, T>(scope, functionUnit);

		for (int i = 0; i < compiledHeader.getParameters().size(); i++) {
			MethodParameter<E, T> argument = compiledHeader.getParameters().get(i);
			SymbolLocal<E, T> symbol = argument.getLocal();

			functionScope.putValue(
					argument.getName(),
					symbol,
					header.getParameters().get(i).getPosition());
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

	private static class EnvironmentFunctionLiteral<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
			implements IMethodScope<E, T>
	{
		private final IMethodScope<E, T> outer;
		private final SymbolicFunction<E, T> functionUnit;
		private final Map<String, IZenSymbol<E, T>> locals;

		public EnvironmentFunctionLiteral(IMethodScope<E, T> outer, SymbolicFunction<E, T> functionUnit)
		{
			this.outer = outer;
			this.functionUnit = functionUnit;
			locals = new HashMap<String, IZenSymbol<E, T>>();
		}
		
		@Override
		public ISymbolicDefinition<E, T> getDefinition()
		{
			return functionUnit;
		}

		@Override
		public AccessScope getAccessScope()
		{
			return outer.getAccessScope();
		}

		@Override
		public ITypeCompiler<E, T> getTypeCompiler()
		{
			return outer.getTypeCompiler();
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
		public IMethodScope<E, T> getConstantEnvironment()
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
		public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
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
		public Statement<E, T> getControlStatement(String label)
		{
			return null;
		}

		@Override
		public T getReturnType()
		{
			return functionUnit.getHeader().getReturnType();
		}

		@Override
		public ICodeErrorLogger<E, T> getErrorLogger()
		{
			return outer.getErrorLogger();
		}

		@Override
		public MethodHeader<E, T> getMethodHeader()
		{
			return functionUnit.getHeader();
		}

		@Override
		public TypeCapture<E, T> getTypeCapture()
		{
			return outer.getTypeCapture();
		}
	}
}
