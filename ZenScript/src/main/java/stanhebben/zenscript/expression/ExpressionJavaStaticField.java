/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.lang.reflect.Field;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.type.generic.TypeCapture;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ExpressionJavaStaticField extends Expression {
	private final Field field;
	private final ZenType type;
	
	public ExpressionJavaStaticField(ZenPosition position, IScopeMethod environment, Field field) {
		super(position, environment);
		
		this.field = field;
		type = environment.getTypes().getNativeType(position, field.getGenericType(), TypeCapture.EMPTY);
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			output.getStaticField(field.getDeclaringClass(), field);
		}
	}
}
