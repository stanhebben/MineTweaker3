/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.statements;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Label;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class StatementSwitch extends Statement {
	private final Expression value;
	private final List<Statement> contents = new ArrayList<Statement>();
	private final List<Expression> caseValues = new ArrayList<Expression>();
	private final List<Integer> caseLabels = new ArrayList<Integer>();
	private int defaultLabel = -1;
	
	public StatementSwitch(ZenPosition position, IScopeMethod scope, Expression value) {
		super(position, scope);
		
		this.value = value;
	}
	
	public ZenType getType() {
		return value.getType();
	}
	
	public void onCase(ZenPosition position, Expression value) {
		if (caseValues.contains(value)) {
			getScope().error(position, "this value already has a case assigned");
		} else {
			caseValues.add(value);
			caseLabels.add(contents.size());
		}
	}
	
	public void onDefault(ZenPosition position) {
		if (defaultLabel >= 0) {
			getScope().error(position, "default case already defined");
		} else {
			defaultLabel = contents.size();
		}
	}
	
	public void onStatement(Statement statement) {
		contents.add(statement);
	}

	@Override
	public void compile(MethodOutput output) {
		Label lblBreak = new Label();
		
		
		// TODO - need precompilation step for the switch values
		blabla
	}
}
