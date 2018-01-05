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

package hub.top.uma;

import hub.top.petrinet.ISystemModel;
import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO;
import hub.top.petrinet.Place;
import hub.top.petrinet.Transition;
import hub.top.petrinet.unfold.DNodeSys_OccurrenceNet;
import hub.top.uma.DNodeSet.DNodeSetElement;
import hub.top.uma.synthesis.NetSynthesis;
import hub.top.uma.synthesis.TransitiveDependencies;
import hub.top.uma.view.MineSimplify;
import hub.top.uma.view.ViewGeneration2;
import hub.top.uma.view.MineSimplify.Configuration;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

import com.google.gwt.dev.util.collect.HashSet;

public class UmaImpliedTest extends hub.top.test.TestCase {
  
  public UmaImpliedTest() {
    super("UmaImpliedTest");
  }
 
  public static void main(String[] args) {
    setParameters(args);
    junit.textui.TestRunner.run(UmaTest.class);
  }
  
  @Test
  public void testMineImplied_1_all() {
    
    lastTest = "testMineImplied_1_all()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.abstract_chains = false;
      config.unfold_refold = false;
      config.remove_flower_places = false;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
      
      String model = "pn_ex_01_alpha.lola";
      String log = "pn_ex_01_alpha_aligned.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      boolean p1=false,p2=false,p3=false;
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("__P2+ate_abort___-->___P3+complete__P3+pi_abort__")) p1 = true;
        if (p.getName().contains("__P3+complete___-->___P4+schedule__P2+pi_abort__")) p2 = true;
        if (p.getName().contains("__P2+suspend___-->___P2+resume__")) p3 = true;
      }
      
      assertEquals(lastTest+": removed implied places", 3, simplify.result.removedImpliedPlaces.size());
      assertTrue(lastTest+": removed place p1", p1);
      assertTrue(lastTest+": removed place p2", p2);
      assertTrue(lastTest+": removed place p3", p3);
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }

  @Test
  public void testMineImplied_1_connected() {
    lastTest = "testMineImplied_1_connected()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.abstract_chains = false;
      config.unfold_refold = false;
      config.remove_flower_places = false;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_CONNECTED;
      
      String model = "pn_ex_01_alpha.lola";
      String log = "pn_ex_01_alpha_aligned.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      boolean p1=false,p2=false;
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("__P1+complete__P3+ate_abort___-->___P2+schedule__")) p1 = true;
        if (p.getName().contains("__P3+schedule___-->___P3+withdraw__P2+resume__")) p2 = true;
      }
      
      assertEquals(lastTest+": removed implied places", 2, simplify.result.removedImpliedPlaces.size());
      assertTrue(lastTest+": removed place p1", p1);
      assertTrue(lastTest+": removed place p2", p2);
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testMineImplied_2_all() {
    lastTest = "testMineImplied_2_all()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.abstract_chains = false;
      config.unfold_refold = false;
      config.remove_flower_places = false;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
      
      
      String model = "pn_ex_01_ilp.lola";
      String log = "pn_ex_01_ilp.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      assertEquals(lastTest+": removed implied places", 0, simplify.result.removedImpliedPlaces.size());
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testMineImplied_2b_all() {
    lastTest = "testMineImplied_2b_all()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();

      config.unfold_refold = true;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
      config.abstract_chains = false;      
      config.remove_flower_places = false;
      
      String model = "pn_ex_01_ilp.lola";
      String log = "pn_ex_01_ilp.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      boolean implied[] = new boolean[2];
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("P_14")) implied[0] = true;
        if (p.getName().contains("P_4")) implied[1] = true;
      }
      
      assertEquals(lastTest+": removed implied places", 2, simplify.result.removedImpliedPlaces.size());
      for (int i=0; i<implied.length; i++) {
        assertTrue(lastTest+": removed place "+i, implied[i]);
      }
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testMineImplied_2c_visible() {
    lastTest = "testMineImplied_2c_visible()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();

      config.unfold_refold = true;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_VISIBLE;
      config.abstract_chains = false;      
      config.remove_flower_places = false;
      config.filter_threshold = 0;
      
      String model = "pn_ex_01_ilp.lola";
      String log = "pn_ex_01_ilp.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      boolean implied[] = new boolean[4];
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("P_14")) implied[0] = true;
        if (p.getName().contains("P_4")) implied[1] = true;
        if (p.getName().contains("P_7")) implied[2] = true;
        if (p.getName().contains("P_10")) implied[3] = true;
      }
      
      assertEquals(lastTest+": removed implied places", 4, simplify.result.removedImpliedPlaces.size());
      for (int i=0; i<implied.length; i++) {
        assertTrue(lastTest+": removed place "+i, implied[i]);
      }
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testMineImplied_2d_visible_filter() {
    lastTest = "testMineImplied_2c_visible()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();

      config.unfold_refold = true;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_VISIBLE;
      config.abstract_chains = false;      
      config.remove_flower_places = false;
      config.filter_threshold = 0.05;
      
      String model = "pn_ex_01_ilp.lola";
      String log = "pn_ex_01_ilp.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      int implied[] = new int[4];
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("P_5")) implied[0]++;
        if (p.getName().contains("P_4")) implied[1]++;
        if (p.getName().contains("P_7")) implied[2]++;
        if (p.getName().contains("P_10")) implied[3]++;
      }
      
      System.out.println(simplify.result.removedImpliedPlaces);
      
      assertEquals(lastTest+": removed implied places", 5, simplify.result.removedImpliedPlaces.size());
      assertEquals(lastTest+": removed place P_5", 2, implied[0]);
      assertEquals(lastTest+": removed place P_4", 1, implied[1]);
      assertEquals(lastTest+": removed place P_7", 1, implied[2]);
      assertEquals(lastTest+": removed place P_10", 1, implied[3]);
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testMineImplied_2_connected() {
    lastTest = "testMineImplied_2_connected()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.abstract_chains = false;
      config.unfold_refold = false;
      config.remove_flower_places = false;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_CONNECTED;
      
      
      String model = "pn_ex_01_ilp.lola";
      String log = "pn_ex_01_ilp.log.txt";
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/"+model, testFileRoot+"/"+log, config);
      simplify.prepareModel();
      simplify.run();

      boolean implied[] = new boolean[5];
      for (Place p : simplify.result.removedImpliedPlaces) {
        if (p.getName().contains("P_2")) implied[0] = true;
        if (p.getName().contains("P_5")) implied[1] = true;
        if (p.getName().contains("P_8")) implied[2] = true;
        if (p.getName().contains("P_4")) implied[3] = true;
        if (p.getName().contains("P_10")) implied[4] = true;
      }
      
      assertEquals(lastTest+": removed implied places", 5, simplify.result.removedImpliedPlaces.size());
      for (int i=0; i<implied.length; i++) {
        assertTrue(lastTest+": removed place "+i, implied[i]);
      }
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }

}
