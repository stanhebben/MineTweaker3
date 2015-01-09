/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.mc1710.brackets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import minetweaker.api.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import static minetweaker.api.minecraft.MineTweakerMC.getIItemStackWildcardSize;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import org.openzen.zencode.java.IJavaScopeGlobal;
import org.openzen.zencode.java.JavaNative;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
@BracketHandler(priority = 100)
@SuppressWarnings("unchecked")
public class ItemBracketHandler implements IBracketHandler
{
	private static final Map<String, Item> itemNames;

	static {
		itemNames = new HashMap<String, Item>();
		for (String itemName : (Set<String>) Item.itemRegistry.getKeys()) {
			itemNames.put(itemName.replace(" ", ""), (Item) Item.itemRegistry.getObject(itemName));
		}
	}

	public static IItemStack getItem(String name, int meta)
	{
		//Item item = (Item) Item.itemRegistry.getObject(name);
		Item item = itemNames.get(name);
		if (item != null)
			return getIItemStackWildcardSize(item, meta);
		else
			return null;
	}

	private final IZenSymbol<IJavaExpression, IJavaType> symbolAny;
	private final IJavaMethod method;

	public ItemBracketHandler(IJavaScopeGlobal scope)
	{
		symbolAny = JavaNative.getStaticFieldSymbol(scope, IngredientAny.class, "INSTANCE");
		method = JavaNative.getStaticMethod(
				scope,
				ItemBracketHandler.class,
				"getItem",
				String.class, int.class);
	}

	@Override
	public IJavaExpression resolve(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, List<Token> tokens)
	{
		// any symbol
		if (tokens.size() == 1 && tokens.get(0).getValue().equals("*"))
			return symbolAny.instance(position, scope).eval();

		// detect special cases:
		//   item: at the start means item-specific syntax
		//   :xxx with xxx an integer means sub-item syntax
		//   :* means any-subitem-syntax
		int fromIndex = 0;
		int toIndex = tokens.size();
		int meta = 0;

		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("item") && tokens.get(1).getValue().equals(":"))
				fromIndex = 2;
			if (tokens.get(tokens.size() - 1).getType() == ZenLexer.TOKEN_INTVALUE
					&& tokens.get(tokens.size() - 2).getValue().equals(":")) {
				toIndex = tokens.size() - 2;
				meta = Integer.parseInt(tokens.get(tokens.size() - 1).getValue());
			} else if (tokens.get(tokens.size() - 1).getValue().equals("*")
					&& tokens.get(tokens.size() - 2).getValue().equals(":")) {
				toIndex = tokens.size() - 2;
				meta = OreDictionary.WILDCARD_VALUE;
			}
		}

		return find(position, scope, tokens, fromIndex, toIndex, meta);
	}

	@Override
	public IAny eval(List<Token> tokens)
	{
		return null;
	}

	private IJavaExpression find(
			CodePosition position,
			IMethodScope<IJavaExpression, IJavaType> scope,
			List<Token> tokens,
			int startIndex, 
			int endIndex,
			int meta)
	{
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}

		String itemName = valueBuilder.toString();
		return method.callStaticWithConstants(position, scope, itemName, meta);
	}
}
