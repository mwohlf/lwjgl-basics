//
// Define part of a grammar called Wavefront
// for reading *.obj files
// 
// see: http://www.antlr.org/wiki/display/ANTLR3/Quick+Starter+on+Parser+Grammars+-+No+Past+Experience+Required
//      http://meri-stuff.blogspot.de/2011/08/antlr-tutorial-hello-word.html
//      http://www.antlr.org/wiki/display/ANTLR4/Actions+and+Attributes
//      http://projects.blender.org/tracker/?func=detail&atid=498&aid=31693&group_id=9
//
// Export/Import Wavefront *.obj file switches Y and Z axes to -Z and Y axes

grammar Wavefront;

options {
language = Java;
}

@lexer::members {
    public static final int WHITESPACE = 1;
    public static final int COMMENTS = 2;
}

// changing the package in the header also effects the outputDirectory in the pom.xml
@header {
package net.wohlfart.gl.antlr4;
}

wavefront:   (SL_COMMENT | element)+ EOF;


element
  : matlib
  | objectName
  | position
  | normal
  | textureCoord
  | material
  | surface
  | face
  ;

matlib: 'mtllib' FILENAME;
  
objectName: 'o' IDENTIFIER;

position: 'v' REAL REAL REAL;

normal: 'vn' REAL REAL REAL;

textureCoord: 'vt' REAL REAL;

material: 'usemtl' IDENTIFIER;

surface: 's' ('on'|'off');

face: 'f' vertIndices vertIndices vertIndices;

vertIndices: NATURAL '/' NATURAL '/' NATURAL;


// ------- lexer, the order matters here...

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
  