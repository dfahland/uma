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

package hub.top.uma.synthesis;

import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO;
import viptool.algorithm.postprocessing.pnetiplp.ExtendedExhuminator;

public class ImplicitPlaces {

  public static int findImplicitPlaces(PetriNet net) {
    try {
      System.loadLibrary("lpsolve55");
      System.loadLibrary("lpsolve55j");
    } catch (Exception e) {
      System.err.println("Unable to load required libraries for ILP solver.");
      System.err.println("Exception thrown: "+e);
      //                  0123456789012345678901234567890123456789012345678901234567890123456789
      System.err.println("Please obtain a copy of 'lpsolve' and make the libraries available");
      System.err.println("to Uma on the java library path:");
      System.err.println("  java <params> -Djava.library.path=path/to/lpsolve/libs");
      System.err.println("Uma provides other settings for removing implicit places that do not");
      System.err.println("require an ILP solver.");
      return 0;
    }
    
    System.out.println(net.getInfo(false));
    int reduced = ExtendedExhuminator.reduce(net);
    System.out.println(net.getInfo(false));
    return reduced;
  }
  
  public static void main(String args[]) throws Exception {
    PetriNet net = PetriNetIO.readNetFromFile(args[0]);
    
    findImplicitPlaces(net);
  }
}
