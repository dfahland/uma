// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g 2018-01-08 10:56:43

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


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LoLAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "KEY_PLACE", "KEY_MARKING", "IDENT", "NUMBER", "SEMICOLON", "KEY_SAFE", "COLON", "COMMA", "KEY_TRANSITION", "KEY_CONSUME", "KEY_PRODUCE", "COMMENT_CONTENTS", "DIGIT", "WHITESPACE"
    };
    public static final int COLON=10;
    public static final int KEY_TRANSITION=12;
    public static final int COMMA=11;
    public static final int KEY_PRODUCE=14;
    public static final int KEY_SAFE=9;
    public static final int NUMBER=7;
    public static final int WHITESPACE=17;
    public static final int IDENT=6;
    public static final int KEY_CONSUME=13;
    public static final int SEMICOLON=8;
    public static final int DIGIT=16;
    public static final int COMMENT_CONTENTS=15;
    public static final int EOF=-1;
    public static final int KEY_PLACE=4;
    public static final int KEY_MARKING=5;

    // delegates
    // delegators


        public LoLAParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LoLAParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return LoLAParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g"; }



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



    // $ANTLR start "net"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:67:1: net returns [PetriNet resultNet] : KEY_PLACE places KEY_MARKING marking_list transitions ;
    public final PetriNet net() throws RecognitionException {
        PetriNet resultNet = null;

        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:67:33: ( KEY_PLACE places KEY_MARKING marking_list transitions )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:67:35: KEY_PLACE places KEY_MARKING marking_list transitions
            {
             net = new PetriNet(); 
            match(input,KEY_PLACE,FOLLOW_KEY_PLACE_in_net64); 
            pushFollow(FOLLOW_places_in_net66);
            places();

            state._fsp--;

            match(input,KEY_MARKING,FOLLOW_KEY_MARKING_in_net69); 
            pushFollow(FOLLOW_marking_list_in_net71);
            marking_list();

            state._fsp--;

            pushFollow(FOLLOW_transitions_in_net75);
            transitions();

            state._fsp--;

            resultNet = net; 

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return resultNet;
    }
    // $ANTLR end "net"


    // $ANTLR start "node_name"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:74:1: node_name returns [String text] : ( IDENT | NUMBER );
    public final String node_name() throws RecognitionException {
        String text = null;

        Token IDENT1=null;
        Token NUMBER2=null;

        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:74:32: ( IDENT | NUMBER )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IDENT) ) {
                alt1=1;
            }
            else if ( (LA1_0==NUMBER) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:75:3: IDENT
                    {
                    IDENT1=(Token)match(input,IDENT,FOLLOW_IDENT_in_node_name94); 
                     text = (IDENT1!=null?IDENT1.getText():null);  

                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:76:3: NUMBER
                    {
                    NUMBER2=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_node_name102); 
                     text = (NUMBER2!=null?NUMBER2.getText():null); 

                    }
                    break;

            }
        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return text;
    }
    // $ANTLR end "node_name"


    // $ANTLR start "places"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:81:1: places : ( capacity place_list SEMICOLON )+ ;
    public final void places() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:81:7: ( ( capacity place_list SEMICOLON )+ )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:82:2: ( capacity place_list SEMICOLON )+
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:82:2: ( capacity place_list SEMICOLON )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=IDENT && LA2_0<=NUMBER)||LA2_0==KEY_SAFE) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:83:3: capacity place_list SEMICOLON
            	    {
            	    pushFollow(FOLLOW_capacity_in_places121);
            	    capacity();

            	    state._fsp--;

            	    pushFollow(FOLLOW_place_list_in_places123);
            	    place_list();

            	    state._fsp--;

            	    match(input,SEMICOLON,FOLLOW_SEMICOLON_in_places125); 
            	     /*capacity_ = 0;*/ 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "places"


    // $ANTLR start "capacity"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:87:1: capacity : ( | KEY_SAFE COLON | KEY_SAFE NUMBER COLON );
    public final void capacity() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:87:9: ( | KEY_SAFE COLON | KEY_SAFE NUMBER COLON )
            int alt3=3;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=IDENT && LA3_0<=NUMBER)) ) {
                alt3=1;
            }
            else if ( (LA3_0==KEY_SAFE) ) {
                int LA3_2 = input.LA(2);

                if ( (LA3_2==COLON) ) {
                    alt3=2;
                }
                else if ( (LA3_2==NUMBER) ) {
                    alt3=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:89:1: 
                    {
                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:89:3: KEY_SAFE COLON
                    {
                    match(input,KEY_SAFE,FOLLOW_KEY_SAFE_in_capacity147); 
                    match(input,COLON,FOLLOW_COLON_in_capacity149); 
                     /*capacity_ = 1;*/ 

                    }
                    break;
                case 3 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:90:3: KEY_SAFE NUMBER COLON
                    {
                    match(input,KEY_SAFE,FOLLOW_KEY_SAFE_in_capacity162); 
                    match(input,NUMBER,FOLLOW_NUMBER_in_capacity164); 
                    match(input,COLON,FOLLOW_COLON_in_capacity166); 
                     /*capacity_ = $2;*/ 

                    }
                    break;

            }
        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "capacity"


    // $ANTLR start "place_list"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:93:1: place_list : name1= node_name ( COMMA name2= node_name )* ;
    public final void place_list() throws RecognitionException {
        String name1 = null;

        String name2 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:93:11: (name1= node_name ( COMMA name2= node_name )* )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:94:5: name1= node_name ( COMMA name2= node_name )*
            {
            pushFollow(FOLLOW_node_name_in_place_list182);
            name1=node_name();

            state._fsp--;

            /*
            	    check(places_[nodeName_.str()] == NULL, "node name already used");
            	    places_[nodeName_.str()] = &pnapi_lola_yynet.createPlace(nodeName_.str(), 0, capacity_);
            	    */
            	    net.addPlace_unique(name1);
            	  
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:101:3: ( COMMA name2= node_name )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:102:4: COMMA name2= node_name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_place_list197); 
            	    pushFollow(FOLLOW_node_name_in_place_list201);
            	    name2=node_name();

            	    state._fsp--;

            	    /*
            	    	    check(places_[nodeName_.str()] == NULL, "node name already used");
            	    	    places_[nodeName_.str()] = &pnapi_lola_yynet.createPlace(nodeName_.str(), 0, capacity_);
            	    	    */
            	    	    net.addPlace_unique(name2);
            	    	  

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "place_list"


    // $ANTLR start "marking_list"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:114:1: marking_list : ( marking ( COMMA marking )* )? SEMICOLON ;
    public final void marking_list() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:114:13: ( ( marking ( COMMA marking )* )? SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:115:3: ( marking ( COMMA marking )* )? SEMICOLON
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:115:3: ( marking ( COMMA marking )* )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0>=IDENT && LA6_0<=NUMBER)) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:115:5: marking ( COMMA marking )*
                    {
                    pushFollow(FOLLOW_marking_in_marking_list226);
                    marking();

                    state._fsp--;

                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:115:13: ( COMMA marking )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==COMMA) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:115:15: COMMA marking
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_marking_list230); 
                    	    pushFollow(FOLLOW_marking_in_marking_list232);
                    	    marking();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_marking_list240); 

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "marking_list"


    // $ANTLR start "marking"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:118:1: marking : node_name COLON NUMBER ;
    public final void marking() throws RecognitionException {
        Token NUMBER4=null;
        String node_name3 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:118:8: ( node_name COLON NUMBER )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:119:3: node_name COLON NUMBER
            {
            pushFollow(FOLLOW_node_name_in_marking251);
            node_name3=node_name();

            state._fsp--;

            match(input,COLON,FOLLOW_COLON_in_marking253); 
            NUMBER4=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_marking255); 

                net.setTokens(node_name3, Integer.parseInt((NUMBER4!=null?NUMBER4.getText():null)));
              

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "marking"


    // $ANTLR start "transitions"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:127:1: transitions : ( transition )* ;
    public final void transitions() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:127:12: ( ( transition )* )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:128:3: ( transition )*
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:128:3: ( transition )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==KEY_TRANSITION) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:128:5: transition
            	    {
            	    pushFollow(FOLLOW_transition_in_transitions276);
            	    transition();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "transitions"


    // $ANTLR start "transition"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:131:1: transition : KEY_TRANSITION node_name KEY_CONSUME arcs KEY_PRODUCE arcs ;
    public final void transition() throws RecognitionException {
        String node_name5 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:131:11: ( KEY_TRANSITION node_name KEY_CONSUME arcs KEY_PRODUCE arcs )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:132:3: KEY_TRANSITION node_name KEY_CONSUME arcs KEY_PRODUCE arcs
            {
            match(input,KEY_TRANSITION,FOLLOW_KEY_TRANSITION_in_transition303); 
            pushFollow(FOLLOW_node_name_in_transition305);
            node_name5=node_name();

            state._fsp--;


              	currentTransition = net.addTransition_unique(node_name5);
              	/* 
                check(!pnapi_lola_yynet.containsNode(nodeName_.str()), "node name already used");
                transition_ = &pnapi_lola_yynet.createTransition(nodeName_.str());
                */
              
            match(input,KEY_CONSUME,FOLLOW_KEY_CONSUME_in_transition314); 

              	/*
                target_ = reinterpret_cast<Node * *>(&transition_);
                source_ = reinterpret_cast<Node * *>(&place_);
                */
                produceArcs = false; 
              
            pushFollow(FOLLOW_arcs_in_transition323);
            arcs();

            state._fsp--;

            match(input,KEY_PRODUCE,FOLLOW_KEY_PRODUCE_in_transition327); 
             
              	/*
                source_ = reinterpret_cast<Node * *>(&transition_);
                target_ = reinterpret_cast<Node * *>(&place_);
                */
                produceArcs = true;
              
            pushFollow(FOLLOW_arcs_in_transition336);
            arcs();

            state._fsp--;


            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "transition"


    // $ANTLR start "arcs"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:160:1: arcs : ( arc ( COMMA arc )* )? SEMICOLON ;
    public final void arcs() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:160:5: ( ( arc ( COMMA arc )* )? SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:161:2: ( arc ( COMMA arc )* )? SEMICOLON
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:161:2: ( arc ( COMMA arc )* )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0>=IDENT && LA9_0<=NUMBER)) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:161:4: arc ( COMMA arc )*
                    {
                    pushFollow(FOLLOW_arc_in_arcs347);
                    arc();

                    state._fsp--;

                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:161:8: ( COMMA arc )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COMMA) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:161:9: COMMA arc
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_arcs350); 
                    	    pushFollow(FOLLOW_arc_in_arcs352);
                    	    arc();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_arcs359); 

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "arcs"


    // $ANTLR start "arc"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:164:1: arc : node_name ( COLON NUMBER )? ;
    public final void arc() throws RecognitionException {
        String node_name6 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:164:4: ( node_name ( COLON NUMBER )? )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:165:3: node_name ( COLON NUMBER )?
            {
            pushFollow(FOLLOW_node_name_in_arc370);
            node_name6=node_name();

            state._fsp--;

            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:165:13: ( COLON NUMBER )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==COLON) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\LoLA.g:165:14: COLON NUMBER
                    {
                    match(input,COLON,FOLLOW_COLON_in_arc373); 
                    match(input,NUMBER,FOLLOW_NUMBER_in_arc375); 

                    }
                    break;

            }


            	// FIXME: add arc weight if arc is already present
                Place p = net.findPlace(node_name6);
                if (p == null) System.err.println("ERROR: place "+node_name6+" not found.");
                else {
                	if (produceArcs)
                		net.addArc(currentTransition, p);
                	else
                		net.addArc(p, currentTransition);
                }
              

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "arc"

    // Delegated rules


 

    public static final BitSet FOLLOW_KEY_PLACE_in_net64 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_places_in_net66 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_KEY_MARKING_in_net69 = new BitSet(new long[]{0x00000000000003C0L});
    public static final BitSet FOLLOW_marking_list_in_net71 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_transitions_in_net75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_node_name94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_node_name102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_capacity_in_places121 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_place_list_in_places123 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_SEMICOLON_in_places125 = new BitSet(new long[]{0x00000000000002C2L});
    public static final BitSet FOLLOW_KEY_SAFE_in_capacity147 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_capacity149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEY_SAFE_in_capacity162 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_capacity164 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_capacity166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_place_list182 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_COMMA_in_place_list197 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_node_name_in_place_list201 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_marking_in_marking_list226 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_COMMA_in_marking_list230 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_marking_in_marking_list232 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_SEMICOLON_in_marking_list240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_marking251 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_marking253 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_marking255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transition_in_transitions276 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_KEY_TRANSITION_in_transition303 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_node_name_in_transition305 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_KEY_CONSUME_in_transition314 = new BitSet(new long[]{0x00000000000003C0L});
    public static final BitSet FOLLOW_arcs_in_transition323 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_KEY_PRODUCE_in_transition327 = new BitSet(new long[]{0x00000000000003C0L});
    public static final BitSet FOLLOW_arcs_in_transition336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arc_in_arcs347 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_COMMA_in_arcs350 = new BitSet(new long[]{0x00000000000002C0L});
    public static final BitSet FOLLOW_arc_in_arcs352 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_SEMICOLON_in_arcs359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_arc370 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COLON_in_arc373 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_arc375 = new BitSet(new long[]{0x0000000000000002L});

}