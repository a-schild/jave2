package ws.schild.jave.utils;

import java.io.File;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use this class in a try-with-resources block to automatically delete the referenced file when
 * this goes out of scope.
 *
 * @author mressler
 */
public class AutoRemoveableFile extends File implements AutoCloseable {

  private static final Logger logger = LoggerFactory.getLogger(AutoRemoveableFile.class);

  private static final long serialVersionUID = 1270202558229293283L;

  public AutoRemoveableFile(File parent, String child) {
    super(parent, child);
  }

  @Override
  public void close() {
    boolean closed = delete();
    if (!closed) {
      logger.warn(
          "File "
              + getAbsolutePath()
              + " did not automatically delete itself: "
              + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
  }
}
