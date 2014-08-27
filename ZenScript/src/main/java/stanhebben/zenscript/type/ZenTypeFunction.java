/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethodArgument;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.type.casting.CastingRuleMatchedFunction;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeFunction extends ZenType {
	private final ZenType returnType;
	private final List<JavaMethodArgument> arguments;
	private final String name;
	
	private final Map<ZenType, CastingRuleMatchedFunction> implementedInterfaces = new HashMap<ZenType, CastingRuleMatchedFunction>();
	private String className = null;
	
	public ZenTypeFunction(IScopeGlobal environment, ZenType returnType, List<JavaMethodArgument> arguments) {
		super(environment);
		
		this.returnType = returnType;
		this.arguments = arguments;
		this.className = environment.makeClassName();
		
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append("function(");
		boolean first = true;
		for (JavaMethodArgument type : arguments) {
			if (first) {
				first = false;
			} else {
				nameBuilder.append(',');
			}
			nameBuilder.append(type.getType().getName());
		}
		nameBuilder.append(returnType.getName());
		name = nameBuilder.toString();
	}
	
	public ZenType getReturnType() {
		return returnType;
	}
	
	public List<JavaMethodArgument> getArguments() {
		return arguments;
	}
	
	@Override
	public String getAnyClassName() {
		// TODO: make any for functions
		return null;
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		environment.error(position, "Functions have no members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		environment.error(position, "Functions have no static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues, MethodOutput output) {
		return null;
	}
	
	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}
	
	@Override
	public ICastingRule getCastingRule(ZenType type) {
		if (implementedInterfaces.containsKey(type)) {
			return implementedInterfaces.get(type);
		}
		
		TypeRegistry types = getEnvironment().getTypes();
		List<IJavaMethod> methods = type.getMethods();
		
		if (methods.isEmpty()) {
			return null;
		}
		
		for (IJavaMethod method : methods) {
			ZenType methodReturnType = method.getReturnType();
			ICastingRule returnCastingRule = null;
			if (!returnType.equals(methodReturnType)) {
				returnCastingRule = returnType.getCastingRule(methodReturnType);
				if (returnCastingRule == null) {
					System.out.println("Return types don't match");
					continue;
				}
			}
			
			JavaMethodArgument[] methodArguments = method.getArguments();
			if (methodArguments.length < arguments.size()) {
				System.out.println("Argument count doesn't match");
				return null;
			}
			
			ICastingRule[] argumentCastingRules = new ICastingRule[arguments.size()];
			for (int i = 0; i < argumentCastingRules.length; i++) {
				ZenType argumentType = methodArguments[i].getType();
				if (!argumentType.equals(arguments.get(i).getType())) {
					argumentCastingRules[i] = argumentType.getCastingRule(arguments.get(i).getType());
					if (argumentCastingRules[i] == null) {
						System.out.println("Argument " + i + " doesn't match");
						System.out.println("Cannot cast " + argumentType.getName() + " to " + arguments.get(i).getType().getName());
						return null;
					}
				}
			}
			
			CastingRuleMatchedFunction castingRule = new CastingRuleMatchedFunction(this, type, returnCastingRule, argumentCastingRules);
			implementedInterfaces.put(type, castingRule);
			System.out.println("Can cast this function");
			return castingRule;
		}
		
		return null;
	}

	@Override
	public Type toASMType() {
		return null; // TODO: NEXT: expand
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return null; // TODO: NEXT: expand
	}

	@Override
	public boolean isNullable() {
		return true;
	}
	
	@Override
	public Expression unary(ZenPosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public Expression binary(ZenPosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression trinary(ZenPosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public List<IJavaMethod> getMethods() {
		// TODO: implement the method
		return null;
	}

	/*@Override
	public Expression call(
			ZenPosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		return null; // TODO: complete
	}
	
	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		return Arrays.copyOf(arguments, numArguments);
	}*/

	@Override
	public Class toJavaClass() {
		// TODO: complete
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Expression defaultValue(ZenPosition position, IScopeMethod environment) {
		return new ExpressionNull(position, environment);
	}

	@Override
	public ZenType nullable() {
		return this;
	}

	@Override
	public ZenType nonNull() {
		return this;
	}
}
