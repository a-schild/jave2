/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import org.junit.jupiter.api.Test;

import ws.schild.jave.utils.Utils;

import static org.junit.jupiter.api.Assertions.*;

/** @author a.schild */
public class UtilsTest {

  public UtilsTest() {}

  @Test
  public void testBuildTimeDuration00() {
    System.out.println("buildTimeDuration00");
    long millis = 0L;
    String expResult = "00";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration01() {
    System.out.println("buildTimeDuration01");
    long millis = -1L;
    String expResult = "-00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration02() {
    System.out.println("buildTimeDuration02");
    long millis = 1L;
    String expResult = "00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration03() {
    System.out.println("buildTimeDuration03");
    long millis = 1000L;
    String expResult = "01";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration04() {
    System.out.println("buildTimeDuration04");
    long millis = 60000L;
    String expResult = "01:00";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration05() {
    System.out.println("buildTimeDuration05");
    long millis = 60001L;
    String expResult = "01:00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration06() {
    System.out.println("buildTimeDuration06");
    long millis = 3600001L;
    String expResult = "01:00:00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration07() {
    System.out.println("buildTimeDuration07");
    long millis = -3600001L;
    String expResult = "-01:00:00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }

  @Test
  public void testBuildTimeDuration08() {
    System.out.println("buildTimeDuration08");
    long millis = -72000001L;
    String expResult = "-20:00:00.001";
    String result = Utils.buildTimeDuration(millis);
    assertEquals(expResult, result);
  }
}
