//
// Define a grammar called Wavefront
// for reading *.obj files
// 
// see: http://www.antlr.org/wiki/display/ANTLR3/Quick+Starter+on+Parser+Grammars+-+No+Past+Experience+Required
//      http://meri-stuff.blogspot.de/2011/08/antlr-tutorial-hello-word.html
//      http://www.antlr.org/wiki/display/ANTLR4/Actions+and+Attributes
//      

grammar Wavefront;

options {
language = Java;
}

@lexer::members {
    public static final int WHITESPACE = 1;
    public static final int COMMENTS = 2;
}

@header {
package net.wohlfart.antlr4;
}

wavefront:   (SL_COMMENT | element)+ EOF;


element
  : matlib
  | objectName
  | vertex
  | normal
  | material
  | surface
  | face
  ;

matlib: 'mtllib' FILENAME;
  
objectName: 'o' IDENTIFIER;

vertex: 'v' REAL REAL REAL;

normal: 'vn' REAL REAL REAL;

material: 'usemtl' IDENTIFIER;

surface: 's' ('on'|'off');

face: 'f' vertNormIdx vertNormIdx vertNormIdx;

vertNormIdx: NATURAL '//' NATURAL;

// ------- lexer  

SL_COMMENT: '#' .*? '\n' -> channel(COMMENTS) ;

IDENTIFIER: LETTER (ALPHANUMERIC)*;

FILENAME: LETTER (VALID_FILECHAR)*;

REAL: (INTEGER '.' NATURAL);

NATURAL: (DIGIT)+;   

INTEGER: ('-')? NATURAL; 
    
fragment VALID_FILECHAR: (ALPHANUMERIC | SPECIAL_CHAR);

fragment ALPHANUMERIC: (DIGIT | LETTER);

fragment SPECIAL_CHAR: ('.'|'-'|'+');

fragment DIGIT: '0'..'9';

fragment LETTER: ('a'..'z' | 'A'..'Z');

WS  :   [ \t\n\r]+ -> channel(WHITESPACE) ;
  