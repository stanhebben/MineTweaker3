package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

public class ExpressionArithmeticUnary extends Expression {
	private final Expression base;
	private final OperatorType operator;
	
	public ExpressionArithmeticUnary(
			CodePosition position,
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
			TypeRegistry types = getScope().getTypes();
			
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
		
		getScope().error(getPosition(), "Invalid operation");
	}
}
