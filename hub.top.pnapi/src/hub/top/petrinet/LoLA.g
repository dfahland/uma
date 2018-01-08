grammar LoLA;

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
        LoLALexer lex = new LoLALexer(new ANTLRFileStream(args[0]));
       	CommonTokenStream tokens = new CommonTokenStream(lex);

        LoLAParser parser = new LoLAParser(tokens);

        try {
            PetriNet result = parser.net();
            System.out.println(result.toDot());
            
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
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
	KEY_PLACE places
	KEY_MARKING marking_list 
	transitions 
	{$resultNet = net; }
	;

node_name returns [String text]:
  IDENT   { $text = $IDENT.text;  }
| NUMBER  { $text = $NUMBER.text; }
;

/* ----------------- PLACES ----------------- */

places:
	(
		capacity place_list SEMICOLON { /*capacity_ = 0;*/ } 
	)+
;
 
capacity:
  /* empty */
| KEY_SAFE COLON        { /*capacity_ = 1;*/ }
| KEY_SAFE NUMBER COLON { /*capacity_ = $2;*/ }
;

place_list:
    name1=node_name
	  {/*
	    check(places_[nodeName_.str()] == NULL, "node name already used");
	    places_[nodeName_.str()] = &pnapi_lola_yynet.createPlace(nodeName_.str(), 0, capacity_);
	    */
	    net.addPlace_unique($name1.text);
	  }
  (	
  	COMMA name2=node_name
	  {/*
	    check(places_[nodeName_.str()] == NULL, "node name already used");
	    places_[nodeName_.str()] = &pnapi_lola_yynet.createPlace(nodeName_.str(), 0, capacity_);
	    */
	    net.addPlace_unique($name2.text);
	  }
  )*
;

/* ----------------- MARKING ----------------- */

marking_list:
  ( marking ( COMMA marking )* )? SEMICOLON
;

marking: 
  node_name COLON NUMBER 
  {
    net.setTokens($node_name.text, Integer.parseInt($NUMBER.text));
  }
;

/* ----------------- TRANSITION ----------------- */

transitions: 
  ( transition )*             
;

transition: 
  KEY_TRANSITION node_name 
  {
  	currentTransition = net.addTransition_unique($node_name.text);
  	/* 
    check(!pnapi_lola_yynet.containsNode(nodeName_.str()), "node name already used");
    transition_ = &pnapi_lola_yynet.createTransition(nodeName_.str());
    */
  }
  KEY_CONSUME
  {
  	/*
    target_ = reinterpret_cast<Node * *>(&transition_);
    source_ = reinterpret_cast<Node * *>(&place_);
    */
    produceArcs = false; 
  } 
  arcs
  KEY_PRODUCE 
  { 
  	/*
    source_ = reinterpret_cast<Node * *>(&transition_);
    target_ = reinterpret_cast<Node * *>(&place_);
    */
    produceArcs = true;
  }
  arcs
;

arcs:
	( arc (COMMA arc)* )? SEMICOLON
;

arc: 
  node_name (COLON NUMBER)?
  {
	// FIXME: add arc weight if arc is already present
    Place p = net.findPlace($node_name.text);
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
	'\{'
	{
    	$channel=98;
    }
    ( ~('\}') )* '\}' ;


KEY_PLACE : 'PLACE' ;
KEY_SAFE : 'SAFE' ;
KEY_MARKING  : 'MARKING' ;
KEY_TRANSITION  : 'TRANSITION' ;
KEY_CONSUME  : 'CONSUME' ;
KEY_PRODUCE  : 'PRODUCE' ;

COLON : ':' ;
COMMA : ',' ;
SEMICOLON : ';' ;

fragment DIGIT	: '0'..'9' ;
NUMBER	: (DIGIT)+ ;
IDENT : ~( ',' | ';' | ':' | '(' | ')' | '\t' | ' ' | '\n' | '\r' | '\{' | '\}' )+ ;

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ 	{ $channel = HIDDEN; } ;
