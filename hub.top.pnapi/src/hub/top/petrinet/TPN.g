grammar TPN;

options {
     language = Java;
}

@header {
/*
 *   Copyright (C) 2008-2018  Dirk Fahland
 *   Java Petri Net API 
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as
 *   published by the Free Software Foundation, either version 3 of the
 *   License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hub.top.petrinet;
}

@members {

	private PetriNet 	net;
	
	private boolean		produceArcs;	
	private Transition  currentTransition;

    public static void main(String[] args) throws Exception {
        TPNLexer lex = new TPNLexer(new ANTLRFileStream(args[0]));
       	CommonTokenStream tokens = new CommonTokenStream(lex);

        TPNParser parser = new TPNParser(tokens);

        try {
            PetriNet result = parser.net();
            System.out.println(result.toDot());
            
        } catch (RecognitionException e)  {
            System.err.println(e.line+":"+e.charPositionInLine+" found "+TPNParser.tokenNames[e.getUnexpectedType()]+" expected "+e.token.getText());
            e.printStackTrace();
        }
    }
    
    private static String stripQuotes(String s) {
        return s.substring(1,s.length()-1);
    }
}

@rulecatch{
    catch (RecognitionException e){
            throw e;
      }
}

@lexer::header {
    package hub.top.petrinet;
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
 
net returns [PetriNet resultNet]: { net = new PetriNet(); }
	places
	transitions 
	{$resultNet = net; }
	;

node_name returns [String text]:
  STRING_LITERAL   { $text = $STRING_LITERAL.text;  }
| NUMBER  { $text = $NUMBER.text; }
;

/* ----------------- PLACES ----------------- */

places:
	place ( place )*
;

place:
	KEY_PLACE name1=node_name
	{
	  net.addPlace_unique(stripQuotes($name1.text));
	}
	(
		KEY_INIT NUMBER 
		{
			net.setTokens(stripQuotes($name1.text), Integer.parseInt($NUMBER.text));
		}
	)?
	SEMICOLON 
; 

/* ----------------- TRANSITION ----------------- */

transitions: 
  transition ( transition )*             
;

transition: 
  KEY_TRANSITION name1=node_name ( TILDE name2=node_name )?
  {
  	currentTransition = net.addTransition_unique(stripQuotes($name1.text));
  }
  KEY_IN
  {
    produceArcs = false; 
  } 
  arcs
  KEY_OUT 
  { 
    produceArcs = true;
  }
  arcs
  SEMICOLON
;

arcs:
	( arc )*
;

arc: 
  node_name
  {
	// FIXME: add arc weight if arc is already present
    Place p = net.findPlace(stripQuotes($node_name.text));
    if (p == null) System.err.println("ERROR: place "+$node_name.text+" not found.");
    else {
    	if (produceArcs)
    		net.addArc(currentTransition, p);
    	else
    		net.addArc(p, currentTransition);
    }
  }
;
 
 /*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

COMMENT_CONTENTS :
	'--'
	{
    	$channel=98;
    }
    ( ~( '\n'|'\r')  )* '\n' ;


KEY_PLACE : 'place' ;
KEY_INIT : 'init' ;
KEY_TRANSITION  : 'trans' ;
KEY_IN  : 'in' ;
KEY_OUT  : 'out' ;

SEMICOLON : ';' ;
TILDE : '~' ;

fragment DIGIT	: '0'..'9' ;
NUMBER	: (DIGIT)+ ;

STRING_LITERAL
  : '"'
    ( ~('"'|'\n'|'\r') )*
    '"'
   ;


WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ 	{ $channel = HIDDEN; } ;
