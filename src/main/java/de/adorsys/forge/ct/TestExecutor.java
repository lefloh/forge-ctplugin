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
package de.adorsys.forge.ct;

import java.io.File;

import javax.inject.Inject;

import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrintWriter;

import de.adorsys.forge.ct.SilentShellBuffer.SilentShellBufferQualifier;

/**
 * Executes mvn test and prints the result
 * Note: we just execute mvn test so if your source-classes are compiled by your IDE
 * your tests will fail with unresolved compilation problems. You should see them in your IDE ;)
 * @author Florian Hirsch - adorsys
 */
public class TestExecutor {

	@Inject
	private Project project;

	@Inject
	private ShellPrintWriter out;
	
	@Inject
	@SilentShellBufferQualifier
	private ShellBuffer buffer;
	
	public boolean execute(File file) {
		
		ShellMessages.info(out, "file changed: " + file.getAbsolutePath());
		ShellMessages.info(out, "building...");
		
		MavenCoreFacet mvnFacet = project.getFacet(MavenCoreFacet.class);
		boolean success = mvnFacet.executeMaven(buffer, new String[] {"test"});
	
		MvnResultParser parser = new MvnResultParser(buffer.getContent());		
		buffer.reset();
		
		if (success) {
			ShellMessages.success(out, "build and test successful");
			ShellMessages.success(out, parser.getTestResult());
			return nextTest();
		}
		
		if (!parser.isBuildFailure()) {
			ShellMessages.info(out, "build cancelled");
			return nextTest();
		}
		
		if (parser.isCompilationFailure()) {
			ShellMessages.error(out, "Compilation Error");
			ShellMessages.error(out, parser.grepErrors());
			return nextTest();
		}
		
		if (parser.isTestError()) {
			ShellMessages.error(out, "Test Error");
			ShellMessages.error(out, parser.grepTestErrors());
			ShellMessages.error(out, parser.getTestResult());
			return nextTest();
		}
		
		ShellMessages.error(out, "Unknown Error");
		ShellMessages.error(out, parser.grepErrors());
		return false;
		
	}
	
	private boolean nextTest() {
		out.println();
		ShellMessages.info(out, "waiting for file-changes...");
		out.println();
		return true;
	}
	
}
