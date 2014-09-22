/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.symbols;

import java.lang.reflect.Field;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionJavaStaticField;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class SymbolJavaStaticField implements IZenSymbol {
	private final Field field;
	
	public SymbolJavaStaticField(Field field) {
		this.field = field;
	}
	
	@Override
	public IPartialExpression instance(ZenPosition position, IScopeMethod environment) {
		return new ExpressionJavaStaticField(position, environment, field);
	}
}
