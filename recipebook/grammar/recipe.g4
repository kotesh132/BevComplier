grammar recipe;

@header {
}


start
: declaration declaration*;

declaration
: (input_output_declaration | symbols_declaration | table_declaration | packet_declaration);

input_output_declaration
: INPUT ':'   key_value_pair*
| OUTPUT ':'  key_value_pair*;


symbols_declaration
:SYMBOLS ':' names_list?;

names_list
: name (',' name)*;

/* table_add <table name> <action name> <match fields> => <action parameters> [priority]" */

table_declaration
: TABLE_CONFIGURATION ':' table_add*;

table_add
: TABLEADD tablename actionname matchfields '=>' actionparams priority?;

tablename
: TABLE ':' IDENTIFIER;

actionname
: ACTION ':' IDENTIFIER;

matchfields
: KEY ':' ( IDENTIFIER '/' Real_number | ipaddress '/' Real_number | IDENTIFIER );

actionparams
:( (ipaddress '/' Real_number)
| (IDENTIFIER '/' Real_number)
| IDENTIFIER )*;

ipaddress
: number '.' number '.' number '.' number '/' number
| number '.' number '.' number '.' number;

priority
: '[' number ']';



/* sr(IP(dst=["192.168.1.1","yahoo.com","slashdot.org"])/TCP(dport=[22,80,443],flags="S"))
 * sr(IP(dst=target, ttl=(4,25),id=RandShort())/TCP(flags=0x2))
 * sr1(IP(dst="192.168.5.1")/UDP()/DNS(rd=1,qd=DNSQR(qname="www.slashdot.org")))
 * p=sr1(IP(dst="www.slashdot.org")/ICMP()/"XXXXXXXXXXX")
 * send(IP(dst="target")/fuzz(UDP()/NTP(version=4)),loop=1)
 * sniff(filter="icmp and host 66.35.250.151", count=2)
 * 
 */
 
 value
 : '[' argument_list ']'
 |arg;
 
 argument_list
 : arg ( ',' arg)*;
 
 arg
 : expression
 |name;
 
 key
 : name;
 
 key_value_pair
 : key '=' value;
 
  key_value_pair_list
 : key_value_pair (',' key_value_pair)*;
 
 protocol_name
 :Ether
|IP
|TCP
|UDP;

protocol_hdr
: protocol_name '(' key_value_pair_list ? ')';

protocol_hdr_list
: protocol_hdr ('/' protocol_hdr)* ;

packet
: '(' protocol_hdr_list ')';
 
packet_declaration
: INPUTPACKET ':' packet*
| OUTPUTPACKET ':' packet*;


expressionList
: expression 
|  expressionList  ','  expression ;

typeRef
: baseType 
|  typeName 
|  specializedType 
|  headerStackType 
|  tupleType ;

prefixedType
: IDENTIFIER 
|  T_ERROR 
|  dotPrefix  IDENTIFIER ;

typeName
: prefixedType ;

tupleType
: TUPLE  '<'  typeArgumentList  '>' ;

headerStackType
: typeName  '['  expression  ']' ;

specializedType
: typeName  '<'  typeArgumentList  '>' ;

baseType
: BOOL 
|  BIT 
|  BIT  '<'  number  '>' 
|  INT  '<'  number  '>' 
|  VARBIT  '<'  number  '>' ;

typeArg
: typeRef 
|  DONTCARE ;

typeArgumentList
: typeArg 
|  typeArgumentList  ','  typeArg ;


dotPrefix
: '.' ;

nonTypeName
: IDENTIFIER 
|  APPLY 
|  KEY 
|  ACTION
|  STATE ;

name
: nonTypeName 
|  IDENTIFIER ;

argumentList
:;

expression
: number 
| IDENTIFIER
|  STRING 
|  TRUE 
|  FALSE 
|  THIS 
|  expression  '['  expression  ']' 
|  expression  '['  expression  ':'  expression  ']' 
|  '{'  expressionList  '}' 
|  '('  expression  ')' 
|  '!'  expression 
|  '~'  expression 
|  '-'  expression 
|  '+'  expression 
|  typeName  '.'  name 
|  T_ERROR  '.'  name 
|  expression  '.'  name 
|  expression  '*'  expression 
|  expression  '/'  expression 
|  expression  '%'  expression 
|  expression  '+'  expression 
|  expression  '-'  expression 
|  expression  SHL  expression 
|  expression  '>'  '>'  expression 
|  expression  LE  expression 
|  expression  GE  expression 
|  expression  '<'  expression 
|  expression  '>'  expression 
|  expression  NE  expression 
|  expression  EQ  expression 
|  expression  '&'  expression 
|  expression  '^'  expression 
|  expression  '|'  expression 
|  expression  PP  expression 
|  expression  AND  expression 
|  expression  OR  expression 
|  expression  '?'  expression  ':'  expression 
|  expression  '<'  typeArgumentList  '>'  '('  argumentList  ')' 
|  expression  '('  argumentList  ')' 
|  typeRef  '('  argumentList  ')' 
|  '('  typeRef  ')'  expression ;



number
: Decimal_number 
|  Octal_number 
|  Binary_number 
|  Hex_number 
|  Real_number ;



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


TRUE :	'true';
FALSE:  'false';
APPLY : 'apply';
KEY :	'key';
ACTION:	'action';
STATE :	'state';
BOOL:	'bool';
BIT:	'bit';
INT:	'int';
VARBIT:	'varbit';
T_ERROR:'error' ;
TABLEADD:	'table_add';
TABLE:	'table';
INPUT:	'input';
OUTPUT: 'output';
INPUTPACKET: 'input_packets';
OUTPUTPACKET: 'output_packets';
TABLE_CONFIGURATION: 'table_configuration';
SYMBOLS: 'symbols';
TUPLE: 'tuple';
THIS:	'this';
Ether:	'Ether';
IP:		'IP';
TCP:	'TCP';
UDP:	'UDP';



STRING : '"' ( ~[\n\r"] )* '"' ;
DONTCARE : '_';
IDENTIFIER : [A-Za-z_][A-Za-z0-9_]*;
MASK : '&&&' ;
RANGE : '..';
SHL : '<<';
AND : '&&' ;
OR : '||';
EQ : '==';
NE : '!=';
GE : '>=';
LE : '<=';
PP : '++';