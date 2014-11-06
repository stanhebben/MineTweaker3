package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class ExpressionOrOr extends Expression {
	private final Expression a;
	private final Expression b;
	
	public ExpressionOrOr(CodePosition position, IScopeMethod environment, Expression a, Expression b) {
		super(position, environment);
		
		if (!a.getType().isNullable() && (a.getType() != environment.getTypes().BOOL))
			throw new IllegalArgumentException("invalid || input type: " + a.getType().getName());
		if (!b.getType().isNullable() && (b.getType() != environment.getTypes().BOOL))
			throw new IllegalArgumentException("invalid || input type: " + a.getType().getName());
		
		this.a = a;
		this.b = b;
	}

	@Override
	public ZenType getType() {
		return a.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		Label lblOther = new Label();
		Label lblAfter = new Label();
		
		a.compile(true, output);
		
		if (a.getType().isNullable()) {
			output.dup();
			output.ifNull(lblOther);
		} else {
			output.dup();
			output.ifEQ(lblOther);
		}
		
		output.goTo(lblAfter);
		output.label(lblOther);
		output.pop();
		
		b.compile(true, output);
		output.label(lblAfter);
		
		if (!result) {
			output.pop();
		}
	}
}
