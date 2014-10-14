package zenscript.lexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import zenscript.util.ZenPosition;

/**
 * A tokener is capable of splitting a single file into tokens. It's intended
 * for use by LL(*) parsers.
 *
 * @author Stan Hebben
 */
public class ZenTokener extends TokenStream
{

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final HashMap<String, Integer> KEYWORDS;

	public static final int TOKEN_ID = 1;
	public static final int TOKEN_INTVALUE = 2;
	public static final int T_FLOATVALUE = 3;
	public static final int T_STRINGVALUE = 4;
	public static final int T_AOPEN = 5;
	public static final int T_ACLOSE = 6;
	public static final int T_SQBROPEN = 7;
	public static final int T_SQBRCLOSE = 8;
	public static final int T_DOT3 = 44;
	public static final int T_DOT2 = 9;
	public static final int T_DOT = 10;
	public static final int T_COMMA = 11;
	public static final int T_PLUSASSIGN = 12;
	public static final int T_PLUS = 13;
	public static final int T_MINUSASSIGN = 14;
	public static final int T_MINUS = 15;
	public static final int T_MULASSIGN = 16;
	public static final int T_MUL = 17;
	public static final int T_DIVASSIGN = 18;
	public static final int T_DIV = 19;
	public static final int T_MODASSIGN = 20;
	public static final int T_MOD = 21;
	public static final int T_ORASSIGN = 22;
	public static final int T_OR = 23;
	public static final int T_OR2 = 40;
	public static final int T_ANDASSIGN = 24;
	public static final int T_AND2 = 41;
	public static final int T_AND = 25;
	public static final int T_XORASSIGN = 26;
	public static final int T_XOR = 27;
	public static final int T_QUEST = 28;
	public static final int T_COLON = 29;
	public static final int T_BROPEN = 30;
	public static final int T_BRCLOSE = 31;
	public static final int T_TILDEASSIGN = 45;
	public static final int T_TILDE = 32;
	public static final int T_SEMICOLON = 33;
	public static final int T_LTEQ = 34;
	public static final int T_LT = 35;
	public static final int T_GTEQ = 36;
	public static final int T_GT = 37;
	public static final int T_EQ = 38;
	public static final int T_ASSIGN = 39;
	public static final int T_NOTEQ = 42;
	public static final int T_NOT = 43;
	public static final int T_DOLLAR = 45;

	public static final int T_ANY = 99;
	public static final int T_BOOL = 100;
	public static final int T_BYTE = 101;
	public static final int T_SHORT = 102;
	public static final int T_INT = 103;
	public static final int T_LONG = 104;
	public static final int T_FLOAT = 105;
	public static final int T_DOUBLE = 106;
	public static final int T_STRING = 107;
	public static final int T_FUNCTION = 108;
	public static final int T_IN = 109;
	public static final int T_VOID = 110;

	public static final int T_AS = 120;
	public static final int T_VERSION = 121;
	public static final int T_IF = 122;
	public static final int T_ELSE = 123;
	public static final int T_FOR = 124;
	public static final int T_RETURN = 125;
	public static final int T_VAR = 126;
	public static final int T_VAL = 127;
	public static final int T_SWITCH = 128;
	public static final int T_CASE = 129;
	public static final int T_DEFAULT = 130;
	public static final int T_TRY = 131;
	public static final int T_CATCH = 132;
	public static final int T_FINALLY = 133;
	public static final int T_CONTINUE = 134;
	public static final int T_BREAK = 135;
	public static final int T_NEW = 136;
	public static final int T_WHILE = 137;
	public static final int T_DO = 138;

	public static final int T_NULL = 140;
	public static final int T_TRUE = 141;
	public static final int T_FALSE = 142;

	public static final int T_IMPORT = 160;
	public static final int T_INCLUDE = 161;
	public static final int T_PACKAGE = 162;

	public static final int T_INTERFACE = 170;
	public static final int T_CLASS = 171;
	public static final int T_STRUCT = 172;
	public static final int T_ENUM = 173;
	public static final int T_THIS = 174;
	public static final int T_SUPER = 175;
	public static final int T_EXPAND = 176;
	public static final int T_IMPLEMENTS = 177;

	public static final int T_PRIVATE = 180;
	public static final int T_PUBLIC = 181;
	public static final int T_EXPORT = 182;
	public static final int T_GET = 183;
	public static final int T_SET = 184;
	public static final int T_FINAL = 185;

	private static final String[] REGEXPS = {
		"#[^\n]*[\n\\e]",
		"//[^\n]*[\n\\e]",
		"/\\*[^\\*]*(\\*[^/]*)*\\*/",
		"[ \t\r\n]*",
		"[a-zA-Z_][a-zA-Z_0-9]*",
		"\\-?(0|[1-9][0-9]*)\\.[0-9]+([eE][\\+\\-]?[0-9]+)?",
		"\\-?(0|[1-9][0-9]*)",
		"\"([^\"\\\\]|\\\\([\'\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))*\"",
		"\'([^\'\\\\]|\\\\([\'\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))*\'",
		"\\{",
		"\\}",
		"\\[",
		"\\]",
		"\\.\\.",
		"\\.",
		",",
		"\\+=",
		"\\+",
		"\\-=",
		"\\-",
		"\\*=",
		"\\*",
		"/=",
		"/",
		"%=",
		"%",
		"\\|=",
		"\\|\\|",
		"\\|",
		"&=",
		"&&",
		"&",
		"\\^=",
		"\\^",
		"\\?",
		":",
		"\\(",
		"\\)",
		"~=",
		"~",
		";",
		"<=",
		"<",
		">=",
		">",
		"==",
		"=",
		"!=",
		"!",
		"$"
	};
	private static final int[] FINALS = {
		-1,
		-1,
		-1,
		-1,
		TOKEN_ID,
		T_FLOATVALUE,
		TOKEN_INTVALUE,
		T_STRINGVALUE,
		T_STRINGVALUE,
		T_AOPEN,
		T_ACLOSE,
		T_SQBROPEN,
		T_SQBRCLOSE,
		T_DOT2,
		T_DOT,
		T_COMMA,
		T_PLUSASSIGN,
		T_PLUS,
		T_MINUSASSIGN,
		T_MINUS,
		T_MULASSIGN,
		T_MUL,
		T_DIVASSIGN,
		T_DIV,
		T_MODASSIGN,
		T_MOD,
		T_ORASSIGN,
		T_OR2,
		T_OR,
		T_ANDASSIGN,
		T_AND2,
		T_AND,
		T_XORASSIGN,
		T_XOR,
		T_QUEST,
		T_COLON,
		T_BROPEN,
		T_BRCLOSE,
		T_TILDEASSIGN,
		T_TILDE,
		T_SEMICOLON,
		T_LTEQ,
		T_LT,
		T_GTEQ,
		T_GT,
		T_EQ,
		T_ASSIGN,
		T_NOTEQ,
		T_NOT,
		T_DOLLAR
	};
	private static final CompiledDFA DFA = new NFA(REGEXPS, FINALS).toDFA().optimize().compile();

	static {
		KEYWORDS = new HashMap<String, Integer>();
		KEYWORDS.put("any", T_ANY);
		KEYWORDS.put("bool", T_BOOL);
		KEYWORDS.put("byte", T_BYTE);
		KEYWORDS.put("short", T_SHORT);
		KEYWORDS.put("int", T_INT);
		KEYWORDS.put("long", T_LONG);
		KEYWORDS.put("float", T_FLOAT);
		KEYWORDS.put("double", T_DOUBLE);
		KEYWORDS.put("string", T_STRING);
		KEYWORDS.put("function", T_FUNCTION);
		KEYWORDS.put("in", T_IN);
		KEYWORDS.put("void", T_VOID);

		KEYWORDS.put("as", T_AS);
		KEYWORDS.put("version", T_VERSION);
		KEYWORDS.put("if", T_IF);
		KEYWORDS.put("else", T_ELSE);
		KEYWORDS.put("for", T_FOR);
		KEYWORDS.put("return", T_RETURN);
		KEYWORDS.put("var", T_VAR);
		KEYWORDS.put("val", T_VAL);
		KEYWORDS.put("try", T_TRY);
		KEYWORDS.put("catch", T_CATCH);
		KEYWORDS.put("finally", T_FINALLY);
		KEYWORDS.put("continue", T_CONTINUE);
		KEYWORDS.put("break", T_BREAK);

		KEYWORDS.put("null", T_NULL);
		KEYWORDS.put("true", T_TRUE);
		KEYWORDS.put("false", T_FALSE);

		KEYWORDS.put("import", T_IMPORT);
		KEYWORDS.put("include", T_INCLUDE);

		KEYWORDS.put("interface", T_INTERFACE);
		KEYWORDS.put("class", T_CLASS);
		KEYWORDS.put("struct", T_STRUCT);
		KEYWORDS.put("enum", T_ENUM);
		KEYWORDS.put("this", T_THIS);
		KEYWORDS.put("super", T_SUPER);
		KEYWORDS.put("package", T_PACKAGE);
		KEYWORDS.put("expand", T_EXPAND);
		KEYWORDS.put("implements", T_IMPLEMENTS);

		KEYWORDS.put("private", T_PRIVATE);
		KEYWORDS.put("public", T_PUBLIC);
		KEYWORDS.put("export", T_EXPORT);
		KEYWORDS.put("final", T_FINAL);
		KEYWORDS.put("get", T_GET);
		KEYWORDS.put("set", T_SET);
	}

	public static ZenTokener fromInputStream(InputStream inputStream) throws IOException
	{
		return new ZenTokener(new InputStreamReader(inputStream, UTF8));
	}

	/**
	 * Constructs a tokener from the given reader.
	 *
	 * @param contents file reader
	 * @throws IOException if the file could not be read properly
	 */
	public ZenTokener(Reader contents) throws IOException
	{
		super(contents, DFA);
	}

	/**
	 * Constructs a tokener from the given string.
	 *
	 * @param contents content string
	 * @throws IOException shouldn't happen
	 */
	public ZenTokener(String contents) throws IOException
	{
		super(new StringReader(contents), DFA);
	}
	
	public ZenPosition getPosition()
	{
		// try to reuse position from peek() if possible
		if (peek() == null) {
			return new ZenPosition(getFile(), getLine(), getLineOffset());
		} else {
			return peek().getPosition();
		}
	}

	public List<String> parseIdentifierDotSequence()
	{
		List<String> name = new ArrayList<String>();
		String nameFirst = requiredIdentifier();
		name.add(nameFirst);

		while (optional(T_DOT) != null) {
			String namePart = requiredIdentifier();
			name.add(namePart);
		}

		return name;
	}

	public String requiredIdentifier()
	{
		return required(TOKEN_ID, "identifier expected").getValue();
	}

	// ##################################
	// ### TokenStream implementation ###
	// ##################################
	
	@Override
	public Token process(Token token)
	{
		if (token.getType() == TOKEN_ID && KEYWORDS.containsKey(token.getValue())) {
			return new Token(token.getValue(), KEYWORDS.get(token.getValue()), token.getPosition());
		}

		return token;
	}
}
