/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class StatementSwitch<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final E value;
	private final List<Statement<E, T>> contents = new ArrayList<Statement<E, T>>();
	private final List<E> caseValues = new ArrayList<E>();
	private final List<Integer> caseLabels = new ArrayList<Integer>();
	private int defaultLabel = -1;

	public StatementSwitch(CodePosition position, IScopeMethod<E, T> scope, E value)
	{
		super(position, scope);

		this.value = value;
	}

	public T getType()
	{
		return value.getType();
	}

	public void onCase(CodePosition position, E value)
	{
		if (caseValues.contains(value))
			getScope().error(position, "this value already has a case assigned");
		else {
			caseValues.add(value);
			caseLabels.add(contents.size());
		}
	}

	public void onDefault(CodePosition position)
	{
		if (defaultLabel >= 0)
			getScope().error(position, "default case already defined");
		else
			defaultLabel = contents.size();
	}

	public void onStatement(Statement<E, T> statement)
	{
		contents.add(statement);
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onSwitch(this);
	}
}
