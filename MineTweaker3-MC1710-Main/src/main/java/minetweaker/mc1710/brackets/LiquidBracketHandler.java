package minetweaker.mc1710.brackets;

import java.util.List;
import minetweaker.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.mc1710.liquid.MCLiquidStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.runtime.symbol.SymbolUtil;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import net.stanhebben.zenscript.lexer.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
@BracketHandler
public class LiquidBracketHandler implements IBracketHandler {
	public static ILiquidStack getLiquid(String name) {
		Fluid fluid = FluidRegistry.getFluid(name);
		if (fluid != null) {
			return new MCLiquidStack(new FluidStack(fluid, 1));
		} else {
			return null;
		}
	}
	
	private final IMethod method;
	
	public LiquidBracketHandler(IScopeGlobal scope) {
		method = SymbolUtil.getZenStaticMethod(scope, LiquidBracketHandler.class, "getLiquid", String.class);
	}

	@Override
	public IZenSymbol resolve(List<Token> tokens) {
		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("liquid") && tokens.get(1).getValue().equals(":")) {
				return find(tokens, 2, tokens.size());
			}
		}
		
		return null;
	}
	
	@Override
	public IAny eval(List<Token> tokens) {
		return null;
	}
	
	private IZenSymbol find(List<Token> tokens, int startIndex, int endIndex) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}
		
		Fluid fluid = FluidRegistry.getFluid(valueBuilder.toString());
		if (fluid != null) {
			return new LiquidReferenceSymbol(valueBuilder.toString());
		}
		
		return null;
	}
	
	private class LiquidReferenceSymbol implements IZenSymbol {
		private final String name;
		
		public LiquidReferenceSymbol(String name) {
			this.name = name;
		}
		
		@Override
		public IPartialExpression instance(ZenPosition position, IScopeMethod scope) {
			return new ExpressionCallStatic(
					position,
					scope,
					method,
					new ExpressionString(position, scope, name));
		}
	}
}
