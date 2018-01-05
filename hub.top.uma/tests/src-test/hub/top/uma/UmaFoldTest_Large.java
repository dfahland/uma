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

import hub.top.petrinet.PetriNet;
import hub.top.uma.view.MineSimplify;
import hub.top.uma.view.MineSimplify.Configuration;

import java.io.IOException;

import org.junit.Test;

public class UmaFoldTest_Large extends hub.top.test.TestCase {
  
  public UmaFoldTest_Large() {
    super("UmaFoldTest_Large");
  }
 
  public static void main(String[] args) {
    setParameters(args);
    junit.textui.TestRunner.run(UmaTest.class);
  }

  @Test
  public void testRegression_fold_maximal_conditions() {
    lastTest = "testRegression_fold_maximal_conditions()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.unfold_refold = true;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
      config.abstract_chains = true;
      config.remove_flower_places = false;
      config.filter_threshold = 0;
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/regression_fold_maximal_a22f0n05.lola", testFileRoot+"/regression_fold_maximal_a22f0n05.log.txt", config);
      simplify.prepareModel();
      simplify.run();
      PetriNet simplifiedNet = simplify.getSimplifiedNet();
      
      assertEquals(lastTest+": number of places", 36, simplifiedNet.getPlaces().size());
      assertEquals(lastTest+": number of transitions", 22, simplifiedNet.getTransitions().size());
      assertEquals(lastTest+": number of arcs", 188, simplifiedNet.getArcs().size());
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
  
  @Test
  public void testRegression_fold_maximal_conditions_filter05() {
    lastTest = "testRegression_fold_maximal_conditions()";
    System.out.println(lastTest);

    try {
      Configuration config = new Configuration();
      config.unfold_refold = true;
      config.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
      config.abstract_chains = true;
      config.remove_flower_places = false;
      config.filter_threshold = 0.05;
      
      MineSimplify simplify = new MineSimplify(testFileRoot+"/regression_fold_maximal_a22f0n05.lola", testFileRoot+"/regression_fold_maximal_a22f0n05.log.txt", config);
      simplify.prepareModel();
      simplify.run();
      PetriNet simplifiedNet = simplify.getSimplifiedNet();
      
      assertEquals(lastTest+": number of places", 38, simplifiedNet.getPlaces().size());
      assertEquals(lastTest+": number of transitions", 22, simplifiedNet.getTransitions().size());
      assertEquals(lastTest+": number of arcs", 186, simplifiedNet.getArcs().size());
      
    } catch (InvalidModelException e) {
      System.err.println("Invalid model: "+e);
      assertTrue(lastTest, false);
    } catch (IOException e) {
      System.err.println("Couldn't read test file: "+e);
      assertTrue(lastTest, false);
    }
  }
}
