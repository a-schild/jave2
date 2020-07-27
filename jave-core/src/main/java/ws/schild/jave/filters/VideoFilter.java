package ws.schild.jave.filters;

/**
 * @author jgiotta
 */
public interface VideoFilter {

	/**
	 * The expression to be used in the video filter argument to ffmpeg
	 * @return
	 */
    public String getExpression();
    
}
