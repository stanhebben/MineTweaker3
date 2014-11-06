package stanhebben.zenscript.expression;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;

public class ExpressionMap extends Expression {
	private final Expression[] keys;
	private final Expression[] values;
	private final ZenTypeAssociative type;
	
	public ExpressionMap(CodePosition position, IScopeMethod environment, Expression[] keys, Expression[] values, ZenTypeAssociative type) {
		super(position, environment);
		
		this.keys = keys;
		this.values = values;
		
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			ZenType keyType = type.getKeyType();
			ZenType valueType = type.getValueType();
			
			output.newObject(HashMap.class);
			output.dup();
			output.invokeSpecial(internal(HashMap.class), "<init>", "()V");
			
			for (int i = 0; i < keys.length; i++) {
				output.dup();
				keys[i].cast(getPosition(), keyType).compile(true, output);
				values[i].cast(getPosition(), valueType).compile(true, output);
				output.invokeInterface(internal(Map.class), "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
				output.pop();
			}
		} else {
			for (Expression key : keys) {
				key.compile(false, output);
			}
			for (Expression value : values) {
				value.compile(false, output);
			}
		}
	}
	
	@Override
	public Expression cast(CodePosition position, ZenType type) {
		if (this.type.equals(type)) {
			return this;
		}
		
		if (type instanceof ZenTypeAssociative) {
			ZenTypeAssociative associativeType = (ZenTypeAssociative) type;
			Expression[] newKeys = new Expression[keys.length];
			Expression[] newValues = new Expression[values.length];
			
			for (int i = 0; i < keys.length; i++) {
				newKeys[i] = keys[i].cast(position, associativeType.getKeyType());
				newValues[i] = values[i].cast(position, associativeType.getValueType());
			}
			
			return new ExpressionMap(getPosition(), getScope(), newKeys, newValues, associativeType);
		} else {
			ICastingRule castingRule = this.type.getCastingRule(getScope().getAccessScope(), type);
			if (castingRule == null) {
				getScope().error(position, "cannot cast " + this.type + " to " + type);
				return new ExpressionInvalid(position, getScope(), type);
			} else {
				return new ExpressionAs(position, getScope(), this, castingRule);
			}
		}
	}
}
