/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.statements;

import org.objectweb.asm.Label;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class StatementDoWhile extends Statement {
	private final Expression condition;
	private Statement contents;
	
	public StatementDoWhile(ZenPosition position, IScopeMethod environment, Expression condition) {
		super(position, environment);
		
		this.condition = condition;
	}
	
	public void setContents(Statement contents) {
		this.contents = contents;
	}

	@Override
	public void compile(MethodOutput output) {
		Label lblRepeat = new Label();
		Label lblContinue = new Label();
		Label lblBreak = new Label();
		output.putControlLabels(this, new MethodOutput.ControlLabels(lblContinue, lblBreak));
		
		output.label(lblRepeat);
		contents.compile(output);
		output.label(lblContinue);
		condition.compileIf(lblRepeat, output);
		output.label(lblBreak);
	}
}
