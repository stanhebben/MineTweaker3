package stanhebben.zenscript.statements;

import java.util.List;
import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.IZenIterator;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.symbols.SymbolLocal;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class StatementForeach extends Statement {
	private final List<SymbolLocal> variables;
	private final Expression list;
	private final IZenIterator iterator;
	private Statement body;
	
	public StatementForeach(CodePosition position, IScopeMethod environment, List<SymbolLocal> variables, Expression list, IZenIterator iterator) {
		super(position, environment);
		
		this.variables = variables;
		this.list = list;
		this.iterator = iterator;
	}
	
	public void setBody(Statement body) {
		this.body = body;
	}

	@Override
	public void compile(MethodOutput output) {
		output.position(getPosition());
		
		int[] localVariables = new int[variables.size()];
		for (int i = 0; i < localVariables.length; i++) {
			localVariables[i] = output.getLocal(variables.get(i));
		}
		
		list.compile(true, output);
		iterator.compileStart(output, localVariables);
		
		Label lblRepeat = new Label();
		Label lblBreak = new Label();
		output.putControlLabels(this, new MethodOutput.ControlLabels(lblRepeat, lblBreak));
		
		output.label(lblRepeat);
		iterator.compilePreIterate(output, localVariables, lblBreak);
		body.compile(output);
		iterator.compilePostIterate(output, localVariables, lblBreak, lblRepeat);
		output.label(lblBreak);
		iterator.compileEnd(output);
	}
}
