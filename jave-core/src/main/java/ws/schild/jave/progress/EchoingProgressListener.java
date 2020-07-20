package ws.schild.jave.progress;

import java.io.PrintStream;

import ws.schild.jave.info.MultimediaInfo;

/**
 * Simple class to echo progress to Standard out - or any PrintStream.
 * @author mressler
 *
 */
public class EchoingProgressListener implements EncoderProgressListener {

	private String prefix;
	private PrintStream out;
	
	public EchoingProgressListener() {
		out = System.out;
		prefix = "";
	}
	public EchoingProgressListener(String prefix) {
		this();
		this.prefix = prefix;
	}
	public EchoingProgressListener(String prefix, PrintStream out) {
		this(prefix);
		this.out = out;
	}
	
	@Override
	public void sourceInfo(MultimediaInfo info) {
		out.println(prefix + " Info Received: " + info);
	}

	@Override
	public void progress(int permil) {
		out.println(prefix + " Progress Received: " + permil);
	}

	@Override
	public void message(String message) {
		out.println(prefix + " Message Received: " + message);
	}

}
