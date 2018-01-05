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

import hub.top.petrinet.Node;
import hub.top.petrinet.PetriNet;
import hub.top.petrinet.PetriNetIO;
import hub.top.petrinet.PetriNetIO_Out;
import hub.top.petrinet.Place;
import hub.top.petrinet.Transition;
import hub.top.uma.DNode;
import hub.top.uma.DNodeSys;
import hub.top.uma.Uma;
import hub.top.uma.er.EventStructure.EventCollection;
import hub.top.uma.view.ViewGeneration2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashSet;

public class EventStructureMiner_BP {
  
  private PetriNet net;
  private EventStructure es;
  
  private Event artificialStart;
  private Place move_on_log;
  
  public EventStructureMiner_BP(PetriNet net) {
    this.net = net;
    
    net.makeNormalNet();
    net.turnIntoLabeledNet();
    
    this.es = new EventStructure();
    
    buildNameTable();
    
    artificialStart = new Event(nameToID.get(NAME_ARTIFICIAL_START), new Event[0]);
    es.add(artificialStart);
    consumedFrom = new HashMap<Event, List<Token>>();
    inTrace = new HashMap<Event, Set<Integer>>();
  }
  
  
  private static class Token {
    public Place on;
    public Event producedBy;
    public Token(Place p, Event e) {
      this.on = p;
      this.producedBy = e;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Token) {
        Token other = (Token)obj;
        return this.on == other.on && this.producedBy == other.producedBy;
      }
      return false;
    }
    
    @Override
    public String toString() {
      return on+" ("+producedBy+")";
    }
  }
  
  private Map<Event, List<Token> > consumedFrom;
  
  private Map<Event, Set<Integer> > inTrace;
  
  private int traceCount = 0;
  
  public void extendByTrace(String trace[]) {
    
    //System.out.println(ViewGeneration2.toString(trace));
    
    traceCount++;
    
    Set<Event> config = new HashSet<Event>();
    LinkedList<Token> marking = new LinkedList<Token>();
    List<Place> startEffect = new LinkedList<Place>();
    
    // create initial marking
    for (Place p : net.getPlaces()) {
      for (int i=0;i<p.getTokens();i++) {
        marking.add(new Token(p,artificialStart));
        startEffect.add(p);
      }
    }
    marking.add(new Token(move_on_log, artificialStart));
    startEffect.add(move_on_log);
    
    Event lastEvent = artificialStart;
    
    if (!inTrace.containsKey(lastEvent)) inTrace.put(lastEvent, new HashSet<Integer>());
    inTrace.get(lastEvent).add(traceCount);
    config.add(lastEvent);

    // introduce a new tau-event for each trace so that we have a separate representation of each trace
    lastEvent = appendEvent(nameToID.get(NAME_TAU), marking, startEffect, marking, config, lastEvent, false);

    if (!inTrace.containsKey(lastEvent)) inTrace.put(lastEvent, new HashSet<Integer>());
    inTrace.get(lastEvent).add(traceCount);
    config.add(lastEvent);

    // set new tau-event into conflict with all other tau-events
    for (DNode d : artificialStart.post) {
      // set conflicts newEvent -> check successors of all other events in the configuration
      if (d == lastEvent) continue;
      Event f = (Event)d;
      es.setDirectConflict(f, lastEvent);
    }

    
    // execute the trace
    nextevent: for (String event : trace) {
      
      boolean isTauStep = false;
      
      String transitionName;
      if (event.startsWith("tau_")) {
        transitionName = event.substring(4);
        isTauStep = true;
      } else {
        transitionName = event;
      }
      
      // find the transition to fire
      Transition toFire = null;
      for (Transition t : net.getTransitions()) {
        if (t.getName().equals(transitionName)) {
          toFire = t;
          break;
        }
      }
      
      if (toFire == null) {
        System.err.println("Error. Net does not contain transition with name "+transitionName);
        return;
      }
      
      int numMissingTokens = 0;
      
      // find the tokens that are consumed by 'toFire'
      List<Token> toConsume = new LinkedList<Token>();
      nextplace: for (Place p : toFire.getPreSet())
      {
        for (Token m : marking) {
          if (m.on == p) {
            toConsume.add(m);
            continue nextplace;
          }
        }
        
        
        // no token on p: add new token
        Token m = new Token(p,lastEvent);
        marking.add(m);
        toConsume.add(m);
        numMissingTokens++;
      }
      List<Place> effect = toFire.getPostSet();
      
      if (numMissingTokens > 0) {
        toConsume.clear();
        for (Token tok : marking) {
          if (tok.on == move_on_log) {
            toConsume.add(tok);
          }
        }
        effect = new LinkedList<Place>();
        effect.add(move_on_log);
      }
      
      short toFireID = nameToID.get(event);
      Event newEvent = appendEvent(toFireID, toConsume, effect, marking, config, lastEvent, true);
      lastEvent = newEvent;
      
      if (numMissingTokens == 0) {
        newEvent.originalModelEvent = true; 
      } else {
        newEvent.originalModelEvent = false;
      }

      // remember that the event participates in this trace
      if (!inTrace.containsKey(lastEvent)) inTrace.put(lastEvent, new HashSet<Integer>());
      inTrace.get(lastEvent).add(traceCount);
      
      /*
      //System.out.println("added "+newEvent+" @ "+config);
      
      Set<Event> newEvent_config = config;
      for (Event d : config) {
        // set conflicts newEvent -> check successors of all other events in the configuration
        if (d == newEvent) continue;
        if (d.post == null) continue;
        
        for (DNode dPost : d.post) {
          if (config.contains(dPost)) continue;
          Event f = (Event)dPost;
          Set<Event> f_pred = es.getPrimeConfiguration(f, true);
          refineConflicts(newEvent, newEvent_config, f, f_pred);
        }
      }
      */

    }
    
    short endEventId = nameToID.get(NAME_ARTIFICIAL_END);
    lastEvent = appendEvent(endEventId, marking, new LinkedList<Place>(), marking, config, lastEvent, true); 

    // remember that the event participates in this trace
    if (!inTrace.containsKey(lastEvent)) inTrace.put(lastEvent, new HashSet<Integer>());
    inTrace.get(lastEvent).add(traceCount);

  }
  
  private Event appendEvent(short toFireID, List<Token> toConsume, List<Place> effect, LinkedList<Token> marking, Set<Event> config, Event lastEvent, boolean mapToExisting) {

    // get the predecessor events based on the consumed tokens
    HashSet<Event> preEvents = new HashSet<Event>();
    for (Token m : toConsume) {
      preEvents.add(m.producedBy);
    }
    
    Event pre[] = new Event[preEvents.size()];
    pre = preEvents.toArray(pre);
    
    // see if this event was already fired
    Event newEvent = null;
    if (mapToExisting) {
      for (Event preEvent : config) {
        if (preEvent.post == null) continue;
        
        for (DNode d : preEvent.post) {
          Event e = (Event)d;

          // make sure that the new event is consistent with the current configuration:
          // not yet executed

          if (config.contains(d)) continue;
          if (e.id == toFireID) {
            newEvent = e;
          }
          
          if (newEvent != null) {
            for (Event preEvent2 : config) {
              if (es.inConflict(preEvent2, newEvent)) { newEvent = null; break; }
            }
          }
  
          // all predecessor events are part of the configuration
          if (newEvent != null) {
            for (DNode pred : newEvent.getAllPredecessors()) {
              if (pred == newEvent) continue;
              if (!config.contains(pred)) { newEvent = null; break; }
              if (!inTrace.get(pred).contains(traceCount))  { newEvent = null; break; }
            }
          }
          if (newEvent != null) break;
        }
      }
    }
    // if not: create it and make it depend on the events from which
    // we consume a token
    if (newEvent == null) {
      newEvent = new Event(toFireID, pre);
      es.add(newEvent);
      for (Event e : pre) {
        e.addPostNode(newEvent);
      }
      consumedFrom.put(newEvent, new LinkedList<Token>(toConsume));
      
      for (Token m : toConsume) {
        if (m.producedBy.post != null) {
          for (DNode conflictingEvent : m.producedBy.post) {
            // just added, skip the newEvent
            if (conflictingEvent == newEvent) continue;
            
            boolean inConflictWithNew = false;
            done: for (Token m2 : consumedFrom.get(conflictingEvent)) {
              for (Token m3 : toConsume) {
                if (m2.equals(m3)) {
                  inConflictWithNew = true;
                  break done;
                }
              }
            }
            if (inConflictWithNew) {
              if (!es.alreadyInConflict_byOther(newEvent, (Event)conflictingEvent)) {
                es.setDirectConflict(newEvent, (Event)conflictingEvent);
              }
            }
          }
        }
      }
    }

    // update the marking
    marking.removeAll(toConsume);
    for (Place p : effect) {
      marking.add(new Token(p,newEvent));
    }
    
    config.add(newEvent);
    
    return newEvent;
  }
  
  private static final int ORD_CONFLICT = 0;
  private static final int ORD_LEQ = 1;
  private static final int ORD_GEQ = 2;
  private static final int ORD_CONCURRENT = 3;

  private int getOrderingRelation(Set<Event> e_pred, Event e, Set<Event> f_pred, Event f) {
    if (es.inConflict(e_pred, e, f_pred, f)) {
      return ORD_CONFLICT;
    }
    if (f_pred.contains(e)) {
      return ORD_LEQ;
    }
    if (e_pred.contains(f)) {
      return ORD_GEQ;
    }
    return ORD_CONCURRENT;
  }
  
  public void refineConflicts() {
    refineConflicts(es.getAllEvents());
  }
  
  public void refineConflicts(Event e, Set<Event> e_pred, Event f, Set<Event> f_pred) {
    int ord = getOrderingRelation(e_pred, e, f_pred, f);
    if (ord == ORD_CONCURRENT) {
      boolean overlap = false;
      for (Integer t_e : inTrace.get(e)) {
        if (inTrace.get(f).contains(t_e)) {
          overlap = true;
          break;
        }
      }
      if (!overlap) {
        if (!es.alreadyInConflict_byOther(e_pred, e, f_pred, f)) {
          es.setDirectConflict(e, f);
          //System.out.println(e+" #!" +f);
        }
      }
    }
    /* -- do not set all conflicts, keep just the minimal ones
    if (ord == ORD_CONFLICT) {
      es.setDirectConflict(e, f);
    }
    */
    
    /*
    // reduce transitive conflicts
    if (es.inDirectConflict(e, f) && es.alreadyInConflict_byOther(e_pred, e, f_pred, f)) {
      es.directConflict.get(e).remove(f);
      es.directConflict.get(f).remove(e);
    }*/
    
  }
  
  public void refineConflicts(EventCollection forEvents) {

    for (Event e : forEvents) {
      Set<Event> e_pred = es.getPrimeConfiguration(e, true);
      
      for (Event g : e_pred) {
        if (g == e || g.post == null) continue;
        for (DNode gPost : g.post) {
          Event f = (Event)gPost;
          
          if (es.inDirectConflict(e, f)) continue;
          
          Set<Event> f_pred = es.getPrimeConfiguration(f, true);
          
          refineConflicts(e, e_pred, f, f_pred);
        }
      }
    }
  }
  
  public String       properNames[];      // translate IDs to names
  public Map<String, Short> nameToID;     // and names to IDs
  public short currentNameID;
  private int nodeNum;
  public HashSet<Short>       terminalNodes;
  
  public static final String NAME_ARTIFICIAL_START = "start";
  public static final String NAME_ARTIFICIAL_END = "end";
  public static final String NAME_TAU = "tau"; 
  
  /**
   * Translate full strings of the Petri net to ids and fill
   * {@link DNodeSys#nameToID} and {@link DNodeSys#properNames}.
   * 
   * @param net
   */
  private void buildNameTable() {
    
    nameToID = new HashMap<String, Short>();
    terminalNodes = new HashSet<Short>();
    
    nodeNum = 0;
    
    // collect all names and assign each new name a new ID
    for (Node n : net.getTransitions()) {
      if (nameToID.get(n.getName()) == null) {
        nameToID.put(n.getName(), currentNameID++);
        nameToID.put("tau_"+n.getName(), currentNameID++);
      }
     nodeNum++;
    }
    for (Node n : net.getPlaces()) {
      if (nameToID.get(n.getName()) == null)
        nameToID.put(n.getName(), currentNameID++);
      nodeNum++;
      
      if (n.getOutgoing().size() == 0)
        // remember ID of this terminal node
        terminalNodes.add(nameToID.get(n.getName()));
    }
    
    if (!nameToID.containsKey(NAME_ARTIFICIAL_START))
      nameToID.put(NAME_ARTIFICIAL_START, currentNameID++);
    if (!nameToID.containsKey(NAME_ARTIFICIAL_END))
      nameToID.put(NAME_ARTIFICIAL_END, currentNameID++);
    if (!nameToID.containsKey(NAME_TAU))
      nameToID.put(NAME_TAU, currentNameID++);
    
    properNames = new String[nameToID.size()];
    for (Entry<String,Short> line : nameToID.entrySet()) {
      properNames[line.getValue()] = line.getKey();
    }
    
    DNode.nameTranslationTable = properNames;
  }
  
  private Set<Event> singleEvents;
  
  private void setSingleEvents() {
    singleEvents = new HashSet<Event>();
    
    for (Event e : es.getAllEvents()) {
      if (   (   e.pre != null && e.pre.length <= 1
              && e.post != null && e.post.length <= 1)
          || !es.directConflict.containsKey(e)
          || es.directConflict.get(e).size() == 0)
      {
        singleEvents.add(e);
      }
    }
  }
  
  boolean co[][];
  
  public void getConcurrentEvents() {
    
    boolean precedes[][] = new boolean[properNames.length][properNames.length];
    for (Event e : es.getAllEvents()) {
      Set<Event> ePre = es.getPrimeConfiguration(e, false);
      for (Event f : ePre) {
        if (f.id != e.id) {
          precedes[f.id][e.id] = true;
        }
      }
    }
    
    co = new boolean[properNames.length][properNames.length];
    for (short e1 = 0; e1 < precedes.length; e1++) {
      for (short e2 = 0; e2 < precedes.length; e2++) {
        if (precedes[e1][e2] && precedes[e2][e1]) {
          co[e1][e2] = true;
          co[e2][e1] = true;
        }
      }
    }
  }
  
  /**
   * Compute the transitive dependencies between all transitions in all
   * oclets of the {@link #system}.
   */
  private boolean[][] computeTransitiveDependencies(DNode[] nodes) {
    
    int nodeNum = nodes.length;
    
    boolean directSucc[][] = new boolean[nodeNum][nodeNum];
    boolean transSucc[][] = new boolean[nodeNum][nodeNum];

    // extract the direct successor relations between nodes
    for (int i=0; i<nodeNum; i++) {
      DNode c = nodes[i];
      if (c.post == null) continue;
      for (DNode d : c.post) {
        int j = 0;
        while (j < nodes.length) {
          if (nodes[j] == d) break;
          j++;
        }
        if (j == nodes.length) continue; // successor 'd' of 'c' is not part of these nodes
        
        directSucc[i][j] = true;
        transSucc[i][j] = true;
      }
    }
    
    /*
     For each j from 1 to n
      For each i from 1 to n
         If T(i,j)=1, then form the Boolean or of row i and row j 
                           and replace row i by it.
         Go on to the next i-value.
      Once you have processed each i-value, go on to the next j-value.

     */
    for (int j=0; j<transSucc.length; j++) {
      for (int i=0; i<transSucc.length; i++) {
        if (i != j && transSucc[i][j]) {
          for (int k = 0; k<transSucc.length; k++) {
            transSucc[i][k] = transSucc[i][k] || transSucc[j][k];
          }
        }
      }
    }
    
    for (int j=0; j<transSucc.length; j++) {
      for (int i=0; i<transSucc.length; i++) {
        if (co[nodes[i].id][nodes[j].id]) {
          transSucc[i][j] = false;
          transSucc[j][i] = false;
        }
      }
    }
    
    // transitive reduction
    for (int j=0; j<transSucc.length; j++) {
      for (int i=0; i<transSucc.length; i++) {
        if (i != j && transSucc[i][j]) {
          for (int k = 0; k<transSucc.length; k++) {
            transSucc[i][k] = transSucc[i][k] && !transSucc[j][k];
          }
        }
      }
    }
    
    
    /*
    for (int i=0;i<nodeNum; i++) {
      for (int j=0;j<nodeNum; j++) {
        if (transSucc[i][j] && !directSucc[i][j])
          System.out.println(nodes[i]+" ==>* "+nodes[j]);
        else if (directSucc[i][j])
          System.out.println(nodes[i]+" -->  "+nodes[j]);
        else if (transSucc[i][j])
          System.out.println(nodes[i]+" -->* "+nodes[j]);
      } 
    }
    */
    
    /*
    System.out.println("---------------------------------------------");
    System.out.println(DNode.toString(nodes));
    for (int i=0;i<nodeNum; i++) {
      for (int j=0;j<nodeNum; j++) {
        if (transSucc[i][j])
          System.out.println(nodes[i]+" --> "+nodes[j]);
      } 
    }
    */
    
    return transSucc;
  }
  
  public void extendConcurrency() {
    
    Map<Event, Set<Event>> cosets = new HashMap<Event, Set<Event>>();
    LinkedList<Event> queue = new LinkedList<Event>();
    queue.add(artificialStart);
    cosets.put(artificialStart, new HashSet<Event>());
    cosets.get(artificialStart).add(artificialStart);
    
    int count = 0;
    int step = es.allEvents.size() / 100 + 1;
    int step_60 = step * 60;
    
    while (!queue.isEmpty()) {
      
      count++;
      
      if (count % step == 0) System.out.print(".");
      if (count % step_60 == 0) System.out.println(count);
      
      EventCollection touched = new EventCollection();
      
      Event e = queue.removeFirst();
      if (singleEvents.contains(e)) {
        // check CO
        LinkedList<Event> coqueue = new LinkedList<Event>();
        Set<Event> e_succ = new HashSet<Event>();
        Set<Event> dont_change = new HashSet<Event>();
        coqueue.add(e);
        e_succ.add(e);

        Event preNew = new Event(nameToID.get(NAME_TAU), 0);
        
        touched.add(preNew);
        inTrace.put(preNew, new HashSet<Integer>());
        for (int i : inTrace.get(e)) {
          inTrace.get(preNew).add(i);
        }

        for (DNode pred : e.getAllPredecessors()) {
          if (pred == e) continue;
          es.setDependency((Event)pred, preNew);
        }
        es.setDependency(preNew, e);
        es.add(preNew);
        
        // copy conflicts
        if (es.directConflict.containsKey(e)) {
          for (Event f : es.directConflict.get(e)) {
            es.setDirectConflict(preNew, f);
          }
        }
        
        e_succ.add(preNew);
        
       
        
        while (!coqueue.isEmpty()) {
          Event f = coqueue.removeFirst();
          if (f.post != null) {
            for (DNode fPost : f.post) {
              if (singleEvents.contains(fPost)) {
                if (!e_succ.contains(fPost)) {
                  e_succ.add((Event)fPost);
                  coqueue.add((Event)fPost);
                }
              } else {
                e_succ.add((Event)fPost);
                //dont_change.add((Event)fPost);
                
                // fPost has not been visited yet, add to the outer queue
                if (!cosets.containsKey(fPost)) {
                  cosets.put((Event)fPost, new HashSet<Event>());
                  cosets.get(fPost).add((Event)fPost);
                  queue.addLast((Event)fPost);
                }
              }
            }
          }
        }
        
        DNode e_succ_array[] = e_succ.toArray(new DNode[e_succ.size()]);
        boolean depends[][] = computeTransitiveDependencies(e_succ_array);
        
        for (int k=0;k<e_succ_array.length;k++) {
          Event d_k = (Event)e_succ_array[k];
          if (dont_change.contains(d_k)) continue;
          
          for (int l=0;l<e_succ_array.length;l++) {
            Event d_l = (Event)e_succ_array[l];
            if (dont_change.contains(d_l)) continue;
            
            if (k != l) {
              if (depends[k][l]) {
                es.setDependency(d_k, d_l);
                
                // do not remove dependencies between original model events  
              } else /*if (!d_l.originalModelEvent || !d_k.originalModelEvent)*/ {
                if (es.removeDependency(d_k, d_l)) {
                
                  if (es.directConflict.containsKey(d_k)) {
                    for (Event conf : es.directConflict.get(d_k)) {
                      if (!es.directConflict.containsKey(d_l))
                        es.directConflict.put(d_l, new HashSet<Event>());
                      es.directConflict.get(d_l).add(conf);
                      //System.out.println("set "+d_l+" # "+conf +" because of "+d_k);
                    }
                  }

                }
              }
            }
          }
          touched.add((Event)e_succ_array[k]);
        }
        
        
      } else if (e.post != null) {
        for (DNode ePost : e.post) {
          // ePost has not been visited yet, add to the outer queue
          if (!cosets.containsKey(ePost)) {
            cosets.put((Event)ePost, new HashSet<Event>());
            cosets.get(ePost).add((Event)ePost);
            queue.addLast((Event)ePost);
          }
        }
      }
      
      //refineConflicts(touched);
      //es.reduceTransitiveConflicts(touched);
      es.removeTransitiveDependencies();
    }

  }
  
  public void coarsenCausality() {
    boolean changed = true;
    while (changed) {
      changed = false;
      
      EventCollection newNodes = new EventCollection();
      EventCollection touched = new EventCollection();
      
      for (Event e : es.getAllEvents()) {
        if (!singleEvents.contains(e)) continue;
        if (e.pre != null) {
          for (DNode f : e.pre) {
            if (!singleEvents.contains(f)) continue;
            
            // f --> e
            if (co[f.id][e.id]) {
              es.removeDependency((Event)f, e);
              
              changed = true;
              
              Event preNew = new Event(nameToID.get(NAME_TAU), 0);
              Event postNew = new Event(nameToID.get(NAME_TAU), 0);
              
              newNodes.add(preNew);
              newNodes.add(postNew);
              touched.add(preNew);
              touched.add(postNew);
              inTrace.put(preNew, new HashSet<Integer>());
              inTrace.put(postNew, new HashSet<Integer>());
              for (int i : inTrace.get(e)) {
                inTrace.get(preNew).add(i);
                inTrace.get(postNew).add(i);
              }
              for (int i : inTrace.get(f)) {
                inTrace.get(preNew).add(i);
                inTrace.get(postNew).add(i);
              }

              if (f.pre != null) {
                DNode fPres[] = f.pre;
                for (DNode fPre : fPres) {
                  es.setDependency((Event)fPre, preNew);
                  //es.removeDependency((Event)fPre, (Event)f);
                }
              }
              if (e.post != null) {
                DNode ePosts[] = e.post;
                for (DNode ePost : ePosts) {
                  es.setDependency(postNew, (Event)ePost);
                  //es.removeDependency(e, (Event)ePost);
                }
              }
              
              es.setDependency(preNew, e);
              es.setDependency(preNew, (Event)f);
              es.setDependency(e, postNew);
              es.setDependency((Event)f, postNew);

              touched.add(e);
              touched.add((Event)f);
            }
          }
        }
      }
      for (Event e : newNodes) es.add(e);
      refineConflicts(touched);
    }
  }
  
  
  //public boolean inConflictOutsideFolding(Event e1, Set<Event> e1_pre, Event e2, Map<Event,Set<Event>> foldingEquivalence) {
  public boolean inConflictOutsideFolding(Event e1, Set<Event> e1_pre, Event e2, Map<Event,Event> canonical) {
    
    
    Set<Event> e2_pre = null;
    
    for (Event f : e1_pre) {
      if (es.directConflict.containsKey(f)) {
        for (Event f_confl : es.directConflict.get(f)) {
          
          // the conflict pair is represented after the folding, don't consider
          if (   canonical.containsKey(f) && canonical.get(f) == f
              && canonical.containsKey(f_confl) && canonical.get(f_confl) == f_confl) continue;
          
          if (e2_pre == null) e2_pre = es.getPrimeConfiguration(e2, true);
          
          if (e2_pre.contains(f_confl)) {
            return true;
          }
        }
      }
    }
    return false;
    
    /*
    for (Event e1_equiv : foldingEquivalence.get(e1)) {
      for (Event e2_equiv : foldingEquivalence.get(e2)) {
        // the conflict pair is represented after the folding, don't consider
        if (es.inDirectConflict(e1_equiv, e2_equiv)) {
          System.out.println("setting "+e1+" #! "+e2+" because of "+e1_equiv+" # "+e2_equiv);
          return true;
        }
      }
    }
    return false;
    */
    //return es.inConflict(e1, e2);
  }

  
  public void foldForward() {
    Map<Event, Set<Event>> foldingEquivalence = new HashMap<Event, Set<Event>>();
    Map<Event, Event> canonical = new HashMap<Event, Event>();
    
    LinkedList<Event> queue = new LinkedList<Event>();
    queue.add(artificialStart);
    foldingEquivalence.put(artificialStart, new HashSet<Event>());
    foldingEquivalence.get(artificialStart).add(artificialStart);
    canonical.put(artificialStart, artificialStart);
    
    while (!queue.isEmpty()) {
      Event e = queue.removeFirst();
      
      Map<Short, Set<Event>> id_equivalent = new HashMap<Short, Set<Event>>();
      
      for (Event eEquiv : foldingEquivalence.get(e)) {
        if (eEquiv.post == null) continue;
        
        for (DNode ePost : eEquiv.post) {
          Event f = (Event)ePost;
          
          boolean allPredecessorsSeen = true;
          for (DNode fPre : f.pre) {
            if (!canonical.containsKey(fPre)) {
              allPredecessorsSeen = false;
            }
          }
          
          if (allPredecessorsSeen) {


            // TODO: and all predecessors must be mutually equivalent
            if (!id_equivalent.containsKey(f.id)) id_equivalent.put(f.id, new HashSet<Event>());
            for (DNode f_other : id_equivalent.get(f.id)) {
              if (f != f_other && !es.inConflict(f, (Event)f_other)) {
                if (nameToID.get(NAME_TAU) != f.id || !es.conflictEquivalent(f, (Event)f_other)) {
                  System.err.println("merging two concurrent events "+f+" and "+f_other);
                }
              }
            }
            id_equivalent.get(f.id).add(f);
          }
        }
      }
      
      for (Map.Entry<Short, Set<Event>> id_equiv_class : id_equivalent.entrySet()) {
        
        Map<Set<Event>, Set<Event>> pred_equivalent = new HashMap<Set<Event>, Set<Event>>();
        for (Event f : id_equiv_class.getValue()) {
          Set<Event> f_pred_canonical = new HashSet<Event>();
          for (DNode fPred : f.pre) {
            f_pred_canonical.add(canonical.get(fPred));
          }
          
          if (!pred_equivalent.containsKey(f_pred_canonical)) pred_equivalent.put(f_pred_canonical, new HashSet<Event>());
          pred_equivalent.get(f_pred_canonical).add(f);
        }
        
        for (Set<Event> equivs : pred_equivalent.values()) {
          Event f = getCanonical(equivs);
          foldingEquivalence.put(f, new HashSet<Event>(equivs));
          queue.add(f);
          for (Event fEquiv : equivs) canonical.put(fEquiv, f);
        }
      }
    }
    
    for (Event e : es.allEvents) {
      if (!canonical.containsKey(e)) {
        System.err.println(e+" has no canonical");
      }
    }
    
    for (Map.Entry<Event, Set<Event>> equiv : foldingEquivalence.entrySet()) {
      System.out.println(equiv);
    }
    
    int count = 0;
    int step = es.allEvents.size() / 100 + 1;
    int step_60 = step * 60;
    
    System.out.println("transferring conflicts to folding...");
    for (Event e : es.allEvents) {
      
      count++;
      if (count % step == 0) System.out.print(".");
      if (count % step_60 == 0) System.out.println(count);
      
      Set<Event> e_pre = null;
      if (canonical.containsKey(e) && canonical.get(e) == e)
      {
        for (Event f : es.allEvents) {
          if (f == e) continue;
          if (canonical.containsKey(f) && canonical.get(f) == f) {
            
            if (e_pre == null) e_pre = es.getPrimeConfiguration(e, true);
            Set<Event> f_pre = es.getPrimeConfiguration(f, true);
            if (!e_pre.contains(f) && !f_pre.contains(e) && inConflictOutsideFolding(e, e_pre, f, canonical))
            {
              es.setDirectConflict(e, f);
            }
          }
        }
      }
    }
    
    count = 0;
    
    System.out.println("clearing conflicts...");
    for (Event e : es.allEvents) {
      
      count++;
      if (count % step == 0) System.out.print(".");
      if (count % step_60 == 0) System.out.println(count);
      
      if (canonical.containsKey(e) && canonical.get(e) == e) {
        
        //System.out.println("clear "+e);
        
        // remove stale conflict relations that hold because predecessors
        // of 'e' are merged with conflicting predecessors but these
        // predecessors were in conflict with 'e'
        if (es.directConflict.containsKey(e)) {
          List<Event> removeFromConflicts = new LinkedList<Event>();
          for (Event eConfl : es.directConflict.get(e)) {
            
            //System.out.println("   -- "+eConfl);
            
            // the events e and f are in conflict, look whether there are
            // equivalent events e* and f* merged into e and f that are 
            // not in conflict
            boolean mergedWithNonConflict = false;
            f_equivalent_events: for (Event eConfl_equiv : foldingEquivalence.get(canonical.get(eConfl))) {
              if (eConfl == eConfl_equiv) continue;
              for (Event eEquiv : foldingEquivalence.get(e)) {
                if (eEquiv == eConfl_equiv) continue;
                if (!es.inConflict(eEquiv, eConfl_equiv)) {
                  //System.out.println(eConfl+" #! "+e+"  non-conflict by "+eEquiv+" #! "+eConfl_equiv);
                  mergedWithNonConflict = true;
                  break f_equivalent_events;
                }
              }
            }
            if (mergedWithNonConflict) {
              removeFromConflicts.add(eConfl);
            }
          }
          for (Event f : removeFromConflicts) {
            es.directConflict.get(e).remove(f);
            es.directConflict.get(f).remove(e);
          }
        }
      }
    }
    
    System.out.println("folding...");
    
    List<Event> toRemove = new LinkedList<Event>();
    for (Event e : es.allEvents) {
      if (canonical.containsKey(e) && canonical.get(e) != e) {
        toRemove.add(e);
      } else {
        
        // collect all predecessors of all folded nodes 
        if (e.pre != null) {
          Set<Event> ePreNew = new HashSet<Event>();
          for (Event e_equiv : foldingEquivalence.get(e)) {
            if (e_equiv.pre != null) {
              for (DNode ePre : e_equiv.pre) {
                // and make their folded predecessors the new predecessors of this node
                ePreNew.add(canonical.get(ePre));
              }
            }
          }
          
          Event[] _ePreNew = ePreNew.toArray(new Event[ePreNew.size()]);
          DNode.sortIDs(_ePreNew);
          
          e.pre = _ePreNew;
        }
        
        // collect all successors of all folded nodes 
        if (e.post != null) {
          Set<Event> ePostNew = new HashSet<Event>();
          for (Event e_equiv : foldingEquivalence.get(e)) {
            if (e_equiv.pre != null) {
              for (DNode ePost : e_equiv.post) {
                // and make their folded successors the new successors of this node
                ePostNew.add(canonical.get(ePost));
              }
            }
          }
          
          Event[] _ePostNew = ePostNew.toArray(new Event[ePostNew.size()]);
          DNode.sortIDs(_ePostNew);
          e.post = _ePostNew;
        }
      }
        
    }

    es.removeAll(toRemove);
  }
  
  public void removeSuperFluousTauEvents() {
    
    List<Event> toRemove = new LinkedList<Event>();
    for (Event e : es.allEvents) {
      if (properNames[e.id].equals(NAME_TAU)) {
        if (es.directConflict.containsKey(e) && es.directConflict.get(e).size() > 0)
          continue;
        
        for (DNode ePre : e.pre) {
          for (DNode ePost : e.post) {
            ePre.addPostNode(ePost);
            ePost.addPreNode(ePre);
          }
        }
        toRemove.add(e);
      }
    }
    
    es.removeAll(toRemove);
  }
  
  public static Event getCanonical(Collection<Event> events) {
    Event min = null;
    for (Event e : events) {
      if (min == null) min = e;
      else {
        if (e.globalId < min.globalId) min = e;
      }
    }
    return min;
  }
  
  /**
   * @param e
   * @return {@code true} iff e is in conflict with a set of events that are pairwise conflict-free
   */
  public boolean isConflictFan(Event e) {
    
    DNode[] allPred = null;
    
    if (!es.directConflict.containsKey(e) || es.directConflict.get(e).size() < 2) return false;
    for (Event f : es.directConflict.get(e)) {
      if (allPred == null) allPred = f.pre;
      
      for (Event g : es.directConflict.get(e)) {
        if (f == g) continue;
        
        // all conflicting events have to have the same predecessors
        if (g.pre.length != allPred.length) return false;
        for (int i=0; i<allPred.length; i++) {
          if (allPred[i] != g.pre[i]) return false;
        }
        
        if (es.inConflict(f, g)) return false;
      }
    }
    return true;
  }
  
  public void makeConflictFansLocal() {
    
    List<Event> newEvents = new LinkedList<Event>();
    for (Event e : es.allEvents) {
      if (!isConflictFan(e)) continue;
      
      Event preNew = new Event(nameToID.get(NAME_TAU), 0);
      
      for (Event f : es.directConflict.get(e)) {
        for (DNode pred : f.pre) {
          es.setDependency((Event)pred, preNew);
        }
        es.setDependency(preNew, f);
      }
      es.setDirectConflict(e, preNew);
      newEvents.add(preNew);
    }
    
    for (Event preNew : newEvents)
      es.add(preNew);
    
  }
  
  public void makeEventsSimilar() {
    
    Map<Short,Set<Short>> largestPre = new HashMap<Short, Set<Short>>();
    
    for (Event e : es.getAllEvents()) {
      
      Set<Short> pre_ids = new HashSet<Short>();
      if (e.pre != null) for (DNode pre : e.pre) pre_ids.add(pre.id);
      if (!largestPre.containsKey(e.id)) largestPre.put(e.id, pre_ids);
      if (largestPre.get(e.id).size() < pre_ids.size()) largestPre.put(e.id, pre_ids);
    }
    
    for (Event e : es.getAllEvents()) {
      
      if (e.id == nameToID.get(NAME_TAU)) continue;
      
      if (e.pre != null) {
        
        int to_add = largestPre.get(e.id).size() - e.pre.length;
            
        if (to_add > 0) {
          
          Set<Event> e_preds = es.getPrimeConfiguration(e, false);
          
          nextmissing: for (Short pre_missing : largestPre.get(e.id)) {
            for (DNode having : e.pre) if (having.id == pre_missing) continue nextmissing;
            
            System.out.println(e+" misses "+properNames[pre_missing]);
            
            boolean found = false;
            
            
            // look in the predecessors of 'e' for an event that has the same id as the missing one
            for (Event e_pred : e_preds) {
              if (e_pred.id == pre_missing) {
                // yes, add a direct dependency
System.out.println("add transitive "+e_pred+" --> "+e);
                es.setDependency(e_pred, e);
                found = true;
                break;
              }
            }
            
            if (!found) {
              pred_conflicts: for (Event e_pred : e_preds) {
                if (es.directConflict.containsKey(e_pred) && !e.hasPred(e_pred))
                {
                  for (Event e_confl : es.directConflict.get(e_pred)) {
                    if (e_confl.id == pre_missing) {

System.out.println("add transitive "+e_pred+" --> "+e);
                      
                      es.setDependency(e_pred, e);
                      found = true;
                      break pred_conflicts;
                    }
                  }
                }
              }
              
            }
            
            if (!found) {
              for (Event e_pred : e_preds) {
                if (!e.hasPred(e_pred))
                {
System.out.println("add transitive "+e_pred+" --> "+e);
                  es.setDependency(e_pred, e);
                  found = true;
                  //break;
                }
              }
              
            }

            
            if (found) {
              to_add--;
              
              // we added a transitive edge to 'e', 'e' inherits all conflicts to make sure
              // that the new transitive edges align with the transitive conflicts
              // when translating the event structure to a branching process
              for (Event e_pred : e_preds) {
                if (es.directConflict.containsKey(e_pred)) {
                  for (Event e_confl : es.directConflict.get(e_pred)) {
                    //es.setDirectConflict(e, e_confl);
                  }
                }
              }
              
            }
            if (to_add == 0) break;
          }
        }
        
        if (to_add > 0) System.out.println("could not generate "+to_add+" depdencies for "+e);
      }
    }
  }
  
  public void addAllPredecessors() {
    
    Map<Short,Set<Short>> largestPre = new HashMap<Short, Set<Short>>();
    
    for (Event e : es.getAllEvents()) {
      
      Set<Short> pre_ids = new HashSet<Short>();
      if (e.pre != null) for (DNode pre : e.pre) pre_ids.add(pre.id);
      if (!largestPre.containsKey(e.id)) largestPre.put(e.id, pre_ids);
      if (largestPre.get(e.id).size() < pre_ids.size()) largestPre.put(e.id, pre_ids);
    }
    
    for (Event e : es.getAllEvents()) {
      
      //if (e.id == nameToID.get(NAME_TAU)) continue;
      
      if (e.pre != null) {
        
        int to_add = largestPre.get(e.id).size() - e.pre.length;
        
        if (to_add > 0) {
        
          Set<Event> e_preds = es.getPrimeConfiguration(e, false);
          for (Event e_pred : e_preds) {
            if (!e.hasPred(e_pred))
            {
  System.out.println("add transitive "+e_pred+" --> "+e);
              es.setDependency(e_pred, e);
            }
          }
        }
      }
    }
  }
  
  public void materializeTransitiveConflictsForTransitiveEdges() {
    for (Event e : es.allEvents) {
      if (e.post == null) continue;
      for (int i=0; i<e.post.length; i++) {
        for (int j=i+1; j<e.post.length; j++) {
          
          if (es.inConflict((Event)e.post[i], (Event)e.post[j])) {
            es.setDirectConflict((Event)e.post[i], (Event)e.post[j]);
          }
        }
      }
    }
  }
  
  public PetriNet replay(List<String[]> allTraces, String tempOutputFile) throws IOException {
    
    EventStructureMiner_BP replay = this;
    
    move_on_log = net.addPlace("move_on_log");
    
    Uma.out.println("build up event structure");
    int tNum = 0;
    for (String[] trace : allTraces) {
      tNum++;
      replay.extendByTrace(trace);
      
      System.out.print(".");
      if (tNum % 60 == 0) System.out.println(tNum);
    }
    Uma.out.println(replay.es.allEvents.size()+" events");

    Uma.out.println("remove transitive dependencies");
    replay.es.removeTransitiveDependencies();
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es.dot");
    
    // -- not needed when conflicts are properly set during trace construction
    //Uma.out.println("refining conflicts");
    //replay.refineConflicts();
    Uma.out.println("reduce transitive conflicts");
    while (replay.es.reduceTransitiveConflicts()) {};
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es2.dot");

    Uma.out.println("set single events");
    replay.setSingleEvents();
    Uma.out.println("identify ordering relations");
    replay.getConcurrentEvents();
    Uma.out.println("maximizing concurrency");
    replay.extendConcurrency();
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es2b.dot");

    Uma.out.println("reducing transitive conflicts");
    replay.es.reduceTransitiveConflicts();
    Uma.out.println("reducing transitive dependencies");
    replay.es.removeTransitiveDependencies();
    Uma.out.println("removing tau events");
    replay.removeSuperFluousTauEvents();
    //Uma.out.println("refining conflicts");
    //replay.refineConflicts();
    Uma.out.println("reducing transitive conflicts");
    while (replay.es.reduceTransitiveConflicts()) {};
    Uma.out.println("reducing transitive dependencies");
    replay.es.removeTransitiveDependencies();
    
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es3.dot");
    
    Uma.out.println("folding forward");
    replay.foldForward();
    //replay.removeSuperFluousTauEvents();
    //replay.refineConflicts();
    Uma.out.println("reducing transitive conflicts");
    while (replay.es.reduceTransitiveConflicts()) {};
    Uma.out.println("reducing transitive dependencies");
    replay.es.removeTransitiveDependencies();
    
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es4.dot");
    
    for (Event e : replay.es.allEvents) {
      Set<DNode> ev = e.getAllPredecessors();
      for (DNode e2 : ev) {
        if (replay.es.inConflict(e, (Event)e2)) {
          System.err.println("Error4: "+e+" #  "+e2);
        }
        if (replay.es.inDirectConflict(e, (Event)e2)) {
          System.err.println("Error4: "+e+" #! "+e2);
          
          System.out.println("FIXING "+e+" #! "+e2);
          replay.es.directConflict.get(e).remove((Event)e2);
          replay.es.directConflict.get((Event)e2).remove(e);
          
          if (replay.es.directConflict.get(e).size() == 0) replay.es.directConflict.remove(e);
          if (replay.es.directConflict.get(e2).size() == 0) replay.es.directConflict.remove(e2);
        }
        
      }
    }
    
    
    Uma.out.println("reducing transitive conflicts");
    while (replay.es.reduceTransitiveConflicts()) {};
    Uma.out.println("localizing conflicts");
    replay.makeConflictFansLocal();
    //replay.removeSuperFluousTauEvents();
    //replay.refineConflicts();
    Uma.out.println("reducing transitive conflicts");
    while (replay.es.reduceTransitiveConflicts()) {};
    Uma.out.println("reducing transitive dependencies");
    //replay.es.removeTransitiveDependencies();
    
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es5.dot");
    
    for (Event e : replay.es.allEvents) {
      Set<DNode> ev = e.getAllPredecessors();
      for (DNode e2 : ev) {
        if (replay.es.inConflict(e, (Event)e2)) {
          System.err.println("Error5: "+e+" #  "+e2);
        }
        if (replay.es.inDirectConflict(e, (Event)e2)) {
          System.err.println("Error5: "+e+" #! "+e2);
          System.err.println(e.getAllPredecessors());
          System.err.println(replay.es.getPrimeConfiguration(e, true));
        }
      }
    }
    
                        ///// <<<<<<<<<<<<-----------------
    boolean useVariant2 = true;    ///// <<<<<<<<<<<<-----------------
                        ///// <<<<<<<<<<<<-----------------
                        ///// <<<<<<<<<<<<-----------------
    
    if (useVariant2) {
      System.out.println("find future equivalence on events");
      replay.es.foldBackwards();
      System.out.println("refining by conflicts");
      replay.es.refineFoldingByConflicts();
      
    } else {
    
      Uma.out.println("making events similar");
      //replay.makeEventsSimilar();
      replay.addAllPredecessors();
      Uma.out.println("materialize transitive conflicts for transitive edges");
      //replay.materializeTransitiveConflictsForTransitiveEdges();
    }
    
    writeFile(replay.es.toDot(replay.properNames), tempOutputFile+"_es6.dot");
    
    PetriNet net;
    Uma.out.println("synthesizing Petri net from "+replay.es.allEvents.size()+" events");
    if (useVariant2) {
      SynthesisFromES2 synth = new SynthesisFromES2(replay.properNames, tempOutputFile);
      net = synth.synthesize(replay.es);
    } else {
      SynthesisFromES synth = new SynthesisFromES(replay.properNames, tempOutputFile);
      net = synth.synthesize(replay.es);
    }
    
    for (Transition t : net.getTransitions()) {
      if (t.getName().startsWith("tau_")) {
        t.setName("tau");
      }
    }

    return net;
  }
  
  public static void main(String args[]) throws Throwable {
    
    //String fileName_system_sysPath = args[0];
    //String fileName_trace = args[1];
    
    String fileName_system_sysPath = "./examples/discovery_from_log/a22f0n00_20.lola";
    String fileName_trace  = "./examples/discovery_from_log/a22f0n00_20.log.txt";

    //String fileName_system_sysPath = "./examples/model_correction/a12f0n00_alpha.lola";
    //String fileName_trace  = "./examples/model_correction/a12f0n05_aligned_to_00.log.txt";
    
    //String fileName_system_sysPath = "./examples/model_correction/a12f0n05_alpha.lola";
    //String fileName_trace  = "./examples/model_correction/a12f0n05_aligned.log_20.txt";
    //String fileName_trace  = "./examples/model_correction/a12f0n05_aligned.log.txt";
    
    //String fileName_system_sysPath = "./examples/model_correction/a22f0n00.lola";
    //String fileName_trace  = "./examples/model_correction/a22f0n05.log_100.txt";
    //String fileName_trace  = "./examples/model_correction/a22f0n05.log.txt";

    //String fileName_system_sysPath = "./examples/model_correction2/wabo1.lola";
    //String fileName_trace  = "./examples/model_correction2/wabo1.log.txt";
    

    List<String[]> allTraces = ViewGeneration2.readTraces(fileName_trace);
    
    Set<String> transitionNames = new HashSet<String>();
    for (String[] trace : allTraces) {
      for (String a : trace) transitionNames.add(a);
    }
    
    
    PetriNet sysModel;
    
    boolean doMining = true;
    if (doMining) {
      // mining: create a flower model and do generalization from there
      sysModel = new PetriNet();
      Place p = sysModel.addPlace("central");
      p.setTokens(1);
      for (String a : transitionNames) {
        Transition t = sysModel.addTransition(a);
        sysModel.addArc(p, t);
        sysModel.addArc(t, p);
      }
    } else {
      // repair: read given net and do generalization from there
      sysModel = PetriNetIO.readNetFromFile(fileName_system_sysPath);
    }
    
    
    EventStructureMiner_BP replay = new EventStructureMiner_BP(sysModel);
    PetriNet net = replay.replay(allTraces, fileName_system_sysPath);
    
    //ImplicitPlaces.findImplicitPlaces(net);
    
    PetriNetIO_Out.writeToFile(net, fileName_system_sysPath+"_mine_bp", PetriNetIO_Out.FORMAT_DOT, 0);
    PetriNetIO_Out.writeToFile(net, fileName_system_sysPath+"_mine_bp", PetriNetIO_Out.FORMAT_LOLA, 0);
  }

  public static void writeFile(String s, String fileName) throws IOException {

    // Create file 
    FileWriter fstream = new FileWriter(fileName);
    BufferedWriter out = new BufferedWriter(fstream);
    
    out.write(s);

    //Close the output stream
    out.close();
  }
}
