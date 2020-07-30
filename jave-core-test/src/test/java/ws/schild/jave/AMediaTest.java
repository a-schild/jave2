/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import java.io.File;

/** @author a.schild */
public abstract class AMediaTest {
  private final String resourceSourcePath;
  private final String resourceTargetPath;

  /**
   * @param sourcePart
   * @param targetPart
   */
  public AMediaTest(String sourcePart, String targetPart) {
    if (sourcePart == null) {
      resourceSourcePath = "src/test/resources/";
    } else {
      if (sourcePart.endsWith("/")) {
        resourceSourcePath = "src/test/resources/" + sourcePart;
      } else {
        resourceSourcePath = "src/test/resources/" + sourcePart + "/";
      }
    }
    new File(resourceSourcePath).mkdirs();
    if (targetPart == null) {
      resourceTargetPath = "target/testoutput/";
    } else {
      if (targetPart.endsWith("/")) {
        resourceTargetPath = "target/testoutput/" + targetPart;
      } else {
        resourceTargetPath = "target/testoutput/" + targetPart + "/";
      }
    }
    new File(resourceTargetPath).mkdirs();
  }

  public String getResourceSourcePath() {
    return resourceSourcePath;
  }

  public String getResourceTargetPath() {
    return resourceTargetPath;
  }
}
