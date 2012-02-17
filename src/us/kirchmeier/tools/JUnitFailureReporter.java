package us.kirchmeier.tools;

import junit.framework.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class JUnitFailureReporter implements JUnitResultFormatter {
	private final PrintStream results;
	private final ByteArrayOutputStream resultStream;
	private OutputStream output;

	public JUnitFailureReporter() {
		resultStream = new ByteArrayOutputStream();
		results = new PrintStream(resultStream);
	}

	@Override
	public void setOutput(OutputStream out) {
		output = out;
	}

	@Override
	public void startTestSuite(JUnitTest suite) throws BuildException {
		results.println("--------------------------");
		results.println("Class : " + suite.getName());
	}

	@Override
	public void addFailure(Test test, AssertionFailedError error) {
		results.println("FAILED: " + formatTestName(test));
		results.println(JUnitTestRunner.getFilteredTrace(error));
	}

	@Override
	public void addError(Test test, Throwable error) {
		results.println("ERROR : " + formatTestName(test));
		results.println(JUnitTestRunner.getFilteredTrace(error));
	}

	@Override
	public void endTestSuite(JUnitTest suite) throws BuildException {
		if (output == null) return;

		try {
			long errors = suite.failureCount() + suite.errorCount();
			if (0 < errors) {
				results.flush();
				output.write(resultStream.toByteArray());
			}

			if (output != System.out && output != System.err) {
				output.close();
			}
		} catch (IOException e) {
			throw new BuildException("I/O Exception");
		}
	}

	private String formatTestName(Test test) {
		if (test == null)
			return "NULL";

		String s = test.toString();
		//Remove the package from the name
		return s.replaceFirst("\\(.*", "");
	}

	@Override
	public void setSystemOutput(String out) throws BuildException {
		//ignore
	}

	@Override
	public void setSystemError(String err) throws BuildException {
		//ignore
	}

	@Override
	public void startTest(Test test) {
		//ignore
	}

	@Override
	public void endTest(Test test) {
		//ignore
	}
}
