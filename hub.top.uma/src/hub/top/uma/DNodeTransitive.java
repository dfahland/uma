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

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashSet;


public class DNodeTransitive extends DNode {
  
  public DNode preTrans[];
  public short visibleEvent = -1;
  
  public DNodeTransitive(short id, int preSize, int preTransSize) {
    super(id, preSize);
    this.preTrans = new DNode[preTransSize];
  }
  
  public DNodeTransitive(short id, DNode pre, DNode preTrans[]) {
    super(id, pre);
    this.preTrans = DNode.sortIDs(preTrans);
  }
  
  public DNodeTransitive(short id, DNode pre[], DNode preTrans[]) {
    super(id, pre);
    this.preTrans = DNode.sortIDs(preTrans);
  }
  
  public DNodeTransitive(short id, int preSize) {
    this(id, preSize,0);
  }
  
  public DNodeTransitive(short id, DNode pre) {
    this(id, pre, null);
  }
  
  public DNodeTransitive(short id, DNode pre[]) {
    this(id, pre, null);
  }

  
  private boolean visibleEventBetween(DNode start, DNode end) {
    if (visibleEvent == -1) return false;
    
    Set<DNode> fPre = end.getAllPredecessors();
    if (!fPre.contains(start)) return false;
    
    // starting from 'start', search forward in all successors of
    // 'start' that are predecessors of 'end' whether one node
    // has the label of the visible event of 'this' node
    LinkedList<DNode> queue = new LinkedList<DNode>();
    HashSet<DNode> seen = new HashSet<DNode>();
    queue.add(start);
    while (!queue.isEmpty()) {
      DNode d = queue.removeFirst();
      if (d != start && d != end && d.id == visibleEvent) return true;
      if (d.post != null) {
        for (DNode dPost : d.post) {
          if (fPre.contains(dPost) && !seen.contains(dPost)) {
            queue.addLast(dPost);
            seen.add(dPost);
          }
        }
      }
    }
    // no node between start and end contained the visible event
    return false;
  }

  /**
   * Refines {@link DNode#suffixOf(DNode)} to check whether this node is a
   * suffix of node {@code complete} and each transitive predecessor (
   * {@link #preTrans}) of this node is in the transitive preset of node
   * 'complete'. Computes an embedding of this node's history into the history
   * of 'complete' if {@code embedding != null}.
   * 
   * @param complete
   * @param embedding (can be {@code null})
   * @return {@code true} iff this node is a suffix of node (@code complete)
   */
  @Override
  public boolean suffixOf(DNode complete, DNodeEmbeddingVisitor embedding) {
    
    // first all direct predecessors have to be a suffix of complete
    if (!super.suffixOf(complete, embedding)) return false;
    // then check each transitive predecessor
    if (preTrans == null || preTrans.length == 0) {
      // super.suffixOf guarantees returns true only if
      // embedding.canBeExtended == true
      if (embedding != null) embedding.extend(this, complete);
      return true;
    }

    nextnode: for (DNode d : this.preTrans) {
      // breadth-first search
      LinkedList<DNode> queue = new LinkedList<DNode>();
      HashSet<DNode> seen = new HashSet<DNode>();
      for (DNode c : complete.pre) {
        queue.add(c);
        seen.add(c);
      }
      while (!queue.isEmpty()) {
        DNode c = queue.removeFirst();
        // 'd' is suffix of 'c' and the visible event of 'this' node
        // is not between the matching node 'c' and 'complete'
        if (d.suffixOf(c, embedding) && !this.visibleEventBetween(c, complete)) {
          // continue matching next predecessor of 'this'
          continue nextnode;
        }
        // cannot match with a predecessor of the visible event, i.e.
        // if 'c' is not a match, it's predecessors won't be one
        if (c.id == visibleEvent) continue;
        
        // not found, look further down in the predecessors of 'complete'
        if (c.pre != null) {
          for (DNode cPre : c.pre) {

            if (!seen.contains(cPre)) {
              queue.addLast(cPre);
              seen.add(cPre);
            }
          }
        }
      }
      // no match for 'd': 'this' is not a suffix of 'complete'
      return false;
    }
    
    if (embedding != null) embedding.extend(this, complete);
    return true;
  }
  
  @Override
  public boolean structuralEquals(DNode other) {
    if (!super.structuralEquals(other)) return false;
    
    int this_preTrans_length = (this.preTrans == null) ? 0 : this.preTrans.length;
    int other_preTrans_length = (!(other instanceof DNodeTransitive)|| ((DNodeTransitive)other).preTrans == null) ? 0 : ((DNodeTransitive)other).preTrans.length;
    
    if (this_preTrans_length != other_preTrans_length) return false;

    // if this_preTrans_length > 0, then (by this_preTrans_length == other_preTrans_length
    // holds other instanceof DNodeTransitive
    for (int i=0; i<this_preTrans_length; i++) {
      if (!this.preTrans[i].structuralEquals( ((DNodeTransitive)other).preTrans[i])) return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "'"+nameTranslationTable[this.id]+"' ("+this.id+")["+this.globalId+"]" + (isAnti ? "-" : "")+" vis: "+visibleEvent; 
  }
}
