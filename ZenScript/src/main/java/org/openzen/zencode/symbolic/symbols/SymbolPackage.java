/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialPackage;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class SymbolPackage<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	implements IZenSymbol<E, T>
{
	private final HashMap<String, IZenSymbol<E, T>> members;

	private final String name;

	public SymbolPackage(String name)
	{
		this.name = name;
		members = new HashMap<String, IZenSymbol<E, T>>();
	}

	public String getName()
	{
		return name;
	}

	public Map<String, IZenSymbol<E, T>> getPackages()
	{
		return members;
	}

	public IZenSymbol<E, T> get(String name)
	{
		return members.get(name);
	}

	public void put(String name, IZenSymbol<E, T> symbol, ICodeErrorLogger<E, T> errors)
	{
		String[] parts = Strings.split(name, '.');
		String[] pkgParts = Arrays.copyOf(parts, parts.length - 1);
		SymbolPackage<E, T> pkgCurrent = this;
		String pkgName = null;
		for (String part : pkgParts) {
			if (pkgName == null)
				pkgName = part;
			else
				pkgName = pkgName + '.' + part;

			if (pkgCurrent.members.containsKey(part)) {
				IZenSymbol<E, T> member = pkgCurrent.members.get(part);
				if (member instanceof SymbolPackage)
					pkgCurrent = (SymbolPackage<E, T>) member;
				else {
					errors.errorNotAPackage(CodePosition.SYSTEM, part);
					return;
				}
			} else {
				SymbolPackage<E, T> child = new SymbolPackage<E, T>(pkgName);
				pkgCurrent.members.put(part, child);
				pkgCurrent = child;
			}
		}

		//System.out.println("Adding " + parts[parts.length - 1] + " to package " + pkgCurrent.getName() + "(" + symbol + ")");
		if (pkgCurrent.members.containsKey(parts[parts.length - 1]))
			errors.errorAlreadyDefinedInPackage(CodePosition.SYSTEM, name, Strings.join(Arrays.copyOf(parts, parts.length - 1), "."));
		else
			pkgCurrent.members.put(parts[parts.length - 1], symbol);
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IMethodScope<E, T> environment)
	{
		return new PartialPackage<E, T>(position, environment, this);
	}
}
