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
    resourceSourcePath = initialiseDirectory(sourcePart, "src/test/resources/");
    resourceTargetPath = initialiseDirectory(targetPart, "target/testoutput/");
  }

  private String initialiseDirectory(String path, String basePath) {
    String resourcePath;

    if (path == null) {
      resourcePath = basePath;
    } else if (path.endsWith("/")) {
      resourcePath = basePath + path;
    } else {
      resourcePath = basePath + path + "/";
    }

    new File(resourcePath).mkdirs();

    return resourcePath;
  }

  public String getResourceSourcePath() {
    return resourceSourcePath;
  }

  public String getResourceTargetPath() {
    return resourceTargetPath;
  }
}
