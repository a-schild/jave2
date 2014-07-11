package it.sauronsoftware.jave;

/**
 * Created with IntelliJ IDEA.
 * User: jgiotta
 * Date: 8/31/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class VideoFilter {
    private String expression;
    public VideoFilter(String expression) {
        this.expression = expression;
    }

    public String getExpression () {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString () {
        return this.expression;
    }
}
