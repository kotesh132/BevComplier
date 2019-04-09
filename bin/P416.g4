grammar P416;
@header {
import com.p4.p416.gen.*;
}

p4program
locals [P4programContextExt extendedContext]
:	|( declaration SEMI?)*
	;
declaration
locals [DeclarationContextExt extendedContext]
:	constantDeclaration
    | externDeclaration
    | actionDeclaration
    | parserDeclaration
    | typeDeclaration
    | controlDeclaration
    | instantiation
    | errorDeclaration
    | matchKindDeclaration;

nonTypeName
locals [NonTypeNameContextExt extendedContext]
:	IDENTIFIER
    | APPLY
    | KEY
    | ACTIONS
    | STATE;

name
locals [NameContextExt extendedContext]
:	nonTypeName
    | IDENTIFIER
    | ERROR
    | EXTRACT
    | APPLY;

optAnnotations
locals [OptAnnotationsContextExt extendedContext]
:	annotations?;

annotations
locals [AnnotationsContextExt extendedContext]
:	annotation+;

annotation
locals [AnnotationContextExt extendedContext]
:	AT name							#simpleAnnotation
    | AT name LPARAN expressionList? RPARAN	#complexAnnotation;

parameterList
locals [ParameterListContextExt extendedContext]
:	parameter (COMMA parameter)*;

parameter
locals [ParameterContextExt extendedContext]
:	optAnnotations direction? typeRef name;

direction
locals [DirectionContextExt extendedContext]
:	IN	#inDirection
	| OUT	#outDirection
	| INOUT	#inOutDirection;

packageTypeDeclaration
locals [PackageTypeDeclarationContextExt extendedContext]
:	optAnnotations PACKAGE name optTypeParameters? LPARAN parameterList? RPARAN;

instantiation
locals [InstantiationContextExt extendedContext]
:	annotations? typeRef LPARAN argumentList? RPARAN name SEMI;

optConstructorParameters
locals [OptConstructorParametersContextExt extendedContext]
:	LPARAN parameterList? RPARAN;

dotPrefix
locals [DotPrefixContextExt extendedContext]
:	DOT;

/**************************** PARSER ******************************/
parserDeclaration
locals [ParserDeclarationContextExt extendedContext]
:	parserTypeDeclaration optConstructorParameters? LCURL parserLocalElements? parserStates RCURL;

parserLocalElements
locals [ParserLocalElementsContextExt extendedContext]
:	parserLocalElement+;

parserLocalElement
locals [ParserLocalElementContextExt extendedContext]
:	constantDeclaration
    | variableDeclaration
    | instantiation;

parserTypeDeclaration
locals [ParserTypeDeclarationContextExt extendedContext]
:	optAnnotations PARSER name optTypeParameters? LPARAN parameterList? RPARAN;

parserStates
locals [ParserStatesContextExt extendedContext]
:	parserState+;

parserState
locals [ParserStateContextExt extendedContext]
:	optAnnotations STATE name LCURL parserStatements? transitionStatement? RCURL;

parserStatements
locals [ParserStatementsContextExt extendedContext]
:	parserStatement+;

parserStatement
locals [ParserStatementContextExt extendedContext]
:	assignmentStatement
    | methodCallStatement
    | directApplication
    | parserBlockStatement
    | constantDeclaration
    | variableDeclaration;

parserBlockStatement
locals [ParserBlockStatementContextExt extendedContext]
:	optAnnotations LCURL parserStatements RCURL;

transitionStatement
locals [TransitionStatementContextExt extendedContext]
:	TRANSITION stateExpression;

stateExpression
locals [StateExpressionContextExt extendedContext]
:	name SEMI
    | selectExpression;

selectExpression
locals [SelectExpressionContextExt extendedContext]
:	SELECT LPARAN expressionList? RPARAN LCURL selectCaseList? RCURL;

selectCaseList
locals [SelectCaseListContextExt extendedContext]
:	selectCase+;

selectCase
locals [SelectCaseContextExt extendedContext]
:	keySetExpression COLON name SEMI;

keySetExpression
locals [KeySetExpressionContextExt extendedContext]
:	tupleKeySetExpression 
  	|  simpleKeySetExpression;

tupleKeySetExpression
locals [TupleKeySetExpressionContextExt extendedContext]
:	LPARAN simpleKeySetExpression ( COMMA simpleKeySetExpression )* RPARAN;

simpleKeySetExpression
locals [SimpleKeySetExpressionContextExt extendedContext]
:	expression
    | DEFAULT
    | DONTCARE
    | expression MASK expression
    | expression RANGE expression;

/*************************** CONTROL ************************/
controlDeclaration
locals [ControlDeclarationContextExt extendedContext]
:	controlTypeDeclaration optConstructorParameters?
      /* no type parameters allowed in controlTypeDeclaration */
      LCURL controlLocalDeclarations? APPLY controlBody RCURL;

controlTypeDeclaration
locals [ControlTypeDeclarationContextExt extendedContext]
:	optAnnotations CONTROL name optTypeParameters?
      LPARAN parameterList? RPARAN;

controlLocalDeclarations
locals [ControlLocalDeclarationsContextExt extendedContext]
:	controlLocalDeclaration+;

controlLocalDeclaration
locals [ControlLocalDeclarationContextExt extendedContext]
:	constantDeclaration
    | actionDeclaration
    | tableDeclaration
    | instantiation
    | variableDeclaration;

controlBody
locals [ControlBodyContextExt extendedContext]
:	blockStatement;

/*************************** EXTERN *************************/
externDeclaration
locals [ExternDeclarationContextExt extendedContext]
:	optAnnotations EXTERN nonTypeName optTypeParameters? LCURL methodPrototypes? RCURL	#externObjectDeclaration
    | optAnnotations EXTERN functionPrototype SEMI										#externFunctionDeclaration;

methodPrototypes
locals [MethodPrototypesContextExt extendedContext]
:	methodPrototype+;

functionPrototype
locals [FunctionPrototypeContextExt extendedContext]
:	typeOrVoid name optTypeParameters? LPARAN parameterList? RPARAN;

methodPrototype
locals [MethodPrototypeContextExt extendedContext]
:	functionPrototype SEMI
    | IDENTIFIER LPARAN parameterList? RPARAN SEMI;

/************************** TYPES ****************************/
typeRef
locals [TypeRefContextExt extendedContext]
:	baseType
    | typeName
    | specializedType
    | headerStackType
    | tupleType;

prefixedType
locals [PrefixedTypeContextExt extendedContext]
:	IDENTIFIER
    | dotPrefix IDENTIFIER
    | ERROR;

typeName
locals [TypeNameContextExt extendedContext]
:	prefixedType;

tupleType
locals [TupleTypeContextExt extendedContext]
:	TUPLE LT typeArgumentList GT;

headerStackType
locals [HeaderStackTypeContextExt extendedContext]
:	typeName LBRKT expression RBRKT;

specializedType
locals [SpecializedTypeContextExt extendedContext]
:	prefixedType LT typeArgumentList GT;

baseType
locals [BaseTypeContextExt extendedContext]
:	BOOL						#boolType
	| ERROR						#errorType
	| BIT						#bitType		
	| BIT  LT  number  GT		#bitSizeType
	| INT  LT  number  GT		#intSizeType
	| VARBIT  LT  number  GT	#varBitSizeType;

typeOrVoid
locals [TypeOrVoidContextExt extendedContext]
:	VOID
    | typeRef
    | nonTypeName;

optTypeParameters
locals [OptTypeParametersContextExt extendedContext]
:	LT typeParameterList GT;

typeParameterList
locals [TypeParameterListContextExt extendedContext]
:	nonTypeName ( COMMA nonTypeName) *;

typeArg
locals [TypeArgContextExt extendedContext]
:	dontcare
    | typeRef;

dontcare
locals [DontcareContextExt extendedContext]
:	DONTCARE;

typeArgumentList
locals [TypeArgumentListContextExt extendedContext]
:	typeArg (COMMA typeArg)*;

typeDeclaration
locals [TypeDeclarationContextExt extendedContext]
:	derivedTypeDeclaration
    | typedefDeclaration
    | parserTypeDeclaration SEMI
    | controlTypeDeclaration SEMI
    | packageTypeDeclaration SEMI;

derivedTypeDeclaration
locals [DerivedTypeDeclarationContextExt extendedContext]
:	headerTypeDeclaration
    | headerUnionDeclaration
    | structTypeDeclaration
    | enumDeclaration;

headerTypeDeclaration
locals [HeaderTypeDeclarationContextExt extendedContext]
:	optAnnotations HEADER name LCURL structFieldList? RCURL;

headerUnionDeclaration
locals [HeaderUnionDeclarationContextExt extendedContext]
:	optAnnotations HEADER_UNION name LCURL structFieldList? RCURL;

structTypeDeclaration
locals [StructTypeDeclarationContextExt extendedContext]
:	optAnnotations STRUCT name LCURL structFieldList? RCURL;

structFieldList
locals [StructFieldListContextExt extendedContext]
:	structField+;

structField
locals [StructFieldContextExt extendedContext]
:	optAnnotations typeRef name SEMI;

enumDeclaration
locals [EnumDeclarationContextExt extendedContext]
:	optAnnotations ENUM name LCURL identifierList RCURL;

errorDeclaration
locals [ErrorDeclarationContextExt extendedContext]
:	ERROR LCURL identifierList RCURL;

matchKindDeclaration
locals [MatchKindDeclarationContextExt extendedContext]
:	MATCH_KIND LCURL identifierList RCURL;

identifierList
locals [IdentifierListContextExt extendedContext]
:	name ( COMMA name )*;

typedefDeclaration
locals [TypedefDeclarationContextExt extendedContext]
:	annotations? TYPEDEF typeRef name SEMI					#simpleTypeDef
    | annotations? TYPEDEF derivedTypeDeclaration name SEMI	#derivedTypeDef;

/*************************** STATEMENTS *************************/
assignmentStatement
locals [AssignmentStatementContextExt extendedContext]
:	lvalue ASSIGN  expression SEMI;

methodCallStatement
locals [MethodCallStatementContextExt extendedContext]
:	lvalue DOT APPLY LPARAN argumentList? RPARAN SEMI	#applyMethodCall
    | lvalue DOT EXTRACT (LT typeArgumentList GT)? LPARAN argumentList? RPARAN SEMI	#extractMethodCall
    | lvalue LPARAN argumentList? RPARAN SEMI								#callWithoutTypeArgs
    | lvalue LT typeArgumentList GT LPARAN argumentList? RPARAN SEMI		#callWithTypeArgs;

emptyStatement
locals [EmptyStatementContextExt extendedContext]
:	SEMI;

returnStatement
locals [ReturnStatementContextExt extendedContext]
:	RETURN SEMI;

exitStatement
locals [ExitStatementContextExt extendedContext]
:	EXIT SEMI;

conditionalStatement
locals [ConditionalStatementContextExt extendedContext]
:	IF LPARAN expression RPARAN statement					#ifStatement
    | IF LPARAN expression RPARAN statement ELSE statement	#ifElseStatement;

directApplication
locals [DirectApplicationContextExt extendedContext]
:	typeName DOT APPLY LPARAN argumentList? RPARAN SEMI;

statement
locals [StatementContextExt extendedContext]
:	assignmentStatement
    | methodCallStatement
    | directApplication
    | conditionalStatement
    | emptyStatement
    | blockStatement
    | exitStatement
    | returnStatement
    | switchStatement;

blockStatement
locals [BlockStatementContextExt extendedContext]
:	optAnnotations LCURL statOrDeclList? RCURL;

statOrDeclList
locals [StatOrDeclListContextExt extendedContext]
:	statementOrDeclaration+;

switchStatement
locals [SwitchStatementContextExt extendedContext]
:	SWITCH LPARAN expression RPARAN LCURL switchCases? RCURL;

switchCases
locals [SwitchCasesContextExt extendedContext]
:	switchCase+;

switchCase
locals [SwitchCaseContextExt extendedContext]
:	switchLabel COLON blockStatement?;

switchLabel
locals [SwitchLabelContextExt extendedContext]
:	name
    | DEFAULT;

statementOrDeclaration
locals [StatementOrDeclarationContextExt extendedContext]
:	variableDeclaration
    | constantDeclaration
    | statement
    | instantiation;

/************ TABLES *************/
tableDeclaration
locals [TableDeclarationContextExt extendedContext]
:	optAnnotations TABLE name LCURL tablePropertyList RCURL;

tablePropertyList
locals [TablePropertyListContextExt extendedContext]
:	tableProperty+;

tableProperty
locals [TablePropertyContextExt extendedContext]
:	KEY  ASSIGN  LCURL  keyElementList?  RCURL 						#key
	| ACTIONS  ASSIGN  LCURL  actionList  RCURL						#actions
	| CONST ENTRIES ASSIGN LCURL entriesList RCURL						#constEntries
	| optAnnotations  CONST  IDENTIFIER  ASSIGN  initializer  SEMI	#localVariable
	| optAnnotations  IDENTIFIER  ASSIGN  initializer  SEMI			#localConstVariable;

keyElementList
locals [KeyElementListContextExt extendedContext]
:	keyElement+;

keyElement
locals [KeyElementContextExt extendedContext]
:	expression COLON name optAnnotations SEMI;

actionList
locals [ActionListContextExt extendedContext]
:	actionRef SEMI (actionRef  SEMI )*;

entriesList
locals [EntriesListContextExt extendedContext]
:	entry+;

entry
locals [EntryContextExt extendedContext]
:	optAnnotations keySetExpression COLON actionRef SEMI;

actionRef
locals [ActionRefContextExt extendedContext]
:	optAnnotations name							#actionWithoutArgs
    | optAnnotations name LPARAN argumentList? RPARAN		#actionWithArgs;

/************************* ACTION ********************************/
actionDeclaration
locals [ActionDeclarationContextExt extendedContext]
:	optAnnotations ACTION name LPARAN parameterList? RPARAN blockStatement;

/************************* VARIABLES *****************************/
variableDeclaration
locals [VariableDeclarationContextExt extendedContext]
:	annotations? typeRef name optInitializer? SEMI;

constantDeclaration
locals [ConstantDeclarationContextExt extendedContext]
:	optAnnotations CONST typeRef name ASSIGN initializer SEMI;

optInitializer
locals [OptInitializerContextExt extendedContext]
:	ASSIGN initializer;

initializer
locals [InitializerContextExt extendedContext]
:	expression;

/************************* Expressions ****************************/
argumentList
locals [ArgumentListContextExt extendedContext]
:	argument (COMMA argument)*;

argument
locals [ArgumentContextExt extendedContext]
:	expression;

expressionList
locals [ExpressionListContextExt extendedContext]
:	expression (COMMA expression)*;

member
locals [MemberContextExt extendedContext]
:	name;

prefixedNonTypeName
locals [PrefixedNonTypeNameContextExt extendedContext]
:	nonTypeName
    | dotPrefix nonTypeName;

lvalue
locals [LvalueContextExt extendedContext]
:	prefixedNonTypeName						#prefixedNonTypeNameLvalue
    | lvalue DOT member							#prefixedNameLvalue
    | lvalue LBRKT expression RBRKT					#arrayIndexLvalue
    | lvalue LBRKT expression COLON expression RBRKT	#rangeIndexLvalue;

expression
locals [ExpressionContextExt extendedContext]
:	number											#integer
    | TRUE												#true
    | FALSE												#false
    | STRING_LITERAL									#string
    | nonTypeName										#nonType
    | DOT nonTypeName									#prefixedNonType
    | expression LPARAN argumentList? RPARAN			#functionCall
    | expression LT typeArgumentList GT LPARAN argumentList? RPARAN #templetizedFunctionCall
    | typeRef LPARAN argumentList? RPARAN				#constructor
    | LPARAN typeRef RPARAN expression					#cast
    | expression LBRKT expression RBRKT					#arrayIndex
    | expression LBRKT expression COLON expression RBRKT	#rangeIndex
    | LCURL expressionList? RCURL						#set
    | LPARAN expression RPARAN							#of
    | unaryExpression_not								#not
    | unaryExpression_tilda								#negate
    | unaryExpression_minus								#unaryMinus
    | unaryExpression_plus								#unaryPlus
    | typeName DOT member								#memberAccess
    | ERROR DOT member									#errorMemberAccess
    | expression DOT member								#exprMemberAccess
    | expression STAR expression						#star
    | expression SLASH expression						#slah
    | expression PRCNT expression						#mod
    | expression PLUS expression						#plus
    | expression MINUS expression						#minus
    | expression SHL expression 						#shiftLeft
    | expression GT GT expression						#shifRight
    | expression AND expression							#bitAnd
    | expression XOR expression							#bitXor
    | expression OR expression							#bitOr
    | expression PP expression							#plusPlus
    | expression LE expression							#lessThanOrEqual
    | expression GE expression							#greaterThanOrEqual
    | expression LT expression							#lessThan
    | expression GT expression							#greaterThan
    | expression NE expression							#notEqual
    | expression EQ expression							#equal
    | expression LAND expression						#and
    | expression LOR expression							#or
    | expression QUESTION expression COLON expression	#ternary;

unaryExpression_not
locals [UnaryExpression_notContextExt extendedContext]
: NOT expression ;

unaryExpression_tilda
locals [UnaryExpression_tildaContextExt extendedContext]
: TILDA expression ;

unaryExpression_plus
locals [UnaryExpression_plusContextExt extendedContext]
: PLUS expression ;

unaryExpression_minus
locals [UnaryExpression_minusContextExt extendedContext]
: MINUS expression ;

number
locals [NumberContextExt extendedContext]
:	decimalNumber
| octalNumber
| binaryNumber
| hexNumber
| realNumber;

decimalNumber
locals [DecimalNumberContextExt extendedContext]
:	Decimal_number;

octalNumber
locals [OctalNumberContextExt extendedContext]
:	Octal_number;

binaryNumber
locals [BinaryNumberContextExt extendedContext]
:	Binary_number;

hexNumber
locals [HexNumberContextExt extendedContext]
:	Hex_number;

realNumber
locals [RealNumberContextExt extendedContext]
:	Real_number;

Hex_number : '0'[xX][0-9a-fA-F_]+ | [0-9]+[ws]'0'[xX][0-9a-fA-F_]+;
Decimal_number : '0'[dD][0-9_]+ | [0-9]+[ws]'0'[dD][0-9_]+;
Octal_number : '0'[oO][0-7_]+ | [0-9]+[ws]'0'[oO][0-7_]+;
Binary_number : '0'[bB][01_]+ | [0-9]+[ws]'0'[bB][01_]+;
Real_number : [0-9][0-9_]* | [0-9]+[ws][0-9_]+ ;
COMMENT
:   ('//' ~('\n'|'\r')* '\r'? '\n' 
        |   '/*' .*? '*/' )-> channel(HIDDEN)
;
WS  :   [ \t\n\r]+ -> channel(HIDDEN)
    ;
ABSTRACT : 'abstract';
ACTION : 'action';
ACTIONS : 'actions';
ENTRIES : 'entries';
APPLY : 'apply';
EXTRACT: 'extract';
BOOL : 'bool';
BIT : 'bit';
CONST : 'const';
CONTROL : 'control';
DEFAULT : 'default';
ELSE : 'else';
ENUM : 'enum';
ERROR : 'error' ;
EXIT : 'exit';
EXTERN : 'extern';
FALSE : 'false';
HEADER : 'header';
HEADER_UNION : 'header_union';
IF : 'if';
IN : 'in';
INOUT : 'inout';
INT : 'int';
KEY : 'key';
MATCH_KIND : 'match_kind';
OUT : 'out';
PARSER : 'parser';
PACKAGE : 'package';
RETURN : 'return';
SELECT : 'select';
STATE : 'state';
STRUCT : 'struct';
SWITCH : 'switch';
TABLE : 'table';
THIS : 'this';
TRANSITION : 'transition';
TRUE : 'true';
TUPLE : 'tuple';
TYPEDEF : 'typedef';
VARBIT : 'varbit';
VOID : 'void';
DONTCARE : '_';
IDENTIFIER : [A-Za-z_][A-Za-z0-9_]*;
MASK : '&&&' ;
RANGE : '..';
SHL : '<<';
LAND : '&&' ;
LOR : '||';
EQ : '==';
NE : '!=';
GE : '>=';
LE : '<=';
PP : '++';
SEMI: ';';
COLON: ':';
AT: '@';
COMMA:',';
LPARAN: '(';
RPARAN: ')';
LCURL: '{';
RCURL: '}';
LBRKT: '[';
RBRKT: ']';
ASSIGN: '=';
DOT: '.';
LT: '<';
GT: '>';
NOT : '!';
TILDA:'~';
MINUS: '-';
PLUS: '+';
STAR: '*';
SLASH: '/';
PRCNT: '%';
AND: '&';
OR: '|';
XOR: '^';
QUESTION : '?';
STRING_LITERAL : '"' ( ~[\n\r"] )* '"' ;
