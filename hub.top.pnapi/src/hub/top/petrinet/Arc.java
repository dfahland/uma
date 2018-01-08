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

/**
 * Represents an arc of a {@link PetriNet}.
 * 
 * @author Dirk Fahland
 */
public class Arc {

  private Node source;
  private Node target;

  /**
   * Create a new arc with arc weight 1
   * @param src
   * @param tgt
   */
  public Arc(Node src, Node tgt) {
    source = src;
    target = tgt;
  }

  /**
   * @return source node of this arc
   */
  public Node getSource() {
    return source;
  }

  /**
   * @return target node of this arc
   */
  public Node getTarget() {
    return target;
  }  
}
