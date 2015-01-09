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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 * Error logger. Implementations can forward errors to their own error logging
 * system.
 * 
 * @author Stan Hebben
 * @param <E>
 * @param <T>
 */
public interface ICodeErrorLogger<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> {
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
	
	public void errorInvalidNumberOfGenericArguments(CodePosition position, T type, List<T> genericArguments);
	
	public void errorInvalidNumberOfArguments(CodePosition position);
	
	public void errorCannotCastImplicit(CodePosition position, T fromType, T toType);
	
	public void errorCannotCastExplicit(CodePosition position, T fromType, T toType);
	
	public void errorCannotCastArrayTo(CodePosition position, T toType);
	
	public void errorCannotCastMapTo(CodePosition position, T toType);
	
	public void errorCannotAssignTo(CodePosition position, IPartialExpression<E, T> target);
	
	public void errorNotAType(CodePosition position, IPartialExpression<E, T> value);
	
	public void errorNotAType(CodePosition position, IPartialExpression<E, T> value, String name);
	
	public void errorCannotBeNull(CodePosition position);
	
	public void errorNotAValidParameterName(CodePosition position);
	
	public void errorUnexpectedEndOfFile(CodePosition position);
	
	public void errorKeyRequired(CodePosition position);
	
	public void errorCouldNotResolveBracket(CodePosition position, List<Token> tokens);
	
	public void errorNotAValidMethod(CodePosition position);
	
	public void errorNoMatchingMethod(CodePosition position, List<IMethod<E, T>> methods, ParsedCallArguments arguments);
	
	public void errorNoSuchDollarVariable(CodePosition position, String name);
	
	public void errorCannotCombineTypes(CodePosition position, T type1, T type2);
	
	public void errorCannotConvertToChar(CodePosition position, String value);
	
	public void errorCouldNotResolveSymbol(CodePosition position, String name);
	
	public void errorNotAValidMemberToken(CodePosition position, Token token);
	
	public void errorCaseOutsideSwitch(CodePosition position);
	
	public void errorDefaultOutsideSwitch(CodePosition position);
	
	public void errorDuplicateCase(CodePosition position, E value);
	
	public void errorDuplicateDefault(CodePosition position);
	
	public void errorNoSuchIterator(CodePosition position, T type, int numVariables);
	
	public void errorCouldNotResolvePackage(CodePosition position, String packageName);
	
	public void errorMissingReturnValue(CodePosition position);
	
	public void errorInvalidSwitchValueType(CodePosition position, E value);
	
	public void errorCannotBeNullable(CodePosition position, T type);
	
	public void errorInvalidExpression(CodePosition position, IPartialExpression<E, T> expression);
	
	public void errorCannotCall(CodePosition position, IPartialExpression<E, T> expression);
	
	public void errorInvalidOperatorArguments(CodePosition position, OperatorType operator, MethodHeader<E, T> header, T... expectedTypes);
	
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
	
	public void errorNoConstructorsForType(CodePosition position, T type);
}
