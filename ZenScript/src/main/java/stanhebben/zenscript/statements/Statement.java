package stanhebben.zenscript.statements;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public abstract class Statement {
	private final CodePosition position;
	private final IScopeMethod scope;
	
	public Statement(CodePosition position, IScopeMethod environment) {
		this.position = position;
		this.scope = environment;
	}
	
	public CodePosition getPosition() {
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
