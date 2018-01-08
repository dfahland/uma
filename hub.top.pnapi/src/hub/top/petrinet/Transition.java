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

package hub.top.petrinet;

import java.util.LinkedList;
import java.util.List;

/**
 * A {@link PetriNet} transition.
 * 
 * @author Dirk Fahland
 */
public class Transition extends Node {

  public Transition(PetriNet net, String name) {
    super(net, name);
  }

  @Override
  public List<Place> getPreSet() {
    LinkedList<Place> preSet = new LinkedList<Place>();
    for (Arc a : getIncoming())
      preSet.add((Place)a.getSource()); 
    return preSet;
  }
  
  @Override
  public List<Place> getPostSet() {
    LinkedList<Place> postSet = new LinkedList<Place>();
    for (Arc a : getOutgoing())
      postSet.add((Place)a.getTarget());
    return postSet;
  }
  
  /*
   * (non-Javadoc)
   * @see hub.top.petrinet.Node#getUniqueIdentifier()
   */
  @Override
  public String getUniqueIdentifier() {
    return "t"+getID()+"_"+getName();
  }
}
