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

package hub.top.uma.view;

import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO_Out;
import hub.top.petrinet.Place;
import hub.top.petrinet.Transition;
import hub.top.uma.InvalidModelException;
import hub.top.uma.synthesis.ImplicitPlaces;
import hub.top.uma.view.MineSimplify.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GenerateSmokingGun {

  public static PetriNet generatePetriNet(int placeNum, int transitionNum) {
    
      PetriNet net = new PetriNet();
      
      Random r = new Random();
      
      int tokens = r.nextInt(placeNum+3);
      
      Place[] places = new Place[placeNum];
      for (int p_num = 0; p_num < placeNum; p_num++) {
        Place p = net.addPlace("p_"+p_num);
        if (p_num < tokens) p.setTokens(1);
        places[p_num] = p;
      }
      
      Transition[] transitions = new Transition[transitionNum];
      for (int t_num = 0; t_num < transitionNum; t_num++) {
        transitions[t_num] = net.addTransition("t_"+t_num);
      }
      
      int arcNum = r.nextInt(3*transitionNum);
      for (int a_num = 0; a_num < arcNum; a_num++) {
        boolean added = false;
        while (!added) {
          int p_num = r.nextInt(placeNum);
          int t_num = r.nextInt(transitionNum);
          if (r.nextBoolean()) {
            // from p to t
            if (!places[p_num].getPostSet().contains(transitions[t_num])) {
              net.addArc(places[p_num], transitions[t_num]);
              added = true;
            }
          } else {
            // from t to p
            if (!transitions[t_num].getPostSet().contains(places[p_num])) {
              net.addArc(transitions[t_num], places[p_num]);
              added = true;
            }
          }
        }
      }
      
      for (Transition t : net.getTransitions()) {
        if (t.getPreSet().isEmpty()) {
          int p = r.nextInt(placeNum);
          net.addArc(places[p], t);
        }
        if (t.getPostSet().isEmpty()) {
          int p = r.nextInt(placeNum);
          net.addArc(t, places[p]);
        }
      }
      
      for (Place p : net.getPlaces()) {
        if (p.getPreSet().isEmpty()) p.setTokens(1);
        if (p.getPostSet().isEmpty()) p.setTokens(0);
      }
    
      System.out.println("generated :"+net.getInfo(false));
      
    return net;
  }
  
  /**
   * Generate a trace from the constructed branching process that can be
   * executed in the system.
   * 
   * @param upperBound   maximum length of the trace
   * @return a trace of the system
   */
  public static List<String> generateRandomTrace(PetriNet net, int upperBound) {
    LinkedList<String> trace = new LinkedList<String>();
    LinkedList<Place> marking = new LinkedList<Place>();
    for (Place p : net.getPlaces()) {
      for (int i=0; i<p.getTokens(); i++) marking.add(p);
    }
    Random r = new Random();
    
    
    Transition lastTransition = null;
    for (int i = 0; i<upperBound; i++) {
      List<Transition> enabled = new LinkedList<Transition>();
      for (Transition t : net.getTransitions()) {
        if (marking.containsAll(t.getPreSet())) enabled.add(t);
      }
      
      if (lastTransition != null)
        enabled.remove(lastTransition);
      
      if (enabled.isEmpty()) {
        break;
      }
            
      Transition[] toChoose = new Transition[enabled.size()];
      enabled.toArray(toChoose);
      int chosenEvent = r.nextInt(toChoose.length);
      
      // get one enabled event and fire it
      Transition fireEvent = toChoose[chosenEvent];
      for (Place p : fireEvent.getPreSet()) {
        marking.removeFirstOccurrence(p);
      }
      for (Place p : fireEvent.getPostSet()) {
        marking.addLast(p);
      }
      trace.addLast(fireEvent.getName());
      lastTransition = fireEvent;
    }
    return trace;
  }
  
  public static void findSmokingGun() {
    
    Random r = new Random();
    
    int t_num = 6;
    int p_num = t_num * 3;
    
    LinkedList<String[]> smokingTrace = null;
    PetriNet smokingGun = null;
    boolean found = false;
    while (!found)
    {
      try {
        PetriNet net = generatePetriNet(p_num, t_num);        
        net.makeNormalNet(); // fix any empty pre- or post-sets
        
        LinkedList<String[]> traces = new LinkedList<String[]>();
        int traceNum = 6;
        for (int i=0; i<traceNum; i++) {
          List<String> t = generateRandomTrace(net, t_num*2);
          traces.add(t.toArray(new String[t.size()]));
        }
        
        Configuration config = new Configuration();
        config.unfold_refold = true;
        config.remove_implied = Configuration.REMOVE_IMPLIED_OFF;
        config.abstract_chains = false;
        config.remove_flower_places = false;
        config.filter_threshold = 0;
        
        for (String[] tr : traces) {
          System.out.println(ViewGeneration2.toString(tr));
        }
        
        MineSimplify sim = new MineSimplify(net, traces, config);
        sim.run();
        
        PetriNet compare = sim.getSimplifiedNet();

        System.out.println("refolded "+compare.getInfo(false));
        
        PetriNet net_0 = new PetriNet(compare);
        PetriNet net_1 = new PetriNet(compare);
        PetriNet net_2 = new PetriNet(compare);
        
        int removed_classic = ImplicitPlaces.findImplicitPlaces(net_0);
        
        Configuration config1 = new Configuration();
        config1.unfold_refold = true;
        config1.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_ALL;
        config1.abstract_chains = false;
        config1.remove_flower_places = false;
        config1.filter_threshold = 0;
        
        MineSimplify sim1 = new MineSimplify(net_1, traces, config1);
        sim1.run();
        int removed_1 = sim1.result.removedImpliedPlaces.size();
        System.out.println(sim1.result.removedImpliedPlaces);
        
        Configuration config2 = new Configuration();
        config2.unfold_refold = true;
        config2.remove_implied = Configuration.REMOVE_IMPLIED_PRESERVE_VISIBLE;
        config2.abstract_chains = false;
        config2.remove_flower_places = false;
        config2.filter_threshold = 0;
        
        MineSimplify sim2 = new MineSimplify(net_2, traces, config2);
        sim2.run();
        int removed_2 = sim2.result.removedImpliedPlaces.size();
        System.out.println(sim2.result.removedImpliedPlaces);
        
        System.out.println(sim2.getSimplifiedNet().getInfo(false));
        
        //if (removed_classic == 0 && removed_new > 0)
        System.out.println("comparison: "+removed_classic+" "+removed_1+" "+removed_2);
        if (removed_1 == 0 && removed_2 > removed_1) {
          System.out.println("+++++++++++++++++++ FOUND");
          found = true;
          smokingGun = compare;
          smokingTrace = traces;
        }
      } catch (InvalidModelException e) {
        e.printStackTrace();
      }
    }
    
    String net = PetriNetIO_Out.toLoLA(smokingGun);
    System.out.println(net);
    for (String[] trace : smokingTrace) {
      System.out.println(ViewGeneration2.toString(trace));
    }

  }
  
  public static void main(String args[]) {
    findSmokingGun();
  }
  
}
