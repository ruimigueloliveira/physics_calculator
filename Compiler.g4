grammar Compiler;

program : (stat)* end
        ;

stat    : define
        | update
        | show
        | decisao
        | cicloWhile
        | function
        | callFunction
        | increment
        ;

define  : type=WORD id=WORD                                             #defineId          
        | type=WORD id=WORD '=' value=operacao                          #defineOperacaoValue  
        ;

update  : id=WORD '=' value=operacao    
        ;
     
show    : 'show' STRING         #mstr
        | 'show' operacao       #moper
        ;

operacao: l1=operacao op=('*'|'/') l2=operacao  #multi
        | l1=operacao op=('-'|'+') l2=operacao  #sum
        | '(' operacao ')'                      #parenteses
        | (v=NUM|v=WORD)                        #operando
        | callNoArgFunction                     #noargs
        | callArgsFunction                      #moreargs
        ;

decisao : 'if''('condicao')''{'stat* stop?'}' decaux?
        ; 

decaux  : 'else''if''('condicao')''{'stat* stop?'}' decaux    #multipleElseIf 
        | 'else''if''('condicao')''{'stat* stop?'}'           #singleElseIf
        | 'else''{'stat* stop?'}'                             #else
        ;

cicloWhile
        : 'while''('condicao')''{'stat* stop?'}'
        ;

condicao: op1=operacao op=('<'|'>'|'<='|'>='|'=='|'!=') op2=operacao logica?                    #condicaoSimples
        | '!' '(' op1=operacao op=('<'|'>'|'<='|'>='|'=='|'!=') op2=operacao ')' logica?        #condicaoNegada
        | '(' condicao ')' logica?                                                              #condicaoParenteses
        ;

logica  : op=('&&'|'||') condicao
        ;

function: type=WORD name=WORD '('arg?')' '{'stat* devolve?'}'
        ; 

arg     : type0=WORD id0=WORD (',' type1=WORD id1=WORD)*         
        ;

callFunction
        : callNoArgFunction     #callnoarg
        | callArgsFunction      #callarg
        ;

callNoArgFunction
        : WORD '('')'
        ;

callArgsFunction
        : WORD '('operacao(','operacao)*')'
        ;

increment
        : WORD op=('++'|'--') #d
        | op=('++'|'--') WORD #a
        ;

devolve : 'return' operacao?
        ;

stop    : 'break'
        ;

end     : EOF
        ;

NUM     : ('-')?[0-9]+('.'[0-9]+)?;
WORD    : [a-zA-Z_][a-zA-Z_0-9]*;
STRING  : '"' .*? '"';
WS      : [ \t\n\r]+ -> skip;

/*
Nota: Tentámos fazer um ciclo for, no entanto decidimos visto à sua utilidade não o usar

cicloFor: 'for''('(define|update)';'condicao';'(operacao|increment)')''{'stat+'}'
        ;

*/