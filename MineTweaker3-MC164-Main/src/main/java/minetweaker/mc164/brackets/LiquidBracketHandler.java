package minetweaker.mc164.brackets;

import java.util.List;
import minetweaker.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.mc164.liquid.MCLiquidStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.runtime.GlobalRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.lexer.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethod;
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

	@Override
	public IZenSymbol resolve(IScopeGlobal environment, List<Token> tokens) {
		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("liquid") && tokens.get(1).getValue().equals(":")) {
				return find(environment, tokens, 2, tokens.size());
			}
		}
		
		return null;
	}
	
	private IZenSymbol find(IScopeGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}
		
		Fluid fluid = FluidRegistry.getFluid(valueBuilder.toString());
		if (fluid != null) {
			return new LiquidReferenceSymbol(environment, valueBuilder.toString());
		}
		
		return null;
	}
	
	private class LiquidReferenceSymbol implements IZenSymbol {
		private final IScopeGlobal environment;
		private final String name;
		
		public LiquidReferenceSymbol(IScopeGlobal environment, String name) {
			this.environment = environment;
			this.name = name;
		}
		
		@Override
		public IPartialExpression instance(ZenPosition position) {
			IJavaMethod method = JavaMethod.get(
					GlobalRegistry.getTypeRegistry(),
					LiquidBracketHandler.class,
					"getLiquid",
					String.class);
			
			return new ExpressionCallStatic(
					position,
					environment,
					method,
					new ExpressionString(position, name));
		}
	}
}
