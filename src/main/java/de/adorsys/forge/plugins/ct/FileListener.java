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

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * Delegates FileChanges to the TestExecutor
 * @author Florian Hirsch - adorsys
 */
public class FileListener implements FileAlterationListener {

	@Inject
	private TestExecutor executor;
	
	@Override
	public void onStart(FileAlterationObserver observer) {
		// ignore
	}

	@Override
	public void onDirectoryCreate(File directory) {
		executor.execute(directory);
	}

	@Override
	public void onDirectoryChange(File directory) {
		executor.execute(directory);
	}

	@Override
	public void onDirectoryDelete(File directory) {
		executor.execute(directory);
	}

	@Override
	public void onFileCreate(File file) {
		executor.execute(file);
	}

	@Override
	public void onFileChange(File file) {
		executor.execute(file);
	}

	@Override
	public void onFileDelete(File file) {
		executor.execute(file);
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		// ignore
	}

}
