/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArray;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethodArgument;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;

/**
 *
 * @author Stan
 */
public class ParsedCallArguments {
	public static ParsedCallArguments parse(ZenTokener tokener, IZenErrorLogger errors) {
		tokener.required(T_BROPEN, "( expected");
		
		List<String> keys = new ArrayList<String>();
		List<ParsedExpression> values = new ArrayList<ParsedExpression>();
		
		boolean canHaveMore = true;
		
		while (canHaveMore) {
			ParsedExpression expression = ParsedExpression.parse(tokener, errors);
			
			if (tokener.optional(T_COLON) != null) {
				String key = expression.asIdentifier();
				if (key == null) {
					errors.error(expression.getPosition(), "Invalid key");
				}
				
				keys.add(key);
				values.add(ParsedExpression.parse(tokener, errors));
			} else {
				keys.add(null);
				values.add(expression);
			}
			
			if (tokener.optional(T_COMMA) == null) {
				canHaveMore = false;
			}
		}
		
		tokener.required(T_BRCLOSE, ") expected");
		return new ParsedCallArguments(keys, values);
	}
	
	private final List<String> keys;
	private final List<ParsedExpression> values;
	private final int numUnkeyedValues;
	
	public ParsedCallArguments(List<String> keys, List<ParsedExpression> values) {
		this.keys = keys;
		this.values = values;
		
		// how many values don't have keys?
		int numUnkeyed = keys.size();
		while (numUnkeyed > 0 && keys.get(numUnkeyed - 1) != null)
			numUnkeyed--;
		
		numUnkeyedValues = numUnkeyed;
	}
	
	public MatchedArguments compile(List<IJavaMethod> methods, IScopeMethod environment) {
		ZenType[] predictedTypes = predict(methods);
		
		// We now have the predicted types array and we can use it to compile
		// the arguments
		Expression[] compiled = new Expression[values.size()];
		for (int i = 0; i < values.size(); i++) {
			compiled[i] = values.get(i).compile(environment, predictedTypes[i]).eval();
		}
		
		// Now match the expression again to find the actual matching method
		
		// do the argument types match exactly?
		for (IJavaMethod method : methods) {
			Expression[] matched = match(method, environment, compiled, true);
			if (matched != null) {
				return new MatchedArguments(method, matched);
			}
		}
		
		// do the argument types match after implicit conversion?
		for (IJavaMethod method : methods) {
			Expression[] matched = match(method, environment, compiled, false);
			if (matched != null) {
				return new MatchedArguments(method, matched);
			}
		}
		
		return null;
	}
	
	// #######################
	// ### Private methods ###
	// #######################
	
	/**
	 * Predicts argument types given a series of methods on which these arguments
	 * are being called.
	 * 
	 * @param methods method candidates
	 * @return predicted types
	 */
	private ZenType[] predict(List<IJavaMethod> methods) {
		ZenType[] predictedTypes = new ZenType[values.size()];
		boolean[] ambiguous = new boolean[values.size()];
		
		outer: for (IJavaMethod method : methods) {
			if (!method.accepts(values.size()))
				continue;
			
			// Does this method match?
			// A method matches if:
			// - the number of arguments is acceptable
			// - named parameters are all available
			// - the omitted arguments are optional
			JavaMethodArgument[] arguments = method.getArguments();
			boolean[] isUsed = new boolean[arguments.length - numUnkeyedValues];
			
			for (int i = numUnkeyedValues; i < values.size(); i++) {
				int parameterIndex = method.getArgumentIndex(keys.get(i));
				if (parameterIndex < numUnkeyedValues) {
					continue outer;
				} else {
					isUsed[parameterIndex - numUnkeyedValues] = true;
				}
			}
			
			for (int i = 0; i < isUsed.length; i++) {
				if (!isUsed[i]) {
					if (arguments[numUnkeyedValues + i].getDefaultValue() == null) {
						continue outer;
					}
				}
			}
			
			// The method matches.
			// Use it to predict the argument types
			for (int i = 0; i < numUnkeyedValues; i++) {
				if (!ambiguous[i]) {
					if (predictedTypes[i] == null) {
						predictedTypes[i] = arguments[i].getType();
					} else if (!predictedTypes[i].equals(arguments[i].getType())) {
						predictedTypes[i] = null;
						ambiguous[i] = true;
					}
				}
			}
			
			for (int i = numUnkeyedValues; i < values.size(); i++) {
				if (!ambiguous[i]) {
					ZenType argumentType = arguments[method.getArgumentIndex(keys.get(i))].getType();
					
					if (predictedTypes[i] == null) {
						predictedTypes[i] = argumentType;
					} else if (!predictedTypes[i].equals(argumentType)) {
						predictedTypes[i] = null;
						ambiguous[i] = true;
					}
				}
			}
		}
		
		return predictedTypes;
	}
	
	/**
	 * Performs a matching of compiled expressions to the method arguments.
	 * Performs type casting where necessary.
	 * 
	 * @param method
	 * @param compiled
	 * @param exactly
	 * @return 
	 */
	private Expression[] match(IJavaMethod method, IScopeMethod environment, Expression[] compiled, boolean exactly) {
		int numUnkeyed = numUnkeyedValues;
		
		JavaMethodArgument[] arguments = method.getArguments();
		
		if (!method.isVarargs() && numUnkeyed > arguments.length)
			return null;

		boolean[] isUsed = new boolean[arguments.length - numUnkeyed];
		boolean isVarargCall = false;
		ZenType varargBaseType = null;
		
		if (method.isVarargs()) {
			varargBaseType = ((ZenTypeArray) arguments[arguments.length - 1].getType()).getBaseType();
		}
		
		// check parameters without names
		for (int i = 0; i < numUnkeyed; i++) {
			if (method.isVarargs() && i >= arguments.length - 1) {
				if (matches(compiled[i], varargBaseType, exactly)) {
					isVarargCall = true;
					continue;
				}
			}
			
			if (i >= arguments.length)
				return null;
			
			if (!matches(compiled[i], arguments[i].getType(), exactly))
				return null;
		}
		
		// is this a vararg call with an empty array?
		if (method.isVarargs() && numUnkeyed == compiled.length && numUnkeyed == arguments.length - 1) {
			isVarargCall = true;
		}
		
		// check parameters with names
		for (int i = numUnkeyed; i < values.size(); i++) {
			int parameterIndex = method.getArgumentIndex(keys.get(i));
			if (parameterIndex < numUnkeyed) {
				return null;
			} else if (exactly && !arguments[parameterIndex].getType().equals(compiled[i].getType())) {
				return null;
			} else if (!exactly && !arguments[parameterIndex].getType().equals(compiled[i].getType())) {
				return null;
			} else {
				isUsed[parameterIndex - numUnkeyed] = true;
			}
		}
		
		// check if all non-optional arguments are filled
		for (int i = 0; i < isUsed.length; i++) {
			if (!isUsed[i]) {
				if (arguments[numUnkeyed + i].getDefaultValue() == null) {
					return null;
				}
			}
		}
		
		Expression[] result = new Expression[arguments.length];
		
		// fill arguments without name
		if (isVarargCall) {
			numUnkeyed = arguments.length - 1;
		}
		
		for (int i = 0; i < numUnkeyed; i++) {
			result[i] = compiled[i].cast(compiled[i].getPosition(), arguments[i].getType());
		}
		
		if (isVarargCall) {
			result[arguments.length - 1] = assembleVararg(arguments[arguments.length - 1], environment, compiled, numUnkeyed);
		} else {
			
			// fill arguments with name
			for (int i = numUnkeyed; i < values.size(); i++) {
				int parameterIndex = method.getArgumentIndex(keys.get(i));
				result[parameterIndex] = compiled[i].cast(compiled[i].getPosition(), arguments[parameterIndex].getType());
			}

			// fill default values
			for (int i = 0; i < isUsed.length; i++) {
				if (!isUsed[i]) {
					result[i + numUnkeyed] = arguments[i + numUnkeyed].getDefaultValue();
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Check if this type matches another type. If exactly is true, the types
	 * must be equal. Otherwise, the expression type must be implicitly castable
	 * to the argument type.
	 * 
	 * @param expression expression to check
	 * @param type argument type
	 * @param exactly
	 * @return 
	 */
	private boolean matches(Expression expression, ZenType type, boolean exactly) {
		if (exactly) {
			return expression.getType().equals(type);
		} else {
			return expression.getType().canCastImplicit(type);
		}
	}
	
	/**
	 * Assembles remaining arguments into a single array. Used to assemble
	 * varargs values. Also works if from == compiled.length .
	 * 
	 * @param argument argument to assemble for
	 * @param compiled compiled expressions array
	 * @param fromIndex index in the expressions array to assemble from
	 * @return array expression with vararg values
	 */
	private Expression assembleVararg(JavaMethodArgument argument, IScopeMethod environment, Expression[] compiled, int fromIndex) {
		ZenType varargBaseType = ((ZenTypeArray) argument.getType()).getBaseType();
		
		// combine varargs into an array expression
		Expression[] arrayMembers = new Expression[compiled.length - fromIndex];
		for (int i = fromIndex; i < compiled.length; i++) {
			arrayMembers[i - fromIndex] = compiled[i].cast(
					compiled[i].getPosition(),
					varargBaseType);
		}
		
		return new ExpressionArray(
				compiled[fromIndex].getPosition(),
				environment,
				(ZenTypeArrayBasic) argument.getType(),
				arrayMembers);
	}
	
	public class MatchedArguments {
		public final IJavaMethod method;
		public final Expression[] arguments;
		
		public MatchedArguments(IJavaMethod method, Expression[] arguments) {
			this.method = method;
			this.arguments = arguments;
		}
	}
}
