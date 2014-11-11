package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class ExpressionConditional extends Expression {
	private final Expression condition;
	private final Expression onIf;
	private final Expression onElse;
	
	public ExpressionConditional(CodePosition position, IScopeMethod environment, Expression condition, Expression onIf, Expression onElse) {
		super(position, environment);
		
		this.condition = condition;
		this.onIf = onIf;
		this.onElse = onElse;
	}

	@Override
	public ZenType getType() {
		return onIf.getType(); // TODO: improve this - merge types
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		Label lblElse = new Label();
		Label lblExit = new Label();
		
		condition.compileElse(lblElse, output);
		onIf.compile(result, output);
		output.goTo(lblExit);
		output.label(lblElse);
		onElse.compile(result, output);
		output.label(lblExit);
	}
}
