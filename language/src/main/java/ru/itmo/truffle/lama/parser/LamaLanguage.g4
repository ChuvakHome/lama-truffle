grammar LamaLanguage;

program
    : importStatement* topScope EOF
    ;

topScope
    : definition* expression;

importStatement
    : IMPORT LIDENT (',' LIDENT)* ';'?
    ;

scopeExpression
    : definition* expression?
    ;

definition
    : varDefinition
    | funDefinition
    ;

varDefinition
    : VAR varInit (',' varInit)* ';'?
    ;

varInit
    : LIDENT ('=' assignExpression)?
    ;

funDefinition
    : FUN LIDENT '(' patternList? ')' '{' scopeExpression '}' ';'?
    ;

patternList
    : pattern (',' pattern)*
    ;

pattern
    : LIDENT '@' pattern                  # aliasPattern
    | PATTERN_VAL                         # patternValTag
    | PATTERN_FUN                         # patternFunTag
    | PATTERN_STR                         # patternStrTag
    | PATTERN_ARRAY                       # patternArrayTag
    | PATTERN_SEXP                        # patternSexpTag
    | PATTERN_BOX                         # patternBoxTag
    | LIDENT                              # varPattern
    | UIDENT '(' patternList? ')'         # sexpPatternWithArgs
    | UIDENT                              # sexpPatternNoArgs
    | '[' patternList? ']'                # arrayPattern
    | '{' patternList? '}'                # listPattern
    | '_'                                 # wildcardPattern
    | DECIMAL                             # decimalPattern
    | STRING                              # stringPattern
    | CHAR                                # charPattern
    | TRUE                                # truePattern
    | FALSE                               # falsePattern
    | '(' pattern ')'                     # parenPattern
    | <assoc=right> pattern COLON pattern # consPattern
    ;

expression
    : sequenceExpression
    ;

sequenceExpression
    : assignExpression (';' assignExpression)*
    ;

assignExpression
    : consExpression (ASSIGN assignExpression)?
    ;

consExpression
    : logicalOrExpression (COLON consExpression)?
    ;

logicalOrExpression
    : logicalAndExpression (OR logicalAndExpression)*
    ;

logicalAndExpression
    : equalityExpression (AND equalityExpression)*
    ;

equalityExpression
    : relationalExpression ((EQ | NE) relationalExpression)*
    ;

relationalExpression
    : additiveExpression ((LT | LE | GT | GE) additiveExpression)*
    ;

additiveExpression
    : multiplicativeExpression ((PLUS | MINUS) multiplicativeExpression)*
    ;

multiplicativeExpression
    : prefixExpression ((MUL | DIV | MOD) prefixExpression)*
    ;

prefixExpression
    : (MINUS)? postfixExpression
    ;

postfixExpression
    : postfixExpression '(' argumentList? ')'   # callExpr
    | postfixExpression '[' expression ']'      # accessExpr
    | postfixExpression '.' LIDENT              # dotExpr
    | primary									# primaryExpr
    ;

primary
    : literal									# lit
    | sexp										# sexpExpr
    | identifier								# iden
    | '(' scopeExpression ')'          			# parenPrimary
    | arrayLiteral								# arrayLit
    | listLiteral								# litstLit
    | ifExpression								# ifExpr
    | letExpression								# letExpr
    | caseExpression							# caseExpr
    | loopExpression							# loopExpr
    | SKIP_RULE									# skip
    | lambdaExpression							# lambdaExpr
    ;

literal
    : DECIMAL
    | STRING
    | CHAR
    | TRUE
    | FALSE
    ;

identifier
    : LIDENT
    | UIDENT
    ;

arrayLiteral
    : '[' ']'
    | '[' expression (',' expression)* ']'
    ;

listLiteral
    : '{' '}'
    | '{' expression (',' expression)* '}'
    ;

sexp
    : UIDENT '(' expression? (',' expression)* ')'
    | UIDENT
    ;

ifExpression
    : IF expression THEN scopeExpression
      (ELIF expression THEN scopeExpression)*
      (ELSE scopeExpression)? FI
    ;

letExpression
    : LET pattern '=' expression IN expression
    ;

caseExpression
    : CASE expression OF caseBranch+ ESAC
    ;

caseBranch
    : pattern '->' expression ('|' pattern '->' expression)*
    ;

loopExpression
    : whileLoop
    | doWhileLoop
    | forLoop
    ;

whileLoop
    : WHILE sequenceExpression DO scopeExpression OD
    ;

doWhileLoop
    : DO scopeExpression WHILE sequenceExpression OD
    ;

forLoop
    : FOR forInit? ',' expression? ',' forStep? DO scopeExpression OD
    ;

forInit
    : scopeExpression
    ;

forStep
    : assignExpression
    ;

lambdaExpression
    : FUN '(' patternList? ')' '{' scopeExpression '}'
    ;

argumentList
    : expression (',' expression)*
    ;


IMPORT : 'import';
VAR    : 'var';
FUN    : 'fun';
IF     : 'if';
THEN   : 'then';
ELSE   : 'else';
FI     : 'fi';
ELIF   : 'elif';
CASE   : 'case';
OF     : 'of';
ESAC   : 'esac';
LET    : 'let';
IN     : 'in';
WHILE  : 'while';
DO     : 'do';
OD     : 'od';
FOR    : 'for';
SKIP_RULE   : 'skip';
TRUE   : 'true';
FALSE  : 'false';
PUBLIC : 'public';

ASSIGN : ':=';
OR     : '!!';
AND    : '&&';
EQ     : '==';
NE     : '!=';
LT     : '<';
LE     : '<=';
GT     : '>';
GE     : '>=';
PLUS   : '+';
MINUS  : '-';
MUL    : '*';
DIV    : '/';
MOD    : '%';
CONS   : '::';
DOT    : '.';

LPAREN : '(';
RPAREN : ')';
LBRACK : '[';
RBRACK : ']';
LBRACE : '{';
RBRACE : '}';
COMMA  : ',';
SEMICOLON : ';';
COLON  : ':';
ARROW  : '->';
UNDERSCORE : '_';

PATTERN_VAL   : '#val';
PATTERN_FUN   : '#fun';
PATTERN_STR   : '#str';
PATTERN_ARRAY : '#array';
PATTERN_SEXP  : '#sexp';
PATTERN_BOX   : '#box';

UIDENT : [A-Z][a-zA-Z_0-9]*;
LIDENT : [a-z][a-zA-Z_0-9]*;

DECIMAL : [0-9]+; // unsigned integer literal (sign is on parser rules)

STRING : '"' ( ~["] | '""' )* '"'; // double quotes, escaped by doubling

CHAR : '\'' ( ~['] | '\'\'' | '\\n' | '\\t' ) '\'';

WS : [ \t\r\n]+ -> skip;
LINE_COMMENT : '--' ~[\r\n]* -> skip;
BLOCK_COMMENT : '(*' (BLOCK_COMMENT | .)*? '*)' -> skip;
