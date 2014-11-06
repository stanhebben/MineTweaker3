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
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodArgument;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.casting.CastingRuleMatchedFunction;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeFunction extends ZenType {
	private final String name;
	private final MethodHeader header;
	
	private final Map<ZenType, CastingRuleMatchedFunction> implementedInterfaces = new HashMap<ZenType, CastingRuleMatchedFunction>();
	private String className = null;
	
	public ZenTypeFunction(MethodHeader header) {
		super(header.getReturnType().getScope());
		
		this.header = header;
		this.className = getScope().makeClassName();
		
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append("function(");
		boolean first = true;
		for (MethodArgument type : header.getArguments()) {
			if (first) {
				first = false;
			} else {
				nameBuilder.append(',');
			}
			nameBuilder.append(type.getType().getName());
		}
		nameBuilder.append(header.getReturnType().getName());
		name = nameBuilder.toString();
	}
	
	public MethodHeader getHeader() {
		return header;
	}
	
	@Override
	public String getAnyClassName() {
		// TODO: make any for functions
		return null;
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		environment.error(position, "Functions have no members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		environment.error(position, "Functions have no static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
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
		List<IMethod> methods = type.getMethods();
		
		if (methods.isEmpty()) {
			return null;
		}
		
		for (IMethod method : methods) {
			ZenType methodReturnType = method.getMethodHeader().getReturnType();
			ICastingRule returnCastingRule = null;
			if (!header.getReturnType().equals(methodReturnType)) {
				returnCastingRule = header.getReturnType().getCastingRule(methodReturnType);
				if (returnCastingRule == null) {
					System.out.println("Return types don't match");
					continue;
				}
			}
			
			List<MethodArgument> methodArguments = method.getMethodHeader().getArguments();
			if (methodArguments.size() < header.getArguments().size()) {
				System.out.println("Argument count doesn't match");
				return null;
			}
			
			ICastingRule[] argumentCastingRules = new ICastingRule[header.getArguments().size()];
			for (int i = 0; i < argumentCastingRules.length; i++) {
				ZenType argumentType = methodArguments.get(i).getType();
				if (!argumentType.equals(header.getArguments().get(i).getType())) {
					argumentCastingRules[i] = argumentType.getCastingRule(header.getArguments().get(i).getType());
					if (argumentCastingRules[i] == null) {
						System.out.println("Argument " + i + " doesn't match");
						System.out.println("Cannot cast " + argumentType.getName() + " to " + header.getArguments().get(i).getType().getName());
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
	public Expression unary(CodePosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public Expression binary(CodePosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression trinary(CodePosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		environment.error(position, "cannot apply operators on a function");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public List<IMethod> getMethods() {
		// TODO: implement the method
		return null;
	}

	/*@Override
	public Expression call(
			CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
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
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
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
