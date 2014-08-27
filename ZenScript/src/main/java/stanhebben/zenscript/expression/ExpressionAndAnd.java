package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

public class ExpressionAndAnd extends Expression {
	private final Expression a;
	private final Expression b;
	
	public ExpressionAndAnd(ZenPosition position, IScopeMethod environment, Expression a, Expression b) {
		super(position, environment);
		
		this.a = a;
		this.b = b;
	}

	@Override
	public ZenType getType() {
		return a.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		// if not a: return false
		// if not b: return false
		// return true
		
		Label skip = new Label();
		a.compile(true, output);
		output.ifEQ(skip);
		b.compile(true, output);
		output.ifEQ(skip);
		output.iConst1();
		output.label(skip);
		
		if (!result) {
			output.pop();
		}
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output) {
		a.compile(true, output);
		output.ifEQ(onElse);
		b.compile(true, output);
		output.ifEQ(onElse);
	}
}
