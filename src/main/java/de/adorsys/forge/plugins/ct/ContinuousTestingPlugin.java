/**
 * Copyright (C) 2012 Florian Hirsch fhi@adorsys.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.adorsys.forge.plugins.ct;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.io.monitor.FileAlterationObserver;
import org.jboss.forge.project.Project;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrintWriter;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;

/**
 * This Plugin continuously builds and tests your Maven-Project
 * @author Florian Hirsch - adorsys
 */
@Alias("ct")
@Help("Continuous build and test your project")
public class ContinuousTestingPlugin implements Plugin {

	boolean shouldExit = false;
	
	@Inject
	private Shell shell;
	
	@Inject
	private Project project;
	
	@Inject
	private FileListener listener;
	
	@DefaultCommand
	public void defaultCommand(final PipeOut out) {
		out.println("try ct run [no params]");
	}
	
	@Command(help = "listens to file-changes in project-root/src and triggers mvn test")
	public void run(final PipeOut out) {
		
		String sourceDir = project.getProjectRoot().getFullyQualifiedName() + "/src";
		
		if (!new File(sourceDir).exists()) {
			ShellMessages.error(out, "SourceDirectory " + sourceDir + " is missing!");
		}
		
		final FileAlterationObserver observer = new FileAlterationObserver(sourceDir);
		observer.addListener(listener);
		
		try {
			observer.initialize();
		} catch (Exception ex) {
			handleError(out, ex);
		}
		
		ShellMessages.info(out, "Monitoring " + sourceDir);
		ShellMessages.info(out, "Cancel with CTRL+C");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!shouldExit) {
					observer.checkAndNotify();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						handleError(out, ex);
					}
				}
			}
		}).start();

		if (shell.scan() == 10) {
			shouldExit = true;
		}

	}	
	
	private void handleError(ShellPrintWriter out, Exception ex) {
		ShellMessages.error(out, ex.getClass().getSimpleName() + ":\n" + ex.getMessage());
	}
}
