package stanhebben.zenscript.expression;

import org.objectweb.asm.Type;
import stanhebben.zenscript.compiler.IScopeMethod;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import zenscript.symbolic.type.casting.ICastingRule;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

public class ExpressionArray extends Expression {
	private final Expression[] contents;
	private final ZenTypeArrayBasic type;
	
	public ExpressionArray(ZenPosition position, IScopeMethod environment, ZenTypeArrayBasic type, Expression... contents) {
		super(position, environment);
		
		this.contents = contents;
		this.type = type;
	}

	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		if (this.type.equals(type)) {
			return this;
		}
		
		if (type instanceof ZenTypeArrayBasic) {
			ZenTypeArrayBasic arrayType = (ZenTypeArrayBasic) type;
			Expression[] newContents = new Expression[contents.length];
			for (int i = 0; i < contents.length; i++) {
				newContents[i] = contents[i].cast(position, arrayType.getBaseType());
			}
			
			return new ExpressionArray(getPosition(), getEnvironment(), arrayType, newContents);
		} else {
			ICastingRule castingRule = this.type.getCastingRule(type);
			if (castingRule == null) {
				getEnvironment().error(position, "cannot cast " + this.type + " to " + type);
				return new ExpressionInvalid(position, getEnvironment(), type);
			} else {
				return new ExpressionAs(position, getEnvironment(), this, castingRule);
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
