/*
 *   Copyright (C) 2008-2018  Dirk Fahland
 *   Uma - Unfolding-based Model Analyzer
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

package hub.top.scenario;

import hub.top.petrinet.LoLALexer;
import hub.top.petrinet.LoLAParser;
import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO;

import java.io.IOException;

public class OcletIO extends PetriNetIO {

  public static PetriNet readNetFromFile(String fileName) throws IOException {
    
    String ext = fileName.substring(fileName.lastIndexOf('.')+1);
    
    if ("oclets".equals(ext)) {
      
      LoLALexer lex = new LoLALexer(new org.antlr.runtime.ANTLRFileStream(fileName));
      org.antlr.runtime.CommonTokenStream tokens = new org.antlr.runtime.CommonTokenStream(lex);
  
      LoLAParser parser = new LoLAParser(tokens);
  
      try {
        PetriNet result = parser.net();
        return result;
          
      } catch (org.antlr.runtime.RecognitionException e)  {
        if (e instanceof org.antlr.runtime.EarlyExitException) {
          org.antlr.runtime.EarlyExitException e2 = (org.antlr.runtime.EarlyExitException)e;
          System.err.println("failed parsing "+fileName);
          System.err.println("found unexpected '"+e2.token.getText()+"' in "
              +"line "+e2.line+ " at column "+e2.charPositionInLine);
          System.exit(1);
        } else if (e instanceof org.antlr.runtime.MismatchedTokenException) {
          org.antlr.runtime.MismatchedTokenException e2 = (org.antlr.runtime.MismatchedTokenException)e;
          System.err.println("found "+e2.token.getText()+" expected "+LoLAParser.tokenNames[e2.expecting]);
          System.err.println(" in line "+e2.line+" at column "+e2.charPositionInLine);
        } else {
          e.printStackTrace();
        }
      }
    }
    
    return PetriNetIO.readNetFromFile(fileName);
  }
}

