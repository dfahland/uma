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

import java.util.ArrayList;
import java.util.HashSet;

import hub.top.petrinet.PetriNet;
import hub.top.petrinet.Place;
import hub.top.petrinet.Transition;

public class Fold {

  public static void fold(PetriNet net) {
    
    ArrayList<Transition> ts = new ArrayList<Transition>(net.getTransitions());
    for (int i=0; i<ts.size(); i++) {
      Transition t = ts.get(i);
      for (int j=i+1; j<ts.size(); j++) {
        Transition t2 = ts.get(j);
        if (!t2.getName().equals(t.getName())) continue;
      }
    }
  }
  
  public static boolean equivalentPostSets(Transition t1, Transition t2) {
    
    boolean maximal1 = true;
    for (Place p : t1.getPostSet())
      if (!p.getOutgoing().isEmpty()) {
        maximal1 = false;
        break;
      }
    boolean maximal2 = true;
    for (Place p : t2.getPostSet())
      if (!p.getOutgoing().isEmpty()) {
        maximal2 = false;
        break;
      }

    if (maximal1 && maximal2) return true;
    
    HashSet<Place> post1 = new HashSet<Place>(t1.getPostSet());
    HashSet<Place> post2 = new HashSet<Place>(t1.getPostSet());
    
    return post1.equals(post2);
  }
  
}
