/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.statements;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.util.MethodOutput;
import stanhebben.zenscript.util.MethodOutput.ControlLabels;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class StatementContinue extends Statement {
	private final Statement target;
	
	public StatementContinue(ZenPosition position, IScopeMethod scope, Statement target) {
		super(position, scope);
		
		this.target = target;
	}

	@Override
	public void compile(MethodOutput output) {
		ControlLabels controls = output.getControlLabels(target);
		if (controls == null) {
			throw new AssertionError("control labels missing");
		}
		if (controls.continueLabel == null) {
			getScope().error(getPosition(), "cannot continue this kind of statement");
		} else {
			output.goTo(controls.continueLabel);
		}
	}
}
