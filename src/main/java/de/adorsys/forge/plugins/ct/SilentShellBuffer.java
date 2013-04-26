package de.adorsys.forge.plugins.ct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellColor;

import com.google.inject.Inject;

import de.adorsys.forge.plugins.ct.SilentShellBuffer.SilentShellBufferQualifier;

/**
 * Buffers Output, prints nothing to the console
 * @author Florian Hirsch - adorsys
 */
@SilentShellBufferQualifier
public class SilentShellBuffer implements ShellBuffer {

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
	
	@Override
	public String getContent() {
		return sb.toString();
	}
	
	@Override
	public void reset() {
		sb.delete(0, sb.length());
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
	@Qualifier
	public @interface SilentShellBufferQualifier {}
	
}
