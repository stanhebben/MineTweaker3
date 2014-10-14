/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.TypeExpansion;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class EnvironmentScript implements IScopeGlobal {
	private final IScopeGlobal parent;
	private final Map<String, IZenSymbol> imports;
	
	public EnvironmentScript(IScopeGlobal parent) {
		this.parent = parent;
		imports = new HashMap<String, IZenSymbol>();
	}

	@Override
	public IZenCompileEnvironment getEnvironment() {
		return parent.getEnvironment();
	}

	@Override
	public String makeClassName() {
		return parent.makeClassName();
	}

	@Override
	public boolean containsClass(String name) {
		return parent.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data) {
		parent.putClass(name, data);
	}

	@Override
	public IPartialExpression getValue(String name, ZenPosition position, IScopeMethod environment) {
		if (imports.containsKey(name)) {
			IZenSymbol imprt = imports.get(name);
			if (imprt == null) throw new RuntimeException("How could this happen?");
			return imprt.instance(position, environment);
		} else {
			return parent.getValue(name, position, environment);
		}
	}

	@Override
	public void putValue(String name, IZenSymbol value, ZenPosition position) {
		if (value == null) throw new IllegalArgumentException("value cannot be null");
		
		if (imports.containsKey(name)) {
			error(position, "Value already defined in this scope: " + name);
		} else {
			imports.put(name, value);
		}
	}

	@Override
	public TypeRegistry getTypes() {
		return parent.getTypes();
	}

	@Override
	public void error(ZenPosition position, String message) {
		parent.error(position, message);
	}

	@Override
	public void warning(ZenPosition position, String message) {
		parent.warning(position, message);
	}

	@Override
	public Set<String> getClassNames() {
		return parent.getClassNames();
	}

	@Override
	public byte[] getClass(String name) {
		return parent.getClass(name);
	}
}
