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

/**
 * Expresses that the input model for the verification is invalid.
 *  
 * @author Dirk Fahland
 */
public class InvalidModelException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 8091384676313192979L;

  public static final int EMPTY_PRESET = 1;
  public static final int EMPTY_POSTSET = 2;
  public static final int OCLET_NO_CAUSALNET = 3;
  public static final int OCLET_HISTORY_NOT_PREFIX = 4;
  public static final int OCLET_HISTORY_INCOMPLETE_PREFIX = 5;
  public static final int NO_INITIAL_STATE = 6;
  public static final int UNKNOWN_MODEL_TYPE = 7;
  
  private Object cause;
  private int reason;
  
  public InvalidModelException(int reason, Object e, String text) {
    super(text);
    this.reason = reason;
    this.cause = e;
  }
  
  public InvalidModelException(int reason, Object e) {
    super(printReason(reason));
    this.reason = reason;
    this.cause = e;
  }
  
  public Object getCausingObject() {
    return cause;
  }
  
  public int getReason() {
    return reason;
  }
  
  public static String printReason(int reason) {
    switch (reason) {
    case EMPTY_PRESET: return "Found transition with empty pre-set";
    case EMPTY_POSTSET: return "Found transition with empty post-set";
    case OCLET_NO_CAUSALNET: return "Oclet is not a causal net";
    case OCLET_HISTORY_NOT_PREFIX: return "Prefix of oclet is not a history";
    case OCLET_HISTORY_INCOMPLETE_PREFIX: return "Prefix of oclet is incomplete";
    case NO_INITIAL_STATE: return "System has no initial state";
    case UNKNOWN_MODEL_TYPE: return "Unknown model type";
    }
    return "";
  }
}
