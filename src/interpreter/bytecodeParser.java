package src.interpreter;

import java.util.HashMap;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bytecodeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LW=3, LW_B=4, ADDI=5, ADD=6, SUB=7, MULT=8, DIV=9, SW=10, 
		SW_B=11, LI=12, LB=13, MOVE=14, BEQ=15, BEQ_B=16, BGT=17, BGE=18, BRANCH=19, 
		JTL=20, JR=21, PRINT=22, HALT=23, COL=24, LABEL=25, REG=26, NUMBER=27, 
		WHITESP=28, ERR=29;
	public static final int
		RULE_assembly = 0, RULE_instruction = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"assembly", "instruction"
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

	@Override
	public String getGrammarFileName() { return "bytecode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public bytecodeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AssemblyContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof bytecodeListener ) ((bytecodeListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof bytecodeListener ) ((bytecodeListener)listener).exitAssembly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof bytecodeVisitor ) return ((bytecodeVisitor<? extends T>)visitor).visitAssembly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LW) | (1L << LW_B) | (1L << ADDI) | (1L << ADD) | (1L << SUB) | (1L << MULT) | (1L << DIV) | (1L << SW) | (1L << SW_B) | (1L << LI) | (1L << LB) | (1L << MOVE) | (1L << BEQ) | (1L << BEQ_B) | (1L << BGT) | (1L << BGE) | (1L << BRANCH) | (1L << JTL) | (1L << JR) | (1L << PRINT) | (1L << HALT) | (1L << LABEL))) != 0)) {
				{
				{
				setState(4);
				instruction();
				}
				}
				setState(9);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public Token r1;
		public Token offset;
		public Token r2;
		public Token r3;
		public Token n;
		public Token label;
		public Token reg;
		public TerminalNode LW() { return getToken(bytecodeParser.LW, 0); }
		public TerminalNode LW_B() { return getToken(bytecodeParser.LW_B, 0); }
		public TerminalNode ADD() { return getToken(bytecodeParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(bytecodeParser.SUB, 0); }
		public TerminalNode DIV() { return getToken(bytecodeParser.DIV, 0); }
		public TerminalNode MULT() { return getToken(bytecodeParser.MULT, 0); }
		public TerminalNode SW() { return getToken(bytecodeParser.SW, 0); }
		public TerminalNode SW_B() { return getToken(bytecodeParser.SW_B, 0); }
		public TerminalNode ADDI() { return getToken(bytecodeParser.ADDI, 0); }
		public TerminalNode LI() { return getToken(bytecodeParser.LI, 0); }
		public TerminalNode LB() { return getToken(bytecodeParser.LB, 0); }
		public TerminalNode MOVE() { return getToken(bytecodeParser.MOVE, 0); }
		public TerminalNode BEQ() { return getToken(bytecodeParser.BEQ, 0); }
		public TerminalNode BGT() { return getToken(bytecodeParser.BGT, 0); }
		public TerminalNode BGE() { return getToken(bytecodeParser.BGE, 0); }
		public TerminalNode BEQ_B() { return getToken(bytecodeParser.BEQ_B, 0); }
		public TerminalNode BRANCH() { return getToken(bytecodeParser.BRANCH, 0); }
		public TerminalNode LABEL() { return getToken(bytecodeParser.LABEL, 0); }
		public TerminalNode JTL() { return getToken(bytecodeParser.JTL, 0); }
		public TerminalNode JR() { return getToken(bytecodeParser.JR, 0); }
		public TerminalNode PRINT() { return getToken(bytecodeParser.PRINT, 0); }
		public TerminalNode HALT() { return getToken(bytecodeParser.HALT, 0); }
		public List<TerminalNode> REG() { return getTokens(bytecodeParser.REG); }
		public TerminalNode REG(int i) {
			return getToken(bytecodeParser.REG, i);
		}
		public TerminalNode NUMBER() { return getToken(bytecodeParser.NUMBER, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof bytecodeListener ) ((bytecodeListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof bytecodeListener ) ((bytecodeListener)listener).exitInstruction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof bytecodeVisitor ) return ((bytecodeVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LW:
				{
				setState(10);
				match(LW);
				setState(11);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(12);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(13);
				match(T__0);
				setState(14);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(15);
				match(T__1);
				}
				break;
			case LW_B:
				{
				setState(16);
				match(LW_B);
				setState(17);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(18);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(19);
				match(T__0);
				setState(20);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(21);
				match(T__1);
				}
				break;
			case ADD:
				{
				setState(22);
				match(ADD);
				setState(23);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(24);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(25);
				((InstructionContext)_localctx).r3 = match(REG);
				}
				break;
			case SUB:
				{
				setState(26);
				match(SUB);
				setState(27);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(28);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(29);
				((InstructionContext)_localctx).r3 = match(REG);
				}
				break;
			case DIV:
				{
				setState(30);
				match(DIV);
				setState(31);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(32);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(33);
				((InstructionContext)_localctx).r3 = match(REG);
				}
				break;
			case MULT:
				{
				setState(34);
				match(MULT);
				setState(35);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(36);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(37);
				((InstructionContext)_localctx).r3 = match(REG);
				}
				break;
			case SW:
				{
				setState(38);
				match(SW);
				setState(39);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(40);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(41);
				match(T__0);
				setState(42);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(43);
				match(T__1);
				}
				break;
			case SW_B:
				{
				setState(44);
				match(SW_B);
				setState(45);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(46);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(47);
				match(T__0);
				setState(48);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(49);
				match(T__1);
				}
				break;
			case ADDI:
				{
				setState(50);
				match(ADDI);
				setState(51);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(52);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(53);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case LI:
				{
				setState(54);
				match(LI);
				setState(55);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(56);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case LB:
				{
				setState(57);
				match(LB);
				setState(58);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(59);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case MOVE:
				{
				setState(60);
				match(MOVE);
				setState(61);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(62);
				((InstructionContext)_localctx).r2 = match(REG);
				}
				break;
			case BEQ:
				{
				setState(63);
				match(BEQ);
				setState(64);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(65);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(66);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case BGT:
				{
				setState(67);
				match(BGT);
				setState(68);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(69);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(70);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case BGE:
				{
				setState(71);
				match(BGE);
				setState(72);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(73);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(74);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case BEQ_B:
				{
				setState(75);
				match(BEQ_B);
				setState(76);
				((InstructionContext)_localctx).r1 = match(REG);
				setState(77);
				((InstructionContext)_localctx).r2 = match(REG);
				setState(78);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case BRANCH:
				{
				setState(79);
				match(BRANCH);
				setState(80);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case LABEL:
				{
				setState(81);
				match(LABEL);
				}
				break;
			case JTL:
				{
				setState(82);
				match(JTL);
				setState(83);
				((InstructionContext)_localctx).label = match(LABEL);
				}
				break;
			case JR:
				{
				setState(84);
				match(JR);
				setState(85);
				((InstructionContext)_localctx).reg = match(REG);
				}
				break;
			case PRINT:
				{
				setState(86);
				match(PRINT);
				setState(87);
				((InstructionContext)_localctx).reg = match(REG);
				}
				break;
			case HALT:
				{
				setState(88);
				match(HALT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37^\4\2\t\2\4\3\t"+
		"\3\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\\\n\3\3\3\2\2\4\2\4\2\2\2q\2\t\3\2\2\2\4[\3\2\2\2\6\b\5\4\3\2\7\6"+
		"\3\2\2\2\b\13\3\2\2\2\t\7\3\2\2\2\t\n\3\2\2\2\n\3\3\2\2\2\13\t\3\2\2\2"+
		"\f\r\7\5\2\2\r\16\7\34\2\2\16\17\7\35\2\2\17\20\7\3\2\2\20\21\7\34\2\2"+
		"\21\\\7\4\2\2\22\23\7\6\2\2\23\24\7\34\2\2\24\25\7\35\2\2\25\26\7\3\2"+
		"\2\26\27\7\34\2\2\27\\\7\4\2\2\30\31\7\b\2\2\31\32\7\34\2\2\32\33\7\34"+
		"\2\2\33\\\7\34\2\2\34\35\7\t\2\2\35\36\7\34\2\2\36\37\7\34\2\2\37\\\7"+
		"\34\2\2 !\7\13\2\2!\"\7\34\2\2\"#\7\34\2\2#\\\7\34\2\2$%\7\n\2\2%&\7\34"+
		"\2\2&\'\7\34\2\2\'\\\7\34\2\2()\7\f\2\2)*\7\34\2\2*+\7\35\2\2+,\7\3\2"+
		"\2,-\7\34\2\2-\\\7\4\2\2./\7\r\2\2/\60\7\34\2\2\60\61\7\35\2\2\61\62\7"+
		"\3\2\2\62\63\7\34\2\2\63\\\7\4\2\2\64\65\7\7\2\2\65\66\7\34\2\2\66\67"+
		"\7\34\2\2\67\\\7\35\2\289\7\16\2\29:\7\34\2\2:\\\7\35\2\2;<\7\17\2\2<"+
		"=\7\34\2\2=\\\7\35\2\2>?\7\20\2\2?@\7\34\2\2@\\\7\34\2\2AB\7\21\2\2BC"+
		"\7\34\2\2CD\7\34\2\2D\\\7\33\2\2EF\7\23\2\2FG\7\34\2\2GH\7\34\2\2H\\\7"+
		"\33\2\2IJ\7\24\2\2JK\7\34\2\2KL\7\34\2\2L\\\7\33\2\2MN\7\22\2\2NO\7\34"+
		"\2\2OP\7\34\2\2P\\\7\33\2\2QR\7\25\2\2R\\\7\33\2\2S\\\7\33\2\2TU\7\26"+
		"\2\2U\\\7\33\2\2VW\7\27\2\2W\\\7\34\2\2XY\7\30\2\2Y\\\7\34\2\2Z\\\7\31"+
		"\2\2[\f\3\2\2\2[\22\3\2\2\2[\30\3\2\2\2[\34\3\2\2\2[ \3\2\2\2[$\3\2\2"+
		"\2[(\3\2\2\2[.\3\2\2\2[\64\3\2\2\2[8\3\2\2\2[;\3\2\2\2[>\3\2\2\2[A\3\2"+
		"\2\2[E\3\2\2\2[I\3\2\2\2[M\3\2\2\2[Q\3\2\2\2[S\3\2\2\2[T\3\2\2\2[V\3\2"+
		"\2\2[X\3\2\2\2[Z\3\2\2\2\\\5\3\2\2\2\4\t[";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}