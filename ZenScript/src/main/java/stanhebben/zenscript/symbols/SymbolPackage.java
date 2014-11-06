/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.symbols;

import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.ICodeErrorLogger;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.expression.partial.PartialPackage;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class SymbolPackage implements IZenSymbol {
	private final HashMap<String, IZenSymbol> members;
	
	private final String name;
	
	public SymbolPackage(String name) {
		this.name = name;
		members = new HashMap<String, IZenSymbol>();
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, IZenSymbol> getPackages() {
		return members;
	}
	
	public IZenSymbol get(String name) {
		return members.get(name);
	}
	
	public void put(String name, IZenSymbol symbol, ICodeErrorLogger errors) {
		String[] parts = Strings.split(name, '.');
		String[] pkgParts = Arrays.copyOf(parts, parts.length - 1);
		SymbolPackage pkgCurrent = this;
		String pkgName = null;
		for (String part : pkgParts) {
			if (pkgName == null) {
				pkgName = part;
			} else {
				pkgName = pkgName + '.' + part;
			}
			
			if (pkgCurrent.members.containsKey(part)) {
				IZenSymbol member = pkgCurrent.members.get(part);
				if (member instanceof SymbolPackage) {
					pkgCurrent = (SymbolPackage) member;
				} else {
					errors.error(null, part + " is not a package");
					return;
				}
			} else {
				SymbolPackage child = new SymbolPackage(pkgName);
				pkgCurrent.members.put(part, child);
				pkgCurrent = child;
			}
		}
		
		//System.out.println("Adding " + parts[parts.length - 1] + " to package " + pkgCurrent.getName() + "(" + symbol + ")");
		if (pkgCurrent.members.containsKey(parts[parts.length - 1])) {
			errors.error(null, parts[parts.length - 1] + " is already defined in that package");
		} else {
			pkgCurrent.members.put(parts[parts.length - 1], symbol);
		}
	}
	
	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod environment) {
		return new PartialPackage(position, environment, this);
	}
}
