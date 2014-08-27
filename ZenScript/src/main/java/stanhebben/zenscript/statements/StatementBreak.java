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
public class StatementBreak extends Statement {
	private final Statement target;
	
	public StatementBreak(ZenPosition position, IScopeMethod scope, Statement target) {
		super(position, scope);
		
		this.target = target;
	}

	@Override
	public void compile(MethodOutput output) {
		ControlLabels labels = output.getControlLabels(target);
		if (labels == null) {
			throw new AssertionError("missing control labels");
		}
		if (labels.breakLabel == null) {
			getScope().error(getPosition(), "cannot break this kind of statement");
		} else {
			output.goTo(labels.breakLabel);
		}
	}
}
