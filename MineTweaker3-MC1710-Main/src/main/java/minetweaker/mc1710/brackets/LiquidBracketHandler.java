package minetweaker.mc1710.brackets;

import java.util.List;
import minetweaker.api.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.mc1710.liquid.MCLiquidStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.IJavaScopeGlobal;
import org.openzen.zencode.java.JavaNative;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
@BracketHandler
public class LiquidBracketHandler implements IBracketHandler
{
	public static ILiquidStack getLiquid(String name)
	{
		Fluid fluid = FluidRegistry.getFluid(name);
		if (fluid != null)
			return new MCLiquidStack(new FluidStack(fluid, 1));
		else
			return null;
	}

	private final IJavaMethod method;

	public LiquidBracketHandler(IJavaScopeGlobal scope)
	{
		method = JavaNative.getStaticMethod(scope, LiquidBracketHandler.class, "getLiquid", String.class);
	}

	@Override
	public IJavaExpression resolve(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, List<Token> tokens)
	{
		if (tokens.size() > 2 && tokens.get(0).getValue().equals("liquid") && tokens.get(1).getValue().equals(":"))
				return find(position, scope, tokens, 2, tokens.size());

		return null;
	}

	@Override
	public IAny eval(List<Token> tokens)
	{
		return null;
	}

	private IJavaExpression find(
			CodePosition position,
			IScopeMethod<IJavaExpression, IJavaType> scope,
			List<Token> tokens,
			int startIndex,
			int endIndex)
	{
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}

		Fluid fluid = FluidRegistry.getFluid(valueBuilder.toString());
		if (fluid == null)
			return null;
		
		return method.callStaticWithConstants(position, scope, valueBuilder.toString());
	}
}
