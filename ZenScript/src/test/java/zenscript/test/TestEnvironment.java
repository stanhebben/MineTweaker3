/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static org.junit.Assert.*;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.util.ClassNameGenerator;
import org.openzen.zencode.symbolic.scope.ScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.java.method.JavaMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TestEnvironment implements IZenCompileEnvironment
{
	public static final TestEnvironment INSTANCE = new TestEnvironment();
	
	public static void print(String message)
	{
		INSTANCE.logs.add(new LogMessage(LogMessageType.PRINT, message));
	}
	
	public static IScopeGlobal createScope() {
		IScopeGlobal result = new ScopeGlobal(INSTANCE, new ClassNameGenerator());
		result.putValue("print", new SymbolStaticMethod(JavaMethod.get(result.getTypes(), TestEnvironment.class, "print", String.class)), null);
		return result;
	}
	
	private final Queue<LogMessage> logs = new LinkedList<LogMessage>();
	private final ICodeErrorLogger errorLogger = new MyErrorLogger();
	
	private TestEnvironment() {}
	
	public void consumeError(String message)
	{
		consume(LogMessageType.ERROR, message);
	}
	
	public void consumeWarning(String message)
	{
		consume(LogMessageType.WARNING, message);
	}
	
	public void consumePrint(String message)
	{
		consume(LogMessageType.PRINT, message);
	}
	
	public void noMoreMessages()
	{
		if (!logs.isEmpty()) {
			System.out.println("Remaining log messages:");
			for (LogMessage logMessage : logs) {
				System.out.println(logMessage.type + ": " + logMessage.message);
			}
		}
		
		assertTrue("unexpected log messages", logs.isEmpty());
	}
	
	private void consume(LogMessageType type, String message)
	{
		assertFalse("missing log message", logs.isEmpty());
		LogMessage firstLogMessage = logs.poll();
		
		if (type != firstLogMessage.type) {
			System.out.println(firstLogMessage.type + ": " + firstLogMessage.message);
		}
		
		assertEquals("wrong message type", type, firstLogMessage.type);
		assertEquals("wrong message value", message, firstLogMessage.message);
	}
	
	@Override
	public ICodeErrorLogger getErrorLogger()
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
	
	private class MyErrorLogger implements ICodeErrorLogger
	{
		private boolean hasErrors = false;
		
		@Override
		public boolean hasErrors()
		{
			return hasErrors;
		}
		
		@Override
		public void error(CodePosition position, String message)
		{
			hasErrors = true;
			logs.add(new LogMessage(LogMessageType.ERROR, message));
		}

		@Override
		public void warning(CodePosition position, String message)
		{
			logs.add(new LogMessage(LogMessageType.WARNING, message));
		}
	}
	
	private static enum LogMessageType
	{
		ERROR,
		WARNING,
		PRINT
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
