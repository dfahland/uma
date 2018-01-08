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

import hub.top.petrinet.PetriNet;
import hub.top.uma.InvalidModelException;

import java.io.IOException;

public class OcletTest extends hub.top.test.TestCase {
  
  public OcletTest() {
    super("OcletTest");
  }
  
  public static void main(String[] args) {
    setParameters(args);
    junit.textui.TestRunner.run(OcletTest.class);
  }
  
  public void testOcletPrefix_fail () {
    
    lastTest = "Detect wrong prefix in oclet";

    try {
      PetriNet net = OcletIO.readNetFromFile(testFileRoot+"/oclet_syntax_1.oclets");
      OcletSpecification os = new OcletSpecification(net);
      Oclet o = os.getOclets().getFirst();
    
      //assertTrue(o.isCausalNet() == true && o.historyIsPrefix() == false);
      int error = 0;
      try {
        o.isValidOclet();
      } catch (InvalidModelException e) {
        error = e.getReason();
      }
      
      assertEquals(error, InvalidModelException.OCLET_HISTORY_NOT_PREFIX );
    
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(false);
    }
  }
  
  public void testOcletCycle_fail () {
    
    lastTest = "Detect cycle in oclet";
    
    try {
      PetriNet net = OcletIO.readNetFromFile(testFileRoot+"/oclet_syntax_2.oclets");
      OcletSpecification os = new OcletSpecification(net);
      Oclet o = os.getOclets().getFirst();
    
      int error = 0;
      try {
        o.isValidOclet();
      } catch (InvalidModelException e) {
        error = e.getReason();
      }
      
      assertEquals(error, InvalidModelException.OCLET_NO_CAUSALNET );
    
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(false);
    }
      
  }
  
  public void testOcletAll_yes () {
    
    lastTest = "Check valid oclet";
    
    try {
      PetriNet net = OcletIO.readNetFromFile(testFileRoot+"/oclet_syntax_3.oclets");
      OcletSpecification os = new OcletSpecification(net);
      Oclet o = os.getOclets().getFirst();
    
      int error = 0;
      try {
        o.isValidOclet();
      } catch (InvalidModelException e) {
        error = e.getReason();
      }
      
      assertEquals(error, 0 );
    
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(false);
    }
  }  
}
