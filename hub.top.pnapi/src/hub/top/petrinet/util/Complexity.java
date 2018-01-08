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

package hub.top.petrinet.util;

import java.io.IOException;

import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO;

public class Complexity extends PetriNetIO {
  
  public static float complexitySimple(PetriNet net) {
    int nodeNum = net.getPlaces().size() + net.getTransitions().size();
    int arcNum = net.getArcs().size();
    
    return (float)arcNum/(float)nodeNum;
  }

  @Override
  public void processFile(String fileName) throws IOException {

    PetriNet net = readNetFromFile(fileName);
    if (net == null) return;

    System.out.println(fileName+": "+complexitySimple(net));
  }
  
  public static final int SIMPLE_METRIC = 0;
  
  private int options_metric = SIMPLE_METRIC;
  
  private void parseCommandLine(String args[]) {
    for (int i=0; i<args.length; i++) {
      if ("-i".equals(args[i])) {
        setInputFile(args[++i]);
      }
      if ("-m".equals(args[i])) {
        ++i;
        if ("simple".equals(args[i]))
          options_metric = SIMPLE_METRIC;
      }
    }
  }
  
  public static void main(String args[]) {
    Complexity pn_comp = new Complexity();
    pn_comp.parseCommandLine(args);
    pn_comp.run();
  }
}
