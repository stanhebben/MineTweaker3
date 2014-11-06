/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.statements;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class StatementNull extends Statement {
	public StatementNull(CodePosition position, IScopeMethod environment) {
		super(position, environment);
	}

	@Override
	public void compile(MethodOutput output) {
		
	}
}
