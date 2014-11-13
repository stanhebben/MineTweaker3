package minetweaker.mods.ic2;

import ic2.api.item.IC2Items;
import java.util.List;
import minetweaker.annotations.BracketHandler;
import minetweaker.annotations.ModOnly;
import minetweaker.api.IBracketHandler;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import static minetweaker.api.minecraft.MineTweakerMC.getIItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.openzen.zencode.java.JavaNative;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 * Makes a ic2-item specific item syntax available in the form
 * &lt;item-ic2:name[:meta]&gt;.
 *
 * @author Stan Hebben
 */
@BracketHandler
@ModOnly("IC2")
public class IC2BracketHandler implements IBracketHandler
{
	public static IItemStack getItem(String name, int meta)
	{
		ItemStack stack = IC2Items.getItem(name);
		stack.setItemDamage(meta);
		return getIItemStack(stack);
	}

	private final IMethod method;

	public IC2BracketHandler(IScopeGlobal global)
	{
		method = JavaNative.getStaticMethod(global, IC2BracketHandler.class, "getItem", String.class, int.class);
	}

	@Override
	public IZenSymbol resolve(List<Token> tokens)
	{
		if (tokens.size() >= 5)
			if (tokens.get(0).getValue().equals("ic2")
					&& tokens.get(1).getValue().equals("-")
					&& tokens.get(2).getValue().equals("item")
					&& tokens.get(3).getValue().equals(":")) {
				String name = tokens.get(4).getValue();
				int meta = 0;
				if (tokens.size() > 6 && tokens.get(5).getValue().equals(":"))
					if (tokens.get(6).getType() == ZenLexer.TOKEN_INTVALUE)
						meta = Integer.parseInt(tokens.get(6).getValue());
					else if (tokens.get(6).getValue().equals("*"))
						meta = OreDictionary.WILDCARD_VALUE;
					else
						MineTweakerAPI.logError("Not a valid meta value: " + tokens.get(6).getValue());
				ItemStack item = IC2Items.getItem(name);
				if (item == null) {
					MineTweakerAPI.logError("Not a valid IC2 item: " + name);
					return null;
				} else
					return new ItemReferenceSymbol(name, meta);
			} else
				return null;
		else
			return null;
	}

	@Override
	public IAny eval(List<Token> tokens)
	{
		return null;
	}

	// #############################
	// ### Private inner classes ###
	// #############################
	private class ItemReferenceSymbol implements IZenSymbol
	{
		private final String name;
		private final int meta;

		public ItemReferenceSymbol(String name, int meta)
		{
			this.name = name;
			this.meta = meta;
		}

		@Override
		public IPartialExpression instance(CodePosition position, IScopeMethod scope)
		{
			return method.call(position, scope, name, meta);
		}
	}
}
