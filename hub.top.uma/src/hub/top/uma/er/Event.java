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


package hub.top.uma.er;

import java.util.LinkedList;

import com.google.gwt.dev.util.collect.HashSet;

import hub.top.uma.DNode;

/**
 * Event of an event structure.
 *
 * @author dfahland
 */
public class Event extends DNode {
  
  public boolean originalModelEvent = false;
  
  public int level = -1;

  public Event(short id, int preSize) {
    super(id, preSize);
    this.isEvent = true;
  }
  
  public Event(short id, DNode pre) {
    super(id, pre);
    this.isEvent = true;
  }
  
  public Event(short id, DNode pre[]) {
    super(id, pre);
    this.isEvent = true;
  }
  
  public boolean hasPred(Event e) {
    for (int i=0;i<pre.length;i++) {
      if (pre[i] == e) return true;
      if (pre[i].id > e.id) return false;
    }
    return false;
  }
  
}
