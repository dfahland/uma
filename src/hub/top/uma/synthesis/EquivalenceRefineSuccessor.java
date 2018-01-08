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
import hub.top.uma.DNodeBP;
import hub.top.uma.Uma;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashSet;

/**
 * Implementation of {@link IEquivalentNodesRefine} to partition an equivalence class
 * of nodes into finer equivalence classes. Two nodes are equivalent iff they have
 * equivalent successor sets.
 * 
 * @author dfahland
 */
public class EquivalenceRefineSuccessor implements IEquivalentNodesRefine {
  
  private DNodeBP bp;
  
  /**
   * The branching process constructor within which we want to refine equivalence
   * classes of nodes based on their successors. The equivalence criterion decides
   * equivalence of successors using {@link DNodeBP#getElementary_ccPair()}.
   * 
   * @param bp
   */
  public EquivalenceRefineSuccessor(DNodeBP bp) {
    this.bp = bp;
  }
  
  /*
   * (non-Javadoc)
   * @see hub.top.uma.synthesis.IEquivalentConditions#splitIntoEquivalenceClasses(java.util.Collection)
   */
  public Collection<Set<DNode>> splitIntoEquivalenceClasses(Set<DNode> nodes)
  {
    // do not split sets of events
    if (!nodes.isEmpty() && nodes.iterator().next().isEvent) {
      Set<Set<DNode>> result = new HashSet<Set<DNode>>();
      result.add(nodes);
      return result;
    }
    
    Uma.out.print("splitting: "+nodes);
    
    Map<DNode[], Set<DNode>> succ = successorEquivalence(bp, nodes);
    
    Uma.out.print(" --> ");
    for (Set<DNode> part : succ.values()) {
      Uma.out.print(part+" | ");
    }
    Uma.out.println();
    
    return succ.values(); // each partition becomes a separate equivalence class
  }
  
  /**
   * Partitions the set of 'nodes' into equivalence classes based on the equivalence relation
   * on the 'nodes' stored in 'bp'.
   *  
   * @param bp
   * @param nodes
   * @return a map that points to the equivalence classes, the key of each equivalence classes
   *         is an array of the canonical representatives of the successors of all nodes in the
   *         class
   */
  public static Map<DNode[], Set<DNode>> successorEquivalence(DNodeBP bp, Set<DNode> nodes) {
    Map<DNode[], Set<DNode>> succ = new HashMap<DNode[], Set<DNode>>();
    
    // partition the set of nodes in this equivalence class based on their
    // successors: two nodes go into the same partition if their sets of successors
    // are equivalent
    for (DNode d : nodes) {
      // compute the set of successors of 'd' (i.e. their canonical representatives)
      DNode s[] = getSuccessorEquivalence(bp, d);
      
      boolean found = false;
      for (DNode[] s2 : succ.keySet()) {  // get all successor sets stored in 'succ'
        if (Arrays.equals(s, s2)) {       // 's' is equivalent to one of those sets
          succ.get(s2).add(d);            // node 'd' has the same successors as 's2'
          found = true;
        }
      }
      if (!found) {
        // the successors of 'd' have not been stored yet: new partition
        succ.put(s, new HashSet<DNode>());
        succ.get(s).add(d);
      }
    }
    
    return succ;
  }
  
  /**
   * @param bp
   * @param d
   * @return a vector of the the canonical representatives of the
   *         successors of 'd' according to the equivalence stored in 'bp'
   */
  public static DNode[] getSuccessorEquivalence(DNodeBP bp, DNode d) {
    if (d.post == null) return new DNode[0];
    
    DNode[] s = new DNode[d.post.length];
    for (int i=0; i<d.post.length; i++) {
      s[i] = bp.futureEquivalence().getElementary_ccPair().get(d.post[i]);
    }
    return s;
  }

}


