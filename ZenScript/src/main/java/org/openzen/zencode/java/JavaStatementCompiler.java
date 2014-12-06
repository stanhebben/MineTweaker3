/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.iterator.IJavaIterator;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.statement.IStatementProcessor;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementBlock;
import org.openzen.zencode.symbolic.statement.StatementBreak;
import org.openzen.zencode.symbolic.statement.StatementContinue;
import org.openzen.zencode.symbolic.statement.StatementDoWhile;
import org.openzen.zencode.symbolic.statement.StatementExpression;
import org.openzen.zencode.symbolic.statement.StatementForeach;
import org.openzen.zencode.symbolic.statement.StatementIf;
import org.openzen.zencode.symbolic.statement.StatementNull;
import org.openzen.zencode.symbolic.statement.StatementReturn;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.statement.StatementVar;
import org.openzen.zencode.symbolic.statement.StatementWhile;

/**
 *
 * @author Stan
 */
public class JavaStatementCompiler implements IStatementProcessor<IJavaExpression, IJavaType, Object>
{
	private final MethodOutput output;
	
	public JavaStatementCompiler(MethodOutput output)
	{
		this.output = output;
	}
	
	@Override
	public Object onBlock(StatementBlock<IJavaExpression, IJavaType> statement)
	{
		for (Statement<IJavaExpression, IJavaType> element : statement.getStatements()) {
			element.process(this);
			
			if (element.isReturn())
				return null;
		}
		
		return null;
	}

	@Override
	public Object onBreak(StatementBreak<IJavaExpression, IJavaType> statement)
	{
		MethodOutput.ControlLabels labels = output.getControlLabels(statement.getTarget());
		if (labels == null)
			throw new AssertionError("missing control labels");
		if (labels.breakLabel == null)
			statement.getScope().error(statement.getPosition(), "cannot break this kind of statement");
		else
			output.goTo(labels.breakLabel);
		
		return null;
	}

	@Override
	public Object onContinue(StatementContinue<IJavaExpression, IJavaType> statement)
	{
		MethodOutput.ControlLabels controls = output.getControlLabels(statement.getTarget());
		if (controls == null)
			throw new AssertionError("control labels missing");
		
		if (controls.continueLabel == null) {
			statement.getScope().error(
					statement.getPosition(),
					"cannot continue this kind of statement");
		} else {
			output.goTo(controls.continueLabel);
		}
		
		return null;
	}

	@Override
	public Object onDoWhile(StatementDoWhile<IJavaExpression, IJavaType> statement)
	{
		Label lblRepeat = new Label();
		Label lblContinue = new Label();
		Label lblBreak = new Label();
		output.putControlLabels(statement, new MethodOutput.ControlLabels(lblContinue, lblBreak));
		
		output.label(lblRepeat);
		statement.getContents().process(this);
		output.label(lblContinue);
		statement.getCondition().compileIf(lblRepeat, output);
		output.label(lblBreak);
		
		return null;
	}

	@Override
	public Object onExpression(StatementExpression<IJavaExpression, IJavaType> statement)
	{
		output.position(statement.getPosition());
		statement.getExpression().compile(false, output);
		
		return null;
	}

	@Override
	public Object onForeach(StatementForeach<IJavaExpression, IJavaType> statement)
	{
		output.position(statement.getPosition());
		
		int[] localVariables = new int[statement.getVariables().size()];
		for (int i = 0; i < localVariables.length; i++) {
			localVariables[i] = output.getLocal(statement.getVariables().get(i));
		}
		
		IJavaIterator iterator = statement.getList().getType().getIterator(statement.getVariables().size());
		
		statement.getList().compile(true, output);
		iterator.compileStart(output, localVariables);
		
		Label lblRepeat = new Label();
		Label lblBreak = new Label();
		output.putControlLabels(statement, new MethodOutput.ControlLabels(lblRepeat, lblBreak));
		
		output.label(lblRepeat);
		iterator.compilePreIterate(output, localVariables, lblBreak);
		statement.getBody().process(this);
		iterator.compilePostIterate(output, localVariables, lblBreak, lblRepeat);
		output.label(lblBreak);
		iterator.compileEnd(output);
		
		return null;
	}

	@Override
	public Object onIf(StatementIf<IJavaExpression, IJavaType> statement)
	{
		output.position(statement.getPosition());
		
		IJavaType expressionType = statement.getCondition().getType();
		if (expressionType != statement.getScope().getTypes().getBool())
			throw new RuntimeException("condition is not a boolean");
		
		Label labelEnd = new Label();
		Label labelElse = statement.getElse() == null ? labelEnd : new Label();

		statement.getCondition().compileElse(labelElse, output);
		statement.getThen().process(this);
		
		if (statement.getElse() != null) {
			output.goTo(labelEnd);
			output.label(labelElse);
			statement.getElse().process(this);
		}
		
		output.label(labelEnd);
		
		return null;
	}

	@Override
	public Object onEmpty(StatementNull<IJavaExpression, IJavaType> statement)
	{
		// nothing to do
		return null;
	}

	@Override
	public Object onReturn(StatementReturn<IJavaExpression, IJavaType> statement)
	{
		output.position(statement.getPosition());

		if (statement.getExpression() == null)
			output.ret();
		else {
			statement.getExpression().compile(true, output);

			Type asmReturnType = statement.getExpression().getType().toASMType();
			output.returnType(asmReturnType);
		}
		
		return null;
	}

	@Override
	public Object onSwitch(StatementSwitch<IJavaExpression, IJavaType> statement)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object onVar(StatementVar<IJavaExpression, IJavaType> statement)
	{
		output.position(statement.getPosition());

		if (statement.getInitializer() != null) {
			statement.getInitializer().compile(true, output);
			output.store(statement.getSymbol().getType().toASMType(), output.getLocal(statement.getSymbol()));
		}
		
		return null;
	}

	@Override
	public Object onWhile(StatementWhile<IJavaExpression, IJavaType> statement)
	{
		Label lblRepeat = new Label();
		Label lblBreak = new Label();

		output.label(lblRepeat);
		statement.getCondition().compileElse(lblBreak, output);
		statement.getContents().process(this);
		output.goTo(lblRepeat);
		output.label(lblBreak);
		
		return null;
	}
}
