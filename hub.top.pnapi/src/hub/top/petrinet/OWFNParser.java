// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g 2018-01-08 10:56:44

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

public class OWFNParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "KEY_PLACE", "KEY_INTERNAL", "KEY_INPUT", "KEY_OUTPUT", "KEY_INITIALMARKING", "KEY_FINALCONDITION", "IDENT", "NUMBER", "KEY_ROLES", "COMMA", "SEMICOLON", "KEY_SAFE", "COLON", "KEY_TRUE", "KEY_TRANSITION", "KEY_CONSUME", "KEY_PRODUCE", "COMMENT_CONTENTS", "KEY_AND", "KEY_EMPTY", "DIGIT", "WHITESPACE"
    };
    public static final int KEY_TRUE=17;
    public static final int KEY_INPUT=6;
    public static final int KEY_EMPTY=23;
    public static final int KEY_OUTPUT=7;
    public static final int NUMBER=11;
    public static final int WHITESPACE=25;
    public static final int SEMICOLON=14;
    public static final int COMMENT_CONTENTS=21;
    public static final int EOF=-1;
    public static final int COLON=16;
    public static final int KEY_ROLES=12;
    public static final int KEY_TRANSITION=18;
    public static final int COMMA=13;
    public static final int KEY_PRODUCE=20;
    public static final int KEY_SAFE=15;
    public static final int KEY_AND=22;
    public static final int KEY_INITIALMARKING=8;
    public static final int KEY_FINALCONDITION=9;
    public static final int IDENT=10;
    public static final int KEY_CONSUME=19;
    public static final int DIGIT=24;
    public static final int KEY_PLACE=4;
    public static final int KEY_INTERNAL=5;

    // delegates
    // delegators


        public OWFNParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public OWFNParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return OWFNParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g"; }



    	private PetriNet 	net;
    	
    	private boolean		produceArcs;	
    	private Transition  currentTransition = null;

        public static void main(String[] args) throws Exception {
            OWFNLexer lex = new OWFNLexer(new ANTLRFileStream(args[0]));
           	CommonTokenStream tokens = new CommonTokenStream(lex);

            OWFNParser parser = new OWFNParser(tokens);

            try {
                PetriNet result = parser.net();
                System.out.println(result.toDot());
                
            } catch (RecognitionException e)  {
                e.printStackTrace();
            }
        }



    // $ANTLR start "net"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:67:1: net returns [PetriNet resultNet] : KEY_PLACE ( roles )? KEY_INTERNAL places KEY_INPUT places KEY_OUTPUT places KEY_INITIALMARKING marking_list KEY_FINALCONDITION final_condition transitions ;
    public final PetriNet net() throws RecognitionException {
        PetriNet resultNet = null;

        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:67:33: ( KEY_PLACE ( roles )? KEY_INTERNAL places KEY_INPUT places KEY_OUTPUT places KEY_INITIALMARKING marking_list KEY_FINALCONDITION final_condition transitions )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:67:35: KEY_PLACE ( roles )? KEY_INTERNAL places KEY_INPUT places KEY_OUTPUT places KEY_INITIALMARKING marking_list KEY_FINALCONDITION final_condition transitions
            {
             net = new PetriNet(); 
            match(input,KEY_PLACE,FOLLOW_KEY_PLACE_in_net64); 
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:69:3: ( roles )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==KEY_ROLES) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:69:4: roles
                    {
                    pushFollow(FOLLOW_roles_in_net69);
                    roles();

                    state._fsp--;


                    }
                    break;

            }

            match(input,KEY_INTERNAL,FOLLOW_KEY_INTERNAL_in_net74); 
            pushFollow(FOLLOW_places_in_net79);
            places();

            state._fsp--;

            match(input,KEY_INPUT,FOLLOW_KEY_INPUT_in_net82); 
            pushFollow(FOLLOW_places_in_net87);
            places();

            state._fsp--;

            match(input,KEY_OUTPUT,FOLLOW_KEY_OUTPUT_in_net90); 
            pushFollow(FOLLOW_places_in_net95);
            places();

            state._fsp--;

            match(input,KEY_INITIALMARKING,FOLLOW_KEY_INITIALMARKING_in_net99); 
            pushFollow(FOLLOW_marking_list_in_net101);
            marking_list();

            state._fsp--;

            match(input,KEY_FINALCONDITION,FOLLOW_KEY_FINALCONDITION_in_net104); 
            pushFollow(FOLLOW_final_condition_in_net106);
            final_condition();

            state._fsp--;

            pushFollow(FOLLOW_transitions_in_net110);
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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:83:1: node_name returns [String text] : ( IDENT | NUMBER );
    public final String node_name() throws RecognitionException {
        String text = null;

        Token IDENT1=null;
        Token NUMBER2=null;

        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:83:32: ( IDENT | NUMBER )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IDENT) ) {
                alt2=1;
            }
            else if ( (LA2_0==NUMBER) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:84:3: IDENT
                    {
                    IDENT1=(Token)match(input,IDENT,FOLLOW_IDENT_in_node_name129); 
                     text = (IDENT1!=null?IDENT1.getText():null);  

                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:85:3: NUMBER
                    {
                    NUMBER2=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_node_name137); 
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


    // $ANTLR start "roles"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:90:1: roles : KEY_ROLES name1= node_name ( COMMA name2= node_name )* SEMICOLON ;
    public final void roles() throws RecognitionException {
        String name1 = null;

        String name2 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:90:6: ( KEY_ROLES name1= node_name ( COMMA name2= node_name )* SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:91:2: KEY_ROLES name1= node_name ( COMMA name2= node_name )* SEMICOLON
            {
            match(input,KEY_ROLES,FOLLOW_KEY_ROLES_in_roles152); 
            pushFollow(FOLLOW_node_name_in_roles159);
            name1=node_name();

            state._fsp--;


                      if (currentTransition != null)
                        currentTransition.addRole(name1);
                      else
                        net.addRole(name1);
                    
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:99:2: ( COMMA name2= node_name )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==COMMA) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:100:4: COMMA name2= node_name
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_roles177); 
            	    pushFollow(FOLLOW_node_name_in_roles181);
            	    name2=node_name();

            	    state._fsp--;


            	              if (currentTransition != null)
            	                currentTransition.addRole(name2);
            	              else
            	                net.addRole(name2);
            	            

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_roles198); 

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "roles"


    // $ANTLR start "places"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:113:1: places : ( capacity place_list SEMICOLON )+ ;
    public final void places() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:113:7: ( ( capacity place_list SEMICOLON )+ )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:114:2: ( capacity place_list SEMICOLON )+
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:114:2: ( capacity place_list SEMICOLON )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=IDENT && LA4_0<=NUMBER)||(LA4_0>=SEMICOLON && LA4_0<=KEY_SAFE)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:115:3: capacity place_list SEMICOLON
            	    {
            	    pushFollow(FOLLOW_capacity_in_places214);
            	    capacity();

            	    state._fsp--;

            	    pushFollow(FOLLOW_place_list_in_places216);
            	    place_list();

            	    state._fsp--;

            	    match(input,SEMICOLON,FOLLOW_SEMICOLON_in_places218); 
            	     /*capacity_ = 0;*/ 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:119:1: capacity : ( | KEY_SAFE COLON | KEY_SAFE NUMBER COLON );
    public final void capacity() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:119:9: ( | KEY_SAFE COLON | KEY_SAFE NUMBER COLON )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0>=IDENT && LA5_0<=NUMBER)||LA5_0==SEMICOLON) ) {
                alt5=1;
            }
            else if ( (LA5_0==KEY_SAFE) ) {
                int LA5_2 = input.LA(2);

                if ( (LA5_2==COLON) ) {
                    alt5=2;
                }
                else if ( (LA5_2==NUMBER) ) {
                    alt5=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:121:1: 
                    {
                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:121:3: KEY_SAFE COLON
                    {
                    match(input,KEY_SAFE,FOLLOW_KEY_SAFE_in_capacity240); 
                    match(input,COLON,FOLLOW_COLON_in_capacity242); 
                     /*capacity_ = 1;*/ 

                    }
                    break;
                case 3 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:122:3: KEY_SAFE NUMBER COLON
                    {
                    match(input,KEY_SAFE,FOLLOW_KEY_SAFE_in_capacity255); 
                    match(input,NUMBER,FOLLOW_NUMBER_in_capacity257); 
                    match(input,COLON,FOLLOW_COLON_in_capacity259); 
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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:125:1: place_list : (name1= node_name ( COMMA name2= node_name )* )? ;
    public final void place_list() throws RecognitionException {
        String name1 = null;

        String name2 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:125:11: ( (name1= node_name ( COMMA name2= node_name )* )? )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:126:2: (name1= node_name ( COMMA name2= node_name )* )?
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:126:2: (name1= node_name ( COMMA name2= node_name )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0>=IDENT && LA7_0<=NUMBER)) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:127:5: name1= node_name ( COMMA name2= node_name )*
                    {
                    pushFollow(FOLLOW_node_name_in_place_list278);
                    name1=node_name();

                    state._fsp--;

                    /*
                    	    check(places_[nodeName_.str()] == NULL, "node name already used");
                    	    places_[nodeName_.str()] = &pnapi_lola_yynet.createPlace(nodeName_.str(), 0, capacity_);
                    	    */
                    	    net.addPlace_unique(name1);
                    	  
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:134:3: ( COMMA name2= node_name )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:135:4: COMMA name2= node_name
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_place_list293); 
                    	    pushFollow(FOLLOW_node_name_in_place_list297);
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
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

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
    // $ANTLR end "place_list"


    // $ANTLR start "marking_list"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:148:1: marking_list : ( marking ( COMMA marking )* )? SEMICOLON ;
    public final void marking_list() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:148:13: ( ( marking ( COMMA marking )* )? SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:149:3: ( marking ( COMMA marking )* )? SEMICOLON
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:149:3: ( marking ( COMMA marking )* )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0>=IDENT && LA9_0<=NUMBER)) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:149:5: marking ( COMMA marking )*
                    {
                    pushFollow(FOLLOW_marking_in_marking_list326);
                    marking();

                    state._fsp--;

                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:149:13: ( COMMA marking )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COMMA) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:149:15: COMMA marking
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_marking_list330); 
                    	    pushFollow(FOLLOW_marking_in_marking_list332);
                    	    marking();

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

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_marking_list340); 

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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:152:1: marking : ( node_name | node_name COLON NUMBER );
    public final void marking() throws RecognitionException {
        Token NUMBER5=null;
        String node_name3 = null;

        String node_name4 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:152:8: ( node_name | node_name COLON NUMBER )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==IDENT) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==COLON) ) {
                    alt10=2;
                }
                else if ( ((LA10_1>=COMMA && LA10_1<=SEMICOLON)) ) {
                    alt10=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA10_0==NUMBER) ) {
                int LA10_2 = input.LA(2);

                if ( (LA10_2==COLON) ) {
                    alt10=2;
                }
                else if ( ((LA10_2>=COMMA && LA10_2<=SEMICOLON)) ) {
                    alt10=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:153:3: node_name
                    {
                    pushFollow(FOLLOW_node_name_in_marking350);
                    node_name3=node_name();

                    state._fsp--;


                        net.setTokens(node_name3, 1);
                      

                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:158:3: node_name COLON NUMBER
                    {
                    pushFollow(FOLLOW_node_name_in_marking363);
                    node_name4=node_name();

                    state._fsp--;

                    match(input,COLON,FOLLOW_COLON_in_marking365); 
                    NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_marking367); 

                        net.setTokens(node_name4, Integer.parseInt((NUMBER5!=null?NUMBER5.getText():null)));
                      

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
    // $ANTLR end "marking"


    // $ANTLR start "final_condition"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:166:1: final_condition : KEY_TRUE SEMICOLON ;
    public final void final_condition() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:166:16: ( KEY_TRUE SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:167:2: KEY_TRUE SEMICOLON
            {
            match(input,KEY_TRUE,FOLLOW_KEY_TRUE_in_final_condition384); 
            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_final_condition386); 

            }

        }

            catch (RecognitionException e){
                    throw e;
              }
        finally {
        }
        return ;
    }
    // $ANTLR end "final_condition"


    // $ANTLR start "transitions"
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:172:1: transitions : ( transition )* ;
    public final void transitions() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:172:12: ( ( transition )* )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:173:3: ( transition )*
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:173:3: ( transition )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==KEY_TRANSITION) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:173:5: transition
            	    {
            	    pushFollow(FOLLOW_transition_in_transitions402);
            	    transition();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:176:1: transition : KEY_TRANSITION node_name ( roles )? KEY_CONSUME arcs KEY_PRODUCE arcs ;
    public final void transition() throws RecognitionException {
        String node_name6 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:176:11: ( KEY_TRANSITION node_name ( roles )? KEY_CONSUME arcs KEY_PRODUCE arcs )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:177:3: KEY_TRANSITION node_name ( roles )? KEY_CONSUME arcs KEY_PRODUCE arcs
            {
            match(input,KEY_TRANSITION,FOLLOW_KEY_TRANSITION_in_transition429); 
            pushFollow(FOLLOW_node_name_in_transition431);
            node_name6=node_name();

            state._fsp--;


              	currentTransition = net.addTransition_unique(node_name6);
              	/* 
                check(!pnapi_lola_yynet.containsNode(nodeName_.str()), "node name already used");
                transition_ = &pnapi_lola_yynet.createTransition(nodeName_.str());
                */
              
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:185:3: ( roles )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==KEY_ROLES) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:185:4: roles
                    {
                    pushFollow(FOLLOW_roles_in_transition441);
                    roles();

                    state._fsp--;


                    }
                    break;

            }

            match(input,KEY_CONSUME,FOLLOW_KEY_CONSUME_in_transition447); 

              	/*
                target_ = reinterpret_cast<Node * *>(&transition_);
                source_ = reinterpret_cast<Node * *>(&place_);
                */
                produceArcs = false; 
              
            pushFollow(FOLLOW_arcs_in_transition456);
            arcs();

            state._fsp--;

            match(input,KEY_PRODUCE,FOLLOW_KEY_PRODUCE_in_transition460); 
             
              	/*
                source_ = reinterpret_cast<Node * *>(&transition_);
                target_ = reinterpret_cast<Node * *>(&place_);
                */
                produceArcs = true;
              
            pushFollow(FOLLOW_arcs_in_transition469);
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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:206:1: arcs : ( arc ( COMMA arc )* )? SEMICOLON ;
    public final void arcs() throws RecognitionException {
        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:206:5: ( ( arc ( COMMA arc )* )? SEMICOLON )
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:207:2: ( arc ( COMMA arc )* )? SEMICOLON
            {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:207:2: ( arc ( COMMA arc )* )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0>=IDENT && LA14_0<=NUMBER)) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:207:4: arc ( COMMA arc )*
                    {
                    pushFollow(FOLLOW_arc_in_arcs480);
                    arc();

                    state._fsp--;

                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:207:8: ( COMMA arc )*
                    loop13:
                    do {
                        int alt13=2;
                        int LA13_0 = input.LA(1);

                        if ( (LA13_0==COMMA) ) {
                            alt13=1;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:207:9: COMMA arc
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_arcs483); 
                    	    pushFollow(FOLLOW_arc_in_arcs485);
                    	    arc();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop13;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_arcs492); 

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
    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:210:1: arc : ( node_name | node_name COLON NUMBER );
    public final void arc() throws RecognitionException {
        String node_name7 = null;

        String node_name8 = null;


        try {
            // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:210:4: ( node_name | node_name COLON NUMBER )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==IDENT) ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==COLON) ) {
                    alt15=2;
                }
                else if ( ((LA15_1>=COMMA && LA15_1<=SEMICOLON)) ) {
                    alt15=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA15_0==NUMBER) ) {
                int LA15_2 = input.LA(2);

                if ( (LA15_2==COLON) ) {
                    alt15=2;
                }
                else if ( ((LA15_2>=COMMA && LA15_2<=SEMICOLON)) ) {
                    alt15=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:211:4: node_name
                    {
                    pushFollow(FOLLOW_node_name_in_arc503);
                    node_name7=node_name();

                    state._fsp--;


                    		// FIXME: add arc weight if arc is already present
                    	    Place p = net.findPlace(node_name7);
                    	    if (p == null) System.err.println("ERROR: place "+node_name7+" not found.");
                    	    else {
                    	    	if (produceArcs)
                    	    		net.addArc(currentTransition, p);
                    	    	else
                    	    		net.addArc(p, currentTransition);
                    	    }
                    	  

                    }
                    break;
                case 2 :
                    // D:\\git\\uma\\hub.top.pnapi\\src\\hub\\top\\petrinet\\OWFN.g:224:4: node_name COLON NUMBER
                    {
                    pushFollow(FOLLOW_node_name_in_arc517);
                    node_name8=node_name();

                    state._fsp--;

                    match(input,COLON,FOLLOW_COLON_in_arc519); 
                    match(input,NUMBER,FOLLOW_NUMBER_in_arc521); 

                    		// FIXME: add arc weight if arc is already present
                    	    Place p = net.findPlace(node_name8);
                    	    if (p == null) System.err.println("ERROR: place "+node_name8+" not found.");
                    	    else {
                    	    	if (produceArcs)
                    	    		net.addArc(currentTransition, p);
                    	    	else
                    	    		net.addArc(p, currentTransition);
                    	    }
                    	  

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
    // $ANTLR end "arc"

    // Delegated rules


 

    public static final BitSet FOLLOW_KEY_PLACE_in_net64 = new BitSet(new long[]{0x0000000000001020L});
    public static final BitSet FOLLOW_roles_in_net69 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_KEY_INTERNAL_in_net74 = new BitSet(new long[]{0x000000000000CC00L});
    public static final BitSet FOLLOW_places_in_net79 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_KEY_INPUT_in_net82 = new BitSet(new long[]{0x000000000000CC00L});
    public static final BitSet FOLLOW_places_in_net87 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_KEY_OUTPUT_in_net90 = new BitSet(new long[]{0x000000000000CC00L});
    public static final BitSet FOLLOW_places_in_net95 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_KEY_INITIALMARKING_in_net99 = new BitSet(new long[]{0x0000000000004C00L});
    public static final BitSet FOLLOW_marking_list_in_net101 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_KEY_FINALCONDITION_in_net104 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_final_condition_in_net106 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_transitions_in_net110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_node_name129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_node_name137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEY_ROLES_in_roles152 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_node_name_in_roles159 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_COMMA_in_roles177 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_node_name_in_roles181 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_SEMICOLON_in_roles198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_capacity_in_places214 = new BitSet(new long[]{0x0000000000004C00L});
    public static final BitSet FOLLOW_place_list_in_places216 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_SEMICOLON_in_places218 = new BitSet(new long[]{0x000000000000CC02L});
    public static final BitSet FOLLOW_KEY_SAFE_in_capacity240 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_COLON_in_capacity242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEY_SAFE_in_capacity255 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_NUMBER_in_capacity257 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_COLON_in_capacity259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_place_list278 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_COMMA_in_place_list293 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_node_name_in_place_list297 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_marking_in_marking_list326 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_COMMA_in_marking_list330 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_marking_in_marking_list332 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_SEMICOLON_in_marking_list340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_marking350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_marking363 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_COLON_in_marking365 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_NUMBER_in_marking367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEY_TRUE_in_final_condition384 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_SEMICOLON_in_final_condition386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transition_in_transitions402 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_KEY_TRANSITION_in_transition429 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_node_name_in_transition431 = new BitSet(new long[]{0x0000000000081000L});
    public static final BitSet FOLLOW_roles_in_transition441 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_KEY_CONSUME_in_transition447 = new BitSet(new long[]{0x0000000000004C00L});
    public static final BitSet FOLLOW_arcs_in_transition456 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_KEY_PRODUCE_in_transition460 = new BitSet(new long[]{0x0000000000004C00L});
    public static final BitSet FOLLOW_arcs_in_transition469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arc_in_arcs480 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_COMMA_in_arcs483 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_arc_in_arcs485 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_SEMICOLON_in_arcs492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_arc503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_name_in_arc517 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_COLON_in_arc519 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_NUMBER_in_arc521 = new BitSet(new long[]{0x0000000000000002L});

}