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

package hub.top.test;

public class TestCase extends junit.framework.TestCase {
  
  public TestCase (String name) {
    super();
    setName(name);
  }
  
  public static String testFileRoot = System.getProperty("test.testFileRoot", "./tests/testfiles/");
  
  public static String lastTest = "";
  public int testNum = 0;
  public int testFail = 0;
  
  private static final String RESULT_FAIL = "[failed]";
  private static final String RESULT_OK = "[ok]";
  
  public void assertTrue2(boolean result) {
    assertEquals2(result, true);
  }
  
  public void assertFalse2(boolean result) {
    assertEquals2(result, false);
  }
  
  public void assertEquals2(Object result, Object expected) {
    String testMessage = lastTest;
    String resultMessage = result+", expected: "+expected;
    boolean resultMatch = result.equals(expected);
    String resultString = (resultMatch ? RESULT_OK : RESULT_FAIL);
    
    int fill = 77 - (testMessage.length() + resultMessage.length() + resultString.length());
    String fillString1 = "";
    String fillString2 = "";
    if (fill >= 1) {
      fillString1 = ": ";
      for (int i=0; i < fill; i++) fillString2 += " ";
    } else {
      fillString1 += ":\n  ";
      fill = 77 - (resultMessage.length() + resultString.length());
      for (int i=0; i < fill; i++) fillString2 += " ";
    }
    System.out.println(testMessage+fillString1+resultMessage+fillString2+resultString);
    
    testNum++;
    if (!resultMatch) testFail++;
  }
  
  public void printHeader() {
    int fill = 79 - (5 + getName().length());
    String header = "=== "+getName()+" ";
    for (int i=0;i<fill;i++) header += "=";
    
    System.out.println(header);
  }
  
  public void evaluateTests() {
    System.out.println("-------------------------------------------------------------------------------");
    System.out.println(" executed "+testNum+" test cases, failed: "+testFail);
    System.out.println("===============================================================================\n");
  }
  
  public static void setParameters(String args[]) {
    if (args.length > 0) testFileRoot = args[0];
  }
}
