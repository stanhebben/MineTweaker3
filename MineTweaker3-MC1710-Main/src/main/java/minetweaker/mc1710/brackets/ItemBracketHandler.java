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
import minetweaker.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import static minetweaker.api.minecraft.MineTweakerMC.getIItemStackWildcardSize;
import minetweaker.runtime.symbol.SymbolUtil;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import net.stanhebben.zenscript.lexer.Token;
import net.stanhebben.zenscript.lexer.ZenTokener;
import zenscript.runtime.IAny;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
@BracketHandler(priority=100)
public class ItemBracketHandler implements IBracketHandler {
	private static final Map<String, Item> itemNames;
	
	static {
		itemNames = new HashMap<String, Item>();
		for (String itemName : (Set<String>) Item.itemRegistry.getKeys()) {
			itemNames.put(itemName.replace(" ", ""), (Item) Item.itemRegistry.getObject(itemName));
		}
	}
	
	public static IItemStack getItem(String name, int meta) {
		//Item item = (Item) Item.itemRegistry.getObject(name);
		Item item = itemNames.get(name);
		if (item != null) {
			return getIItemStackWildcardSize(item, meta);
		} else {
			return null;
		}
	}
	
	private final IZenSymbol symbolAny;
	private final IMethod method;
	
	public ItemBracketHandler(IScopeGlobal scope) {
		symbolAny = SymbolUtil.getZenStaticField(scope, IngredientAny.class, "INSTANCE");
		method = SymbolUtil.getZenStaticMethod(
				scope,
				ItemBracketHandler.class,
				"getItem",
				String.class, int.class);
	}

	@Override
	public IZenSymbol resolve(List<Token> tokens) {
		// any symbol
		if (tokens.size() == 1 && tokens.get(0).getValue().equals("*")) {
			return symbolAny;
		}
		
		// detect special cases:
		//   item: at the start means item-specific syntax
		//   :xxx with xxx an integer means sub-item syntax
		//   :* means any-subitem-syntax
		int fromIndex = 0;
		int toIndex = tokens.size();
		int meta = 0;
		
		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("item") && tokens.get(1).getValue().equals(":")) {
				fromIndex = 2;
			}
			if (tokens.get(tokens.size() - 1).getType() == ZenTokener.TOKEN_INTVALUE
					&& tokens.get(tokens.size() - 2).getValue().equals(":")) {
				toIndex = tokens.size() - 2;
				meta = Integer.parseInt(tokens.get(tokens.size() - 1).getValue());
			} else if (tokens.get(tokens.size() - 1).getValue().equals("*")
					&& tokens.get(tokens.size() - 2).getValue().equals(":")) {
				toIndex = tokens.size() - 2;
				meta = OreDictionary.WILDCARD_VALUE;
			}
		}
		
		return find(tokens, fromIndex, toIndex, meta);
	}
	
	@Override
	public IAny eval(List<Token> tokens) {
		return null;
	}
	
	private IZenSymbol find(List<Token> tokens, int startIndex, int endIndex, int meta) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}
		
		String itemName = valueBuilder.toString();
		return new ItemReferenceSymbol(itemName, meta);
	}
	
	private class ItemReferenceSymbol implements IZenSymbol {
		private final String name;
		private final int meta;
		
		public ItemReferenceSymbol(String name, int meta) {
			this.name = name;
			this.meta = meta;
		}
		
		@Override
		public IPartialExpression instance(ZenPosition position, IScopeMethod scope) {
			return new ExpressionCallStatic(
					position,
					scope,
					method,
					new ExpressionString(position, scope, name),
					new ExpressionInt(position, scope, meta, scope.getTypes().INT));
		}
	}
}
