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

import hub.top.uma.DNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashSet;

/**
 * Implementation of {@link IEquivalentNodesRefine} to partition an equivalence class
 * of nodes into finer equivalence classes. Two nodes are equivalent iff they have
 * the same label.
 * 
 * @author dfahland
 */
public class EquivalenceRefineLabel implements IEquivalentNodesRefine {

  public static final EquivalenceRefineLabel instance = new EquivalenceRefineLabel();
  
  /*
   * (non-Javadoc)
   * @see hub.top.uma.synthesis.IEquivalentConditions#splitIntoEquivalenceClasses(java.util.Collection)
   */
  public Collection<Set<DNode>> splitIntoEquivalenceClasses(Set<DNode> nodes) {
    
    Map<Short, Set<DNode>> labelClasses = new HashMap<Short, Set<DNode>>();
    for (DNode d : nodes) {
      if (!labelClasses.containsKey(d.id))
        labelClasses.put(d.id, new HashSet<DNode>());
      labelClasses.get(d.id).add(d);
    }
    
    if (labelClasses.keySet().size() > 1) {
      // there are several nodes with different labels in the same equivalence class
      // we cannot fold them to the same node, so split the class
      System.out.println("splitting equivalence class "+nodes+" by label");
    }
    
    return labelClasses.values();
  }
}
