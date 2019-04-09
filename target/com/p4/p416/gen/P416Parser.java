// Generated from P416.g4 by ANTLR 4.5
package com.p4.p416.gen;

import com.p4.p416.gen.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class P416Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Hex_number=1, Decimal_number=2, Octal_number=3, Binary_number=4, Real_number=5, 
		COMMENT=6, WS=7, ABSTRACT=8, ACTION=9, ACTIONS=10, ENTRIES=11, APPLY=12, 
		EXTRACT=13, BOOL=14, BIT=15, CONST=16, CONTROL=17, DEFAULT=18, ELSE=19, 
		ENUM=20, ERROR=21, EXIT=22, EXTERN=23, FALSE=24, HEADER=25, HEADER_UNION=26, 
		IF=27, IN=28, INOUT=29, INT=30, KEY=31, MATCH_KIND=32, OUT=33, PARSER=34, 
		PACKAGE=35, RETURN=36, SELECT=37, STATE=38, STRUCT=39, SWITCH=40, TABLE=41, 
		THIS=42, TRANSITION=43, TRUE=44, TUPLE=45, TYPEDEF=46, VARBIT=47, VOID=48, 
		DONTCARE=49, IDENTIFIER=50, MASK=51, RANGE=52, SHL=53, LAND=54, LOR=55, 
		EQ=56, NE=57, GE=58, LE=59, PP=60, SEMI=61, COLON=62, AT=63, COMMA=64, 
		LPARAN=65, RPARAN=66, LCURL=67, RCURL=68, LBRKT=69, RBRKT=70, ASSIGN=71, 
		DOT=72, LT=73, GT=74, NOT=75, TILDA=76, MINUS=77, PLUS=78, STAR=79, SLASH=80, 
		PRCNT=81, AND=82, OR=83, XOR=84, QUESTION=85, STRING_LITERAL=86;
	public static final int
		RULE_p4program = 0, RULE_declaration = 1, RULE_nonTypeName = 2, RULE_name = 3, 
		RULE_optAnnotations = 4, RULE_annotations = 5, RULE_annotation = 6, RULE_parameterList = 7, 
		RULE_parameter = 8, RULE_direction = 9, RULE_packageTypeDeclaration = 10, 
		RULE_instantiation = 11, RULE_optConstructorParameters = 12, RULE_dotPrefix = 13, 
		RULE_parserDeclaration = 14, RULE_parserLocalElements = 15, RULE_parserLocalElement = 16, 
		RULE_parserTypeDeclaration = 17, RULE_parserStates = 18, RULE_parserState = 19, 
		RULE_parserStatements = 20, RULE_parserStatement = 21, RULE_parserBlockStatement = 22, 
		RULE_transitionStatement = 23, RULE_stateExpression = 24, RULE_selectExpression = 25, 
		RULE_selectCaseList = 26, RULE_selectCase = 27, RULE_keySetExpression = 28, 
		RULE_tupleKeySetExpression = 29, RULE_simpleKeySetExpression = 30, RULE_controlDeclaration = 31, 
		RULE_controlTypeDeclaration = 32, RULE_controlLocalDeclarations = 33, 
		RULE_controlLocalDeclaration = 34, RULE_controlBody = 35, RULE_externDeclaration = 36, 
		RULE_methodPrototypes = 37, RULE_functionPrototype = 38, RULE_methodPrototype = 39, 
		RULE_typeRef = 40, RULE_prefixedType = 41, RULE_typeName = 42, RULE_tupleType = 43, 
		RULE_headerStackType = 44, RULE_specializedType = 45, RULE_baseType = 46, 
		RULE_typeOrVoid = 47, RULE_optTypeParameters = 48, RULE_typeParameterList = 49, 
		RULE_typeArg = 50, RULE_dontcare = 51, RULE_typeArgumentList = 52, RULE_typeDeclaration = 53, 
		RULE_derivedTypeDeclaration = 54, RULE_headerTypeDeclaration = 55, RULE_headerUnionDeclaration = 56, 
		RULE_structTypeDeclaration = 57, RULE_structFieldList = 58, RULE_structField = 59, 
		RULE_enumDeclaration = 60, RULE_errorDeclaration = 61, RULE_matchKindDeclaration = 62, 
		RULE_identifierList = 63, RULE_typedefDeclaration = 64, RULE_assignmentStatement = 65, 
		RULE_methodCallStatement = 66, RULE_emptyStatement = 67, RULE_returnStatement = 68, 
		RULE_exitStatement = 69, RULE_conditionalStatement = 70, RULE_directApplication = 71, 
		RULE_statement = 72, RULE_blockStatement = 73, RULE_statOrDeclList = 74, 
		RULE_switchStatement = 75, RULE_switchCases = 76, RULE_switchCase = 77, 
		RULE_switchLabel = 78, RULE_statementOrDeclaration = 79, RULE_tableDeclaration = 80, 
		RULE_tablePropertyList = 81, RULE_tableProperty = 82, RULE_keyElementList = 83, 
		RULE_keyElement = 84, RULE_actionList = 85, RULE_entriesList = 86, RULE_entry = 87, 
		RULE_actionRef = 88, RULE_actionDeclaration = 89, RULE_variableDeclaration = 90, 
		RULE_constantDeclaration = 91, RULE_optInitializer = 92, RULE_initializer = 93, 
		RULE_argumentList = 94, RULE_argument = 95, RULE_expressionList = 96, 
		RULE_member = 97, RULE_prefixedNonTypeName = 98, RULE_lvalue = 99, RULE_expression = 100, 
		RULE_unaryExpression_not = 101, RULE_unaryExpression_tilda = 102, RULE_unaryExpression_plus = 103, 
		RULE_unaryExpression_minus = 104, RULE_number = 105, RULE_decimalNumber = 106, 
		RULE_octalNumber = 107, RULE_binaryNumber = 108, RULE_hexNumber = 109, 
		RULE_realNumber = 110;
	public static final String[] ruleNames = {
		"p4program", "declaration", "nonTypeName", "name", "optAnnotations", "annotations", 
		"annotation", "parameterList", "parameter", "direction", "packageTypeDeclaration", 
		"instantiation", "optConstructorParameters", "dotPrefix", "parserDeclaration", 
		"parserLocalElements", "parserLocalElement", "parserTypeDeclaration", 
		"parserStates", "parserState", "parserStatements", "parserStatement", 
		"parserBlockStatement", "transitionStatement", "stateExpression", "selectExpression", 
		"selectCaseList", "selectCase", "keySetExpression", "tupleKeySetExpression", 
		"simpleKeySetExpression", "controlDeclaration", "controlTypeDeclaration", 
		"controlLocalDeclarations", "controlLocalDeclaration", "controlBody", 
		"externDeclaration", "methodPrototypes", "functionPrototype", "methodPrototype", 
		"typeRef", "prefixedType", "typeName", "tupleType", "headerStackType", 
		"specializedType", "baseType", "typeOrVoid", "optTypeParameters", "typeParameterList", 
		"typeArg", "dontcare", "typeArgumentList", "typeDeclaration", "derivedTypeDeclaration", 
		"headerTypeDeclaration", "headerUnionDeclaration", "structTypeDeclaration", 
		"structFieldList", "structField", "enumDeclaration", "errorDeclaration", 
		"matchKindDeclaration", "identifierList", "typedefDeclaration", "assignmentStatement", 
		"methodCallStatement", "emptyStatement", "returnStatement", "exitStatement", 
		"conditionalStatement", "directApplication", "statement", "blockStatement", 
		"statOrDeclList", "switchStatement", "switchCases", "switchCase", "switchLabel", 
		"statementOrDeclaration", "tableDeclaration", "tablePropertyList", "tableProperty", 
		"keyElementList", "keyElement", "actionList", "entriesList", "entry", 
		"actionRef", "actionDeclaration", "variableDeclaration", "constantDeclaration", 
		"optInitializer", "initializer", "argumentList", "argument", "expressionList", 
		"member", "prefixedNonTypeName", "lvalue", "expression", "unaryExpression_not", 
		"unaryExpression_tilda", "unaryExpression_plus", "unaryExpression_minus", 
		"number", "decimalNumber", "octalNumber", "binaryNumber", "hexNumber", 
		"realNumber"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, "'abstract'", "'action'", 
		"'actions'", "'entries'", "'apply'", "'extract'", "'bool'", "'bit'", "'const'", 
		"'control'", "'default'", "'else'", "'enum'", "'error'", "'exit'", "'extern'", 
		"'false'", "'header'", "'header_union'", "'if'", "'in'", "'inout'", "'int'", 
		"'key'", "'match_kind'", "'out'", "'parser'", "'package'", "'return'", 
		"'select'", "'state'", "'struct'", "'switch'", "'table'", "'this'", "'transition'", 
		"'true'", "'tuple'", "'typedef'", "'varbit'", "'void'", "'_'", null, "'&&&'", 
		"'..'", "'<<'", "'&&'", "'||'", "'=='", "'!='", "'>='", "'<='", "'++'", 
		"';'", "':'", "'@'", "','", "'('", "')'", "'{'", "'}'", "'['", "']'", 
		"'='", "'.'", "'<'", "'>'", "'!'", "'~'", "'-'", "'+'", "'*'", "'/'", 
		"'%'", "'&'", "'|'", "'^'", "'?'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "Hex_number", "Decimal_number", "Octal_number", "Binary_number", 
		"Real_number", "COMMENT", "WS", "ABSTRACT", "ACTION", "ACTIONS", "ENTRIES", 
		"APPLY", "EXTRACT", "BOOL", "BIT", "CONST", "CONTROL", "DEFAULT", "ELSE", 
		"ENUM", "ERROR", "EXIT", "EXTERN", "FALSE", "HEADER", "HEADER_UNION", 
		"IF", "IN", "INOUT", "INT", "KEY", "MATCH_KIND", "OUT", "PARSER", "PACKAGE", 
		"RETURN", "SELECT", "STATE", "STRUCT", "SWITCH", "TABLE", "THIS", "TRANSITION", 
		"TRUE", "TUPLE", "TYPEDEF", "VARBIT", "VOID", "DONTCARE", "IDENTIFIER", 
		"MASK", "RANGE", "SHL", "LAND", "LOR", "EQ", "NE", "GE", "LE", "PP", "SEMI", 
		"COLON", "AT", "COMMA", "LPARAN", "RPARAN", "LCURL", "RCURL", "LBRKT", 
		"RBRKT", "ASSIGN", "DOT", "LT", "GT", "NOT", "TILDA", "MINUS", "PLUS", 
		"STAR", "SLASH", "PRCNT", "AND", "OR", "XOR", "QUESTION", "STRING_LITERAL"
	};
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
	public String getGrammarFileName() { return "P416.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public P416Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class P4programContext extends ParserRuleContext {
		public P4programContextExt extendedContext;
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(P416Parser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(P416Parser.SEMI, i);
		}
		public P4programContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_p4program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterP4program(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitP4program(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitP4program(this);
			else return visitor.visitChildren(this);
		}
	}

	public final P4programContext p4program() throws RecognitionException {
		P4programContext _localctx = new P4programContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_p4program);
		int _la;
		try {
			setState(232);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & ((1L << (ACTION - 9)) | (1L << (BOOL - 9)) | (1L << (BIT - 9)) | (1L << (CONST - 9)) | (1L << (CONTROL - 9)) | (1L << (ENUM - 9)) | (1L << (ERROR - 9)) | (1L << (EXTERN - 9)) | (1L << (HEADER - 9)) | (1L << (HEADER_UNION - 9)) | (1L << (INT - 9)) | (1L << (MATCH_KIND - 9)) | (1L << (PARSER - 9)) | (1L << (PACKAGE - 9)) | (1L << (STRUCT - 9)) | (1L << (TUPLE - 9)) | (1L << (TYPEDEF - 9)) | (1L << (VARBIT - 9)) | (1L << (IDENTIFIER - 9)) | (1L << (AT - 9)) | (1L << (DOT - 9)))) != 0)) {
					{
					{
					setState(223);
					declaration();
					setState(225);
					_la = _input.LA(1);
					if (_la==SEMI) {
						{
						setState(224);
						match(SEMI);
						}
					}

					}
					}
					setState(231);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
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

	public static class DeclarationContext extends ParserRuleContext {
		public DeclarationContextExt extendedContext;
		public ConstantDeclarationContext constantDeclaration() {
			return getRuleContext(ConstantDeclarationContext.class,0);
		}
		public ExternDeclarationContext externDeclaration() {
			return getRuleContext(ExternDeclarationContext.class,0);
		}
		public ActionDeclarationContext actionDeclaration() {
			return getRuleContext(ActionDeclarationContext.class,0);
		}
		public ParserDeclarationContext parserDeclaration() {
			return getRuleContext(ParserDeclarationContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public ControlDeclarationContext controlDeclaration() {
			return getRuleContext(ControlDeclarationContext.class,0);
		}
		public InstantiationContext instantiation() {
			return getRuleContext(InstantiationContext.class,0);
		}
		public ErrorDeclarationContext errorDeclaration() {
			return getRuleContext(ErrorDeclarationContext.class,0);
		}
		public MatchKindDeclarationContext matchKindDeclaration() {
			return getRuleContext(MatchKindDeclarationContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			setState(243);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(234);
				constantDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(235);
				externDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(236);
				actionDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(237);
				parserDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(238);
				typeDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(239);
				controlDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(240);
				instantiation();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(241);
				errorDeclaration();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(242);
				matchKindDeclaration();
				}
				break;
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

	public static class NonTypeNameContext extends ParserRuleContext {
		public NonTypeNameContextExt extendedContext;
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public TerminalNode APPLY() { return getToken(P416Parser.APPLY, 0); }
		public TerminalNode KEY() { return getToken(P416Parser.KEY, 0); }
		public TerminalNode ACTIONS() { return getToken(P416Parser.ACTIONS, 0); }
		public TerminalNode STATE() { return getToken(P416Parser.STATE, 0); }
		public NonTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNonTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNonTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNonTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonTypeNameContext nonTypeName() throws RecognitionException {
		NonTypeNameContext _localctx = new NonTypeNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_nonTypeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIONS) | (1L << APPLY) | (1L << KEY) | (1L << STATE) | (1L << IDENTIFIER))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
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

	public static class NameContext extends ParserRuleContext {
		public NameContextExt extendedContext;
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public TerminalNode ERROR() { return getToken(P416Parser.ERROR, 0); }
		public TerminalNode EXTRACT() { return getToken(P416Parser.EXTRACT, 0); }
		public TerminalNode APPLY() { return getToken(P416Parser.APPLY, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_name);
		try {
			setState(252);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				nonTypeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				match(ERROR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(250);
				match(EXTRACT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(251);
				match(APPLY);
				}
				break;
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

	public static class OptAnnotationsContext extends ParserRuleContext {
		public OptAnnotationsContextExt extendedContext;
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public OptAnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optAnnotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOptAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOptAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOptAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptAnnotationsContext optAnnotations() throws RecognitionException {
		OptAnnotationsContext _localctx = new OptAnnotationsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_optAnnotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(254);
				annotations();
				}
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

	public static class AnnotationsContext extends ParserRuleContext {
		public AnnotationsContextExt extendedContext;
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public AnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(257);
				annotation();
				}
				}
				setState(260); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==AT );
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

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationContextExt extendedContext;
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
	 
		public AnnotationContext() { }
		public void copyFrom(AnnotationContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class SimpleAnnotationContext extends AnnotationContext {
		public TerminalNode AT() { return getToken(P416Parser.AT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public SimpleAnnotationContext(AnnotationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSimpleAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSimpleAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSimpleAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComplexAnnotationContext extends AnnotationContext {
		public TerminalNode AT() { return getToken(P416Parser.AT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ComplexAnnotationContext(AnnotationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterComplexAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitComplexAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitComplexAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_annotation);
		int _la;
		try {
			setState(272);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				_localctx = new SimpleAnnotationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(262);
				match(AT);
				setState(263);
				name();
				}
				break;
			case 2:
				_localctx = new ComplexAnnotationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(264);
				match(AT);
				setState(265);
				name();
				setState(266);
				match(LPARAN);
				setState(268);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(267);
					expressionList();
					}
				}

				setState(270);
				match(RPARAN);
				}
				break;
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

	public static class ParameterListContext extends ParserRuleContext {
		public ParameterListContextExt extendedContext;
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			parameter();
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(275);
				match(COMMA);
				setState(276);
				parameter();
				}
				}
				setState(281);
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

	public static class ParameterContext extends ParserRuleContext {
		public ParameterContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DirectionContext direction() {
			return getRuleContext(DirectionContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			optAnnotations();
			setState(284);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IN) | (1L << INOUT) | (1L << OUT))) != 0)) {
				{
				setState(283);
				direction();
				}
			}

			setState(286);
			typeRef();
			setState(287);
			name();
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

	public static class DirectionContext extends ParserRuleContext {
		public DirectionContextExt extendedContext;
		public DirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_direction; }
	 
		public DirectionContext() { }
		public void copyFrom(DirectionContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class InOutDirectionContext extends DirectionContext {
		public TerminalNode INOUT() { return getToken(P416Parser.INOUT, 0); }
		public InOutDirectionContext(DirectionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterInOutDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitInOutDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitInOutDirection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InDirectionContext extends DirectionContext {
		public TerminalNode IN() { return getToken(P416Parser.IN, 0); }
		public InDirectionContext(DirectionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterInDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitInDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitInDirection(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OutDirectionContext extends DirectionContext {
		public TerminalNode OUT() { return getToken(P416Parser.OUT, 0); }
		public OutDirectionContext(DirectionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOutDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOutDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOutDirection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectionContext direction() throws RecognitionException {
		DirectionContext _localctx = new DirectionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_direction);
		try {
			setState(292);
			switch (_input.LA(1)) {
			case IN:
				_localctx = new InDirectionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(289);
				match(IN);
				}
				break;
			case OUT:
				_localctx = new OutDirectionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(290);
				match(OUT);
				}
				break;
			case INOUT:
				_localctx = new InOutDirectionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(291);
				match(INOUT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class PackageTypeDeclarationContext extends ParserRuleContext {
		public PackageTypeDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode PACKAGE() { return getToken(P416Parser.PACKAGE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public OptTypeParametersContext optTypeParameters() {
			return getRuleContext(OptTypeParametersContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public PackageTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPackageTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPackageTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPackageTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageTypeDeclarationContext packageTypeDeclaration() throws RecognitionException {
		PackageTypeDeclarationContext _localctx = new PackageTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_packageTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			optAnnotations();
			setState(295);
			match(PACKAGE);
			setState(296);
			name();
			setState(298);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(297);
				optTypeParameters();
				}
			}

			setState(300);
			match(LPARAN);
			setState(302);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(301);
				parameterList();
				}
			}

			setState(304);
			match(RPARAN);
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

	public static class InstantiationContext extends ParserRuleContext {
		public InstantiationContextExt extendedContext;
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public InstantiationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instantiation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterInstantiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitInstantiation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitInstantiation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstantiationContext instantiation() throws RecognitionException {
		InstantiationContext _localctx = new InstantiationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_instantiation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(306);
				annotations();
				}
			}

			setState(309);
			typeRef();
			setState(310);
			match(LPARAN);
			setState(312);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
				{
				setState(311);
				argumentList();
				}
			}

			setState(314);
			match(RPARAN);
			setState(315);
			name();
			setState(316);
			match(SEMI);
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

	public static class OptConstructorParametersContext extends ParserRuleContext {
		public OptConstructorParametersContextExt extendedContext;
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public OptConstructorParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optConstructorParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOptConstructorParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOptConstructorParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOptConstructorParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptConstructorParametersContext optConstructorParameters() throws RecognitionException {
		OptConstructorParametersContext _localctx = new OptConstructorParametersContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_optConstructorParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(LPARAN);
			setState(320);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(319);
				parameterList();
				}
			}

			setState(322);
			match(RPARAN);
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

	public static class DotPrefixContext extends ParserRuleContext {
		public DotPrefixContextExt extendedContext;
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public DotPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dotPrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDotPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDotPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDotPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DotPrefixContext dotPrefix() throws RecognitionException {
		DotPrefixContext _localctx = new DotPrefixContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_dotPrefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(DOT);
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

	public static class ParserDeclarationContext extends ParserRuleContext {
		public ParserDeclarationContextExt extendedContext;
		public ParserTypeDeclarationContext parserTypeDeclaration() {
			return getRuleContext(ParserTypeDeclarationContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public ParserStatesContext parserStates() {
			return getRuleContext(ParserStatesContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public OptConstructorParametersContext optConstructorParameters() {
			return getRuleContext(OptConstructorParametersContext.class,0);
		}
		public ParserLocalElementsContext parserLocalElements() {
			return getRuleContext(ParserLocalElementsContext.class,0);
		}
		public ParserDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserDeclarationContext parserDeclaration() throws RecognitionException {
		ParserDeclarationContext _localctx = new ParserDeclarationContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parserDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			parserTypeDeclaration();
			setState(328);
			_la = _input.LA(1);
			if (_la==LPARAN) {
				{
				setState(327);
				optConstructorParameters();
				}
			}

			setState(330);
			match(LCURL);
			setState(332);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(331);
				parserLocalElements();
				}
				break;
			}
			setState(334);
			parserStates();
			setState(335);
			match(RCURL);
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

	public static class ParserLocalElementsContext extends ParserRuleContext {
		public ParserLocalElementsContextExt extendedContext;
		public List<ParserLocalElementContext> parserLocalElement() {
			return getRuleContexts(ParserLocalElementContext.class);
		}
		public ParserLocalElementContext parserLocalElement(int i) {
			return getRuleContext(ParserLocalElementContext.class,i);
		}
		public ParserLocalElementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserLocalElements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserLocalElements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserLocalElements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserLocalElements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserLocalElementsContext parserLocalElements() throws RecognitionException {
		ParserLocalElementsContext _localctx = new ParserLocalElementsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parserLocalElements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(338); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(337);
					parserLocalElement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(340); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class ParserLocalElementContext extends ParserRuleContext {
		public ParserLocalElementContextExt extendedContext;
		public ConstantDeclarationContext constantDeclaration() {
			return getRuleContext(ConstantDeclarationContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public InstantiationContext instantiation() {
			return getRuleContext(InstantiationContext.class,0);
		}
		public ParserLocalElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserLocalElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserLocalElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserLocalElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserLocalElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserLocalElementContext parserLocalElement() throws RecognitionException {
		ParserLocalElementContext _localctx = new ParserLocalElementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_parserLocalElement);
		try {
			setState(345);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(342);
				constantDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(343);
				variableDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(344);
				instantiation();
				}
				break;
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

	public static class ParserTypeDeclarationContext extends ParserRuleContext {
		public ParserTypeDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode PARSER() { return getToken(P416Parser.PARSER, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public OptTypeParametersContext optTypeParameters() {
			return getRuleContext(OptTypeParametersContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ParserTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserTypeDeclarationContext parserTypeDeclaration() throws RecognitionException {
		ParserTypeDeclarationContext _localctx = new ParserTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_parserTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			optAnnotations();
			setState(348);
			match(PARSER);
			setState(349);
			name();
			setState(351);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(350);
				optTypeParameters();
				}
			}

			setState(353);
			match(LPARAN);
			setState(355);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(354);
				parameterList();
				}
			}

			setState(357);
			match(RPARAN);
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

	public static class ParserStatesContext extends ParserRuleContext {
		public ParserStatesContextExt extendedContext;
		public List<ParserStateContext> parserState() {
			return getRuleContexts(ParserStateContext.class);
		}
		public ParserStateContext parserState(int i) {
			return getRuleContext(ParserStateContext.class,i);
		}
		public ParserStatesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserStates; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserStates(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserStates(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserStates(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserStatesContext parserStates() throws RecognitionException {
		ParserStatesContext _localctx = new ParserStatesContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_parserStates);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(359);
				parserState();
				}
				}
				setState(362); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==STATE || _la==AT );
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

	public static class ParserStateContext extends ParserRuleContext {
		public ParserStateContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode STATE() { return getToken(P416Parser.STATE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ParserStatementsContext parserStatements() {
			return getRuleContext(ParserStatementsContext.class,0);
		}
		public TransitionStatementContext transitionStatement() {
			return getRuleContext(TransitionStatementContext.class,0);
		}
		public ParserStateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserState; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserState(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserState(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserState(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserStateContext parserState() throws RecognitionException {
		ParserStateContext _localctx = new ParserStateContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_parserState);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			optAnnotations();
			setState(365);
			match(STATE);
			setState(366);
			name();
			setState(367);
			match(LCURL);
			setState(369);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (CONST - 10)) | (1L << (ERROR - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (STATE - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (AT - 10)) | (1L << (LCURL - 10)) | (1L << (DOT - 10)))) != 0)) {
				{
				setState(368);
				parserStatements();
				}
			}

			setState(372);
			_la = _input.LA(1);
			if (_la==TRANSITION) {
				{
				setState(371);
				transitionStatement();
				}
			}

			setState(374);
			match(RCURL);
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

	public static class ParserStatementsContext extends ParserRuleContext {
		public ParserStatementsContextExt extendedContext;
		public List<ParserStatementContext> parserStatement() {
			return getRuleContexts(ParserStatementContext.class);
		}
		public ParserStatementContext parserStatement(int i) {
			return getRuleContext(ParserStatementContext.class,i);
		}
		public ParserStatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserStatements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserStatements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserStatementsContext parserStatements() throws RecognitionException {
		ParserStatementsContext _localctx = new ParserStatementsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_parserStatements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(377); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(376);
				parserStatement();
				}
				}
				setState(379); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (CONST - 10)) | (1L << (ERROR - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (STATE - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (AT - 10)) | (1L << (LCURL - 10)) | (1L << (DOT - 10)))) != 0) );
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

	public static class ParserStatementContext extends ParserRuleContext {
		public ParserStatementContextExt extendedContext;
		public AssignmentStatementContext assignmentStatement() {
			return getRuleContext(AssignmentStatementContext.class,0);
		}
		public MethodCallStatementContext methodCallStatement() {
			return getRuleContext(MethodCallStatementContext.class,0);
		}
		public DirectApplicationContext directApplication() {
			return getRuleContext(DirectApplicationContext.class,0);
		}
		public ParserBlockStatementContext parserBlockStatement() {
			return getRuleContext(ParserBlockStatementContext.class,0);
		}
		public ConstantDeclarationContext constantDeclaration() {
			return getRuleContext(ConstantDeclarationContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ParserStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserStatementContext parserStatement() throws RecognitionException {
		ParserStatementContext _localctx = new ParserStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_parserStatement);
		try {
			setState(387);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				assignmentStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(382);
				methodCallStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(383);
				directApplication();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(384);
				parserBlockStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(385);
				constantDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(386);
				variableDeclaration();
				}
				break;
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

	public static class ParserBlockStatementContext extends ParserRuleContext {
		public ParserBlockStatementContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public ParserStatementsContext parserStatements() {
			return getRuleContext(ParserStatementsContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ParserBlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserBlockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterParserBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitParserBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitParserBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserBlockStatementContext parserBlockStatement() throws RecognitionException {
		ParserBlockStatementContext _localctx = new ParserBlockStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_parserBlockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			optAnnotations();
			setState(390);
			match(LCURL);
			setState(391);
			parserStatements();
			setState(392);
			match(RCURL);
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

	public static class TransitionStatementContext extends ParserRuleContext {
		public TransitionStatementContextExt extendedContext;
		public TerminalNode TRANSITION() { return getToken(P416Parser.TRANSITION, 0); }
		public StateExpressionContext stateExpression() {
			return getRuleContext(StateExpressionContext.class,0);
		}
		public TransitionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transitionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTransitionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTransitionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTransitionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransitionStatementContext transitionStatement() throws RecognitionException {
		TransitionStatementContext _localctx = new TransitionStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_transitionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
			match(TRANSITION);
			setState(395);
			stateExpression();
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

	public static class StateExpressionContext extends ParserRuleContext {
		public StateExpressionContextExt extendedContext;
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public SelectExpressionContext selectExpression() {
			return getRuleContext(SelectExpressionContext.class,0);
		}
		public StateExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stateExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStateExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStateExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStateExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StateExpressionContext stateExpression() throws RecognitionException {
		StateExpressionContext _localctx = new StateExpressionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_stateExpression);
		try {
			setState(401);
			switch (_input.LA(1)) {
			case ACTIONS:
			case APPLY:
			case EXTRACT:
			case ERROR:
			case KEY:
			case STATE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(397);
				name();
				setState(398);
				match(SEMI);
				}
				break;
			case SELECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(400);
				selectExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class SelectExpressionContext extends ParserRuleContext {
		public SelectExpressionContextExt extendedContext;
		public TerminalNode SELECT() { return getToken(P416Parser.SELECT, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public SelectCaseListContext selectCaseList() {
			return getRuleContext(SelectCaseListContext.class,0);
		}
		public SelectExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSelectExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSelectExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSelectExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectExpressionContext selectExpression() throws RecognitionException {
		SelectExpressionContext _localctx = new SelectExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_selectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			match(SELECT);
			setState(404);
			match(LPARAN);
			setState(406);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
				{
				setState(405);
				expressionList();
				}
			}

			setState(408);
			match(RPARAN);
			setState(409);
			match(LCURL);
			setState(411);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << DEFAULT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << DONTCARE) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
				{
				setState(410);
				selectCaseList();
				}
			}

			setState(413);
			match(RCURL);
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

	public static class SelectCaseListContext extends ParserRuleContext {
		public SelectCaseListContextExt extendedContext;
		public List<SelectCaseContext> selectCase() {
			return getRuleContexts(SelectCaseContext.class);
		}
		public SelectCaseContext selectCase(int i) {
			return getRuleContext(SelectCaseContext.class,i);
		}
		public SelectCaseListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectCaseList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSelectCaseList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSelectCaseList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSelectCaseList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectCaseListContext selectCaseList() throws RecognitionException {
		SelectCaseListContext _localctx = new SelectCaseListContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_selectCaseList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(415);
				selectCase();
				}
				}
				setState(418); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << DEFAULT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << DONTCARE) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0) );
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

	public static class SelectCaseContext extends ParserRuleContext {
		public SelectCaseContextExt extendedContext;
		public KeySetExpressionContext keySetExpression() {
			return getRuleContext(KeySetExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public SelectCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSelectCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSelectCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSelectCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectCaseContext selectCase() throws RecognitionException {
		SelectCaseContext _localctx = new SelectCaseContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_selectCase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			keySetExpression();
			setState(421);
			match(COLON);
			setState(422);
			name();
			setState(423);
			match(SEMI);
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

	public static class KeySetExpressionContext extends ParserRuleContext {
		public KeySetExpressionContextExt extendedContext;
		public TupleKeySetExpressionContext tupleKeySetExpression() {
			return getRuleContext(TupleKeySetExpressionContext.class,0);
		}
		public SimpleKeySetExpressionContext simpleKeySetExpression() {
			return getRuleContext(SimpleKeySetExpressionContext.class,0);
		}
		public KeySetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keySetExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterKeySetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitKeySetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitKeySetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeySetExpressionContext keySetExpression() throws RecognitionException {
		KeySetExpressionContext _localctx = new KeySetExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_keySetExpression);
		try {
			setState(427);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(425);
				tupleKeySetExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(426);
				simpleKeySetExpression();
				}
				break;
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

	public static class TupleKeySetExpressionContext extends ParserRuleContext {
		public TupleKeySetExpressionContextExt extendedContext;
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public List<SimpleKeySetExpressionContext> simpleKeySetExpression() {
			return getRuleContexts(SimpleKeySetExpressionContext.class);
		}
		public SimpleKeySetExpressionContext simpleKeySetExpression(int i) {
			return getRuleContext(SimpleKeySetExpressionContext.class,i);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public TupleKeySetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleKeySetExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTupleKeySetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTupleKeySetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTupleKeySetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleKeySetExpressionContext tupleKeySetExpression() throws RecognitionException {
		TupleKeySetExpressionContext _localctx = new TupleKeySetExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_tupleKeySetExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(LPARAN);
			setState(430);
			simpleKeySetExpression();
			setState(435);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(431);
				match(COMMA);
				setState(432);
				simpleKeySetExpression();
				}
				}
				setState(437);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(438);
			match(RPARAN);
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

	public static class SimpleKeySetExpressionContext extends ParserRuleContext {
		public SimpleKeySetExpressionContextExt extendedContext;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode DEFAULT() { return getToken(P416Parser.DEFAULT, 0); }
		public TerminalNode DONTCARE() { return getToken(P416Parser.DONTCARE, 0); }
		public TerminalNode MASK() { return getToken(P416Parser.MASK, 0); }
		public TerminalNode RANGE() { return getToken(P416Parser.RANGE, 0); }
		public SimpleKeySetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleKeySetExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSimpleKeySetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSimpleKeySetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSimpleKeySetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleKeySetExpressionContext simpleKeySetExpression() throws RecognitionException {
		SimpleKeySetExpressionContext _localctx = new SimpleKeySetExpressionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_simpleKeySetExpression);
		try {
			setState(451);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(440);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(441);
				match(DEFAULT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(442);
				match(DONTCARE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(443);
				expression(0);
				setState(444);
				match(MASK);
				setState(445);
				expression(0);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(447);
				expression(0);
				setState(448);
				match(RANGE);
				setState(449);
				expression(0);
				}
				break;
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

	public static class ControlDeclarationContext extends ParserRuleContext {
		public ControlDeclarationContextExt extendedContext;
		public ControlTypeDeclarationContext controlTypeDeclaration() {
			return getRuleContext(ControlTypeDeclarationContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode APPLY() { return getToken(P416Parser.APPLY, 0); }
		public ControlBodyContext controlBody() {
			return getRuleContext(ControlBodyContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public OptConstructorParametersContext optConstructorParameters() {
			return getRuleContext(OptConstructorParametersContext.class,0);
		}
		public ControlLocalDeclarationsContext controlLocalDeclarations() {
			return getRuleContext(ControlLocalDeclarationsContext.class,0);
		}
		public ControlDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterControlDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitControlDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitControlDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlDeclarationContext controlDeclaration() throws RecognitionException {
		ControlDeclarationContext _localctx = new ControlDeclarationContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_controlDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			controlTypeDeclaration();
			setState(455);
			_la = _input.LA(1);
			if (_la==LPARAN) {
				{
				setState(454);
				optConstructorParameters();
				}
			}

			setState(457);
			match(LCURL);
			setState(459);
			_la = _input.LA(1);
			if (((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & ((1L << (ACTION - 9)) | (1L << (BOOL - 9)) | (1L << (BIT - 9)) | (1L << (CONST - 9)) | (1L << (ERROR - 9)) | (1L << (INT - 9)) | (1L << (TABLE - 9)) | (1L << (TUPLE - 9)) | (1L << (VARBIT - 9)) | (1L << (IDENTIFIER - 9)) | (1L << (AT - 9)) | (1L << (DOT - 9)))) != 0)) {
				{
				setState(458);
				controlLocalDeclarations();
				}
			}

			setState(461);
			match(APPLY);
			setState(462);
			controlBody();
			setState(463);
			match(RCURL);
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

	public static class ControlTypeDeclarationContext extends ParserRuleContext {
		public ControlTypeDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode CONTROL() { return getToken(P416Parser.CONTROL, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public OptTypeParametersContext optTypeParameters() {
			return getRuleContext(OptTypeParametersContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ControlTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterControlTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitControlTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitControlTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlTypeDeclarationContext controlTypeDeclaration() throws RecognitionException {
		ControlTypeDeclarationContext _localctx = new ControlTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_controlTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			optAnnotations();
			setState(466);
			match(CONTROL);
			setState(467);
			name();
			setState(469);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(468);
				optTypeParameters();
				}
			}

			setState(471);
			match(LPARAN);
			setState(473);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(472);
				parameterList();
				}
			}

			setState(475);
			match(RPARAN);
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

	public static class ControlLocalDeclarationsContext extends ParserRuleContext {
		public ControlLocalDeclarationsContextExt extendedContext;
		public List<ControlLocalDeclarationContext> controlLocalDeclaration() {
			return getRuleContexts(ControlLocalDeclarationContext.class);
		}
		public ControlLocalDeclarationContext controlLocalDeclaration(int i) {
			return getRuleContext(ControlLocalDeclarationContext.class,i);
		}
		public ControlLocalDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlLocalDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterControlLocalDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitControlLocalDeclarations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitControlLocalDeclarations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlLocalDeclarationsContext controlLocalDeclarations() throws RecognitionException {
		ControlLocalDeclarationsContext _localctx = new ControlLocalDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_controlLocalDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(477);
				controlLocalDeclaration();
				}
				}
				setState(480); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & ((1L << (ACTION - 9)) | (1L << (BOOL - 9)) | (1L << (BIT - 9)) | (1L << (CONST - 9)) | (1L << (ERROR - 9)) | (1L << (INT - 9)) | (1L << (TABLE - 9)) | (1L << (TUPLE - 9)) | (1L << (VARBIT - 9)) | (1L << (IDENTIFIER - 9)) | (1L << (AT - 9)) | (1L << (DOT - 9)))) != 0) );
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

	public static class ControlLocalDeclarationContext extends ParserRuleContext {
		public ControlLocalDeclarationContextExt extendedContext;
		public ConstantDeclarationContext constantDeclaration() {
			return getRuleContext(ConstantDeclarationContext.class,0);
		}
		public ActionDeclarationContext actionDeclaration() {
			return getRuleContext(ActionDeclarationContext.class,0);
		}
		public TableDeclarationContext tableDeclaration() {
			return getRuleContext(TableDeclarationContext.class,0);
		}
		public InstantiationContext instantiation() {
			return getRuleContext(InstantiationContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ControlLocalDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlLocalDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterControlLocalDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitControlLocalDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitControlLocalDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlLocalDeclarationContext controlLocalDeclaration() throws RecognitionException {
		ControlLocalDeclarationContext _localctx = new ControlLocalDeclarationContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_controlLocalDeclaration);
		try {
			setState(487);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(482);
				constantDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(483);
				actionDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(484);
				tableDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(485);
				instantiation();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(486);
				variableDeclaration();
				}
				break;
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

	public static class ControlBodyContext extends ParserRuleContext {
		public ControlBodyContextExt extendedContext;
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public ControlBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterControlBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitControlBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitControlBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlBodyContext controlBody() throws RecognitionException {
		ControlBodyContext _localctx = new ControlBodyContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_controlBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
			blockStatement();
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

	public static class ExternDeclarationContext extends ParserRuleContext {
		public ExternDeclarationContextExt extendedContext;
		public ExternDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_externDeclaration; }
	 
		public ExternDeclarationContext() { }
		public void copyFrom(ExternDeclarationContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class ExternObjectDeclarationContext extends ExternDeclarationContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode EXTERN() { return getToken(P416Parser.EXTERN, 0); }
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public OptTypeParametersContext optTypeParameters() {
			return getRuleContext(OptTypeParametersContext.class,0);
		}
		public MethodPrototypesContext methodPrototypes() {
			return getRuleContext(MethodPrototypesContext.class,0);
		}
		public ExternObjectDeclarationContext(ExternDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExternObjectDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExternObjectDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExternObjectDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExternFunctionDeclarationContext extends ExternDeclarationContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode EXTERN() { return getToken(P416Parser.EXTERN, 0); }
		public FunctionPrototypeContext functionPrototype() {
			return getRuleContext(FunctionPrototypeContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ExternFunctionDeclarationContext(ExternDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExternFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExternFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExternFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExternDeclarationContext externDeclaration() throws RecognitionException {
		ExternDeclarationContext _localctx = new ExternDeclarationContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_externDeclaration);
		int _la;
		try {
			setState(508);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				_localctx = new ExternObjectDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				optAnnotations();
				setState(492);
				match(EXTERN);
				setState(493);
				nonTypeName();
				setState(495);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(494);
					optTypeParameters();
					}
				}

				setState(497);
				match(LCURL);
				setState(499);
				_la = _input.LA(1);
				if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (ERROR - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (STATE - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (VOID - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (DOT - 10)))) != 0)) {
					{
					setState(498);
					methodPrototypes();
					}
				}

				setState(501);
				match(RCURL);
				}
				break;
			case 2:
				_localctx = new ExternFunctionDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(503);
				optAnnotations();
				setState(504);
				match(EXTERN);
				setState(505);
				functionPrototype();
				setState(506);
				match(SEMI);
				}
				break;
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

	public static class MethodPrototypesContext extends ParserRuleContext {
		public MethodPrototypesContextExt extendedContext;
		public List<MethodPrototypeContext> methodPrototype() {
			return getRuleContexts(MethodPrototypeContext.class);
		}
		public MethodPrototypeContext methodPrototype(int i) {
			return getRuleContext(MethodPrototypeContext.class,i);
		}
		public MethodPrototypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodPrototypes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMethodPrototypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMethodPrototypes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMethodPrototypes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodPrototypesContext methodPrototypes() throws RecognitionException {
		MethodPrototypesContext _localctx = new MethodPrototypesContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_methodPrototypes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(510);
				methodPrototype();
				}
				}
				setState(513); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (ERROR - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (STATE - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (VOID - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (DOT - 10)))) != 0) );
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

	public static class FunctionPrototypeContext extends ParserRuleContext {
		public FunctionPrototypeContextExt extendedContext;
		public TypeOrVoidContext typeOrVoid() {
			return getRuleContext(TypeOrVoidContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public OptTypeParametersContext optTypeParameters() {
			return getRuleContext(OptTypeParametersContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FunctionPrototypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionPrototype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterFunctionPrototype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitFunctionPrototype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitFunctionPrototype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionPrototypeContext functionPrototype() throws RecognitionException {
		FunctionPrototypeContext _localctx = new FunctionPrototypeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_functionPrototype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			typeOrVoid();
			setState(516);
			name();
			setState(518);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(517);
				optTypeParameters();
				}
			}

			setState(520);
			match(LPARAN);
			setState(522);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(521);
				parameterList();
				}
			}

			setState(524);
			match(RPARAN);
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

	public static class MethodPrototypeContext extends ParserRuleContext {
		public MethodPrototypeContextExt extendedContext;
		public FunctionPrototypeContext functionPrototype() {
			return getRuleContext(FunctionPrototypeContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public MethodPrototypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodPrototype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMethodPrototype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMethodPrototype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMethodPrototype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodPrototypeContext methodPrototype() throws RecognitionException {
		MethodPrototypeContext _localctx = new MethodPrototypeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_methodPrototype);
		int _la;
		try {
			setState(536);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(526);
				functionPrototype();
				setState(527);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(529);
				match(IDENTIFIER);
				setState(530);
				match(LPARAN);
				setState(532);
				_la = _input.LA(1);
				if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
					{
					setState(531);
					parameterList();
					}
				}

				setState(534);
				match(RPARAN);
				setState(535);
				match(SEMI);
				}
				break;
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

	public static class TypeRefContext extends ParserRuleContext {
		public TypeRefContextExt extendedContext;
		public BaseTypeContext baseType() {
			return getRuleContext(BaseTypeContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public SpecializedTypeContext specializedType() {
			return getRuleContext(SpecializedTypeContext.class,0);
		}
		public HeaderStackTypeContext headerStackType() {
			return getRuleContext(HeaderStackTypeContext.class,0);
		}
		public TupleTypeContext tupleType() {
			return getRuleContext(TupleTypeContext.class,0);
		}
		public TypeRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeRefContext typeRef() throws RecognitionException {
		TypeRefContext _localctx = new TypeRefContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_typeRef);
		try {
			setState(543);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(538);
				baseType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(539);
				typeName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(540);
				specializedType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(541);
				headerStackType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(542);
				tupleType();
				}
				break;
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

	public static class PrefixedTypeContext extends ParserRuleContext {
		public PrefixedTypeContextExt extendedContext;
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public DotPrefixContext dotPrefix() {
			return getRuleContext(DotPrefixContext.class,0);
		}
		public TerminalNode ERROR() { return getToken(P416Parser.ERROR, 0); }
		public PrefixedTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixedType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPrefixedType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPrefixedType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPrefixedType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixedTypeContext prefixedType() throws RecognitionException {
		PrefixedTypeContext _localctx = new PrefixedTypeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_prefixedType);
		try {
			setState(550);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(545);
				match(IDENTIFIER);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(546);
				dotPrefix();
				setState(547);
				match(IDENTIFIER);
				}
				break;
			case ERROR:
				enterOuterAlt(_localctx, 3);
				{
				setState(549);
				match(ERROR);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class TypeNameContext extends ParserRuleContext {
		public TypeNameContextExt extendedContext;
		public PrefixedTypeContext prefixedType() {
			return getRuleContext(PrefixedTypeContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(552);
			prefixedType();
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

	public static class TupleTypeContext extends ParserRuleContext {
		public TupleTypeContextExt extendedContext;
		public TerminalNode TUPLE() { return getToken(P416Parser.TUPLE, 0); }
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeArgumentListContext typeArgumentList() {
			return getRuleContext(TypeArgumentListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public TupleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTupleType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTupleType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeContext tupleType() throws RecognitionException {
		TupleTypeContext _localctx = new TupleTypeContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_tupleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(554);
			match(TUPLE);
			setState(555);
			match(LT);
			setState(556);
			typeArgumentList();
			setState(557);
			match(GT);
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

	public static class HeaderStackTypeContext extends ParserRuleContext {
		public HeaderStackTypeContextExt extendedContext;
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode LBRKT() { return getToken(P416Parser.LBRKT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRKT() { return getToken(P416Parser.RBRKT, 0); }
		public HeaderStackTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerStackType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterHeaderStackType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitHeaderStackType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitHeaderStackType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderStackTypeContext headerStackType() throws RecognitionException {
		HeaderStackTypeContext _localctx = new HeaderStackTypeContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_headerStackType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			typeName();
			setState(560);
			match(LBRKT);
			setState(561);
			expression(0);
			setState(562);
			match(RBRKT);
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

	public static class SpecializedTypeContext extends ParserRuleContext {
		public SpecializedTypeContextExt extendedContext;
		public PrefixedTypeContext prefixedType() {
			return getRuleContext(PrefixedTypeContext.class,0);
		}
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeArgumentListContext typeArgumentList() {
			return getRuleContext(TypeArgumentListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public SpecializedTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specializedType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSpecializedType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSpecializedType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSpecializedType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecializedTypeContext specializedType() throws RecognitionException {
		SpecializedTypeContext _localctx = new SpecializedTypeContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_specializedType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			prefixedType();
			setState(565);
			match(LT);
			setState(566);
			typeArgumentList();
			setState(567);
			match(GT);
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

	public static class BaseTypeContext extends ParserRuleContext {
		public BaseTypeContextExt extendedContext;
		public BaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseType; }
	 
		public BaseTypeContext() { }
		public void copyFrom(BaseTypeContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class ErrorTypeContext extends BaseTypeContext {
		public TerminalNode ERROR() { return getToken(P416Parser.ERROR, 0); }
		public ErrorTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterErrorType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitErrorType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitErrorType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarBitSizeTypeContext extends BaseTypeContext {
		public TerminalNode VARBIT() { return getToken(P416Parser.VARBIT, 0); }
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public VarBitSizeTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterVarBitSizeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitVarBitSizeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitVarBitSizeType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitSizeTypeContext extends BaseTypeContext {
		public TerminalNode BIT() { return getToken(P416Parser.BIT, 0); }
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public BitSizeTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBitSizeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBitSizeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBitSizeType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitTypeContext extends BaseTypeContext {
		public TerminalNode BIT() { return getToken(P416Parser.BIT, 0); }
		public BitTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBitType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBitType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolTypeContext extends BaseTypeContext {
		public TerminalNode BOOL() { return getToken(P416Parser.BOOL, 0); }
		public BoolTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBoolType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBoolType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBoolType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntSizeTypeContext extends BaseTypeContext {
		public TerminalNode INT() { return getToken(P416Parser.INT, 0); }
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public IntSizeTypeContext(BaseTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterIntSizeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitIntSizeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitIntSizeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseTypeContext baseType() throws RecognitionException {
		BaseTypeContext _localctx = new BaseTypeContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_baseType);
		try {
			setState(587);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				_localctx = new BoolTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(569);
				match(BOOL);
				}
				break;
			case 2:
				_localctx = new ErrorTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(570);
				match(ERROR);
				}
				break;
			case 3:
				_localctx = new BitTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(571);
				match(BIT);
				}
				break;
			case 4:
				_localctx = new BitSizeTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(572);
				match(BIT);
				setState(573);
				match(LT);
				setState(574);
				number();
				setState(575);
				match(GT);
				}
				break;
			case 5:
				_localctx = new IntSizeTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(577);
				match(INT);
				setState(578);
				match(LT);
				setState(579);
				number();
				setState(580);
				match(GT);
				}
				break;
			case 6:
				_localctx = new VarBitSizeTypeContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(582);
				match(VARBIT);
				setState(583);
				match(LT);
				setState(584);
				number();
				setState(585);
				match(GT);
				}
				break;
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

	public static class TypeOrVoidContext extends ParserRuleContext {
		public TypeOrVoidContextExt extendedContext;
		public TerminalNode VOID() { return getToken(P416Parser.VOID, 0); }
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public TypeOrVoidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeOrVoid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeOrVoid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeOrVoid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeOrVoid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeOrVoidContext typeOrVoid() throws RecognitionException {
		TypeOrVoidContext _localctx = new TypeOrVoidContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_typeOrVoid);
		try {
			setState(592);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(589);
				match(VOID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(590);
				typeRef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(591);
				nonTypeName();
				}
				break;
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

	public static class OptTypeParametersContext extends ParserRuleContext {
		public OptTypeParametersContextExt extendedContext;
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeParameterListContext typeParameterList() {
			return getRuleContext(TypeParameterListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public OptTypeParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optTypeParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOptTypeParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOptTypeParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOptTypeParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptTypeParametersContext optTypeParameters() throws RecognitionException {
		OptTypeParametersContext _localctx = new OptTypeParametersContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_optTypeParameters);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			match(LT);
			setState(595);
			typeParameterList();
			setState(596);
			match(GT);
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

	public static class TypeParameterListContext extends ParserRuleContext {
		public TypeParameterListContextExt extendedContext;
		public List<NonTypeNameContext> nonTypeName() {
			return getRuleContexts(NonTypeNameContext.class);
		}
		public NonTypeNameContext nonTypeName(int i) {
			return getRuleContext(NonTypeNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public TypeParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeParameterListContext typeParameterList() throws RecognitionException {
		TypeParameterListContext _localctx = new TypeParameterListContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_typeParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			nonTypeName();
			setState(603);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(599);
				match(COMMA);
				setState(600);
				nonTypeName();
				}
				}
				setState(605);
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

	public static class TypeArgContext extends ParserRuleContext {
		public TypeArgContextExt extendedContext;
		public DontcareContext dontcare() {
			return getRuleContext(DontcareContext.class,0);
		}
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public TypeArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeArgContext typeArg() throws RecognitionException {
		TypeArgContext _localctx = new TypeArgContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_typeArg);
		try {
			setState(608);
			switch (_input.LA(1)) {
			case DONTCARE:
				enterOuterAlt(_localctx, 1);
				{
				setState(606);
				dontcare();
				}
				break;
			case BOOL:
			case BIT:
			case ERROR:
			case INT:
			case TUPLE:
			case VARBIT:
			case IDENTIFIER:
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(607);
				typeRef();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class DontcareContext extends ParserRuleContext {
		public DontcareContextExt extendedContext;
		public TerminalNode DONTCARE() { return getToken(P416Parser.DONTCARE, 0); }
		public DontcareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dontcare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDontcare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDontcare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDontcare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DontcareContext dontcare() throws RecognitionException {
		DontcareContext _localctx = new DontcareContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_dontcare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(610);
			match(DONTCARE);
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

	public static class TypeArgumentListContext extends ParserRuleContext {
		public TypeArgumentListContextExt extendedContext;
		public List<TypeArgContext> typeArg() {
			return getRuleContexts(TypeArgContext.class);
		}
		public TypeArgContext typeArg(int i) {
			return getRuleContext(TypeArgContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public TypeArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeArgumentListContext typeArgumentList() throws RecognitionException {
		TypeArgumentListContext _localctx = new TypeArgumentListContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_typeArgumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			typeArg();
			setState(617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(613);
				match(COMMA);
				setState(614);
				typeArg();
				}
				}
				setState(619);
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

	public static class TypeDeclarationContext extends ParserRuleContext {
		public TypeDeclarationContextExt extendedContext;
		public DerivedTypeDeclarationContext derivedTypeDeclaration() {
			return getRuleContext(DerivedTypeDeclarationContext.class,0);
		}
		public TypedefDeclarationContext typedefDeclaration() {
			return getRuleContext(TypedefDeclarationContext.class,0);
		}
		public ParserTypeDeclarationContext parserTypeDeclaration() {
			return getRuleContext(ParserTypeDeclarationContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ControlTypeDeclarationContext controlTypeDeclaration() {
			return getRuleContext(ControlTypeDeclarationContext.class,0);
		}
		public PackageTypeDeclarationContext packageTypeDeclaration() {
			return getRuleContext(PackageTypeDeclarationContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_typeDeclaration);
		try {
			setState(631);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(620);
				derivedTypeDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(621);
				typedefDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(622);
				parserTypeDeclaration();
				setState(623);
				match(SEMI);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(625);
				controlTypeDeclaration();
				setState(626);
				match(SEMI);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(628);
				packageTypeDeclaration();
				setState(629);
				match(SEMI);
				}
				break;
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

	public static class DerivedTypeDeclarationContext extends ParserRuleContext {
		public DerivedTypeDeclarationContextExt extendedContext;
		public HeaderTypeDeclarationContext headerTypeDeclaration() {
			return getRuleContext(HeaderTypeDeclarationContext.class,0);
		}
		public HeaderUnionDeclarationContext headerUnionDeclaration() {
			return getRuleContext(HeaderUnionDeclarationContext.class,0);
		}
		public StructTypeDeclarationContext structTypeDeclaration() {
			return getRuleContext(StructTypeDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public DerivedTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_derivedTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDerivedTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDerivedTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDerivedTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DerivedTypeDeclarationContext derivedTypeDeclaration() throws RecognitionException {
		DerivedTypeDeclarationContext _localctx = new DerivedTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_derivedTypeDeclaration);
		try {
			setState(637);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(633);
				headerTypeDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(634);
				headerUnionDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(635);
				structTypeDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(636);
				enumDeclaration();
				}
				break;
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

	public static class HeaderTypeDeclarationContext extends ParserRuleContext {
		public HeaderTypeDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode HEADER() { return getToken(P416Parser.HEADER, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public StructFieldListContext structFieldList() {
			return getRuleContext(StructFieldListContext.class,0);
		}
		public HeaderTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterHeaderTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitHeaderTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitHeaderTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderTypeDeclarationContext headerTypeDeclaration() throws RecognitionException {
		HeaderTypeDeclarationContext _localctx = new HeaderTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_headerTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			optAnnotations();
			setState(640);
			match(HEADER);
			setState(641);
			name();
			setState(642);
			match(LCURL);
			setState(644);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (INT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(643);
				structFieldList();
				}
			}

			setState(646);
			match(RCURL);
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

	public static class HeaderUnionDeclarationContext extends ParserRuleContext {
		public HeaderUnionDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode HEADER_UNION() { return getToken(P416Parser.HEADER_UNION, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public StructFieldListContext structFieldList() {
			return getRuleContext(StructFieldListContext.class,0);
		}
		public HeaderUnionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerUnionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterHeaderUnionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitHeaderUnionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitHeaderUnionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderUnionDeclarationContext headerUnionDeclaration() throws RecognitionException {
		HeaderUnionDeclarationContext _localctx = new HeaderUnionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_headerUnionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(648);
			optAnnotations();
			setState(649);
			match(HEADER_UNION);
			setState(650);
			name();
			setState(651);
			match(LCURL);
			setState(653);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (INT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(652);
				structFieldList();
				}
			}

			setState(655);
			match(RCURL);
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

	public static class StructTypeDeclarationContext extends ParserRuleContext {
		public StructTypeDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode STRUCT() { return getToken(P416Parser.STRUCT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public StructFieldListContext structFieldList() {
			return getRuleContext(StructFieldListContext.class,0);
		}
		public StructTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStructTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStructTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStructTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructTypeDeclarationContext structTypeDeclaration() throws RecognitionException {
		StructTypeDeclarationContext _localctx = new StructTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_structTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(657);
			optAnnotations();
			setState(658);
			match(STRUCT);
			setState(659);
			name();
			setState(660);
			match(LCURL);
			setState(662);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (INT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(661);
				structFieldList();
				}
			}

			setState(664);
			match(RCURL);
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

	public static class StructFieldListContext extends ParserRuleContext {
		public StructFieldListContextExt extendedContext;
		public List<StructFieldContext> structField() {
			return getRuleContexts(StructFieldContext.class);
		}
		public StructFieldContext structField(int i) {
			return getRuleContext(StructFieldContext.class,i);
		}
		public StructFieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structFieldList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStructFieldList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStructFieldList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStructFieldList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructFieldListContext structFieldList() throws RecognitionException {
		StructFieldListContext _localctx = new StructFieldListContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_structFieldList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(666);
				structField();
				}
				}
				setState(669); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (INT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0) );
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

	public static class StructFieldContext extends ParserRuleContext {
		public StructFieldContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public StructFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStructField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStructField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStructField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructFieldContext structField() throws RecognitionException {
		StructFieldContext _localctx = new StructFieldContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_structField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(671);
			optAnnotations();
			setState(672);
			typeRef();
			setState(673);
			name();
			setState(674);
			match(SEMI);
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

	public static class EnumDeclarationContext extends ParserRuleContext {
		public EnumDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode ENUM() { return getToken(P416Parser.ENUM, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitEnumDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitEnumDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			optAnnotations();
			setState(677);
			match(ENUM);
			setState(678);
			name();
			setState(679);
			match(LCURL);
			setState(680);
			identifierList();
			setState(681);
			match(RCURL);
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

	public static class ErrorDeclarationContext extends ParserRuleContext {
		public ErrorDeclarationContextExt extendedContext;
		public TerminalNode ERROR() { return getToken(P416Parser.ERROR, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ErrorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_errorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterErrorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitErrorDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitErrorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ErrorDeclarationContext errorDeclaration() throws RecognitionException {
		ErrorDeclarationContext _localctx = new ErrorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_errorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			match(ERROR);
			setState(684);
			match(LCURL);
			setState(685);
			identifierList();
			setState(686);
			match(RCURL);
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

	public static class MatchKindDeclarationContext extends ParserRuleContext {
		public MatchKindDeclarationContextExt extendedContext;
		public TerminalNode MATCH_KIND() { return getToken(P416Parser.MATCH_KIND, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public MatchKindDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchKindDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMatchKindDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMatchKindDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMatchKindDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchKindDeclarationContext matchKindDeclaration() throws RecognitionException {
		MatchKindDeclarationContext _localctx = new MatchKindDeclarationContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_matchKindDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(688);
			match(MATCH_KIND);
			setState(689);
			match(LCURL);
			setState(690);
			identifierList();
			setState(691);
			match(RCURL);
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

	public static class IdentifierListContext extends ParserRuleContext {
		public IdentifierListContextExt extendedContext;
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public IdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterIdentifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitIdentifierList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitIdentifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierListContext identifierList() throws RecognitionException {
		IdentifierListContext _localctx = new IdentifierListContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_identifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(693);
			name();
			setState(698);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(694);
				match(COMMA);
				setState(695);
				name();
				}
				}
				setState(700);
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

	public static class TypedefDeclarationContext extends ParserRuleContext {
		public TypedefDeclarationContextExt extendedContext;
		public TypedefDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedefDeclaration; }
	 
		public TypedefDeclarationContext() { }
		public void copyFrom(TypedefDeclarationContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class DerivedTypeDefContext extends TypedefDeclarationContext {
		public TerminalNode TYPEDEF() { return getToken(P416Parser.TYPEDEF, 0); }
		public DerivedTypeDeclarationContext derivedTypeDeclaration() {
			return getRuleContext(DerivedTypeDeclarationContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public DerivedTypeDefContext(TypedefDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDerivedTypeDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDerivedTypeDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDerivedTypeDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleTypeDefContext extends TypedefDeclarationContext {
		public TerminalNode TYPEDEF() { return getToken(P416Parser.TYPEDEF, 0); }
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public SimpleTypeDefContext(TypedefDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSimpleTypeDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSimpleTypeDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSimpleTypeDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedefDeclarationContext typedefDeclaration() throws RecognitionException {
		TypedefDeclarationContext _localctx = new TypedefDeclarationContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_typedefDeclaration);
		int _la;
		try {
			setState(717);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				_localctx = new SimpleTypeDefContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(702);
				_la = _input.LA(1);
				if (_la==AT) {
					{
					setState(701);
					annotations();
					}
				}

				setState(704);
				match(TYPEDEF);
				setState(705);
				typeRef();
				setState(706);
				name();
				setState(707);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new DerivedTypeDefContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(710);
				_la = _input.LA(1);
				if (_la==AT) {
					{
					setState(709);
					annotations();
					}
				}

				setState(712);
				match(TYPEDEF);
				setState(713);
				derivedTypeDeclaration();
				setState(714);
				name();
				setState(715);
				match(SEMI);
				}
				break;
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

	public static class AssignmentStatementContext extends ParserRuleContext {
		public AssignmentStatementContextExt extendedContext;
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public AssignmentStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterAssignmentStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitAssignmentStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitAssignmentStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentStatementContext assignmentStatement() throws RecognitionException {
		AssignmentStatementContext _localctx = new AssignmentStatementContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_assignmentStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(719);
			lvalue(0);
			setState(720);
			match(ASSIGN);
			setState(721);
			expression(0);
			setState(722);
			match(SEMI);
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

	public static class MethodCallStatementContext extends ParserRuleContext {
		public MethodCallStatementContextExt extendedContext;
		public MethodCallStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodCallStatement; }
	 
		public MethodCallStatementContext() { }
		public void copyFrom(MethodCallStatementContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class ExtractMethodCallContext extends MethodCallStatementContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public TerminalNode EXTRACT() { return getToken(P416Parser.EXTRACT, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeArgumentListContext typeArgumentList() {
			return getRuleContext(TypeArgumentListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ExtractMethodCallContext(MethodCallStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExtractMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExtractMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExtractMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ApplyMethodCallContext extends MethodCallStatementContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public TerminalNode APPLY() { return getToken(P416Parser.APPLY, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ApplyMethodCallContext(MethodCallStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterApplyMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitApplyMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitApplyMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CallWithTypeArgsContext extends MethodCallStatementContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeArgumentListContext typeArgumentList() {
			return getRuleContext(TypeArgumentListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public CallWithTypeArgsContext(MethodCallStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterCallWithTypeArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitCallWithTypeArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitCallWithTypeArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CallWithoutTypeArgsContext extends MethodCallStatementContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public CallWithoutTypeArgsContext(MethodCallStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterCallWithoutTypeArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitCallWithoutTypeArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitCallWithoutTypeArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodCallStatementContext methodCallStatement() throws RecognitionException {
		MethodCallStatementContext _localctx = new MethodCallStatementContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_methodCallStatement);
		int _la;
		try {
			setState(769);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				_localctx = new ApplyMethodCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(724);
				lvalue(0);
				setState(725);
				match(DOT);
				setState(726);
				match(APPLY);
				setState(727);
				match(LPARAN);
				setState(729);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(728);
					argumentList();
					}
				}

				setState(731);
				match(RPARAN);
				setState(732);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new ExtractMethodCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(734);
				lvalue(0);
				setState(735);
				match(DOT);
				setState(736);
				match(EXTRACT);
				setState(741);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(737);
					match(LT);
					setState(738);
					typeArgumentList();
					setState(739);
					match(GT);
					}
				}

				setState(743);
				match(LPARAN);
				setState(745);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(744);
					argumentList();
					}
				}

				setState(747);
				match(RPARAN);
				setState(748);
				match(SEMI);
				}
				break;
			case 3:
				_localctx = new CallWithoutTypeArgsContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(750);
				lvalue(0);
				setState(751);
				match(LPARAN);
				setState(753);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(752);
					argumentList();
					}
				}

				setState(755);
				match(RPARAN);
				setState(756);
				match(SEMI);
				}
				break;
			case 4:
				_localctx = new CallWithTypeArgsContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(758);
				lvalue(0);
				setState(759);
				match(LT);
				setState(760);
				typeArgumentList();
				setState(761);
				match(GT);
				setState(762);
				match(LPARAN);
				setState(764);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(763);
					argumentList();
					}
				}

				setState(766);
				match(RPARAN);
				setState(767);
				match(SEMI);
				}
				break;
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

	public static class EmptyStatementContext extends ParserRuleContext {
		public EmptyStatementContextExt extendedContext;
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public EmptyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterEmptyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitEmptyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitEmptyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyStatementContext emptyStatement() throws RecognitionException {
		EmptyStatementContext _localctx = new EmptyStatementContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_emptyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(771);
			match(SEMI);
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

	public static class ReturnStatementContext extends ParserRuleContext {
		public ReturnStatementContextExt extendedContext;
		public TerminalNode RETURN() { return getToken(P416Parser.RETURN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_returnStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			match(RETURN);
			setState(774);
			match(SEMI);
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

	public static class ExitStatementContext extends ParserRuleContext {
		public ExitStatementContextExt extendedContext;
		public TerminalNode EXIT() { return getToken(P416Parser.EXIT, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ExitStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exitStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExitStatementContext exitStatement() throws RecognitionException {
		ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(EXIT);
			setState(777);
			match(SEMI);
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

	public static class ConditionalStatementContext extends ParserRuleContext {
		public ConditionalStatementContextExt extendedContext;
		public ConditionalStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalStatement; }
	 
		public ConditionalStatementContext() { }
		public void copyFrom(ConditionalStatementContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class IfElseStatementContext extends ConditionalStatementContext {
		public TerminalNode IF() { return getToken(P416Parser.IF, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(P416Parser.ELSE, 0); }
		public IfElseStatementContext(ConditionalStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterIfElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitIfElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitIfElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfStatementContext extends ConditionalStatementContext {
		public TerminalNode IF() { return getToken(P416Parser.IF, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public IfStatementContext(ConditionalStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalStatementContext conditionalStatement() throws RecognitionException {
		ConditionalStatementContext _localctx = new ConditionalStatementContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_conditionalStatement);
		try {
			setState(793);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(779);
				match(IF);
				setState(780);
				match(LPARAN);
				setState(781);
				expression(0);
				setState(782);
				match(RPARAN);
				setState(783);
				statement();
				}
				break;
			case 2:
				_localctx = new IfElseStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(785);
				match(IF);
				setState(786);
				match(LPARAN);
				setState(787);
				expression(0);
				setState(788);
				match(RPARAN);
				setState(789);
				statement();
				setState(790);
				match(ELSE);
				setState(791);
				statement();
				}
				break;
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

	public static class DirectApplicationContext extends ParserRuleContext {
		public DirectApplicationContextExt extendedContext;
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public TerminalNode APPLY() { return getToken(P416Parser.APPLY, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public DirectApplicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directApplication; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDirectApplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDirectApplication(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDirectApplication(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectApplicationContext directApplication() throws RecognitionException {
		DirectApplicationContext _localctx = new DirectApplicationContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_directApplication);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(795);
			typeName();
			setState(796);
			match(DOT);
			setState(797);
			match(APPLY);
			setState(798);
			match(LPARAN);
			setState(800);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
				{
				setState(799);
				argumentList();
				}
			}

			setState(802);
			match(RPARAN);
			setState(803);
			match(SEMI);
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

	public static class StatementContext extends ParserRuleContext {
		public StatementContextExt extendedContext;
		public AssignmentStatementContext assignmentStatement() {
			return getRuleContext(AssignmentStatementContext.class,0);
		}
		public MethodCallStatementContext methodCallStatement() {
			return getRuleContext(MethodCallStatementContext.class,0);
		}
		public DirectApplicationContext directApplication() {
			return getRuleContext(DirectApplicationContext.class,0);
		}
		public ConditionalStatementContext conditionalStatement() {
			return getRuleContext(ConditionalStatementContext.class,0);
		}
		public EmptyStatementContext emptyStatement() {
			return getRuleContext(EmptyStatementContext.class,0);
		}
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public ExitStatementContext exitStatement() {
			return getRuleContext(ExitStatementContext.class,0);
		}
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public SwitchStatementContext switchStatement() {
			return getRuleContext(SwitchStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_statement);
		try {
			setState(814);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(805);
				assignmentStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(806);
				methodCallStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(807);
				directApplication();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(808);
				conditionalStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(809);
				emptyStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(810);
				blockStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(811);
				exitStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(812);
				returnStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(813);
				switchStatement();
				}
				break;
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

	public static class BlockStatementContext extends ParserRuleContext {
		public BlockStatementContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public StatOrDeclListContext statOrDeclList() {
			return getRuleContext(StatOrDeclListContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_blockStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(816);
			optAnnotations();
			setState(817);
			match(LCURL);
			setState(819);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (CONST - 10)) | (1L << (ERROR - 10)) | (1L << (EXIT - 10)) | (1L << (IF - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (RETURN - 10)) | (1L << (STATE - 10)) | (1L << (SWITCH - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (SEMI - 10)) | (1L << (AT - 10)) | (1L << (LCURL - 10)) | (1L << (DOT - 10)))) != 0)) {
				{
				setState(818);
				statOrDeclList();
				}
			}

			setState(821);
			match(RCURL);
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

	public static class StatOrDeclListContext extends ParserRuleContext {
		public StatOrDeclListContextExt extendedContext;
		public List<StatementOrDeclarationContext> statementOrDeclaration() {
			return getRuleContexts(StatementOrDeclarationContext.class);
		}
		public StatementOrDeclarationContext statementOrDeclaration(int i) {
			return getRuleContext(StatementOrDeclarationContext.class,i);
		}
		public StatOrDeclListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statOrDeclList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStatOrDeclList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStatOrDeclList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStatOrDeclList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatOrDeclListContext statOrDeclList() throws RecognitionException {
		StatOrDeclListContext _localctx = new StatOrDeclListContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_statOrDeclList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(823);
				statementOrDeclaration();
				}
				}
				setState(826); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (ACTIONS - 10)) | (1L << (APPLY - 10)) | (1L << (BOOL - 10)) | (1L << (BIT - 10)) | (1L << (CONST - 10)) | (1L << (ERROR - 10)) | (1L << (EXIT - 10)) | (1L << (IF - 10)) | (1L << (INT - 10)) | (1L << (KEY - 10)) | (1L << (RETURN - 10)) | (1L << (STATE - 10)) | (1L << (SWITCH - 10)) | (1L << (TUPLE - 10)) | (1L << (VARBIT - 10)) | (1L << (IDENTIFIER - 10)) | (1L << (SEMI - 10)) | (1L << (AT - 10)) | (1L << (LCURL - 10)) | (1L << (DOT - 10)))) != 0) );
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

	public static class SwitchStatementContext extends ParserRuleContext {
		public SwitchStatementContextExt extendedContext;
		public TerminalNode SWITCH() { return getToken(P416Parser.SWITCH, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public SwitchCasesContext switchCases() {
			return getRuleContext(SwitchCasesContext.class,0);
		}
		public SwitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSwitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSwitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(828);
			match(SWITCH);
			setState(829);
			match(LPARAN);
			setState(830);
			expression(0);
			setState(831);
			match(RPARAN);
			setState(832);
			match(LCURL);
			setState(834);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIONS) | (1L << APPLY) | (1L << EXTRACT) | (1L << DEFAULT) | (1L << ERROR) | (1L << KEY) | (1L << STATE) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(833);
				switchCases();
				}
			}

			setState(836);
			match(RCURL);
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

	public static class SwitchCasesContext extends ParserRuleContext {
		public SwitchCasesContextExt extendedContext;
		public List<SwitchCaseContext> switchCase() {
			return getRuleContexts(SwitchCaseContext.class);
		}
		public SwitchCaseContext switchCase(int i) {
			return getRuleContext(SwitchCaseContext.class,i);
		}
		public SwitchCasesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCases; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSwitchCases(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSwitchCases(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSwitchCases(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCasesContext switchCases() throws RecognitionException {
		SwitchCasesContext _localctx = new SwitchCasesContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_switchCases);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(839); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(838);
				switchCase();
				}
				}
				setState(841); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIONS) | (1L << APPLY) | (1L << EXTRACT) | (1L << DEFAULT) | (1L << ERROR) | (1L << KEY) | (1L << STATE) | (1L << IDENTIFIER))) != 0) );
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

	public static class SwitchCaseContext extends ParserRuleContext {
		public SwitchCaseContextExt extendedContext;
		public SwitchLabelContext switchLabel() {
			return getRuleContext(SwitchLabelContext.class,0);
		}
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public SwitchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSwitchCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSwitchCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSwitchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseContext switchCase() throws RecognitionException {
		SwitchCaseContext _localctx = new SwitchCaseContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_switchCase);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
			switchLabel();
			setState(844);
			match(COLON);
			setState(846);
			_la = _input.LA(1);
			if (_la==AT || _la==LCURL) {
				{
				setState(845);
				blockStatement();
				}
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

	public static class SwitchLabelContext extends ParserRuleContext {
		public SwitchLabelContextExt extendedContext;
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(P416Parser.DEFAULT, 0); }
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSwitchLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSwitchLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSwitchLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_switchLabel);
		try {
			setState(850);
			switch (_input.LA(1)) {
			case ACTIONS:
			case APPLY:
			case EXTRACT:
			case ERROR:
			case KEY:
			case STATE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(848);
				name();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(849);
				match(DEFAULT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class StatementOrDeclarationContext extends ParserRuleContext {
		public StatementOrDeclarationContextExt extendedContext;
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ConstantDeclarationContext constantDeclaration() {
			return getRuleContext(ConstantDeclarationContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public InstantiationContext instantiation() {
			return getRuleContext(InstantiationContext.class,0);
		}
		public StatementOrDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementOrDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStatementOrDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStatementOrDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStatementOrDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementOrDeclarationContext statementOrDeclaration() throws RecognitionException {
		StatementOrDeclarationContext _localctx = new StatementOrDeclarationContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_statementOrDeclaration);
		try {
			setState(856);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(852);
				variableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(853);
				constantDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(854);
				statement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(855);
				instantiation();
				}
				break;
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

	public static class TableDeclarationContext extends ParserRuleContext {
		public TableDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode TABLE() { return getToken(P416Parser.TABLE, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TablePropertyListContext tablePropertyList() {
			return getRuleContext(TablePropertyListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public TableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableDeclarationContext tableDeclaration() throws RecognitionException {
		TableDeclarationContext _localctx = new TableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_tableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			optAnnotations();
			setState(859);
			match(TABLE);
			setState(860);
			name();
			setState(861);
			match(LCURL);
			setState(862);
			tablePropertyList();
			setState(863);
			match(RCURL);
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

	public static class TablePropertyListContext extends ParserRuleContext {
		public TablePropertyListContextExt extendedContext;
		public List<TablePropertyContext> tableProperty() {
			return getRuleContexts(TablePropertyContext.class);
		}
		public TablePropertyContext tableProperty(int i) {
			return getRuleContext(TablePropertyContext.class,i);
		}
		public TablePropertyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tablePropertyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTablePropertyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTablePropertyList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTablePropertyList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TablePropertyListContext tablePropertyList() throws RecognitionException {
		TablePropertyListContext _localctx = new TablePropertyListContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_tablePropertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(865);
				tableProperty();
				}
				}
				setState(868); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIONS) | (1L << CONST) | (1L << KEY) | (1L << IDENTIFIER) | (1L << AT))) != 0) );
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

	public static class TablePropertyContext extends ParserRuleContext {
		public TablePropertyContextExt extendedContext;
		public TablePropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableProperty; }
	 
		public TablePropertyContext() { }
		public void copyFrom(TablePropertyContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class ConstEntriesContext extends TablePropertyContext {
		public TerminalNode CONST() { return getToken(P416Parser.CONST, 0); }
		public TerminalNode ENTRIES() { return getToken(P416Parser.ENTRIES, 0); }
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public EntriesListContext entriesList() {
			return getRuleContext(EntriesListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ConstEntriesContext(TablePropertyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterConstEntries(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitConstEntries(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitConstEntries(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LocalConstVariableContext extends TablePropertyContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public LocalConstVariableContext(TablePropertyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterLocalConstVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitLocalConstVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitLocalConstVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LocalVariableContext extends TablePropertyContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode CONST() { return getToken(P416Parser.CONST, 0); }
		public TerminalNode IDENTIFIER() { return getToken(P416Parser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public LocalVariableContext(TablePropertyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterLocalVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitLocalVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitLocalVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ActionsContext extends TablePropertyContext {
		public TerminalNode ACTIONS() { return getToken(P416Parser.ACTIONS, 0); }
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public ActionListContext actionList() {
			return getRuleContext(ActionListContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ActionsContext(TablePropertyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterActions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitActions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitActions(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class KeyContext extends TablePropertyContext {
		public TerminalNode KEY() { return getToken(P416Parser.KEY, 0); }
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public KeyElementListContext keyElementList() {
			return getRuleContext(KeyElementListContext.class,0);
		}
		public KeyContext(TablePropertyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TablePropertyContext tableProperty() throws RecognitionException {
		TablePropertyContext _localctx = new TablePropertyContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_tableProperty);
		int _la;
		try {
			setState(903);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				_localctx = new KeyContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(870);
				match(KEY);
				setState(871);
				match(ASSIGN);
				setState(872);
				match(LCURL);
				setState(874);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(873);
					keyElementList();
					}
				}

				setState(876);
				match(RCURL);
				}
				break;
			case 2:
				_localctx = new ActionsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(877);
				match(ACTIONS);
				setState(878);
				match(ASSIGN);
				setState(879);
				match(LCURL);
				setState(880);
				actionList();
				setState(881);
				match(RCURL);
				}
				break;
			case 3:
				_localctx = new ConstEntriesContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(883);
				match(CONST);
				setState(884);
				match(ENTRIES);
				setState(885);
				match(ASSIGN);
				setState(886);
				match(LCURL);
				setState(887);
				entriesList();
				setState(888);
				match(RCURL);
				}
				break;
			case 4:
				_localctx = new LocalVariableContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(890);
				optAnnotations();
				setState(891);
				match(CONST);
				setState(892);
				match(IDENTIFIER);
				setState(893);
				match(ASSIGN);
				setState(894);
				initializer();
				setState(895);
				match(SEMI);
				}
				break;
			case 5:
				_localctx = new LocalConstVariableContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(897);
				optAnnotations();
				setState(898);
				match(IDENTIFIER);
				setState(899);
				match(ASSIGN);
				setState(900);
				initializer();
				setState(901);
				match(SEMI);
				}
				break;
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

	public static class KeyElementListContext extends ParserRuleContext {
		public KeyElementListContextExt extendedContext;
		public List<KeyElementContext> keyElement() {
			return getRuleContexts(KeyElementContext.class);
		}
		public KeyElementContext keyElement(int i) {
			return getRuleContext(KeyElementContext.class,i);
		}
		public KeyElementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyElementList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterKeyElementList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitKeyElementList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitKeyElementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyElementListContext keyElementList() throws RecognitionException {
		KeyElementListContext _localctx = new KeyElementListContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_keyElementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(906); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(905);
				keyElement();
				}
				}
				setState(908); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0) );
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

	public static class KeyElementContext extends ParserRuleContext {
		public KeyElementContextExt extendedContext;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public KeyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterKeyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitKeyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitKeyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyElementContext keyElement() throws RecognitionException {
		KeyElementContext _localctx = new KeyElementContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_keyElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(910);
			expression(0);
			setState(911);
			match(COLON);
			setState(912);
			name();
			setState(913);
			optAnnotations();
			setState(914);
			match(SEMI);
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

	public static class ActionListContext extends ParserRuleContext {
		public ActionListContextExt extendedContext;
		public List<ActionRefContext> actionRef() {
			return getRuleContexts(ActionRefContext.class);
		}
		public ActionRefContext actionRef(int i) {
			return getRuleContext(ActionRefContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(P416Parser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(P416Parser.SEMI, i);
		}
		public ActionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterActionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitActionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitActionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionListContext actionList() throws RecognitionException {
		ActionListContext _localctx = new ActionListContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_actionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(916);
			actionRef();
			setState(917);
			match(SEMI);
			setState(923);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIONS) | (1L << APPLY) | (1L << EXTRACT) | (1L << ERROR) | (1L << KEY) | (1L << STATE) | (1L << IDENTIFIER) | (1L << AT))) != 0)) {
				{
				{
				setState(918);
				actionRef();
				setState(919);
				match(SEMI);
				}
				}
				setState(925);
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

	public static class EntriesListContext extends ParserRuleContext {
		public EntriesListContextExt extendedContext;
		public List<EntryContext> entry() {
			return getRuleContexts(EntryContext.class);
		}
		public EntryContext entry(int i) {
			return getRuleContext(EntryContext.class,i);
		}
		public EntriesListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entriesList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterEntriesList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitEntriesList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitEntriesList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntriesListContext entriesList() throws RecognitionException {
		EntriesListContext _localctx = new EntriesListContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_entriesList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(927); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(926);
				entry();
				}
				}
				setState(929); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << DEFAULT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << DONTCARE) | (1L << IDENTIFIER) | (1L << AT))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0) );
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

	public static class EntryContext extends ParserRuleContext {
		public EntryContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public KeySetExpressionContext keySetExpression() {
			return getRuleContext(KeySetExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public ActionRefContext actionRef() {
			return getRuleContext(ActionRefContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public EntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryContext entry() throws RecognitionException {
		EntryContext _localctx = new EntryContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_entry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(931);
			optAnnotations();
			setState(932);
			keySetExpression();
			setState(933);
			match(COLON);
			setState(934);
			actionRef();
			setState(935);
			match(SEMI);
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

	public static class ActionRefContext extends ParserRuleContext {
		public ActionRefContextExt extendedContext;
		public ActionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionRef; }
	 
		public ActionRefContext() { }
		public void copyFrom(ActionRefContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class ActionWithoutArgsContext extends ActionRefContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ActionWithoutArgsContext(ActionRefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterActionWithoutArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitActionWithoutArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitActionWithoutArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ActionWithArgsContext extends ActionRefContext {
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ActionWithArgsContext(ActionRefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterActionWithArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitActionWithArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitActionWithArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionRefContext actionRef() throws RecognitionException {
		ActionRefContext _localctx = new ActionRefContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_actionRef);
		int _la;
		try {
			setState(948);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				_localctx = new ActionWithoutArgsContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(937);
				optAnnotations();
				setState(938);
				name();
				}
				break;
			case 2:
				_localctx = new ActionWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(940);
				optAnnotations();
				setState(941);
				name();
				setState(942);
				match(LPARAN);
				setState(944);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(943);
					argumentList();
					}
				}

				setState(946);
				match(RPARAN);
				}
				break;
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

	public static class ActionDeclarationContext extends ParserRuleContext {
		public ActionDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode ACTION() { return getToken(P416Parser.ACTION, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ActionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterActionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitActionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitActionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionDeclarationContext actionDeclaration() throws RecognitionException {
		ActionDeclarationContext _localctx = new ActionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_actionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(950);
			optAnnotations();
			setState(951);
			match(ACTION);
			setState(952);
			name();
			setState(953);
			match(LPARAN);
			setState(955);
			_la = _input.LA(1);
			if (((((_la - 14)) & ~0x3f) == 0 && ((1L << (_la - 14)) & ((1L << (BOOL - 14)) | (1L << (BIT - 14)) | (1L << (ERROR - 14)) | (1L << (IN - 14)) | (1L << (INOUT - 14)) | (1L << (INT - 14)) | (1L << (OUT - 14)) | (1L << (TUPLE - 14)) | (1L << (VARBIT - 14)) | (1L << (IDENTIFIER - 14)) | (1L << (AT - 14)) | (1L << (DOT - 14)))) != 0)) {
				{
				setState(954);
				parameterList();
				}
			}

			setState(957);
			match(RPARAN);
			setState(958);
			blockStatement();
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

	public static class VariableDeclarationContext extends ParserRuleContext {
		public VariableDeclarationContextExt extendedContext;
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public OptInitializerContext optInitializer() {
			return getRuleContext(OptInitializerContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_variableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(961);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(960);
				annotations();
				}
			}

			setState(963);
			typeRef();
			setState(964);
			name();
			setState(966);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(965);
				optInitializer();
				}
			}

			setState(968);
			match(SEMI);
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

	public static class ConstantDeclarationContext extends ParserRuleContext {
		public ConstantDeclarationContextExt extendedContext;
		public OptAnnotationsContext optAnnotations() {
			return getRuleContext(OptAnnotationsContext.class,0);
		}
		public TerminalNode CONST() { return getToken(P416Parser.CONST, 0); }
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(P416Parser.SEMI, 0); }
		public ConstantDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterConstantDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitConstantDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitConstantDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantDeclarationContext constantDeclaration() throws RecognitionException {
		ConstantDeclarationContext _localctx = new ConstantDeclarationContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_constantDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			optAnnotations();
			setState(971);
			match(CONST);
			setState(972);
			typeRef();
			setState(973);
			name();
			setState(974);
			match(ASSIGN);
			setState(975);
			initializer();
			setState(976);
			match(SEMI);
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

	public static class OptInitializerContext extends ParserRuleContext {
		public OptInitializerContextExt extendedContext;
		public TerminalNode ASSIGN() { return getToken(P416Parser.ASSIGN, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public OptInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOptInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOptInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOptInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptInitializerContext optInitializer() throws RecognitionException {
		OptInitializerContext _localctx = new OptInitializerContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_optInitializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(978);
			match(ASSIGN);
			setState(979);
			initializer();
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

	public static class InitializerContext extends ParserRuleContext {
		public InitializerContextExt extendedContext;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializerContext initializer() throws RecognitionException {
		InitializerContext _localctx = new InitializerContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_initializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			expression(0);
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

	public static class ArgumentListContext extends ParserRuleContext {
		public ArgumentListContextExt extendedContext;
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(983);
			argument();
			setState(988);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(984);
				match(COMMA);
				setState(985);
				argument();
				}
				}
				setState(990);
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

	public static class ArgumentContext extends ParserRuleContext {
		public ArgumentContextExt extendedContext;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(991);
			expression(0);
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

	public static class ExpressionListContext extends ParserRuleContext {
		public ExpressionListContextExt extendedContext;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(P416Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(P416Parser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(993);
			expression(0);
			setState(998);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(994);
				match(COMMA);
				setState(995);
				expression(0);
				}
				}
				setState(1000);
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

	public static class MemberContext extends ParserRuleContext {
		public MemberContextExt extendedContext;
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_member);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			name();
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

	public static class PrefixedNonTypeNameContext extends ParserRuleContext {
		public PrefixedNonTypeNameContextExt extendedContext;
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public DotPrefixContext dotPrefix() {
			return getRuleContext(DotPrefixContext.class,0);
		}
		public PrefixedNonTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixedNonTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPrefixedNonTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPrefixedNonTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPrefixedNonTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixedNonTypeNameContext prefixedNonTypeName() throws RecognitionException {
		PrefixedNonTypeNameContext _localctx = new PrefixedNonTypeNameContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_prefixedNonTypeName);
		try {
			setState(1007);
			switch (_input.LA(1)) {
			case ACTIONS:
			case APPLY:
			case KEY:
			case STATE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1003);
				nonTypeName();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1004);
				dotPrefix();
				setState(1005);
				nonTypeName();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class LvalueContext extends ParserRuleContext {
		public LvalueContextExt extendedContext;
		public LvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lvalue; }
	 
		public LvalueContext() { }
		public void copyFrom(LvalueContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class PrefixedNameLvalueContext extends LvalueContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public MemberContext member() {
			return getRuleContext(MemberContext.class,0);
		}
		public PrefixedNameLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPrefixedNameLvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPrefixedNameLvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPrefixedNameLvalue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayIndexLvalueContext extends LvalueContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode LBRKT() { return getToken(P416Parser.LBRKT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRKT() { return getToken(P416Parser.RBRKT, 0); }
		public ArrayIndexLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterArrayIndexLvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitArrayIndexLvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitArrayIndexLvalue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RangeIndexLvalueContext extends LvalueContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode LBRKT() { return getToken(P416Parser.LBRKT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public TerminalNode RBRKT() { return getToken(P416Parser.RBRKT, 0); }
		public RangeIndexLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterRangeIndexLvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitRangeIndexLvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitRangeIndexLvalue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrefixedNonTypeNameLvalueContext extends LvalueContext {
		public PrefixedNonTypeNameContext prefixedNonTypeName() {
			return getRuleContext(PrefixedNonTypeNameContext.class,0);
		}
		public PrefixedNonTypeNameLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPrefixedNonTypeNameLvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPrefixedNonTypeNameLvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPrefixedNonTypeNameLvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LvalueContext lvalue() throws RecognitionException {
		return lvalue(0);
	}

	private LvalueContext lvalue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LvalueContext _localctx = new LvalueContext(_ctx, _parentState);
		LvalueContext _prevctx = _localctx;
		int _startState = 198;
		enterRecursionRule(_localctx, 198, RULE_lvalue, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new PrefixedNonTypeNameLvalueContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(1010);
			prefixedNonTypeName();
			}
			_ctx.stop = _input.LT(-1);
			setState(1029);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,97,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1027);
					switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
					case 1:
						{
						_localctx = new PrefixedNameLvalueContext(new LvalueContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_lvalue);
						setState(1012);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1013);
						match(DOT);
						setState(1014);
						member();
						}
						break;
					case 2:
						{
						_localctx = new ArrayIndexLvalueContext(new LvalueContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_lvalue);
						setState(1015);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1016);
						match(LBRKT);
						setState(1017);
						expression(0);
						setState(1018);
						match(RBRKT);
						}
						break;
					case 3:
						{
						_localctx = new RangeIndexLvalueContext(new LvalueContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_lvalue);
						setState(1020);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(1021);
						match(LBRKT);
						setState(1022);
						expression(0);
						setState(1023);
						match(COLON);
						setState(1024);
						expression(0);
						setState(1025);
						match(RBRKT);
						}
						break;
					}
					} 
				}
				setState(1031);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,97,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContextExt extendedContext;
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
			this.extendedContext = ctx.extendedContext;
		}
	}
	public static class MinusContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MINUS() { return getToken(P416Parser.MINUS, 0); }
		public MinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RangeIndexContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LBRKT() { return getToken(P416Parser.LBRKT, 0); }
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public TerminalNode RBRKT() { return getToken(P416Parser.RBRKT, 0); }
		public RangeIndexContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterRangeIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitRangeIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitRangeIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ModContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PRCNT() { return getToken(P416Parser.PRCNT, 0); }
		public ModContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMod(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends ExpressionContext {
		public TerminalNode STRING_LITERAL() { return getToken(P416Parser.STRING_LITERAL, 0); }
		public StringContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitOrContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OR() { return getToken(P416Parser.OR, 0); }
		public BitOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBitOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrefixedNonTypeContext extends ExpressionContext {
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public PrefixedNonTypeContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPrefixedNonType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPrefixedNonType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPrefixedNonType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerContext extends ExpressionContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public IntegerContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitInteger(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CastContext extends ExpressionContext {
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CastContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterCast(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitCast(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitCast(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotContext extends ExpressionContext {
		public UnaryExpression_notContext unaryExpression_not() {
			return getRuleContext(UnaryExpression_notContext.class,0);
		}
		public NotContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShiftLeftContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SHL() { return getToken(P416Parser.SHL, 0); }
		public ShiftLeftContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterShiftLeft(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitShiftLeft(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitShiftLeft(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PlusPlusContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PP() { return getToken(P416Parser.PP, 0); }
		public PlusPlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPlusPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPlusPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPlusPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LAND() { return getToken(P416Parser.LAND, 0); }
		public AndContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OfContext extends ExpressionContext {
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public OfContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOf(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public LessThanContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterLessThan(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitLessThan(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitLessThan(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TempletizedFunctionCallContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LT() { return getToken(P416Parser.LT, 0); }
		public TypeArgumentListContext typeArgumentList() {
			return getRuleContext(TypeArgumentListContext.class,0);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public TempletizedFunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTempletizedFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTempletizedFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTempletizedFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GreaterThanContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GT() { return getToken(P416Parser.GT, 0); }
		public GreaterThanContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterGreaterThan(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitGreaterThan(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitGreaterThan(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MemberAccessContext extends ExpressionContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public MemberContext member() {
			return getRuleContext(MemberContext.class,0);
		}
		public MemberAccessContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterMemberAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitMemberAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprMemberAccessContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public MemberContext member() {
			return getRuleContext(MemberContext.class,0);
		}
		public ExprMemberAccessContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterExprMemberAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitExprMemberAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitExprMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetContext extends ExpressionContext {
		public TerminalNode LCURL() { return getToken(P416Parser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(P416Parser.RCURL, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public SetContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShifRightContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> GT() { return getTokens(P416Parser.GT); }
		public TerminalNode GT(int i) {
			return getToken(P416Parser.GT, i);
		}
		public ShifRightContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterShifRight(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitShifRight(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitShifRight(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LOR() { return getToken(P416Parser.LOR, 0); }
		public OrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StarContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode STAR() { return getToken(P416Parser.STAR, 0); }
		public StarContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterStar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitStar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitStar(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FalseContext extends ExpressionContext {
		public TerminalNode FALSE() { return getToken(P416Parser.FALSE, 0); }
		public FalseContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterFalse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitFalse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstructorContext extends ExpressionContext {
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ConstructorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitConstructor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotEqualContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode NE() { return getToken(P416Parser.NE, 0); }
		public NotEqualContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNotEqual(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNotEqual(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNotEqual(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NonTypeContext extends ExpressionContext {
		public NonTypeNameContext nonTypeName() {
			return getRuleContext(NonTypeNameContext.class,0);
		}
		public NonTypeContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNonType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNonType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNonType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PlusContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(P416Parser.PLUS, 0); }
		public PlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GreaterThanOrEqualContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GE() { return getToken(P416Parser.GE, 0); }
		public GreaterThanOrEqualContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterGreaterThanOrEqual(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitGreaterThanOrEqual(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitGreaterThanOrEqual(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQ() { return getToken(P416Parser.EQ, 0); }
		public EqualContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterEqual(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitEqual(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitEqual(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitAndContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(P416Parser.AND, 0); }
		public BitAndContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBitAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryPlusContext extends ExpressionContext {
		public UnaryExpression_plusContext unaryExpression_plus() {
			return getRuleContext(UnaryExpression_plusContext.class,0);
		}
		public UnaryPlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SlahContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(P416Parser.SLASH, 0); }
		public SlahContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterSlah(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitSlah(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitSlah(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegateContext extends ExpressionContext {
		public UnaryExpression_tildaContext unaryExpression_tilda() {
			return getRuleContext(UnaryExpression_tildaContext.class,0);
		}
		public NegateContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNegate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNegate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNegate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitXorContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode XOR() { return getToken(P416Parser.XOR, 0); }
		public BitXorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBitXor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBitXor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBitXor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanOrEqualContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LE() { return getToken(P416Parser.LE, 0); }
		public LessThanOrEqualContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterLessThanOrEqual(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitLessThanOrEqual(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitLessThanOrEqual(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LPARAN() { return getToken(P416Parser.LPARAN, 0); }
		public TerminalNode RPARAN() { return getToken(P416Parser.RPARAN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TrueContext extends ExpressionContext {
		public TerminalNode TRUE() { return getToken(P416Parser.TRUE, 0); }
		public TrueContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTrue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTrue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryMinusContext extends ExpressionContext {
		public UnaryExpression_minusContext unaryExpression_minus() {
			return getRuleContext(UnaryExpression_minusContext.class,0);
		}
		public UnaryMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ErrorMemberAccessContext extends ExpressionContext {
		public TerminalNode ERROR() { return getToken(P416Parser.ERROR, 0); }
		public TerminalNode DOT() { return getToken(P416Parser.DOT, 0); }
		public MemberContext member() {
			return getRuleContext(MemberContext.class,0);
		}
		public ErrorMemberAccessContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterErrorMemberAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitErrorMemberAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitErrorMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayIndexContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LBRKT() { return getToken(P416Parser.LBRKT, 0); }
		public TerminalNode RBRKT() { return getToken(P416Parser.RBRKT, 0); }
		public ArrayIndexContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterArrayIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitArrayIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitArrayIndex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TernaryContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode QUESTION() { return getToken(P416Parser.QUESTION, 0); }
		public TerminalNode COLON() { return getToken(P416Parser.COLON, 0); }
		public TernaryContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterTernary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitTernary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitTernary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 200;
		enterRecursionRule(_localctx, 200, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1072);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				{
				_localctx = new CastContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1033);
				match(LPARAN);
				setState(1034);
				typeRef();
				setState(1035);
				match(RPARAN);
				setState(1036);
				expression(32);
				}
				break;
			case 2:
				{
				_localctx = new IntegerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1038);
				number();
				}
				break;
			case 3:
				{
				_localctx = new TrueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1039);
				match(TRUE);
				}
				break;
			case 4:
				{
				_localctx = new FalseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1040);
				match(FALSE);
				}
				break;
			case 5:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1041);
				match(STRING_LITERAL);
				}
				break;
			case 6:
				{
				_localctx = new NonTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1042);
				nonTypeName();
				}
				break;
			case 7:
				{
				_localctx = new PrefixedNonTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1043);
				match(DOT);
				setState(1044);
				nonTypeName();
				}
				break;
			case 8:
				{
				_localctx = new ConstructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1045);
				typeRef();
				setState(1046);
				match(LPARAN);
				setState(1048);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(1047);
					argumentList();
					}
				}

				setState(1050);
				match(RPARAN);
				}
				break;
			case 9:
				{
				_localctx = new SetContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1052);
				match(LCURL);
				setState(1054);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
					{
					setState(1053);
					expressionList();
					}
				}

				setState(1056);
				match(RCURL);
				}
				break;
			case 10:
				{
				_localctx = new OfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1057);
				match(LPARAN);
				setState(1058);
				expression(0);
				setState(1059);
				match(RPARAN);
				}
				break;
			case 11:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1061);
				unaryExpression_not();
				}
				break;
			case 12:
				{
				_localctx = new NegateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1062);
				unaryExpression_tilda();
				}
				break;
			case 13:
				{
				_localctx = new UnaryMinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1063);
				unaryExpression_minus();
				}
				break;
			case 14:
				{
				_localctx = new UnaryPlusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1064);
				unaryExpression_plus();
				}
				break;
			case 15:
				{
				_localctx = new MemberAccessContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1065);
				typeName();
				setState(1066);
				match(DOT);
				setState(1067);
				member();
				}
				break;
			case 16:
				{
				_localctx = new ErrorMemberAccessContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1069);
				match(ERROR);
				setState(1070);
				match(DOT);
				setState(1071);
				member();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1171);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1169);
					switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
					case 1:
						{
						_localctx = new StarContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1074);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(1075);
						match(STAR);
						setState(1076);
						expression(21);
						}
						break;
					case 2:
						{
						_localctx = new SlahContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1077);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(1078);
						match(SLASH);
						setState(1079);
						expression(20);
						}
						break;
					case 3:
						{
						_localctx = new ModContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1080);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1081);
						match(PRCNT);
						setState(1082);
						expression(19);
						}
						break;
					case 4:
						{
						_localctx = new PlusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1083);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1084);
						match(PLUS);
						setState(1085);
						expression(18);
						}
						break;
					case 5:
						{
						_localctx = new MinusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1086);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1087);
						match(MINUS);
						setState(1088);
						expression(17);
						}
						break;
					case 6:
						{
						_localctx = new ShiftLeftContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1089);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(1090);
						match(SHL);
						setState(1091);
						expression(16);
						}
						break;
					case 7:
						{
						_localctx = new ShifRightContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1092);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(1093);
						match(GT);
						setState(1094);
						match(GT);
						setState(1095);
						expression(15);
						}
						break;
					case 8:
						{
						_localctx = new BitAndContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1096);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1097);
						match(AND);
						setState(1098);
						expression(14);
						}
						break;
					case 9:
						{
						_localctx = new BitXorContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1099);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1100);
						match(XOR);
						setState(1101);
						expression(13);
						}
						break;
					case 10:
						{
						_localctx = new BitOrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1102);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1103);
						match(OR);
						setState(1104);
						expression(12);
						}
						break;
					case 11:
						{
						_localctx = new PlusPlusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1105);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1106);
						match(PP);
						setState(1107);
						expression(11);
						}
						break;
					case 12:
						{
						_localctx = new LessThanOrEqualContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1108);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1109);
						match(LE);
						setState(1110);
						expression(10);
						}
						break;
					case 13:
						{
						_localctx = new GreaterThanOrEqualContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1111);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1112);
						match(GE);
						setState(1113);
						expression(9);
						}
						break;
					case 14:
						{
						_localctx = new LessThanContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1114);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1115);
						match(LT);
						setState(1116);
						expression(8);
						}
						break;
					case 15:
						{
						_localctx = new GreaterThanContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1117);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1118);
						match(GT);
						setState(1119);
						expression(7);
						}
						break;
					case 16:
						{
						_localctx = new NotEqualContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1120);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1121);
						match(NE);
						setState(1122);
						expression(6);
						}
						break;
					case 17:
						{
						_localctx = new EqualContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1123);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1124);
						match(EQ);
						setState(1125);
						expression(5);
						}
						break;
					case 18:
						{
						_localctx = new AndContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1126);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1127);
						match(LAND);
						setState(1128);
						expression(4);
						}
						break;
					case 19:
						{
						_localctx = new OrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1129);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1130);
						match(LOR);
						setState(1131);
						expression(3);
						}
						break;
					case 20:
						{
						_localctx = new TernaryContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1132);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(1133);
						match(QUESTION);
						setState(1134);
						expression(0);
						setState(1135);
						match(COLON);
						setState(1136);
						expression(2);
						}
						break;
					case 21:
						{
						_localctx = new FunctionCallContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1138);
						if (!(precpred(_ctx, 35))) throw new FailedPredicateException(this, "precpred(_ctx, 35)");
						setState(1139);
						match(LPARAN);
						setState(1141);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
							{
							setState(1140);
							argumentList();
							}
						}

						setState(1143);
						match(RPARAN);
						}
						break;
					case 22:
						{
						_localctx = new TempletizedFunctionCallContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1144);
						if (!(precpred(_ctx, 34))) throw new FailedPredicateException(this, "precpred(_ctx, 34)");
						setState(1145);
						match(LT);
						setState(1146);
						typeArgumentList();
						setState(1147);
						match(GT);
						setState(1148);
						match(LPARAN);
						setState(1150);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Hex_number) | (1L << Decimal_number) | (1L << Octal_number) | (1L << Binary_number) | (1L << Real_number) | (1L << ACTIONS) | (1L << APPLY) | (1L << BOOL) | (1L << BIT) | (1L << ERROR) | (1L << FALSE) | (1L << INT) | (1L << KEY) | (1L << STATE) | (1L << TRUE) | (1L << TUPLE) | (1L << VARBIT) | (1L << IDENTIFIER))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LPARAN - 65)) | (1L << (LCURL - 65)) | (1L << (DOT - 65)) | (1L << (NOT - 65)) | (1L << (TILDA - 65)) | (1L << (MINUS - 65)) | (1L << (PLUS - 65)) | (1L << (STRING_LITERAL - 65)))) != 0)) {
							{
							setState(1149);
							argumentList();
							}
						}

						setState(1152);
						match(RPARAN);
						}
						break;
					case 23:
						{
						_localctx = new ArrayIndexContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1154);
						if (!(precpred(_ctx, 31))) throw new FailedPredicateException(this, "precpred(_ctx, 31)");
						setState(1155);
						match(LBRKT);
						setState(1156);
						expression(0);
						setState(1157);
						match(RBRKT);
						}
						break;
					case 24:
						{
						_localctx = new RangeIndexContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1159);
						if (!(precpred(_ctx, 30))) throw new FailedPredicateException(this, "precpred(_ctx, 30)");
						setState(1160);
						match(LBRKT);
						setState(1161);
						expression(0);
						setState(1162);
						match(COLON);
						setState(1163);
						expression(0);
						setState(1164);
						match(RBRKT);
						}
						break;
					case 25:
						{
						_localctx = new ExprMemberAccessContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1166);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(1167);
						match(DOT);
						setState(1168);
						member();
						}
						break;
					}
					} 
				}
				setState(1173);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class UnaryExpression_notContext extends ParserRuleContext {
		public UnaryExpression_notContextExt extendedContext;
		public TerminalNode NOT() { return getToken(P416Parser.NOT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExpression_notContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression_not; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryExpression_not(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryExpression_not(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryExpression_not(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpression_notContext unaryExpression_not() throws RecognitionException {
		UnaryExpression_notContext _localctx = new UnaryExpression_notContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_unaryExpression_not);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1174);
			match(NOT);
			setState(1175);
			expression(0);
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

	public static class UnaryExpression_tildaContext extends ParserRuleContext {
		public UnaryExpression_tildaContextExt extendedContext;
		public TerminalNode TILDA() { return getToken(P416Parser.TILDA, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExpression_tildaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression_tilda; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryExpression_tilda(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryExpression_tilda(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryExpression_tilda(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpression_tildaContext unaryExpression_tilda() throws RecognitionException {
		UnaryExpression_tildaContext _localctx = new UnaryExpression_tildaContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_unaryExpression_tilda);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1177);
			match(TILDA);
			setState(1178);
			expression(0);
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

	public static class UnaryExpression_plusContext extends ParserRuleContext {
		public UnaryExpression_plusContextExt extendedContext;
		public TerminalNode PLUS() { return getToken(P416Parser.PLUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExpression_plusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression_plus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryExpression_plus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryExpression_plus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryExpression_plus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpression_plusContext unaryExpression_plus() throws RecognitionException {
		UnaryExpression_plusContext _localctx = new UnaryExpression_plusContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_unaryExpression_plus);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1180);
			match(PLUS);
			setState(1181);
			expression(0);
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

	public static class UnaryExpression_minusContext extends ParserRuleContext {
		public UnaryExpression_minusContextExt extendedContext;
		public TerminalNode MINUS() { return getToken(P416Parser.MINUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExpression_minusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression_minus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterUnaryExpression_minus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitUnaryExpression_minus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitUnaryExpression_minus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpression_minusContext unaryExpression_minus() throws RecognitionException {
		UnaryExpression_minusContext _localctx = new UnaryExpression_minusContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_unaryExpression_minus);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1183);
			match(MINUS);
			setState(1184);
			expression(0);
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

	public static class NumberContext extends ParserRuleContext {
		public NumberContextExt extendedContext;
		public DecimalNumberContext decimalNumber() {
			return getRuleContext(DecimalNumberContext.class,0);
		}
		public OctalNumberContext octalNumber() {
			return getRuleContext(OctalNumberContext.class,0);
		}
		public BinaryNumberContext binaryNumber() {
			return getRuleContext(BinaryNumberContext.class,0);
		}
		public HexNumberContext hexNumber() {
			return getRuleContext(HexNumberContext.class,0);
		}
		public RealNumberContext realNumber() {
			return getRuleContext(RealNumberContext.class,0);
		}
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_number);
		try {
			setState(1191);
			switch (_input.LA(1)) {
			case Decimal_number:
				enterOuterAlt(_localctx, 1);
				{
				setState(1186);
				decimalNumber();
				}
				break;
			case Octal_number:
				enterOuterAlt(_localctx, 2);
				{
				setState(1187);
				octalNumber();
				}
				break;
			case Binary_number:
				enterOuterAlt(_localctx, 3);
				{
				setState(1188);
				binaryNumber();
				}
				break;
			case Hex_number:
				enterOuterAlt(_localctx, 4);
				{
				setState(1189);
				hexNumber();
				}
				break;
			case Real_number:
				enterOuterAlt(_localctx, 5);
				{
				setState(1190);
				realNumber();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class DecimalNumberContext extends ParserRuleContext {
		public DecimalNumberContextExt extendedContext;
		public TerminalNode Decimal_number() { return getToken(P416Parser.Decimal_number, 0); }
		public DecimalNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterDecimalNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitDecimalNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitDecimalNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalNumberContext decimalNumber() throws RecognitionException {
		DecimalNumberContext _localctx = new DecimalNumberContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_decimalNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1193);
			match(Decimal_number);
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

	public static class OctalNumberContext extends ParserRuleContext {
		public OctalNumberContextExt extendedContext;
		public TerminalNode Octal_number() { return getToken(P416Parser.Octal_number, 0); }
		public OctalNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_octalNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterOctalNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitOctalNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitOctalNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OctalNumberContext octalNumber() throws RecognitionException {
		OctalNumberContext _localctx = new OctalNumberContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_octalNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1195);
			match(Octal_number);
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

	public static class BinaryNumberContext extends ParserRuleContext {
		public BinaryNumberContextExt extendedContext;
		public TerminalNode Binary_number() { return getToken(P416Parser.Binary_number, 0); }
		public BinaryNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterBinaryNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitBinaryNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitBinaryNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryNumberContext binaryNumber() throws RecognitionException {
		BinaryNumberContext _localctx = new BinaryNumberContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_binaryNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1197);
			match(Binary_number);
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

	public static class HexNumberContext extends ParserRuleContext {
		public HexNumberContextExt extendedContext;
		public TerminalNode Hex_number() { return getToken(P416Parser.Hex_number, 0); }
		public HexNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hexNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterHexNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitHexNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitHexNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HexNumberContext hexNumber() throws RecognitionException {
		HexNumberContext _localctx = new HexNumberContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_hexNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(Hex_number);
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

	public static class RealNumberContext extends ParserRuleContext {
		public RealNumberContextExt extendedContext;
		public TerminalNode Real_number() { return getToken(P416Parser.Real_number, 0); }
		public RealNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_realNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).enterRealNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof P416Listener ) ((P416Listener)listener).exitRealNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof P416Visitor ) return ((P416Visitor<? extends T>)visitor).visitRealNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RealNumberContext realNumber() throws RecognitionException {
		RealNumberContext _localctx = new RealNumberContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_realNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1201);
			match(Real_number);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 99:
			return lvalue_sempred((LvalueContext)_localctx, predIndex);
		case 100:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean lvalue_sempred(LvalueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);
		case 1:
			return precpred(_ctx, 2);
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 20);
		case 4:
			return precpred(_ctx, 19);
		case 5:
			return precpred(_ctx, 18);
		case 6:
			return precpred(_ctx, 17);
		case 7:
			return precpred(_ctx, 16);
		case 8:
			return precpred(_ctx, 15);
		case 9:
			return precpred(_ctx, 14);
		case 10:
			return precpred(_ctx, 13);
		case 11:
			return precpred(_ctx, 12);
		case 12:
			return precpred(_ctx, 11);
		case 13:
			return precpred(_ctx, 10);
		case 14:
			return precpred(_ctx, 9);
		case 15:
			return precpred(_ctx, 8);
		case 16:
			return precpred(_ctx, 7);
		case 17:
			return precpred(_ctx, 6);
		case 18:
			return precpred(_ctx, 5);
		case 19:
			return precpred(_ctx, 4);
		case 20:
			return precpred(_ctx, 3);
		case 21:
			return precpred(_ctx, 2);
		case 22:
			return precpred(_ctx, 1);
		case 23:
			return precpred(_ctx, 35);
		case 24:
			return precpred(_ctx, 34);
		case 25:
			return precpred(_ctx, 31);
		case 26:
			return precpred(_ctx, 30);
		case 27:
			return precpred(_ctx, 21);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3X\u04b6\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\3\2\3\2\3\2\5\2\u00e4\n\2\7\2\u00e6\n"+
		"\2\f\2\16\2\u00e9\13\2\5\2\u00eb\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u00f6\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5\u00ff\n\5\3\6\5\6\u0102"+
		"\n\6\3\7\6\7\u0105\n\7\r\7\16\7\u0106\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u010f"+
		"\n\b\3\b\3\b\5\b\u0113\n\b\3\t\3\t\3\t\7\t\u0118\n\t\f\t\16\t\u011b\13"+
		"\t\3\n\3\n\5\n\u011f\n\n\3\n\3\n\3\n\3\13\3\13\3\13\5\13\u0127\n\13\3"+
		"\f\3\f\3\f\3\f\5\f\u012d\n\f\3\f\3\f\5\f\u0131\n\f\3\f\3\f\3\r\5\r\u0136"+
		"\n\r\3\r\3\r\3\r\5\r\u013b\n\r\3\r\3\r\3\r\3\r\3\16\3\16\5\16\u0143\n"+
		"\16\3\16\3\16\3\17\3\17\3\20\3\20\5\20\u014b\n\20\3\20\3\20\5\20\u014f"+
		"\n\20\3\20\3\20\3\20\3\21\6\21\u0155\n\21\r\21\16\21\u0156\3\22\3\22\3"+
		"\22\5\22\u015c\n\22\3\23\3\23\3\23\3\23\5\23\u0162\n\23\3\23\3\23\5\23"+
		"\u0166\n\23\3\23\3\23\3\24\6\24\u016b\n\24\r\24\16\24\u016c\3\25\3\25"+
		"\3\25\3\25\3\25\5\25\u0174\n\25\3\25\5\25\u0177\n\25\3\25\3\25\3\26\6"+
		"\26\u017c\n\26\r\26\16\26\u017d\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0186"+
		"\n\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\5\32"+
		"\u0194\n\32\3\33\3\33\3\33\5\33\u0199\n\33\3\33\3\33\3\33\5\33\u019e\n"+
		"\33\3\33\3\33\3\34\6\34\u01a3\n\34\r\34\16\34\u01a4\3\35\3\35\3\35\3\35"+
		"\3\35\3\36\3\36\5\36\u01ae\n\36\3\37\3\37\3\37\3\37\7\37\u01b4\n\37\f"+
		"\37\16\37\u01b7\13\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \5 \u01c6"+
		"\n \3!\3!\5!\u01ca\n!\3!\3!\5!\u01ce\n!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\5"+
		"\"\u01d8\n\"\3\"\3\"\5\"\u01dc\n\"\3\"\3\"\3#\6#\u01e1\n#\r#\16#\u01e2"+
		"\3$\3$\3$\3$\3$\5$\u01ea\n$\3%\3%\3&\3&\3&\3&\5&\u01f2\n&\3&\3&\5&\u01f6"+
		"\n&\3&\3&\3&\3&\3&\3&\3&\5&\u01ff\n&\3\'\6\'\u0202\n\'\r\'\16\'\u0203"+
		"\3(\3(\3(\5(\u0209\n(\3(\3(\5(\u020d\n(\3(\3(\3)\3)\3)\3)\3)\3)\5)\u0217"+
		"\n)\3)\3)\5)\u021b\n)\3*\3*\3*\3*\3*\5*\u0222\n*\3+\3+\3+\3+\3+\5+\u0229"+
		"\n+\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\5\60\u024e\n\60\3\61\3\61\3\61\5\61\u0253\n\61\3\62\3\62\3\62\3"+
		"\62\3\63\3\63\3\63\7\63\u025c\n\63\f\63\16\63\u025f\13\63\3\64\3\64\5"+
		"\64\u0263\n\64\3\65\3\65\3\66\3\66\3\66\7\66\u026a\n\66\f\66\16\66\u026d"+
		"\13\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\5\67\u027a"+
		"\n\67\38\38\38\38\58\u0280\n8\39\39\39\39\39\59\u0287\n9\39\39\3:\3:\3"+
		":\3:\3:\5:\u0290\n:\3:\3:\3;\3;\3;\3;\3;\5;\u0299\n;\3;\3;\3<\6<\u029e"+
		"\n<\r<\16<\u029f\3=\3=\3=\3=\3=\3>\3>\3>\3>\3>\3>\3>\3?\3?\3?\3?\3?\3"+
		"@\3@\3@\3@\3@\3A\3A\3A\7A\u02bb\nA\fA\16A\u02be\13A\3B\5B\u02c1\nB\3B"+
		"\3B\3B\3B\3B\3B\5B\u02c9\nB\3B\3B\3B\3B\3B\5B\u02d0\nB\3C\3C\3C\3C\3C"+
		"\3D\3D\3D\3D\3D\5D\u02dc\nD\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\5D\u02e8\nD"+
		"\3D\3D\5D\u02ec\nD\3D\3D\3D\3D\3D\3D\5D\u02f4\nD\3D\3D\3D\3D\3D\3D\3D"+
		"\3D\3D\5D\u02ff\nD\3D\3D\3D\5D\u0304\nD\3E\3E\3F\3F\3F\3G\3G\3G\3H\3H"+
		"\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\5H\u031c\nH\3I\3I\3I\3I\3I\5I\u0323"+
		"\nI\3I\3I\3I\3J\3J\3J\3J\3J\3J\3J\3J\3J\5J\u0331\nJ\3K\3K\3K\5K\u0336"+
		"\nK\3K\3K\3L\6L\u033b\nL\rL\16L\u033c\3M\3M\3M\3M\3M\3M\5M\u0345\nM\3"+
		"M\3M\3N\6N\u034a\nN\rN\16N\u034b\3O\3O\3O\5O\u0351\nO\3P\3P\5P\u0355\n"+
		"P\3Q\3Q\3Q\3Q\5Q\u035b\nQ\3R\3R\3R\3R\3R\3R\3R\3S\6S\u0365\nS\rS\16S\u0366"+
		"\3T\3T\3T\3T\5T\u036d\nT\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T"+
		"\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\5T\u038a\nT\3U\6U\u038d\nU\rU\16"+
		"U\u038e\3V\3V\3V\3V\3V\3V\3W\3W\3W\3W\3W\7W\u039c\nW\fW\16W\u039f\13W"+
		"\3X\6X\u03a2\nX\rX\16X\u03a3\3Y\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\3Z\3Z\3Z\3Z\5"+
		"Z\u03b3\nZ\3Z\3Z\5Z\u03b7\nZ\3[\3[\3[\3[\3[\5[\u03be\n[\3[\3[\3[\3\\\5"+
		"\\\u03c4\n\\\3\\\3\\\3\\\5\\\u03c9\n\\\3\\\3\\\3]\3]\3]\3]\3]\3]\3]\3"+
		"]\3^\3^\3^\3_\3_\3`\3`\3`\7`\u03dd\n`\f`\16`\u03e0\13`\3a\3a\3b\3b\3b"+
		"\7b\u03e7\nb\fb\16b\u03ea\13b\3c\3c\3d\3d\3d\3d\5d\u03f2\nd\3e\3e\3e\3"+
		"e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\3e\7e\u0406\ne\fe\16e\u0409\13"+
		"e\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\5f\u041b\nf\3f\3f\3"+
		"f\3f\5f\u0421\nf\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\5f\u0433"+
		"\nf\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f"+
		"\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f"+
		"\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\3f\5f"+
		"\u0478\nf\3f\3f\3f\3f\3f\3f\3f\5f\u0481\nf\3f\3f\3f\3f\3f\3f\3f\3f\3f"+
		"\3f\3f\3f\3f\3f\3f\3f\3f\7f\u0494\nf\ff\16f\u0497\13f\3g\3g\3g\3h\3h\3"+
		"h\3i\3i\3i\3j\3j\3j\3k\3k\3k\3k\3k\5k\u04aa\nk\3l\3l\3m\3m\3n\3n\3o\3"+
		"o\3p\3p\3p\2\4\u00c8\u00caq\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c"+
		"\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4"+
		"\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc"+
		"\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\2\3\7\2\f\f\16"+
		"\16!!((\64\64\u050b\2\u00ea\3\2\2\2\4\u00f5\3\2\2\2\6\u00f7\3\2\2\2\b"+
		"\u00fe\3\2\2\2\n\u0101\3\2\2\2\f\u0104\3\2\2\2\16\u0112\3\2\2\2\20\u0114"+
		"\3\2\2\2\22\u011c\3\2\2\2\24\u0126\3\2\2\2\26\u0128\3\2\2\2\30\u0135\3"+
		"\2\2\2\32\u0140\3\2\2\2\34\u0146\3\2\2\2\36\u0148\3\2\2\2 \u0154\3\2\2"+
		"\2\"\u015b\3\2\2\2$\u015d\3\2\2\2&\u016a\3\2\2\2(\u016e\3\2\2\2*\u017b"+
		"\3\2\2\2,\u0185\3\2\2\2.\u0187\3\2\2\2\60\u018c\3\2\2\2\62\u0193\3\2\2"+
		"\2\64\u0195\3\2\2\2\66\u01a2\3\2\2\28\u01a6\3\2\2\2:\u01ad\3\2\2\2<\u01af"+
		"\3\2\2\2>\u01c5\3\2\2\2@\u01c7\3\2\2\2B\u01d3\3\2\2\2D\u01e0\3\2\2\2F"+
		"\u01e9\3\2\2\2H\u01eb\3\2\2\2J\u01fe\3\2\2\2L\u0201\3\2\2\2N\u0205\3\2"+
		"\2\2P\u021a\3\2\2\2R\u0221\3\2\2\2T\u0228\3\2\2\2V\u022a\3\2\2\2X\u022c"+
		"\3\2\2\2Z\u0231\3\2\2\2\\\u0236\3\2\2\2^\u024d\3\2\2\2`\u0252\3\2\2\2"+
		"b\u0254\3\2\2\2d\u0258\3\2\2\2f\u0262\3\2\2\2h\u0264\3\2\2\2j\u0266\3"+
		"\2\2\2l\u0279\3\2\2\2n\u027f\3\2\2\2p\u0281\3\2\2\2r\u028a\3\2\2\2t\u0293"+
		"\3\2\2\2v\u029d\3\2\2\2x\u02a1\3\2\2\2z\u02a6\3\2\2\2|\u02ad\3\2\2\2~"+
		"\u02b2\3\2\2\2\u0080\u02b7\3\2\2\2\u0082\u02cf\3\2\2\2\u0084\u02d1\3\2"+
		"\2\2\u0086\u0303\3\2\2\2\u0088\u0305\3\2\2\2\u008a\u0307\3\2\2\2\u008c"+
		"\u030a\3\2\2\2\u008e\u031b\3\2\2\2\u0090\u031d\3\2\2\2\u0092\u0330\3\2"+
		"\2\2\u0094\u0332\3\2\2\2\u0096\u033a\3\2\2\2\u0098\u033e\3\2\2\2\u009a"+
		"\u0349\3\2\2\2\u009c\u034d\3\2\2\2\u009e\u0354\3\2\2\2\u00a0\u035a\3\2"+
		"\2\2\u00a2\u035c\3\2\2\2\u00a4\u0364\3\2\2\2\u00a6\u0389\3\2\2\2\u00a8"+
		"\u038c\3\2\2\2\u00aa\u0390\3\2\2\2\u00ac\u0396\3\2\2\2\u00ae\u03a1\3\2"+
		"\2\2\u00b0\u03a5\3\2\2\2\u00b2\u03b6\3\2\2\2\u00b4\u03b8\3\2\2\2\u00b6"+
		"\u03c3\3\2\2\2\u00b8\u03cc\3\2\2\2\u00ba\u03d4\3\2\2\2\u00bc\u03d7\3\2"+
		"\2\2\u00be\u03d9\3\2\2\2\u00c0\u03e1\3\2\2\2\u00c2\u03e3\3\2\2\2\u00c4"+
		"\u03eb\3\2\2\2\u00c6\u03f1\3\2\2\2\u00c8\u03f3\3\2\2\2\u00ca\u0432\3\2"+
		"\2\2\u00cc\u0498\3\2\2\2\u00ce\u049b\3\2\2\2\u00d0\u049e\3\2\2\2\u00d2"+
		"\u04a1\3\2\2\2\u00d4\u04a9\3\2\2\2\u00d6\u04ab\3\2\2\2\u00d8\u04ad\3\2"+
		"\2\2\u00da\u04af\3\2\2\2\u00dc\u04b1\3\2\2\2\u00de\u04b3\3\2\2\2\u00e0"+
		"\u00eb\3\2\2\2\u00e1\u00e3\5\4\3\2\u00e2\u00e4\7?\2\2\u00e3\u00e2\3\2"+
		"\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e6\3\2\2\2\u00e5\u00e1\3\2\2\2\u00e6"+
		"\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00eb\3\2"+
		"\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00e0\3\2\2\2\u00ea\u00e7\3\2\2\2\u00eb"+
		"\3\3\2\2\2\u00ec\u00f6\5\u00b8]\2\u00ed\u00f6\5J&\2\u00ee\u00f6\5\u00b4"+
		"[\2\u00ef\u00f6\5\36\20\2\u00f0\u00f6\5l\67\2\u00f1\u00f6\5@!\2\u00f2"+
		"\u00f6\5\30\r\2\u00f3\u00f6\5|?\2\u00f4\u00f6\5~@\2\u00f5\u00ec\3\2\2"+
		"\2\u00f5\u00ed\3\2\2\2\u00f5\u00ee\3\2\2\2\u00f5\u00ef\3\2\2\2\u00f5\u00f0"+
		"\3\2\2\2\u00f5\u00f1\3\2\2\2\u00f5\u00f2\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5"+
		"\u00f4\3\2\2\2\u00f6\5\3\2\2\2\u00f7\u00f8\t\2\2\2\u00f8\7\3\2\2\2\u00f9"+
		"\u00ff\5\6\4\2\u00fa\u00ff\7\64\2\2\u00fb\u00ff\7\27\2\2\u00fc\u00ff\7"+
		"\17\2\2\u00fd\u00ff\7\16\2\2\u00fe\u00f9\3\2\2\2\u00fe\u00fa\3\2\2\2\u00fe"+
		"\u00fb\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00fd\3\2\2\2\u00ff\t\3\2\2\2"+
		"\u0100\u0102\5\f\7\2\u0101\u0100\3\2\2\2\u0101\u0102\3\2\2\2\u0102\13"+
		"\3\2\2\2\u0103\u0105\5\16\b\2\u0104\u0103\3\2\2\2\u0105\u0106\3\2\2\2"+
		"\u0106\u0104\3\2\2\2\u0106\u0107\3\2\2\2\u0107\r\3\2\2\2\u0108\u0109\7"+
		"A\2\2\u0109\u0113\5\b\5\2\u010a\u010b\7A\2\2\u010b\u010c\5\b\5\2\u010c"+
		"\u010e\7C\2\2\u010d\u010f\5\u00c2b\2\u010e\u010d\3\2\2\2\u010e\u010f\3"+
		"\2\2\2\u010f\u0110\3\2\2\2\u0110\u0111\7D\2\2\u0111\u0113\3\2\2\2\u0112"+
		"\u0108\3\2\2\2\u0112\u010a\3\2\2\2\u0113\17\3\2\2\2\u0114\u0119\5\22\n"+
		"\2\u0115\u0116\7B\2\2\u0116\u0118\5\22\n\2\u0117\u0115\3\2\2\2\u0118\u011b"+
		"\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\21\3\2\2\2\u011b"+
		"\u0119\3\2\2\2\u011c\u011e\5\n\6\2\u011d\u011f\5\24\13\2\u011e\u011d\3"+
		"\2\2\2\u011e\u011f\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u0121\5R*\2\u0121"+
		"\u0122\5\b\5\2\u0122\23\3\2\2\2\u0123\u0127\7\36\2\2\u0124\u0127\7#\2"+
		"\2\u0125\u0127\7\37\2\2\u0126\u0123\3\2\2\2\u0126\u0124\3\2\2\2\u0126"+
		"\u0125\3\2\2\2\u0127\25\3\2\2\2\u0128\u0129\5\n\6\2\u0129\u012a\7%\2\2"+
		"\u012a\u012c\5\b\5\2\u012b\u012d\5b\62\2\u012c\u012b\3\2\2\2\u012c\u012d"+
		"\3\2\2\2\u012d\u012e\3\2\2\2\u012e\u0130\7C\2\2\u012f\u0131\5\20\t\2\u0130"+
		"\u012f\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\7D"+
		"\2\2\u0133\27\3\2\2\2\u0134\u0136\5\f\7\2\u0135\u0134\3\2\2\2\u0135\u0136"+
		"\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u0138\5R*\2\u0138\u013a\7C\2\2\u0139"+
		"\u013b\5\u00be`\2\u013a\u0139\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013c"+
		"\3\2\2\2\u013c\u013d\7D\2\2\u013d\u013e\5\b\5\2\u013e\u013f\7?\2\2\u013f"+
		"\31\3\2\2\2\u0140\u0142\7C\2\2\u0141\u0143\5\20\t\2\u0142\u0141\3\2\2"+
		"\2\u0142\u0143\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145\7D\2\2\u0145\33"+
		"\3\2\2\2\u0146\u0147\7J\2\2\u0147\35\3\2\2\2\u0148\u014a\5$\23\2\u0149"+
		"\u014b\5\32\16\2\u014a\u0149\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c\3"+
		"\2\2\2\u014c\u014e\7E\2\2\u014d\u014f\5 \21\2\u014e\u014d\3\2\2\2\u014e"+
		"\u014f\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0151\5&\24\2\u0151\u0152\7F"+
		"\2\2\u0152\37\3\2\2\2\u0153\u0155\5\"\22\2\u0154\u0153\3\2\2\2\u0155\u0156"+
		"\3\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157!\3\2\2\2\u0158"+
		"\u015c\5\u00b8]\2\u0159\u015c\5\u00b6\\\2\u015a\u015c\5\30\r\2\u015b\u0158"+
		"\3\2\2\2\u015b\u0159\3\2\2\2\u015b\u015a\3\2\2\2\u015c#\3\2\2\2\u015d"+
		"\u015e\5\n\6\2\u015e\u015f\7$\2\2\u015f\u0161\5\b\5\2\u0160\u0162\5b\62"+
		"\2\u0161\u0160\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0165"+
		"\7C\2\2\u0164\u0166\5\20\t\2\u0165\u0164\3\2\2\2\u0165\u0166\3\2\2\2\u0166"+
		"\u0167\3\2\2\2\u0167\u0168\7D\2\2\u0168%\3\2\2\2\u0169\u016b\5(\25\2\u016a"+
		"\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2"+
		"\2\2\u016d\'\3\2\2\2\u016e\u016f\5\n\6\2\u016f\u0170\7(\2\2\u0170\u0171"+
		"\5\b\5\2\u0171\u0173\7E\2\2\u0172\u0174\5*\26\2\u0173\u0172\3\2\2\2\u0173"+
		"\u0174\3\2\2\2\u0174\u0176\3\2\2\2\u0175\u0177\5\60\31\2\u0176\u0175\3"+
		"\2\2\2\u0176\u0177\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179\7F\2\2\u0179"+
		")\3\2\2\2\u017a\u017c\5,\27\2\u017b\u017a\3\2\2\2\u017c\u017d\3\2\2\2"+
		"\u017d\u017b\3\2\2\2\u017d\u017e\3\2\2\2\u017e+\3\2\2\2\u017f\u0186\5"+
		"\u0084C\2\u0180\u0186\5\u0086D\2\u0181\u0186\5\u0090I\2\u0182\u0186\5"+
		".\30\2\u0183\u0186\5\u00b8]\2\u0184\u0186\5\u00b6\\\2\u0185\u017f\3\2"+
		"\2\2\u0185\u0180\3\2\2\2\u0185\u0181\3\2\2\2\u0185\u0182\3\2\2\2\u0185"+
		"\u0183\3\2\2\2\u0185\u0184\3\2\2\2\u0186-\3\2\2\2\u0187\u0188\5\n\6\2"+
		"\u0188\u0189\7E\2\2\u0189\u018a\5*\26\2\u018a\u018b\7F\2\2\u018b/\3\2"+
		"\2\2\u018c\u018d\7-\2\2\u018d\u018e\5\62\32\2\u018e\61\3\2\2\2\u018f\u0190"+
		"\5\b\5\2\u0190\u0191\7?\2\2\u0191\u0194\3\2\2\2\u0192\u0194\5\64\33\2"+
		"\u0193\u018f\3\2\2\2\u0193\u0192\3\2\2\2\u0194\63\3\2\2\2\u0195\u0196"+
		"\7\'\2\2\u0196\u0198\7C\2\2\u0197\u0199\5\u00c2b\2\u0198\u0197\3\2\2\2"+
		"\u0198\u0199\3\2\2\2\u0199\u019a\3\2\2\2\u019a\u019b\7D\2\2\u019b\u019d"+
		"\7E\2\2\u019c\u019e\5\66\34\2\u019d\u019c\3\2\2\2\u019d\u019e\3\2\2\2"+
		"\u019e\u019f\3\2\2\2\u019f\u01a0\7F\2\2\u01a0\65\3\2\2\2\u01a1\u01a3\5"+
		"8\35\2\u01a2\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a4"+
		"\u01a5\3\2\2\2\u01a5\67\3\2\2\2\u01a6\u01a7\5:\36\2\u01a7\u01a8\7@\2\2"+
		"\u01a8\u01a9\5\b\5\2\u01a9\u01aa\7?\2\2\u01aa9\3\2\2\2\u01ab\u01ae\5<"+
		"\37\2\u01ac\u01ae\5> \2\u01ad\u01ab\3\2\2\2\u01ad\u01ac\3\2\2\2\u01ae"+
		";\3\2\2\2\u01af\u01b0\7C\2\2\u01b0\u01b5\5> \2\u01b1\u01b2\7B\2\2\u01b2"+
		"\u01b4\5> \2\u01b3\u01b1\3\2\2\2\u01b4\u01b7\3\2\2\2\u01b5\u01b3\3\2\2"+
		"\2\u01b5\u01b6\3\2\2\2\u01b6\u01b8\3\2\2\2\u01b7\u01b5\3\2\2\2\u01b8\u01b9"+
		"\7D\2\2\u01b9=\3\2\2\2\u01ba\u01c6\5\u00caf\2\u01bb\u01c6\7\24\2\2\u01bc"+
		"\u01c6\7\63\2\2\u01bd\u01be\5\u00caf\2\u01be\u01bf\7\65\2\2\u01bf\u01c0"+
		"\5\u00caf\2\u01c0\u01c6\3\2\2\2\u01c1\u01c2\5\u00caf\2\u01c2\u01c3\7\66"+
		"\2\2\u01c3\u01c4\5\u00caf\2\u01c4\u01c6\3\2\2\2\u01c5\u01ba\3\2\2\2\u01c5"+
		"\u01bb\3\2\2\2\u01c5\u01bc\3\2\2\2\u01c5\u01bd\3\2\2\2\u01c5\u01c1\3\2"+
		"\2\2\u01c6?\3\2\2\2\u01c7\u01c9\5B\"\2\u01c8\u01ca\5\32\16\2\u01c9\u01c8"+
		"\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01cd\7E\2\2\u01cc"+
		"\u01ce\5D#\2\u01cd\u01cc\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01cf\3\2\2"+
		"\2\u01cf\u01d0\7\16\2\2\u01d0\u01d1\5H%\2\u01d1\u01d2\7F\2\2\u01d2A\3"+
		"\2\2\2\u01d3\u01d4\5\n\6\2\u01d4\u01d5\7\23\2\2\u01d5\u01d7\5\b\5\2\u01d6"+
		"\u01d8\5b\62\2\u01d7\u01d6\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01d9\3\2"+
		"\2\2\u01d9\u01db\7C\2\2\u01da\u01dc\5\20\t\2\u01db\u01da\3\2\2\2\u01db"+
		"\u01dc\3\2\2\2\u01dc\u01dd\3\2\2\2\u01dd\u01de\7D\2\2\u01deC\3\2\2\2\u01df"+
		"\u01e1\5F$\2\u01e0\u01df\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e0\3\2\2"+
		"\2\u01e2\u01e3\3\2\2\2\u01e3E\3\2\2\2\u01e4\u01ea\5\u00b8]\2\u01e5\u01ea"+
		"\5\u00b4[\2\u01e6\u01ea\5\u00a2R\2\u01e7\u01ea\5\30\r\2\u01e8\u01ea\5"+
		"\u00b6\\\2\u01e9\u01e4\3\2\2\2\u01e9\u01e5\3\2\2\2\u01e9\u01e6\3\2\2\2"+
		"\u01e9\u01e7\3\2\2\2\u01e9\u01e8\3\2\2\2\u01eaG\3\2\2\2\u01eb\u01ec\5"+
		"\u0094K\2\u01ecI\3\2\2\2\u01ed\u01ee\5\n\6\2\u01ee\u01ef\7\31\2\2\u01ef"+
		"\u01f1\5\6\4\2\u01f0\u01f2\5b\62\2\u01f1\u01f0\3\2\2\2\u01f1\u01f2\3\2"+
		"\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f5\7E\2\2\u01f4\u01f6\5L\'\2\u01f5\u01f4"+
		"\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f8\7F\2\2\u01f8"+
		"\u01ff\3\2\2\2\u01f9\u01fa\5\n\6\2\u01fa\u01fb\7\31\2\2\u01fb\u01fc\5"+
		"N(\2\u01fc\u01fd\7?\2\2\u01fd\u01ff\3\2\2\2\u01fe\u01ed\3\2\2\2\u01fe"+
		"\u01f9\3\2\2\2\u01ffK\3\2\2\2\u0200\u0202\5P)\2\u0201\u0200\3\2\2\2\u0202"+
		"\u0203\3\2\2\2\u0203\u0201\3\2\2\2\u0203\u0204\3\2\2\2\u0204M\3\2\2\2"+
		"\u0205\u0206\5`\61\2\u0206\u0208\5\b\5\2\u0207\u0209\5b\62\2\u0208\u0207"+
		"\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020c\7C\2\2\u020b"+
		"\u020d\5\20\t\2\u020c\u020b\3\2\2\2\u020c\u020d\3\2\2\2\u020d\u020e\3"+
		"\2\2\2\u020e\u020f\7D\2\2\u020fO\3\2\2\2\u0210\u0211\5N(\2\u0211\u0212"+
		"\7?\2\2\u0212\u021b\3\2\2\2\u0213\u0214\7\64\2\2\u0214\u0216\7C\2\2\u0215"+
		"\u0217\5\20\t\2\u0216\u0215\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0218\3"+
		"\2\2\2\u0218\u0219\7D\2\2\u0219\u021b\7?\2\2\u021a\u0210\3\2\2\2\u021a"+
		"\u0213\3\2\2\2\u021bQ\3\2\2\2\u021c\u0222\5^\60\2\u021d\u0222\5V,\2\u021e"+
		"\u0222\5\\/\2\u021f\u0222\5Z.\2\u0220\u0222\5X-\2\u0221\u021c\3\2\2\2"+
		"\u0221\u021d\3\2\2\2\u0221\u021e\3\2\2\2\u0221\u021f\3\2\2\2\u0221\u0220"+
		"\3\2\2\2\u0222S\3\2\2\2\u0223\u0229\7\64\2\2\u0224\u0225\5\34\17\2\u0225"+
		"\u0226\7\64\2\2\u0226\u0229\3\2\2\2\u0227\u0229\7\27\2\2\u0228\u0223\3"+
		"\2\2\2\u0228\u0224\3\2\2\2\u0228\u0227\3\2\2\2\u0229U\3\2\2\2\u022a\u022b"+
		"\5T+\2\u022bW\3\2\2\2\u022c\u022d\7/\2\2\u022d\u022e\7K\2\2\u022e\u022f"+
		"\5j\66\2\u022f\u0230\7L\2\2\u0230Y\3\2\2\2\u0231\u0232\5V,\2\u0232\u0233"+
		"\7G\2\2\u0233\u0234\5\u00caf\2\u0234\u0235\7H\2\2\u0235[\3\2\2\2\u0236"+
		"\u0237\5T+\2\u0237\u0238\7K\2\2\u0238\u0239\5j\66\2\u0239\u023a\7L\2\2"+
		"\u023a]\3\2\2\2\u023b\u024e\7\20\2\2\u023c\u024e\7\27\2\2\u023d\u024e"+
		"\7\21\2\2\u023e\u023f\7\21\2\2\u023f\u0240\7K\2\2\u0240\u0241\5\u00d4"+
		"k\2\u0241\u0242\7L\2\2\u0242\u024e\3\2\2\2\u0243\u0244\7 \2\2\u0244\u0245"+
		"\7K\2\2\u0245\u0246\5\u00d4k\2\u0246\u0247\7L\2\2\u0247\u024e\3\2\2\2"+
		"\u0248\u0249\7\61\2\2\u0249\u024a\7K\2\2\u024a\u024b\5\u00d4k\2\u024b"+
		"\u024c\7L\2\2\u024c\u024e\3\2\2\2\u024d\u023b\3\2\2\2\u024d\u023c\3\2"+
		"\2\2\u024d\u023d\3\2\2\2\u024d\u023e\3\2\2\2\u024d\u0243\3\2\2\2\u024d"+
		"\u0248\3\2\2\2\u024e_\3\2\2\2\u024f\u0253\7\62\2\2\u0250\u0253\5R*\2\u0251"+
		"\u0253\5\6\4\2\u0252\u024f\3\2\2\2\u0252\u0250\3\2\2\2\u0252\u0251\3\2"+
		"\2\2\u0253a\3\2\2\2\u0254\u0255\7K\2\2\u0255\u0256\5d\63\2\u0256\u0257"+
		"\7L\2\2\u0257c\3\2\2\2\u0258\u025d\5\6\4\2\u0259\u025a\7B\2\2\u025a\u025c"+
		"\5\6\4\2\u025b\u0259\3\2\2\2\u025c\u025f\3\2\2\2\u025d\u025b\3\2\2\2\u025d"+
		"\u025e\3\2\2\2\u025ee\3\2\2\2\u025f\u025d\3\2\2\2\u0260\u0263\5h\65\2"+
		"\u0261\u0263\5R*\2\u0262\u0260\3\2\2\2\u0262\u0261\3\2\2\2\u0263g\3\2"+
		"\2\2\u0264\u0265\7\63\2\2\u0265i\3\2\2\2\u0266\u026b\5f\64\2\u0267\u0268"+
		"\7B\2\2\u0268\u026a\5f\64\2\u0269\u0267\3\2\2\2\u026a\u026d\3\2\2\2\u026b"+
		"\u0269\3\2\2\2\u026b\u026c\3\2\2\2\u026ck\3\2\2\2\u026d\u026b\3\2\2\2"+
		"\u026e\u027a\5n8\2\u026f\u027a\5\u0082B\2\u0270\u0271\5$\23\2\u0271\u0272"+
		"\7?\2\2\u0272\u027a\3\2\2\2\u0273\u0274\5B\"\2\u0274\u0275\7?\2\2\u0275"+
		"\u027a\3\2\2\2\u0276\u0277\5\26\f\2\u0277\u0278\7?\2\2\u0278\u027a\3\2"+
		"\2\2\u0279\u026e\3\2\2\2\u0279\u026f\3\2\2\2\u0279\u0270\3\2\2\2\u0279"+
		"\u0273\3\2\2\2\u0279\u0276\3\2\2\2\u027am\3\2\2\2\u027b\u0280\5p9\2\u027c"+
		"\u0280\5r:\2\u027d\u0280\5t;\2\u027e\u0280\5z>\2\u027f\u027b\3\2\2\2\u027f"+
		"\u027c\3\2\2\2\u027f\u027d\3\2\2\2\u027f\u027e\3\2\2\2\u0280o\3\2\2\2"+
		"\u0281\u0282\5\n\6\2\u0282\u0283\7\33\2\2\u0283\u0284\5\b\5\2\u0284\u0286"+
		"\7E\2\2\u0285\u0287\5v<\2\u0286\u0285\3\2\2\2\u0286\u0287\3\2\2\2\u0287"+
		"\u0288\3\2\2\2\u0288\u0289\7F\2\2\u0289q\3\2\2\2\u028a\u028b\5\n\6\2\u028b"+
		"\u028c\7\34\2\2\u028c\u028d\5\b\5\2\u028d\u028f\7E\2\2\u028e\u0290\5v"+
		"<\2\u028f\u028e\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0291\3\2\2\2\u0291"+
		"\u0292\7F\2\2\u0292s\3\2\2\2\u0293\u0294\5\n\6\2\u0294\u0295\7)\2\2\u0295"+
		"\u0296\5\b\5\2\u0296\u0298\7E\2\2\u0297\u0299\5v<\2\u0298\u0297\3\2\2"+
		"\2\u0298\u0299\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029b\7F\2\2\u029bu\3"+
		"\2\2\2\u029c\u029e\5x=\2\u029d\u029c\3\2\2\2\u029e\u029f\3\2\2\2\u029f"+
		"\u029d\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0w\3\2\2\2\u02a1\u02a2\5\n\6\2"+
		"\u02a2\u02a3\5R*\2\u02a3\u02a4\5\b\5\2\u02a4\u02a5\7?\2\2\u02a5y\3\2\2"+
		"\2\u02a6\u02a7\5\n\6\2\u02a7\u02a8\7\26\2\2\u02a8\u02a9\5\b\5\2\u02a9"+
		"\u02aa\7E\2\2\u02aa\u02ab\5\u0080A\2\u02ab\u02ac\7F\2\2\u02ac{\3\2\2\2"+
		"\u02ad\u02ae\7\27\2\2\u02ae\u02af\7E\2\2\u02af\u02b0\5\u0080A\2\u02b0"+
		"\u02b1\7F\2\2\u02b1}\3\2\2\2\u02b2\u02b3\7\"\2\2\u02b3\u02b4\7E\2\2\u02b4"+
		"\u02b5\5\u0080A\2\u02b5\u02b6\7F\2\2\u02b6\177\3\2\2\2\u02b7\u02bc\5\b"+
		"\5\2\u02b8\u02b9\7B\2\2\u02b9\u02bb\5\b\5\2\u02ba\u02b8\3\2\2\2\u02bb"+
		"\u02be\3\2\2\2\u02bc\u02ba\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd\u0081\3\2"+
		"\2\2\u02be\u02bc\3\2\2\2\u02bf\u02c1\5\f\7\2\u02c0\u02bf\3\2\2\2\u02c0"+
		"\u02c1\3\2\2\2\u02c1\u02c2\3\2\2\2\u02c2\u02c3\7\60\2\2\u02c3\u02c4\5"+
		"R*\2\u02c4\u02c5\5\b\5\2\u02c5\u02c6\7?\2\2\u02c6\u02d0\3\2\2\2\u02c7"+
		"\u02c9\5\f\7\2\u02c8\u02c7\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9\u02ca\3\2"+
		"\2\2\u02ca\u02cb\7\60\2\2\u02cb\u02cc\5n8\2\u02cc\u02cd\5\b\5\2\u02cd"+
		"\u02ce\7?\2\2\u02ce\u02d0\3\2\2\2\u02cf\u02c0\3\2\2\2\u02cf\u02c8\3\2"+
		"\2\2\u02d0\u0083\3\2\2\2\u02d1\u02d2\5\u00c8e\2\u02d2\u02d3\7I\2\2\u02d3"+
		"\u02d4\5\u00caf\2\u02d4\u02d5\7?\2\2\u02d5\u0085\3\2\2\2\u02d6\u02d7\5"+
		"\u00c8e\2\u02d7\u02d8\7J\2\2\u02d8\u02d9\7\16\2\2\u02d9\u02db\7C\2\2\u02da"+
		"\u02dc\5\u00be`\2\u02db\u02da\3\2\2\2\u02db\u02dc\3\2\2\2\u02dc\u02dd"+
		"\3\2\2\2\u02dd\u02de\7D\2\2\u02de\u02df\7?\2\2\u02df\u0304\3\2\2\2\u02e0"+
		"\u02e1\5\u00c8e\2\u02e1\u02e2\7J\2\2\u02e2\u02e7\7\17\2\2\u02e3\u02e4"+
		"\7K\2\2\u02e4\u02e5\5j\66\2\u02e5\u02e6\7L\2\2\u02e6\u02e8\3\2\2\2\u02e7"+
		"\u02e3\3\2\2\2\u02e7\u02e8\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02eb\7C"+
		"\2\2\u02ea\u02ec\5\u00be`\2\u02eb\u02ea\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec"+
		"\u02ed\3\2\2\2\u02ed\u02ee\7D\2\2\u02ee\u02ef\7?\2\2\u02ef\u0304\3\2\2"+
		"\2\u02f0\u02f1\5\u00c8e\2\u02f1\u02f3\7C\2\2\u02f2\u02f4\5\u00be`\2\u02f3"+
		"\u02f2\3\2\2\2\u02f3\u02f4\3\2\2\2\u02f4\u02f5\3\2\2\2\u02f5\u02f6\7D"+
		"\2\2\u02f6\u02f7\7?\2\2\u02f7\u0304\3\2\2\2\u02f8\u02f9\5\u00c8e\2\u02f9"+
		"\u02fa\7K\2\2\u02fa\u02fb\5j\66\2\u02fb\u02fc\7L\2\2\u02fc\u02fe\7C\2"+
		"\2\u02fd\u02ff\5\u00be`\2\u02fe\u02fd\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ff"+
		"\u0300\3\2\2\2\u0300\u0301\7D\2\2\u0301\u0302\7?\2\2\u0302\u0304\3\2\2"+
		"\2\u0303\u02d6\3\2\2\2\u0303\u02e0\3\2\2\2\u0303\u02f0\3\2\2\2\u0303\u02f8"+
		"\3\2\2\2\u0304\u0087\3\2\2\2\u0305\u0306\7?\2\2\u0306\u0089\3\2\2\2\u0307"+
		"\u0308\7&\2\2\u0308\u0309\7?\2\2\u0309\u008b\3\2\2\2\u030a\u030b\7\30"+
		"\2\2\u030b\u030c\7?\2\2\u030c\u008d\3\2\2\2\u030d\u030e\7\35\2\2\u030e"+
		"\u030f\7C\2\2\u030f\u0310\5\u00caf\2\u0310\u0311\7D\2\2\u0311\u0312\5"+
		"\u0092J\2\u0312\u031c\3\2\2\2\u0313\u0314\7\35\2\2\u0314\u0315\7C\2\2"+
		"\u0315\u0316\5\u00caf\2\u0316\u0317\7D\2\2\u0317\u0318\5\u0092J\2\u0318"+
		"\u0319\7\25\2\2\u0319\u031a\5\u0092J\2\u031a\u031c\3\2\2\2\u031b\u030d"+
		"\3\2\2\2\u031b\u0313\3\2\2\2\u031c\u008f\3\2\2\2\u031d\u031e\5V,\2\u031e"+
		"\u031f\7J\2\2\u031f\u0320\7\16\2\2\u0320\u0322\7C\2\2\u0321\u0323\5\u00be"+
		"`\2\u0322\u0321\3\2\2\2\u0322\u0323\3\2\2\2\u0323\u0324\3\2\2\2\u0324"+
		"\u0325\7D\2\2\u0325\u0326\7?\2\2\u0326\u0091\3\2\2\2\u0327\u0331\5\u0084"+
		"C\2\u0328\u0331\5\u0086D\2\u0329\u0331\5\u0090I\2\u032a\u0331\5\u008e"+
		"H\2\u032b\u0331\5\u0088E\2\u032c\u0331\5\u0094K\2\u032d\u0331\5\u008c"+
		"G\2\u032e\u0331\5\u008aF\2\u032f\u0331\5\u0098M\2\u0330\u0327\3\2\2\2"+
		"\u0330\u0328\3\2\2\2\u0330\u0329\3\2\2\2\u0330\u032a\3\2\2\2\u0330\u032b"+
		"\3\2\2\2\u0330\u032c\3\2\2\2\u0330\u032d\3\2\2\2\u0330\u032e\3\2\2\2\u0330"+
		"\u032f\3\2\2\2\u0331\u0093\3\2\2\2\u0332\u0333\5\n\6\2\u0333\u0335\7E"+
		"\2\2\u0334\u0336\5\u0096L\2\u0335\u0334\3\2\2\2\u0335\u0336\3\2\2\2\u0336"+
		"\u0337\3\2\2\2\u0337\u0338\7F\2\2\u0338\u0095\3\2\2\2\u0339\u033b\5\u00a0"+
		"Q\2\u033a\u0339\3\2\2\2\u033b\u033c\3\2\2\2\u033c\u033a\3\2\2\2\u033c"+
		"\u033d\3\2\2\2\u033d\u0097\3\2\2\2\u033e\u033f\7*\2\2\u033f\u0340\7C\2"+
		"\2\u0340\u0341\5\u00caf\2\u0341\u0342\7D\2\2\u0342\u0344\7E\2\2\u0343"+
		"\u0345\5\u009aN\2\u0344\u0343\3\2\2\2\u0344\u0345\3\2\2\2\u0345\u0346"+
		"\3\2\2\2\u0346\u0347\7F\2\2\u0347\u0099\3\2\2\2\u0348\u034a\5\u009cO\2"+
		"\u0349\u0348\3\2\2\2\u034a\u034b\3\2\2\2\u034b\u0349\3\2\2\2\u034b\u034c"+
		"\3\2\2\2\u034c\u009b\3\2\2\2\u034d\u034e\5\u009eP\2\u034e\u0350\7@\2\2"+
		"\u034f\u0351\5\u0094K\2\u0350\u034f\3\2\2\2\u0350\u0351\3\2\2\2\u0351"+
		"\u009d\3\2\2\2\u0352\u0355\5\b\5\2\u0353\u0355\7\24\2\2\u0354\u0352\3"+
		"\2\2\2\u0354\u0353\3\2\2\2\u0355\u009f\3\2\2\2\u0356\u035b\5\u00b6\\\2"+
		"\u0357\u035b\5\u00b8]\2\u0358\u035b\5\u0092J\2\u0359\u035b\5\30\r\2\u035a"+
		"\u0356\3\2\2\2\u035a\u0357\3\2\2\2\u035a\u0358\3\2\2\2\u035a\u0359\3\2"+
		"\2\2\u035b\u00a1\3\2\2\2\u035c\u035d\5\n\6\2\u035d\u035e\7+\2\2\u035e"+
		"\u035f\5\b\5\2\u035f\u0360\7E\2\2\u0360\u0361\5\u00a4S\2\u0361\u0362\7"+
		"F\2\2\u0362\u00a3\3\2\2\2\u0363\u0365\5\u00a6T\2\u0364\u0363\3\2\2\2\u0365"+
		"\u0366\3\2\2\2\u0366\u0364\3\2\2\2\u0366\u0367\3\2\2\2\u0367\u00a5\3\2"+
		"\2\2\u0368\u0369\7!\2\2\u0369\u036a\7I\2\2\u036a\u036c\7E\2\2\u036b\u036d"+
		"\5\u00a8U\2\u036c\u036b\3\2\2\2\u036c\u036d\3\2\2\2\u036d\u036e\3\2\2"+
		"\2\u036e\u038a\7F\2\2\u036f\u0370\7\f\2\2\u0370\u0371\7I\2\2\u0371\u0372"+
		"\7E\2\2\u0372\u0373\5\u00acW\2\u0373\u0374\7F\2\2\u0374\u038a\3\2\2\2"+
		"\u0375\u0376\7\22\2\2\u0376\u0377\7\r\2\2\u0377\u0378\7I\2\2\u0378\u0379"+
		"\7E\2\2\u0379\u037a\5\u00aeX\2\u037a\u037b\7F\2\2\u037b\u038a\3\2\2\2"+
		"\u037c\u037d\5\n\6\2\u037d\u037e\7\22\2\2\u037e\u037f\7\64\2\2\u037f\u0380"+
		"\7I\2\2\u0380\u0381\5\u00bc_\2\u0381\u0382\7?\2\2\u0382\u038a\3\2\2\2"+
		"\u0383\u0384\5\n\6\2\u0384\u0385\7\64\2\2\u0385\u0386\7I\2\2\u0386\u0387"+
		"\5\u00bc_\2\u0387\u0388\7?\2\2\u0388\u038a\3\2\2\2\u0389\u0368\3\2\2\2"+
		"\u0389\u036f\3\2\2\2\u0389\u0375\3\2\2\2\u0389\u037c\3\2\2\2\u0389\u0383"+
		"\3\2\2\2\u038a\u00a7\3\2\2\2\u038b\u038d\5\u00aaV\2\u038c\u038b\3\2\2"+
		"\2\u038d\u038e\3\2\2\2\u038e\u038c\3\2\2\2\u038e\u038f\3\2\2\2\u038f\u00a9"+
		"\3\2\2\2\u0390\u0391\5\u00caf\2\u0391\u0392\7@\2\2\u0392\u0393\5\b\5\2"+
		"\u0393\u0394\5\n\6\2\u0394\u0395\7?\2\2\u0395\u00ab\3\2\2\2\u0396\u0397"+
		"\5\u00b2Z\2\u0397\u039d\7?\2\2\u0398\u0399\5\u00b2Z\2\u0399\u039a\7?\2"+
		"\2\u039a\u039c\3\2\2\2\u039b\u0398\3\2\2\2\u039c\u039f\3\2\2\2\u039d\u039b"+
		"\3\2\2\2\u039d\u039e\3\2\2\2\u039e\u00ad\3\2\2\2\u039f\u039d\3\2\2\2\u03a0"+
		"\u03a2\5\u00b0Y\2\u03a1\u03a0\3\2\2\2\u03a2\u03a3\3\2\2\2\u03a3\u03a1"+
		"\3\2\2\2\u03a3\u03a4\3\2\2\2\u03a4\u00af\3\2\2\2\u03a5\u03a6\5\n\6\2\u03a6"+
		"\u03a7\5:\36\2\u03a7\u03a8\7@\2\2\u03a8\u03a9\5\u00b2Z\2\u03a9\u03aa\7"+
		"?\2\2\u03aa\u00b1\3\2\2\2\u03ab\u03ac\5\n\6\2\u03ac\u03ad\5\b\5\2\u03ad"+
		"\u03b7\3\2\2\2\u03ae\u03af\5\n\6\2\u03af\u03b0\5\b\5\2\u03b0\u03b2\7C"+
		"\2\2\u03b1\u03b3\5\u00be`\2\u03b2\u03b1\3\2\2\2\u03b2\u03b3\3\2\2\2\u03b3"+
		"\u03b4\3\2\2\2\u03b4\u03b5\7D\2\2\u03b5\u03b7\3\2\2\2\u03b6\u03ab\3\2"+
		"\2\2\u03b6\u03ae\3\2\2\2\u03b7\u00b3\3\2\2\2\u03b8\u03b9\5\n\6\2\u03b9"+
		"\u03ba\7\13\2\2\u03ba\u03bb\5\b\5\2\u03bb\u03bd\7C\2\2\u03bc\u03be\5\20"+
		"\t\2\u03bd\u03bc\3\2\2\2\u03bd\u03be\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf"+
		"\u03c0\7D\2\2\u03c0\u03c1\5\u0094K\2\u03c1\u00b5\3\2\2\2\u03c2\u03c4\5"+
		"\f\7\2\u03c3\u03c2\3\2\2\2\u03c3\u03c4\3\2\2\2\u03c4\u03c5\3\2\2\2\u03c5"+
		"\u03c6\5R*\2\u03c6\u03c8\5\b\5\2\u03c7\u03c9\5\u00ba^\2\u03c8\u03c7\3"+
		"\2\2\2\u03c8\u03c9\3\2\2\2\u03c9\u03ca\3\2\2\2\u03ca\u03cb\7?\2\2\u03cb"+
		"\u00b7\3\2\2\2\u03cc\u03cd\5\n\6\2\u03cd\u03ce\7\22\2\2\u03ce\u03cf\5"+
		"R*\2\u03cf\u03d0\5\b\5\2\u03d0\u03d1\7I\2\2\u03d1\u03d2\5\u00bc_\2\u03d2"+
		"\u03d3\7?\2\2\u03d3\u00b9\3\2\2\2\u03d4\u03d5\7I\2\2\u03d5\u03d6\5\u00bc"+
		"_\2\u03d6\u00bb\3\2\2\2\u03d7\u03d8\5\u00caf\2\u03d8\u00bd\3\2\2\2\u03d9"+
		"\u03de\5\u00c0a\2\u03da\u03db\7B\2\2\u03db\u03dd\5\u00c0a\2\u03dc\u03da"+
		"\3\2\2\2\u03dd\u03e0\3\2\2\2\u03de\u03dc\3\2\2\2\u03de\u03df\3\2\2\2\u03df"+
		"\u00bf\3\2\2\2\u03e0\u03de\3\2\2\2\u03e1\u03e2\5\u00caf\2\u03e2\u00c1"+
		"\3\2\2\2\u03e3\u03e8\5\u00caf\2\u03e4\u03e5\7B\2\2\u03e5\u03e7\5\u00ca"+
		"f\2\u03e6\u03e4\3\2\2\2\u03e7\u03ea\3\2\2\2\u03e8\u03e6\3\2\2\2\u03e8"+
		"\u03e9\3\2\2\2\u03e9\u00c3\3\2\2\2\u03ea\u03e8\3\2\2\2\u03eb\u03ec\5\b"+
		"\5\2\u03ec\u00c5\3\2\2\2\u03ed\u03f2\5\6\4\2\u03ee\u03ef\5\34\17\2\u03ef"+
		"\u03f0\5\6\4\2\u03f0\u03f2\3\2\2\2\u03f1\u03ed\3\2\2\2\u03f1\u03ee\3\2"+
		"\2\2\u03f2\u00c7\3\2\2\2\u03f3\u03f4\be\1\2\u03f4\u03f5\5\u00c6d\2\u03f5"+
		"\u0407\3\2\2\2\u03f6\u03f7\f\5\2\2\u03f7\u03f8\7J\2\2\u03f8\u0406\5\u00c4"+
		"c\2\u03f9\u03fa\f\4\2\2\u03fa\u03fb\7G\2\2\u03fb\u03fc\5\u00caf\2\u03fc"+
		"\u03fd\7H\2\2\u03fd\u0406\3\2\2\2\u03fe\u03ff\f\3\2\2\u03ff\u0400\7G\2"+
		"\2\u0400\u0401\5\u00caf\2\u0401\u0402\7@\2\2\u0402\u0403\5\u00caf\2\u0403"+
		"\u0404\7H\2\2\u0404\u0406\3\2\2\2\u0405\u03f6\3\2\2\2\u0405\u03f9\3\2"+
		"\2\2\u0405\u03fe\3\2\2\2\u0406\u0409\3\2\2\2\u0407\u0405\3\2\2\2\u0407"+
		"\u0408\3\2\2\2\u0408\u00c9\3\2\2\2\u0409\u0407\3\2\2\2\u040a\u040b\bf"+
		"\1\2\u040b\u040c\7C\2\2\u040c\u040d\5R*\2\u040d\u040e\7D\2\2\u040e\u040f"+
		"\5\u00caf\"\u040f\u0433\3\2\2\2\u0410\u0433\5\u00d4k\2\u0411\u0433\7."+
		"\2\2\u0412\u0433\7\32\2\2\u0413\u0433\7X\2\2\u0414\u0433\5\6\4\2\u0415"+
		"\u0416\7J\2\2\u0416\u0433\5\6\4\2\u0417\u0418\5R*\2\u0418\u041a\7C\2\2"+
		"\u0419\u041b\5\u00be`\2\u041a\u0419\3\2\2\2\u041a\u041b\3\2\2\2\u041b"+
		"\u041c\3\2\2\2\u041c\u041d\7D\2\2\u041d\u0433\3\2\2\2\u041e\u0420\7E\2"+
		"\2\u041f\u0421\5\u00c2b\2\u0420\u041f\3\2\2\2\u0420\u0421\3\2\2\2\u0421"+
		"\u0422\3\2\2\2\u0422\u0433\7F\2\2\u0423\u0424\7C\2\2\u0424\u0425\5\u00ca"+
		"f\2\u0425\u0426\7D\2\2\u0426\u0433\3\2\2\2\u0427\u0433\5\u00ccg\2\u0428"+
		"\u0433\5\u00ceh\2\u0429\u0433\5\u00d2j\2\u042a\u0433\5\u00d0i\2\u042b"+
		"\u042c\5V,\2\u042c\u042d\7J\2\2\u042d\u042e\5\u00c4c\2\u042e\u0433\3\2"+
		"\2\2\u042f\u0430\7\27\2\2\u0430\u0431\7J\2\2\u0431\u0433\5\u00c4c\2\u0432"+
		"\u040a\3\2\2\2\u0432\u0410\3\2\2\2\u0432\u0411\3\2\2\2\u0432\u0412\3\2"+
		"\2\2\u0432\u0413\3\2\2\2\u0432\u0414\3\2\2\2\u0432\u0415\3\2\2\2\u0432"+
		"\u0417\3\2\2\2\u0432\u041e\3\2\2\2\u0432\u0423\3\2\2\2\u0432\u0427\3\2"+
		"\2\2\u0432\u0428\3\2\2\2\u0432\u0429\3\2\2\2\u0432\u042a\3\2\2\2\u0432"+
		"\u042b\3\2\2\2\u0432\u042f\3\2\2\2\u0433\u0495\3\2\2\2\u0434\u0435\f\26"+
		"\2\2\u0435\u0436\7Q\2\2\u0436\u0494\5\u00caf\27\u0437\u0438\f\25\2\2\u0438"+
		"\u0439\7R\2\2\u0439\u0494\5\u00caf\26\u043a\u043b\f\24\2\2\u043b\u043c"+
		"\7S\2\2\u043c\u0494\5\u00caf\25\u043d\u043e\f\23\2\2\u043e\u043f\7P\2"+
		"\2\u043f\u0494\5\u00caf\24\u0440\u0441\f\22\2\2\u0441\u0442\7O\2\2\u0442"+
		"\u0494\5\u00caf\23\u0443\u0444\f\21\2\2\u0444\u0445\7\67\2\2\u0445\u0494"+
		"\5\u00caf\22\u0446\u0447\f\20\2\2\u0447\u0448\7L\2\2\u0448\u0449\7L\2"+
		"\2\u0449\u0494\5\u00caf\21\u044a\u044b\f\17\2\2\u044b\u044c\7T\2\2\u044c"+
		"\u0494\5\u00caf\20\u044d\u044e\f\16\2\2\u044e\u044f\7V\2\2\u044f\u0494"+
		"\5\u00caf\17\u0450\u0451\f\r\2\2\u0451\u0452\7U\2\2\u0452\u0494\5\u00ca"+
		"f\16\u0453\u0454\f\f\2\2\u0454\u0455\7>\2\2\u0455\u0494\5\u00caf\r\u0456"+
		"\u0457\f\13\2\2\u0457\u0458\7=\2\2\u0458\u0494\5\u00caf\f\u0459\u045a"+
		"\f\n\2\2\u045a\u045b\7<\2\2\u045b\u0494\5\u00caf\13\u045c\u045d\f\t\2"+
		"\2\u045d\u045e\7K\2\2\u045e\u0494\5\u00caf\n\u045f\u0460\f\b\2\2\u0460"+
		"\u0461\7L\2\2\u0461\u0494\5\u00caf\t\u0462\u0463\f\7\2\2\u0463\u0464\7"+
		";\2\2\u0464\u0494\5\u00caf\b\u0465\u0466\f\6\2\2\u0466\u0467\7:\2\2\u0467"+
		"\u0494\5\u00caf\7\u0468\u0469\f\5\2\2\u0469\u046a\78\2\2\u046a\u0494\5"+
		"\u00caf\6\u046b\u046c\f\4\2\2\u046c\u046d\79\2\2\u046d\u0494\5\u00caf"+
		"\5\u046e\u046f\f\3\2\2\u046f\u0470\7W\2\2\u0470\u0471\5\u00caf\2\u0471"+
		"\u0472\7@\2\2\u0472\u0473\5\u00caf\4\u0473\u0494\3\2\2\2\u0474\u0475\f"+
		"%\2\2\u0475\u0477\7C\2\2\u0476\u0478\5\u00be`\2\u0477\u0476\3\2\2\2\u0477"+
		"\u0478\3\2\2\2\u0478\u0479\3\2\2\2\u0479\u0494\7D\2\2\u047a\u047b\f$\2"+
		"\2\u047b\u047c\7K\2\2\u047c\u047d\5j\66\2\u047d\u047e\7L\2\2\u047e\u0480"+
		"\7C\2\2\u047f\u0481\5\u00be`\2\u0480\u047f\3\2\2\2\u0480\u0481\3\2\2\2"+
		"\u0481\u0482\3\2\2\2\u0482\u0483\7D\2\2\u0483\u0494\3\2\2\2\u0484\u0485"+
		"\f!\2\2\u0485\u0486\7G\2\2\u0486\u0487\5\u00caf\2\u0487\u0488\7H\2\2\u0488"+
		"\u0494\3\2\2\2\u0489\u048a\f \2\2\u048a\u048b\7G\2\2\u048b\u048c\5\u00ca"+
		"f\2\u048c\u048d\7@\2\2\u048d\u048e\5\u00caf\2\u048e\u048f\7H\2\2\u048f"+
		"\u0494\3\2\2\2\u0490\u0491\f\27\2\2\u0491\u0492\7J\2\2\u0492\u0494\5\u00c4"+
		"c\2\u0493\u0434\3\2\2\2\u0493\u0437\3\2\2\2\u0493\u043a\3\2\2\2\u0493"+
		"\u043d\3\2\2\2\u0493\u0440\3\2\2\2\u0493\u0443\3\2\2\2\u0493\u0446\3\2"+
		"\2\2\u0493\u044a\3\2\2\2\u0493\u044d\3\2\2\2\u0493\u0450\3\2\2\2\u0493"+
		"\u0453\3\2\2\2\u0493\u0456\3\2\2\2\u0493\u0459\3\2\2\2\u0493\u045c\3\2"+
		"\2\2\u0493\u045f\3\2\2\2\u0493\u0462\3\2\2\2\u0493\u0465\3\2\2\2\u0493"+
		"\u0468\3\2\2\2\u0493\u046b\3\2\2\2\u0493\u046e\3\2\2\2\u0493\u0474\3\2"+
		"\2\2\u0493\u047a\3\2\2\2\u0493\u0484\3\2\2\2\u0493\u0489\3\2\2\2\u0493"+
		"\u0490\3\2\2\2\u0494\u0497\3\2\2\2\u0495\u0493\3\2\2\2\u0495\u0496\3\2"+
		"\2\2\u0496\u00cb\3\2\2\2\u0497\u0495\3\2\2\2\u0498\u0499\7M\2\2\u0499"+
		"\u049a\5\u00caf\2\u049a\u00cd\3\2\2\2\u049b\u049c\7N\2\2\u049c\u049d\5"+
		"\u00caf\2\u049d\u00cf\3\2\2\2\u049e\u049f\7P\2\2\u049f\u04a0\5\u00caf"+
		"\2\u04a0\u00d1\3\2\2\2\u04a1\u04a2\7O\2\2\u04a2\u04a3\5\u00caf\2\u04a3"+
		"\u00d3\3\2\2\2\u04a4\u04aa\5\u00d6l\2\u04a5\u04aa\5\u00d8m\2\u04a6\u04aa"+
		"\5\u00dan\2\u04a7\u04aa\5\u00dco\2\u04a8\u04aa\5\u00dep\2\u04a9\u04a4"+
		"\3\2\2\2\u04a9\u04a5\3\2\2\2\u04a9\u04a6\3\2\2\2\u04a9\u04a7\3\2\2\2\u04a9"+
		"\u04a8\3\2\2\2\u04aa\u00d5\3\2\2\2\u04ab\u04ac\7\4\2\2\u04ac\u00d7\3\2"+
		"\2\2\u04ad\u04ae\7\5\2\2\u04ae\u00d9\3\2\2\2\u04af\u04b0\7\6\2\2\u04b0"+
		"\u00db\3\2\2\2\u04b1\u04b2\7\3\2\2\u04b2\u00dd\3\2\2\2\u04b3\u04b4\7\7"+
		"\2\2\u04b4\u00df\3\2\2\2l\u00e3\u00e7\u00ea\u00f5\u00fe\u0101\u0106\u010e"+
		"\u0112\u0119\u011e\u0126\u012c\u0130\u0135\u013a\u0142\u014a\u014e\u0156"+
		"\u015b\u0161\u0165\u016c\u0173\u0176\u017d\u0185\u0193\u0198\u019d\u01a4"+
		"\u01ad\u01b5\u01c5\u01c9\u01cd\u01d7\u01db\u01e2\u01e9\u01f1\u01f5\u01fe"+
		"\u0203\u0208\u020c\u0216\u021a\u0221\u0228\u024d\u0252\u025d\u0262\u026b"+
		"\u0279\u027f\u0286\u028f\u0298\u029f\u02bc\u02c0\u02c8\u02cf\u02db\u02e7"+
		"\u02eb\u02f3\u02fe\u0303\u031b\u0322\u0330\u0335\u033c\u0344\u034b\u0350"+
		"\u0354\u035a\u0366\u036c\u0389\u038e\u039d\u03a3\u03b2\u03b6\u03bd\u03c3"+
		"\u03c8\u03de\u03e8\u03f1\u0405\u0407\u041a\u0420\u0432\u0477\u0480\u0493"+
		"\u0495\u04a9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}