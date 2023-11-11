package ws.schild.jave.encode;

/**
 * The type of arguments you can provide to ffmpeg. The naming comes from `ffmpeg -help`
 *
 * @author mressler
 */
public enum ArgType {
    /** Global argument for ffmpeg */
    GLOBAL,
    /** Arguments for input file(s) */
    INFILE,
    /** Argument for output file(s) */
    OUTFILE;
}
