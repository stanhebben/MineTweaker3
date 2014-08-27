package stanhebben.zenscript.statements;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

public abstract class Statement {
	private final ZenPosition position;
	private final IScopeMethod scope;
	
	public Statement(ZenPosition position, IScopeMethod environment) {
		this.position = position;
		this.scope = environment;
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public IScopeMethod getScope() {
		return scope;
	}
	
	public boolean isReturn() {
		return false;
	}
	
	public abstract void compile(MethodOutput output);
}
