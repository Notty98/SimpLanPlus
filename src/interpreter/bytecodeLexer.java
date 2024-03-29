package src.interpreter;

import java.util.HashMap;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bytecodeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LW=3, LW_B=4, ADDI=5, ADD=6, SUB=7, MULT=8, DIV=9, SW=10, 
		SW_B=11, LI=12, LB=13, MOVE=14, BEQ=15, BEQ_B=16, BGT=17, BGE=18, BRANCH=19, 
		JTL=20, JR=21, PRINT=22, HALT=23, COL=24, LABEL=25, REG=26, NUMBER=27, 
		WHITESP=28, ERR=29;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "LW", "LW_B", "ADDI", "ADD", "SUB", "MULT", "DIV", "SW", 
			"SW_B", "LI", "LB", "MOVE", "BEQ", "BEQ_B", "BGT", "BGE", "BRANCH", "JTL", 
			"JR", "PRINT", "HALT", "DIGIT", "LETTER", "COL", "LABEL", "REG", "NUMBER", 
			"WHITESP", "ERR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'lw'", "'lw_b'", "'addi'", "'add'", "'sub'", "'mult'", 
			"'div'", "'sw'", "'sw_b'", "'li'", "'lb'", "'move'", "'beq'", "'beq_b'", 
			"'bgt'", "'bge'", "'branch'", "'jtl'", "'jr'", "'print'", "'halt'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "LW", "LW_B", "ADDI", "ADD", "SUB", "MULT", "DIV", 
			"SW", "SW_B", "LI", "LB", "MOVE", "BEQ", "BEQ_B", "BGT", "BGE", "BRANCH", 
			"JTL", "JR", "PRINT", "HALT", "COL", "LABEL", "REG", "NUMBER", "WHITESP", 
			"ERR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public int lexicalErrors=0;


	public bytecodeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "bytecode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 30:
			ERR_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void ERR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 System.err.println("Invalid char: "+ getText()); lexicalErrors++;  
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u00cd\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3"+
		"\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\7\34\u00aa"+
		"\n\34\f\34\16\34\u00ad\13\34\3\35\3\35\3\35\3\35\5\35\u00b3\n\35\3\36"+
		"\6\36\u00b6\n\36\r\36\16\36\u00b7\3\36\3\36\6\36\u00bc\n\36\r\36\16\36"+
		"\u00bd\5\36\u00c0\n\36\3\37\6\37\u00c3\n\37\r\37\16\37\u00c4\3\37\3\37"+
		"\3 \3 \3 \3 \3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\2\63\2"+
		"\65\32\67\339\34;\35=\36?\37\3\2\5\4\2C\\c|\6\2\62;C\\aac|\5\2\13\f\17"+
		"\17\"\"\2\u00d0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"+
		"\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3\2\2\2\7E\3\2\2\2\tH\3\2\2\2\13M\3\2\2"+
		"\2\rR\3\2\2\2\17V\3\2\2\2\21Z\3\2\2\2\23_\3\2\2\2\25c\3\2\2\2\27f\3\2"+
		"\2\2\31k\3\2\2\2\33n\3\2\2\2\35q\3\2\2\2\37v\3\2\2\2!z\3\2\2\2#\u0080"+
		"\3\2\2\2%\u0084\3\2\2\2\'\u0088\3\2\2\2)\u008f\3\2\2\2+\u0093\3\2\2\2"+
		"-\u0096\3\2\2\2/\u009c\3\2\2\2\61\u00a1\3\2\2\2\63\u00a3\3\2\2\2\65\u00a5"+
		"\3\2\2\2\67\u00a7\3\2\2\29\u00ae\3\2\2\2;\u00bf\3\2\2\2=\u00c2\3\2\2\2"+
		"?\u00c8\3\2\2\2AB\7*\2\2B\4\3\2\2\2CD\7+\2\2D\6\3\2\2\2EF\7n\2\2FG\7y"+
		"\2\2G\b\3\2\2\2HI\7n\2\2IJ\7y\2\2JK\7a\2\2KL\7d\2\2L\n\3\2\2\2MN\7c\2"+
		"\2NO\7f\2\2OP\7f\2\2PQ\7k\2\2Q\f\3\2\2\2RS\7c\2\2ST\7f\2\2TU\7f\2\2U\16"+
		"\3\2\2\2VW\7u\2\2WX\7w\2\2XY\7d\2\2Y\20\3\2\2\2Z[\7o\2\2[\\\7w\2\2\\]"+
		"\7n\2\2]^\7v\2\2^\22\3\2\2\2_`\7f\2\2`a\7k\2\2ab\7x\2\2b\24\3\2\2\2cd"+
		"\7u\2\2de\7y\2\2e\26\3\2\2\2fg\7u\2\2gh\7y\2\2hi\7a\2\2ij\7d\2\2j\30\3"+
		"\2\2\2kl\7n\2\2lm\7k\2\2m\32\3\2\2\2no\7n\2\2op\7d\2\2p\34\3\2\2\2qr\7"+
		"o\2\2rs\7q\2\2st\7x\2\2tu\7g\2\2u\36\3\2\2\2vw\7d\2\2wx\7g\2\2xy\7s\2"+
		"\2y \3\2\2\2z{\7d\2\2{|\7g\2\2|}\7s\2\2}~\7a\2\2~\177\7d\2\2\177\"\3\2"+
		"\2\2\u0080\u0081\7d\2\2\u0081\u0082\7i\2\2\u0082\u0083\7v\2\2\u0083$\3"+
		"\2\2\2\u0084\u0085\7d\2\2\u0085\u0086\7i\2\2\u0086\u0087\7g\2\2\u0087"+
		"&\3\2\2\2\u0088\u0089\7d\2\2\u0089\u008a\7t\2\2\u008a\u008b\7c\2\2\u008b"+
		"\u008c\7p\2\2\u008c\u008d\7e\2\2\u008d\u008e\7j\2\2\u008e(\3\2\2\2\u008f"+
		"\u0090\7l\2\2\u0090\u0091\7v\2\2\u0091\u0092\7n\2\2\u0092*\3\2\2\2\u0093"+
		"\u0094\7l\2\2\u0094\u0095\7t\2\2\u0095,\3\2\2\2\u0096\u0097\7r\2\2\u0097"+
		"\u0098\7t\2\2\u0098\u0099\7k\2\2\u0099\u009a\7p\2\2\u009a\u009b\7v\2\2"+
		"\u009b.\3\2\2\2\u009c\u009d\7j\2\2\u009d\u009e\7c\2\2\u009e\u009f\7n\2"+
		"\2\u009f\u00a0\7v\2\2\u00a0\60\3\2\2\2\u00a1\u00a2\4\62;\2\u00a2\62\3"+
		"\2\2\2\u00a3\u00a4\4c|\2\u00a4\64\3\2\2\2\u00a5\u00a6\7<\2\2\u00a6\66"+
		"\3\2\2\2\u00a7\u00ab\t\2\2\2\u00a8\u00aa\t\3\2\2\u00a9\u00a8\3\2\2\2\u00aa"+
		"\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac8\3\2\2\2"+
		"\u00ad\u00ab\3\2\2\2\u00ae\u00af\7&\2\2\u00af\u00b2\5\63\32\2\u00b0\u00b3"+
		"\5\63\32\2\u00b1\u00b3\5\61\31\2\u00b2\u00b0\3\2\2\2\u00b2\u00b1\3\2\2"+
		"\2\u00b3:\3\2\2\2\u00b4\u00b6\5\61\31\2\u00b5\u00b4\3\2\2\2\u00b6\u00b7"+
		"\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00c0\3\2\2\2\u00b9"+
		"\u00bb\7/\2\2\u00ba\u00bc\5\61\31\2\u00bb\u00ba\3\2\2\2\u00bc\u00bd\3"+
		"\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf"+
		"\u00b5\3\2\2\2\u00bf\u00b9\3\2\2\2\u00c0<\3\2\2\2\u00c1\u00c3\t\4\2\2"+
		"\u00c2\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5"+
		"\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c7\b\37\2\2\u00c7>\3\2\2\2\u00c8"+
		"\u00c9\13\2\2\2\u00c9\u00ca\b \3\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\b "+
		"\2\2\u00cc@\3\2\2\2\t\2\u00ab\u00b2\u00b7\u00bd\u00bf\u00c4\4\2\3\2\3"+
		" \2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}