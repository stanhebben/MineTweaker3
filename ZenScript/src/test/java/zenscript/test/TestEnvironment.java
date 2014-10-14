/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.ClassNameGenerator;
import stanhebben.zenscript.compiler.EnvironmentGlobal;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class TestEnvironment implements IZenCompileEnvironment
{
	public static IScopeGlobal createScope() {
		return new EnvironmentGlobal(new TestEnvironment(), new HashMap<String, byte[]>(), new ClassNameGenerator());
	}
	
	private final Queue<LogMessage> logs = new LinkedList<LogMessage>();
	private final IZenErrorLogger errorLogger = new MyErrorLogger();
	
	public void consumeError(String message)
	{
		consume(LogMessageType.ERROR, message);
	}
	
	public void consumeWarning(String message)
	{
		consume(LogMessageType.WARNING, message);
	}
	
	private void consume(LogMessageType type, String message)
	{
		assertFalse("missing log message", logs.isEmpty());
		LogMessage firstLogMessage = logs.poll();
		assertEquals("wrong message type", type, firstLogMessage.type);
		assertEquals("wrong message value", message);
	}
	
	@Override
	public IZenErrorLogger getErrorLogger()
	{
		return errorLogger;
	}

	@Override
	public IZenSymbol getGlobal(String name)
	{
		return null;
	}

	@Override
	public IZenSymbol getDollar(String name)
	{
		return null;
	}

	@Override
	public IZenSymbol getBracketed(IScopeGlobal environment, List<Token> tokens)
	{
		return null;
	}

	@Override
	public IAny evalGlobal(String name)
	{
		return null;
	}

	@Override
	public IAny evalDollar(String name)
	{
		return null;
	}

	@Override
	public IAny evalBracketed(List<Token> tokens)
	{
		return null;
	}
	
	private class MyErrorLogger implements IZenErrorLogger
	{
		@Override
		public void error(ZenPosition position, String message)
		{
			logs.add(new LogMessage(LogMessageType.ERROR, message));
		}

		@Override
		public void warning(ZenPosition position, String message)
		{
			logs.add(new LogMessage(LogMessageType.WARNING, message));
		}
	}
	
	private static enum LogMessageType
	{
		ERROR,
		WARNING
	}
	
	private static class LogMessage
	{
		private final LogMessageType type;
		private final String message;
		
		private LogMessage(LogMessageType type, String message)
		{
			this.type = type;
			this.message = message;
		}
	}
}
