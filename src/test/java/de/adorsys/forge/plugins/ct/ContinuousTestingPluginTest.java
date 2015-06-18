/**
 * Copyright (C) 2012 Florian Hirsch
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

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.project.dependencies.DependencyResolver;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import de.adorsys.forge.plugins.ct.ContinuousTestingPlugin;

/**
 * Tests the ContinuousTestingPlugin
 * @author Florian Hirsch - adorsys
 */
public class ContinuousTestingPluginTest extends AbstractShellTest {

	@Inject
	private DependencyResolver resolver;

	@Deployment
	public static JavaArchive getDeployment() {
		return AbstractShellTest.getDeployment().addPackages(true, ContinuousTestingPlugin.class.getPackage());
	}

	@Test
	public void testCt() throws Exception {
		initializeJavaProject();
		queueInputLines("y");
		getShell().execute("ct");
		getShell().execute("ct run");
	}

}
