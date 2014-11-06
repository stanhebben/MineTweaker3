package stanhebben.zenscript.expression;

import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeMethod;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class ExpressionArray extends Expression {
	private final Expression[] contents;
	private final ZenTypeArrayBasic type;
	
	public ExpressionArray(CodePosition position, IScopeMethod environment, ZenTypeArrayBasic type, Expression... contents) {
		super(position, environment);
		
		this.contents = contents;
		this.type = type;
	}

	@Override
	public Expression cast(CodePosition position, ZenType type) {
		if (this.type.equals(type)) {
			return this;
		}
		
		if (type instanceof ZenTypeArrayBasic) {
			ZenTypeArrayBasic arrayType = (ZenTypeArrayBasic) type;
			Expression[] newContents = new Expression[contents.length];
			for (int i = 0; i < contents.length; i++) {
				newContents[i] = contents[i].cast(position, arrayType.getBaseType());
			}
			
			return new ExpressionArray(getPosition(), getScope(), arrayType, newContents);
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

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		ZenType baseType = type.getBaseType();
		Type asmBaseType = type.getBaseType().toASMType();
		
		output.constant(contents.length);
		output.newArray(asmBaseType);
		
		for (int i = 0; i < contents.length; i++) {
			output.dup();
			output.constant(i);
			contents[i].cast(this.getPosition(), baseType).compile(result, output);
			output.arrayStore(asmBaseType);
		}
	}
}
