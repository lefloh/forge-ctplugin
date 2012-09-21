package de.adorsys.forge.ct;

import javax.enterprise.inject.Alternative;

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellColor;
import org.jboss.forge.shell.ShellPrintWriter;

import com.google.inject.Inject;

/**
 * Buffers Output, prints nothing to the console
 * @author Florian Hirsch - adorsys
 */
@Alternative
public class ShellBuffer implements ShellPrintWriter {

	@Inject
	private Shell shell;
	
	private StringBuilder sb = new StringBuilder();
	
	@Override
	public void write(int b) {
		sb.append(b);
	}

	@Override
	public void write(byte b) {
		sb.append((char) b);
	}

	@Override
	public void write(byte[] b) {
		sb.append(new String(b));
	}

	@Override
	public void write(byte[] b, int offset, int length) {
		sb.append(new String(b, offset, length));
	}

	@Override
	public void print(String output) {
		sb.append(output);
	}

	@Override
	public void println(String output) {
		sb.append(output).append('\n');
	}

	@Override
	public void println() {
		sb.append('\n');
	}

	@Override
	public void print(ShellColor color, String output) {
		sb.append(renderColor(color, output));
	}

	@Override
	public void println(ShellColor color, String output) {
		sb.append(renderColor(color, output)).append('\n');
	}

	@Override
	public String renderColor(ShellColor color, String output) {
		return shell.renderColor(color, output);
	}

	@Override
	public void flush() {
		// Don't flush!
	}
	
	/**
	 * gets the bufferd content
	 * @return
	 */
	public String getContent() {
		return sb.toString();
	}
	
}
