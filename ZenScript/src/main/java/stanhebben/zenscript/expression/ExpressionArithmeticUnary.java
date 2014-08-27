package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.annotations.OperatorType;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

public class ExpressionArithmeticUnary extends Expression {
	private final Expression base;
	private final OperatorType operator;
	
	public ExpressionArithmeticUnary(
			ZenPosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression base) {
		super(position, environment);
		
		this.base = base;
		this.operator = operator;
	}

	@Override
	public ZenType getType() {
		return base.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		base.compile(result, output);
		
		if (result) {
			TypeRegistry types = getEnvironment().getTypes();
			
			ZenType type = base.getType();
			if (type == types.BOOL) {
				if (operator == OperatorType.NOT) {
					output.iNot();
					return;
				}
			} else if (type == types.BYTE || type == types.SHORT || type == types.INT) {
				if (operator == OperatorType.NOT) {
					output.iNot();
					return;
				} else if (operator == OperatorType.NEG) {
					output.iNeg();
					return;
				}
			} else if (type == types.LONG) {
				if (operator == OperatorType.NOT) {
					output.lNot();
					return;
				} else if (operator == OperatorType.NEG) {
					output.lNeg();
					return;
				}
			} else if (type == types.FLOAT) {
				if (operator == OperatorType.NEG) {
					output.fNeg();
					return;
				}
			} else if (type == types.DOUBLE) {
				if (operator == OperatorType.NEG) {
					output.fNeg();
					return;
				}
			}
		}
		
		getEnvironment().error(getPosition(), "Invalid operation");
	}
}
