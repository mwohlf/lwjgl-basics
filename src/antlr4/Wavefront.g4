//
// Define a grammar called Wavefront
// for reading *.obj files
// 
// see: http://www.antlr.org/wiki/display/ANTLR3/Quick+Starter+on+Parser+Grammars+-+No+Past+Experience+Required
//

grammar Wavefront;
options {
     language = Java;
}
@header {
     package net.wohlfart.antlr4;
}

r  : 'hello' ID ;         // match keyword hello followed by an identifier
ID : [a-z]+ ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
