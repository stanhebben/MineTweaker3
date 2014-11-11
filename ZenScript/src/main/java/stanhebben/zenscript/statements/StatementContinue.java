/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.statements;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.MethodOutput.ControlLabels;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class StatementContinue extends Statement {
	private final Statement target;
	
	public StatementContinue(CodePosition position, IScopeMethod scope, Statement target) {
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
