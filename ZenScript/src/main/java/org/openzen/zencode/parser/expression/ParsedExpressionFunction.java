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
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.parser.definition.ParsedFunctionParameter;
import org.openzen.zencode.parser.definition.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.type.IGenericType;
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
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> asType)
	{
		MethodHeader<E> compiledHeader = header.compile(scope);
		
		if (asType != null) {
			MethodHeader<E> function = asType.getFunctionHeader();
			if (function != null) {
				List<IGenericType<E>> argumentTypes = new ArrayList<IGenericType<E>>();
				for (ParsedFunctionParameter argument : header.getParameters()) {
					argumentTypes.add(argument.getType().compile(scope));
				}

				IGenericType<E> newReturnType = compiledHeader.getReturnType();
				if (compiledHeader.getReturnType() == scope.getTypeCompiler().any)
					newReturnType = function.getReturnType();

				for (int i = 0; i < argumentTypes.size(); i++) {
					if (i < function.getParameters().size() && argumentTypes.get(i) == scope.getTypeCompiler().any)
						argumentTypes.set(i, function.getParameters().get(i).getType());
				}

				List<MethodParameter<E>> newArguments = new ArrayList<>();
				for (ParsedFunctionParameter parameter : header.getParameters()) {
					newArguments.add(parameter.compile(scope));
				}

				compiledHeader = new MethodHeader<E>(
						compiledHeader.getPosition(),
						function.getGenericParameters(),
						newReturnType,
						newArguments,
						function.isVarargs());
			}
		}

		SymbolicFunction<E> functionUnit = new SymbolicFunction<E>(getPosition(), Modifier.PRIVATE.getCode(), null, compiledHeader, scope);
		EnvironmentFunctionLiteral<E> functionScope = new EnvironmentFunctionLiteral<E>(scope, functionUnit);

		for (int i = 0; i < compiledHeader.getParameters().size(); i++) {
			MethodParameter<E> argument = compiledHeader.getParameters().get(i);
			LocalSymbol<E> symbol = argument.getLocal();

			functionScope.putValue(
					argument.getName(),
					symbol,
					header.getParameters().get(i).getPosition());
		}

		List<Statement<E>> cStatements = new ArrayList<Statement<E>>();
		for (ParsedStatement statement : statements) {
			Statement<E> cStatement = statement.compile(functionScope);
			cStatements.add(cStatement);
		}

		return scope.getExpressionCompiler().functionExpression(getPosition(), scope, compiledHeader, cStatements);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		return null;
	}

	private static class EnvironmentFunctionLiteral<E extends IPartialExpression<E>>
			implements IMethodScope<E>
	{
		private final IMethodScope<E> outer;
		private final SymbolicFunction<E> functionUnit;
		private final Map<String, IZenSymbol<E>> locals;

		public EnvironmentFunctionLiteral(IMethodScope<E> outer, SymbolicFunction<E> functionUnit)
		{
			this.outer = outer;
			this.functionUnit = functionUnit;
			locals = new HashMap<String, IZenSymbol<E>>();
		}
		
		@Override
		public ISymbolicDefinition<E> getDefinition()
		{
			return functionUnit;
		}

		@Override
		public AccessScope getAccessScope()
		{
			return outer.getAccessScope();
		}

		@Override
		public TypeRegistry<E> getTypeCompiler()
		{
			return outer.getTypeCompiler();
		}

		@Override
		public IZenCompileEnvironment<E> getEnvironment()
		{
			return outer.getEnvironment();
		}

		@Override
		public IExpressionCompiler<E> getExpressionCompiler()
		{
			return outer.getExpressionCompiler();
		}
		
		@Override
		public IMethodScope<E> getConstantScope()
		{
			return outer.getConstantScope();
		}

		@Override
		public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
		{
			if (locals.containsKey(name))
				return locals.get(name).instance(position, environment);
			else
				return outer.getValue(name, position, environment).via(functionUnit);
		}

		@Override
		public void putValue(String name, IZenSymbol<E> value, CodePosition position)
		{
			locals.put(name, value);
		}

		@Override
		public Statement<E> getControlStatement(String label)
		{
			return null;
		}

		@Override
		public IGenericType<E> getReturnType()
		{
			return functionUnit.getHeader().getReturnType();
		}

		@Override
		public ICodeErrorLogger<E> getErrorLogger()
		{
			return outer.getErrorLogger();
		}

		@Override
		public MethodHeader<E> getMethodHeader()
		{
			return functionUnit.getHeader();
		}

		@Override
		public TypeCapture<E> getTypeCapture()
		{
			return outer.getTypeCapture();
		}

		@Override
		public IZenSymbol<E> getSymbol(String name)
		{
			if (locals.containsKey(name))
				return locals.get(name);
			
			return outer.getSymbol(name);
		}

		@Override
		public boolean contains(String name)
		{
			return locals.containsKey(name) || outer.contains(name);
		}

		@Override
		public ZenPackage<E> getRootPackage()
		{
			return outer.getRootPackage();
		}

		@Override
		public void putImport(String name, IZenSymbol<E> symbol, CodePosition position)
		{
			putValue(name, symbol, position);
		}

		@Override
		public boolean isConstructor()
		{
			return false;
		}
	}
}
