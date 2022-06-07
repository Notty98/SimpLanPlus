grammar bytecode;

@header {
import java.util.HashMap;
}

@lexer::members {
public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

assembly: (instruction)* ;

instruction:
    ( LW r1=REG offset=NUMBER'('r2=REG')' // 32 bit - integer
      | LW_B r1=REG offset=NUMBER '('r2=REG')' //8 bit - bool
      | ADD r1=REG r2=REG r3=REG
      | SUB r1=REG r2=REG r3=REG
      | DIV r1=REG r2=REG r3=REG
      | MULT r1=REG r2=REG r3=REG
      | SW r1=REG offset=NUMBER '('r2=REG')' // 32 bit - integer
      | SW_B r1=REG offset=NUMBER '('r2=REG')' //8 bit - bool
      | ADDI r1=REG r2=REG n=NUMBER
      | LI r1=REG n=NUMBER
      | LB r1=REG n=NUMBER
      | MOVE r1=REG r2=REG
      | BEQ r1=REG r2=REG label=LABEL
      | BGT r1=REG r2=REG label=LABEL
      | BGE r1=REG r2=REG label=LABEL
      | BEQ_B r1=REG r2=REG label=LABEL
      | BRANCH label=LABEL
	  | LABEL
	  | JTL label=LABEL
	  | JR reg=REG
	  | PRINT reg=REG
	  | HALT
	  ) ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

LW: 'lw';
LW_B: 'lw_b';
ADDI: 'addi';
ADD	 : 'add' ;  	// add two values from the stack
SUB	 : 'sub' ;	// add two values from the stack
MULT	 : 'mult' ;  	// add two values from the stack
DIV	 : 'div' ;	// add two values from the stack
SW: 'sw';
SW_B: 'sw_b';
LI: 'li';
LB: 'lb';
MOVE: 'move';
BEQ: 'beq';
BEQ_B: 'beq_b';
BGT: 'bgt';
BGE: 'bge';
BRANCH: 'branch';
JTL: 'jtl';
JR: 'jr';
PRINT	 : 'print';	// print top of stack
HALT	 : 'halt' ;	// stop execution

fragment DIGIT: '0'..'9';
fragment LETTER: 'a'..'z';

COL	 : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9' | '_')* ;
REG: '$' LETTER (LETTER | DIGIT);
NUMBER: DIGIT+ | '-' DIGIT+;
//NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);

ERR   	 : . { System.err.println("Invalid char: "+ getText()); lexicalErrors++;  } -> channel(HIDDEN);

