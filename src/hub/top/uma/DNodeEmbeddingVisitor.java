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

import java.util.HashMap;
import java.util.Map;

public class DNodeEmbeddingVisitor {
  
  private Map<DNode, DNode> embedding;
  
  public DNodeEmbeddingVisitor() {
    this(new HashMap<DNode, DNode>());
  }

  public DNodeEmbeddingVisitor(Map<DNode, DNode> embedding) {
    this.embedding = embedding;
  }

  public boolean canBeExtended(DNode toEmbed, DNode embedIn) {
    // check whether this node was embedded before (as predecessor of another node) 
    // and whether the embedding would be the same
    for (Map.Entry<DNode, DNode> m : embedding.entrySet()) {
      System.out.println(m.getKey()+" --> "+m.getValue());
    }
    System.out.println("trying "+toEmbed+" --> "+embedIn);
    if (embedding.containsKey(toEmbed) && embedding.get(toEmbed) != embedIn) {
      System.out.println("does not match");
      return false;
    }
    return true;
  }

  public void extend(DNode toEmbed, DNode embedIn) {
    embedding.put(toEmbed, embedIn);
  }

  public Map<DNode, DNode> getEmbedding() {
    return embedding;
  }
}
