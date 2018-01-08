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

package hub.top.uma.check;

import java.io.IOException;

import com.google.gwt.dev.util.collect.HashSet;

import hub.top.petrinet.ISystemModel;
import hub.top.uma.DNode;
import hub.top.uma.DNodeBP;
import hub.top.uma.DNodeSys;
import hub.top.uma.InvalidModelException;
import hub.top.uma.Options;
import hub.top.uma.Uma;

public class QuasiLive {

  ISystemModel  sys;
  DNodeSys      sysModel;
  DNodeBP       build;
  
  HashSet<Short>  eventsToFire;
  
  
  public QuasiLive(String modelFileName) throws IOException, InvalidModelException {
    sys = Uma.readSystemFromFile(modelFileName);
    sysModel = Uma.getBehavioralSystemModel(sys);
    eventsToFire = new HashSet<Short>();
    for (DNode e : sysModel.fireableEvents) {
      eventsToFire.add(e.id);
    }
  }
  
  public boolean check() {
    
    Options o = new Options(sysModel);
    o.configure_buildOnly();
    build = new DNodeBP(sysModel, o);
    
    while (!eventsToFire.isEmpty()) {
      short to_fire = eventsToFire.iterator().next();
      
      System.out.println("checking for "+sysModel.uniqueNames[to_fire]);
      
      o.configure_checkExecutable(to_fire);
      
      int step = 0;
      int old_size = build.getBranchingProcess().allEvents.size();
      while ((step = build.step()) > 0) {
        Uma.out.println(step+".. ");
      }
      
      for (DNode e : build.getBranchingProcess().allEvents) {
        eventsToFire.remove(e.id);
      }
      
      // prefix not extended, all events seen
      if (build.getBranchingProcess().allEvents.size() == old_size)
        break;
      
      build.resetPropertyCheck();
    }
    
    if (!eventsToFire.isEmpty()) {
      System.out.println("non-fired events:");
      for (Short id : eventsToFire) {
        System.out.println("  "+sysModel.uniqueNames[id]);
      }
    }
    
    return eventsToFire.isEmpty();
  }
  
  public static void main(String[] args) {
    if (args.length == 0) return;
    
    try {
      QuasiLive ql = new QuasiLive(args[0]);
      if (ql.check() == true)
        System.out.println("System is quasi-live");
      else
        System.out.println("System is not quasi-live");
      
    } catch (IOException e) {
      System.err.println("Error reading file: "+args[0]);
      System.err.println(e);
    } catch (InvalidModelException e) {
      System.err.println("Invalid system model.");
      System.err.println(e);
    }
  }
}
