package stanhebben.zenscript.statements;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class StatementIf extends Statement {
	private final Expression condition;
	private final Statement onThen;
	private final Statement onElse;
	
	public StatementIf(CodePosition position, IScopeMethod environment, Expression condition, Statement onThen, Statement onElse) {
		super(position, environment);
		
		this.condition = condition;
		this.onThen = onThen;
		this.onElse = onElse;
	}

	@Override
	public void compile(MethodOutput output) {
		output.position(getPosition());
		
		ZenType expressionType = condition.getType();
		if (expressionType != getScope().getTypes().BOOL)
			throw new RuntimeException("condition is not a boolean");
		
		Label labelEnd = new Label();
		Label labelElse = onElse == null ? labelEnd : new Label();

		condition.compileElse(labelElse, output);
		onThen.compile(output);
		
		if (onElse != null) {
			output.goTo(labelEnd);
			output.label(labelElse);
			onElse.compile(output);
		}
		
		output.label(labelEnd);
	}
}
