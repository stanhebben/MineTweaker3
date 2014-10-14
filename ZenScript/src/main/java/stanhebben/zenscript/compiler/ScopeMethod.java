/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ScopeMethod implements IScopeMethod {
	private final IScopeClass environment;
	private final Map<String, IZenSymbol> local;
	private final ZenType returnType;
	
	public ScopeMethod(IScopeClass environment, ZenType returnType) {
		this.environment = environment;
		this.local = new HashMap<String, IZenSymbol>();
		this.returnType = returnType;
	}
	
	@Override
	public TypeRegistry getTypes() {
		return environment.getTypes();
	}

	@Override
	public void error(ZenPosition position, String message) {
		environment.error(position, message);
	}

	@Override
	public void warning(ZenPosition position, String message) {
		environment.warning(position, message);
	}

	@Override
	public IZenCompileEnvironment getEnvironment() {
		return environment.getEnvironment();
	}
	
	@Override
	public String makeClassName() {
		return environment.makeClassName();
	}

	@Override
	public void putClass(String name, byte[] data) {
		environment.putClass(name, data);
	}

	@Override
	public boolean containsClass(String name) {
		return environment.containsClass(name);
	}

	@Override
	public IPartialExpression getValue(String name, ZenPosition position, IScopeMethod environment) {
		if (local.containsKey(name)) {
			return local.get(name).instance(position, environment);
		} else {
			return environment.getValue(name, position, environment);
		}
	}

	@Override
	public void putValue(String name, IZenSymbol value, ZenPosition position) {
		if (local.containsKey(name)) {
			error(position, "Value already defined in this scope: " + name);
		} else {
			local.put(name, value);
		}
	}

	@Override
	public Set<String> getClassNames() {
		return environment.getClassNames();
	}

	@Override
	public byte[] getClass(String name) {
		return environment.getClass(name);
	}

	@Override
	public Statement getControlStatement(String label) {
		return null;
	}

	@Override
	public ZenType getReturnType() {
		return returnType;
	}
}
