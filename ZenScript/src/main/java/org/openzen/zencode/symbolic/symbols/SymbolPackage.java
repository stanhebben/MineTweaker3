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
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class SymbolPackage<E extends IPartialExpression<E>> implements IZenSymbol<E>
{
	private final HashMap<String, IZenSymbol<E>> members;

	private final String name;

	public SymbolPackage(String name)
	{
		this.name = name;
		members = new HashMap<String, IZenSymbol<E>>();
	}

	public String getName()
	{
		return name;
	}

	public Map<String, IZenSymbol<E>> getPackages()
	{
		return members;
	}

	public IZenSymbol<E> get(String name)
	{
		return members.get(name);
	}

	public void put(String name, IZenSymbol<E> symbol, ICodeErrorLogger<E> errors)
	{
		String[] parts = Strings.split(name, '.');
		String[] pkgParts = Arrays.copyOf(parts, parts.length - 1);
		SymbolPackage<E> pkgCurrent = this;
		String pkgName = null;
		for (String part : pkgParts) {
			if (pkgName == null)
				pkgName = part;
			else
				pkgName = pkgName + '.' + part;

			if (pkgCurrent.members.containsKey(part)) {
				IZenSymbol<E> member = pkgCurrent.members.get(part);
				if (member instanceof SymbolPackage)
					pkgCurrent = (SymbolPackage<E>) member;
				else {
					errors.errorNotAPackage(CodePosition.SYSTEM, part);
					return;
				}
			} else {
				SymbolPackage<E> child = new SymbolPackage<E>(pkgName);
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
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> environment)
	{
		return new PartialPackage<E>(position, environment, this);
	}
}
