/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionCompareGeneric extends Expression {
	private final Expression value; // should return a compareTo value (<=>0)
	private final CompareType type;
	
	public ExpressionCompareGeneric(CodePosition position, IScopeMethod environment, Expression value, CompareType type) {
		super(position, environment);
		
		this.value = value;
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().BOOL;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		value.compile(result, output);
		
		if (result) {
			Label lblThen = new Label();
			Label lblEnd = new Label();
			
			switch (type) {
				case LT: output.ifLT(lblThen); break;
				case GT: output.ifGT(lblThen); break;
				case LE: output.ifLE(lblThen); break;
				case GE: output.ifGE(lblThen); break;
				case NE: output.ifNE(lblThen); break;
				case EQ: output.ifEQ(lblThen); break;
				default:
					throw new RuntimeException();
			}
			
			output.iConst0();
			output.goTo(lblEnd);
			output.label(lblThen);
			output.iConst1();
			output.label(lblEnd);
		}
	}
}
