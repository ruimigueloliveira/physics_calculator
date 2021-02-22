grammar TypesInterpreter;

file    : stat* EOF
        ;

stat    : expr NEWLINE
        | expr
        ;

expr    : exprSec                   # parseSec
        | IDI '['IDI']' ':' type    # parsePrim
        | IDI ('['IDI']')? ':' NUM  # const
        ;

exprSec : id=IDI ':' comp
        ;

comp    : c1=comp OP c2=comp            # op
        | '(' comp ')' ('^' EXP)?       # parenthesis
        | IDI ('^' EXP)?                # exp
        ;

type returns[String t]
        : 'Integer' { $t = "int"; } 
        | 'Double' { $t = "double"; }
        ;

OP      : '*'|'/';
IDI     : [a-zA-Z]+;
EXP     : [2-9]+ [0-9]* | '-'[1-9]+ ;
NUM     : [0-9]+ | [0-9]+ '.' [0-9]+;
NEWLINE : '\r'? '\n' ;
WS      : [ \t\n\r]+ -> skip;


