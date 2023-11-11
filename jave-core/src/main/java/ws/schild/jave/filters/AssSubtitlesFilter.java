package ws.schild.jave.filters;

import java.io.File;

/**
 * Add subtitles to the video
 * 
 * @author andre
 */
public class AssSubtitlesFilter extends Filter {

  /**
   * A simple instantiation of the <a href="https://trac.ffmpeg.org/wiki/HowToBurnSubtitlesIntoVideo">ass</a> subtitle filter.
   * @param source The source ass subtitle file to be used for this movie filter.
   */
  public AssSubtitlesFilter(File source) {
    super("ass");
    /*
     * Need escaping special characters []\':,;
     */
    addOrderedArgument(escapingPath(source.getAbsolutePath()));
  }

  /**
   * 
   * @param source The source ass subtitle file to be used for this movie filter.
   * @param outputLabel The label to add as subtitle
   */
  public AssSubtitlesFilter(File source, String outputLabel) {
    this(source);
    addOutputLabel(outputLabel);
  }

}
