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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Helps getting the interesting information from the maven outputs
 * @author Florian Hirsch - adorsys
 */
public class MvnResultParser {

	private static final Pattern LINE_SPLITTER = Pattern.compile("[\r\n]+");
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
		
	// Some maven Strings
	private static final java.lang.String FINISHED = "Finished at:";
	private static final java.lang.String ERROR = "[ERROR]";
	private static final java.lang.String TEST_FAIL = "testFail";
	private static final java.lang.String TESTS_RUN = "Tests run:";
	private static final java.lang.String TESTS_IN_ERROR = "Tests in error:";
	private static final java.lang.String FAILED_TESTS = "Failed tests:";
	private static final java.lang.String COMPILATION_ERROR = "COMPILATION ERROR";
	private static final java.lang.String BUILD_FAILURE = "BUILD FAILURE";
 	
	private String content;
	
	private List<String> lines;
	
	public MvnResultParser(String content) {
		if (content == null || "".equals(content.trim())) {
			throw new IllegalStateException("MvnResult is empty!");
		}
		this.content = content;
		this.lines = Arrays.asList(LINE_SPLITTER.split(content));
	}
	
	/**
	 * @return build failure or cancelled by user
	 */
	public boolean isBuildFailure() {
		return content.contains(BUILD_FAILURE);
	}
	
	/**
	 * @return if compilation fails
	 */
	public boolean isCompilationFailure() {
		return content.contains(COMPILATION_ERROR);
	}
	
	/**
	 * @return if tests failed
	 */
	public boolean isTestError() {
		return content.contains(FAILED_TESTS) || content.contains(TESTS_IN_ERROR);
	}
	
	/**
	 * @return just the line with the whole test results
	 */
	public String getTestResult() {
		List<String> results = grep(TESTS_RUN);
		return results.get(results.size() - 1);
	}
	

	/**
	 * @return all failed Tests
	 */
	public String grepTestErrors() {
		return linesAsString(grep(TEST_FAIL));
	}
	
	/**
	 * @return all lines marked es [ERROR]
	 */
	public String grepErrors() {
		return linesAsString(grep(ERROR));
	}
	
	/**
	 * grep lines
	 */
	private List<String> grep(String searchPhrase) {
		return grep(searchPhrase, null);
	}
	
	/**
	 * greps lines
	 * @param searchPhrase: what you want to grep
	 * @param limit: stops if line contains limit. default if null: "Finished at:"
	 */
	private List<String> grep(String searchPhrase, String limit) {
		if (searchPhrase == null || "".equals(searchPhrase.trim())) { 
			return Collections.emptyList();
		}
		if (limit == null || "".equals(limit.trim())) {
			limit = FINISHED;
		}
		List<String> result = new ArrayList<String>();
		for (String line : lines) {
			line = line.trim();
			if (line.contains(limit)) {
				break;
			}
			if (line.contains(searchPhrase)) {
				result.add(line);
			}
		}
		return result;
	}
	
	/**
	 * @return the lines as formated String
	 */
	private String linesAsString(List<String> lines) {
		if (lines == null || lines.isEmpty()) {
			return "";
		}
		StringBuilder out = new StringBuilder();
		for (String line : lines) {
			out.append(line).append(LINE_SEPARATOR);
		}
		return out.toString().substring(0, out.length() - 1);
	}

}
