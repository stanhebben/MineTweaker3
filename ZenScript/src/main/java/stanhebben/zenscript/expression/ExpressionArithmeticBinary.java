/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionArithmeticBinary extends Expression {
	private final OperatorType operator;
	private final Expression a;
	private final Expression b;
	
	public ExpressionArithmeticBinary(
			ZenPosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression a,
			Expression b) {
		super(position, environment);
		
		this.operator = operator;
		this.a = a;
		this.b = b;
	}

	@Override
	public ZenType getType() {
		return a.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			TypeRegistry types = getEnvironment().getTypes();
			
			a.compile(result, output);
			b.compile(result, output);
			
			ZenType type = a.getType();
			
			if (type == types.BOOL) {
				switch (operator) {
					case AND:
						output.iAnd();
						break;
					case OR:
						output.iOr();
						break;
					case XOR:
						output.iXor();
						break;
					default:
						throw new RuntimeException("Unsupported operator on " + type + ": " + operator);
				}
			} else if (type == types.BYTE || type == types.SHORT || type == types.INT) {
				switch (operator) {
					case ADD:
						output.iAdd();
						break;
					case SUB:
						output.iSub();
						break;
					case MUL:
						output.iMul();
						break;
					case DIV:
						output.iDiv();
						break;
					case MOD:
						output.iRem();
						break;
					case AND:
						output.iAnd();
						break;
					case OR:
						output.iOr();
						break;
					case XOR:
						output.iXor();
						break;
					default:
						throw new RuntimeException("Unsupported operator on " + type + ": " + operator);
				}
			} else if (type == types.LONG) {
				switch (operator) {
					case ADD:
						output.lAdd();
						break;
					case SUB:
						output.lSub();
						break;
					case MUL:
						output.lMul();
						break;
					case DIV:
						output.lDiv();
						break;
					case MOD:
						output.lRem();
						break;
					case AND:
						output.lAnd();
						break;
					case OR:
						output.lOr();
						break;
					case XOR:
						output.lXor();
						break;
					default:
						throw new RuntimeException("Unsupported operator on " + type + ": " + operator);
				}
			} else if (type == types.FLOAT) {
				switch (operator) {
					case ADD:
						output.fAdd();
						break;
					case SUB:
						output.fSub();
						break;
					case MUL:
						output.fMul();
						break;
					case DIV:
						output.fDiv();
						break;
					case MOD:
						output.fRem();
						break;
					default:
						throw new RuntimeException("Unsupported operator on " + type + ": " + operator);
				}
			} else if (type == types.DOUBLE) {
				switch (operator) {
					case ADD:
						output.dAdd();
						break;
					case SUB:
						output.dSub();
						break;
					case MUL:
						output.dMul();
						break;
					case DIV:
						output.dDiv();
						break;
					case MOD:
						output.dRem();
						break;
					default:
						throw new RuntimeException("Unsupported operator on " + type + ": " + operator);
				}
			} else {
				throw new RuntimeException("Internal compilation error: " + type + " is not a supported arithmetic type");
			}
		} else {
			a.compile(result, output);
			b.compile(result, output);
		}
	}
}
