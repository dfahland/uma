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
 * A {@link PetriNet} place.
 * 
 * @author Dirk Fahland
 */
public class Place extends Node {
  
  private int tokens;

  /**
   * Create a new unmarked place
   * @param name
   */
  public Place(PetriNet net, String name) {
    super(net, name);
    setTokens(0);
  }

  /**
   * Mark this place with the given number of <code>tokens</code>.
   * @param tokens
   */
  public void setTokens(int tokens) {
    this.tokens = tokens;
  }

  /**
   * @return the number of tokens on this place
   */
  public int getTokens() {
    return tokens;
  }

  @Override
  public List<Transition> getPreSet() {
    LinkedList<Transition> preSet = new LinkedList<Transition>();
    for (Arc a : getIncoming())
      preSet.add((Transition)a.getSource());
    return preSet;
  }
  
  @Override
  public List<Transition> getPostSet() {
    LinkedList<Transition> postSet = new LinkedList<Transition>();
    for (Arc a : getOutgoing())
      postSet.add((Transition)a.getTarget());
    return postSet;
  }
  
  /*
   * (non-Javadoc)
   * @see hub.top.petrinet.Node#getUniqueIdentifier()
   */
  @Override
  public String getUniqueIdentifier() {
    return "p"+getID()+"_"+getName();
  }
  
}
