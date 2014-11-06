package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

public class ExpressionArithmeticCompare extends Expression {
	private final Expression a;
	private final Expression b;
	private final CompareType type;
	
	public ExpressionArithmeticCompare(CodePosition position, IScopeMethod method, CompareType type, Expression a, Expression b) {
		super(position, method);
		
		this.a = a;
		this.b = b;
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().BOOL;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		a.compile(result, output);
		b.compile(result, output);
		
		if (result) {
			if (a.getType() == getScope().getTypes().BOOL) {
				if (type == CompareType.EQ) {
					Label onThen = new Label();
					Label onEnd = new Label();
					
					output.ifICmpEQ(onThen);
					output.iConst0();
					output.goTo(onEnd);
					output.label(onThen);
					output.iConst1();
					output.label(onEnd);
				} else if (type == CompareType.NE) {
					Label onThen = new Label();
					Label onEnd = new Label();
					
					output.ifICmpNE(onThen);
					output.iConst0();
					output.goTo(onEnd);
					output.label(onThen);
					output.iConst1();
					output.label(onEnd);
				} else {
					getScope().error(getPosition(), "this kind of comparison is not supported on bool values");
				}
			} else {
				Label onThen = new Label();
				Label onEnd = new Label();
				
				TypeRegistry types = getScope().getTypes();
				if (a.getType() == types.LONG) {
					output.lCmp();
				} else if (a.getType() == types.FLOAT) {
					output.fCmp();
				} else if (a.getType() == types.DOUBLE) {
					output.dCmp();
				} else if (a.getType() == types.BYTE
						|| a.getType() == types.SHORT
						|| a.getType() == types.INT) {
					// nothing to do
				} else {
					throw new RuntimeException("Unsupported type for arithmetic compare");
				}
				
				switch (type) {
					case EQ: output.ifICmpEQ(onThen); break;
					case NE: output.ifICmpNE(onThen); break;
					case LE: output.ifICmpLE(onThen); break;
					case GE: output.ifICmpGE(onThen); break;
					case LT: output.ifICmpLT(onThen); break;
					case GT: output.ifICmpGT(onThen); break;
					default:
						getScope().error(getPosition(), "this kind of comparison is not supported on int values");
						return;
				}
				
				output.iConst0();
				output.goTo(onEnd);
				output.label(onThen);
				output.iConst1();
				output.label(onEnd);
			}
		}
	}
}
