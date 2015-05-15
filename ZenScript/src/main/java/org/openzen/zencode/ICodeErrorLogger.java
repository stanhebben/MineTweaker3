/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode;

import java.io.IOException;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.parser.expression.ParsedCallArguments;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 * Error logger. Implementations can forward errors to their own error logging
 * system.
 * 
 * @author Stan Hebben
 * @param <E>
 */
public interface ICodeErrorLogger<E extends IPartialExpression<E>> {
	/**
	 * Checks if any errors have been logged through this logger.
	 * 
	 * @return 
	 */
	public boolean hasErrors();
	
	public void errorSymbolNameAlreadyExists(CodePosition position, String name);
	
	public void errorCannotAssignInConstantScope(CodePosition position);
	
	public void errorNoBreakableControlStatement(CodePosition position);
	
	public void errorNoContinuableControlStatement(CodePosition position);
	
	public void errorNoLabeledControlStatement(CodePosition position, String label);
	
	public void errorInvalidNumberOfGenericArguments(CodePosition position, ITypeDefinition<E> type, List<IGenericType<E>> genericArguments);
	
	public void errorInvalidNumberOfArguments(CodePosition position);
	
	public void errorCannotCastImplicit(CodePosition position, IGenericType<E> fromType, IGenericType<E> toType);
	
	public void errorCannotCastExplicit(CodePosition position, IGenericType<E> fromType, IGenericType<E> toType);
	
	public void errorCannotCastArrayTo(CodePosition position, IGenericType<E> toType);
	
	public void errorCannotCastMapTo(CodePosition position, IGenericType<E> toType);
	
	public void errorCannotAssignTo(CodePosition position, IPartialExpression<E> target);
	
	public void errorNotAType(CodePosition position, IZenSymbol<E> symbol, String name);
	
	public void errorNotAType(CodePosition position, IImportable<E> importable);
	
	public void errorCannotBeNull(CodePosition position);
	
	public void errorNotAValidParameterName(CodePosition position);
	
	public void errorUnexpectedEndOfFile(CodePosition position);
	
	public void errorKeyRequired(CodePosition position);
	
	public void errorCouldNotResolveBracket(CodePosition position, List<Token> tokens);
	
	public void errorNotAValidMethod(CodePosition position);
	
	public void errorNoMatchingMethod(CodePosition position, List<ICallable<E>> methods, ParsedCallArguments arguments);
	
	public void errorNoSuchDollarVariable(CodePosition position, String name);
	
	public void errorCannotCombineTypes(CodePosition position, IGenericType<E> type1, IGenericType<E> type2);
	
	public void errorCannotConvertToChar(CodePosition position, String value);
	
	public void errorCouldNotResolveSymbol(CodePosition position, String name);
	
	public void errorNotAValidMemberToken(CodePosition position, Token token);
	
	public void errorCaseOutsideSwitch(CodePosition position);
	
	public void errorDefaultOutsideSwitch(CodePosition position);
	
	public void errorDuplicateCase(CodePosition position, E value);
	
	public void errorDuplicateDefault(CodePosition position);
	
	public void errorNoSuchIterator(CodePosition position, IGenericType<E> type, int numVariables);
	
	public void errorNoSuchOperator(CodePosition position, IGenericType<E> type, OperatorType operator);
	
	public void errorCouldNotResolvePackage(CodePosition position, String packageName);
	
	public void errorMissingReturnValue(CodePosition position);
	
	public void errorInvalidSwitchValueType(CodePosition position, E value);
	
	public void errorCannotBeNullable(CodePosition position, IGenericType<E> type);
	
	public void errorInvalidExpression(CodePosition position, IPartialExpression<E> expression);
	
	public void errorCannotCall(CodePosition position, IPartialExpression<E> expression);
	
	public void errorInvalidOperatorArguments(CodePosition position, OperatorType operator, MethodHeader<E> header, IGenericType<E>... expectedTypes);
	
	public void errorConstructorHasReturnType(CodePosition position);
	
	public void errorVoidParameter(CodePosition position, String parameterName);
	
	public void errorCannotLoadInclude(CodePosition position, String filename);
	
	public void errorCannotLoadInclude(CodePosition position, String filename, IOException exception);
	
	public void errorModifiersForInclude(CodePosition position);
	
	public void errorAnnotationsForInclude(CodePosition position);
	
	public void errorModifiersForStatement(CodePosition position);
	
	public void errorAnnotationsForStatement(CodePosition position);
	
	public void errorNotAPackage(CodePosition position, String name);
	
	public void errorAlreadyDefinedInPackage(CodePosition position, String name, String packageName);
	
	public void errorDuplicateModifier(CodePosition position, Modifier modifier);
	
	public void errorIncompatibleModifier(CodePosition position, Modifier a, Modifier b);
	
	public void errorMultipleSuperclasses(CodePosition position, String className);
	
	public void errorNoConstructorsForType(CodePosition position, IGenericType<E> type);
	
	public void errorNoSuchMember(CodePosition position, IImportable<E> importable, String name);
	
	public void errorNamedWildcardImport(CodePosition position, List<String> importName);
	
	public void errorNoGetterForMember(CodePosition position, String name);
	
	public void errorNoSetterForMember(CodePosition position, String name);

	public void errorAmbiguousMethodCall(CodePosition position);
	
	public void errorFunctionHasNoMembers(CodePosition position);
	
	public void errorNullHasNoMembers(CodePosition position);
	
	public void errorVoidHasNoMembers(CodePosition position);
	
	public void errorNotAStaticMember(CodePosition position, IMember<E> member);
	
	public void errorValueOutsideRange(CodePosition position, long value);
}
