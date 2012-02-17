package us.kirchmeier.tools;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.StringUtils;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This code is a stripped down version of
 * {@link org.apache.tools.ant.DefaultLogger}
 */
public class QuietLogger implements BuildLogger {
	protected PrintStream out;
	protected PrintStream err;
	protected int msgOutputLevel = Project.MSG_ERR;
	protected static final String lSep = System.getProperty("line.separator");

	@Override
	public void setMessageOutputLevel(int level) {
		/* Downgrade Error Levels, unless Debug.*/
		switch(level){
		case Project.MSG_ERR:
		case Project.MSG_WARN:
			this.msgOutputLevel = Project.MSG_ERR;
		case Project.MSG_INFO:
			this.msgOutputLevel = Project.MSG_WARN;
		case Project.MSG_DEBUG:
		default:
			this.msgOutputLevel = level;
		}
	}

	@Override
	public void setOutputPrintStream(PrintStream output) {
		this.out = new PrintStream(output, true);
	}

	@Override
	public void setErrorPrintStream(PrintStream err) {
		this.err = new PrintStream(err, true);
	}

	@Override
	public void setEmacsMode(boolean emacsMode) {
	}

	@Override
	public void buildStarted(BuildEvent event) {
	}

	@Override
	public void buildFinished(BuildEvent event) {
		Throwable error = event.getException();
		StringBuilder message = new StringBuilder();
		if (error != null) {
			message.append(lSep);
			message.append("BUILD FAILED");
			message.append(lSep);
			throwableMessage(message, error);
		}
		message.append(lSep);
		message.append("Build Completed at ");
		message.append(new SimpleDateFormat("h:mm:ss aa").format(new Date()));

		(error == null ? out : err).println(message.toString());
	}

	@Override
	public void targetStarted(BuildEvent event) {
	}

	@Override
	public void targetFinished(BuildEvent event) {
	}

	@Override
	public void taskStarted(BuildEvent event) {
	}

	@Override
	public void taskFinished(BuildEvent event) {
	}

	@Override
	public void messageLogged(BuildEvent event) {
		int priority = event.getPriority();
		if (priority <= msgOutputLevel) {
			StringBuilder message = new StringBuilder();
			message.append(event.getMessage());

			Throwable ex = event.getException();
			if (Project.MSG_DEBUG <= msgOutputLevel && ex != null) {
				message.append(StringUtils.getStackTrace(ex));
			}

			(priority == Project.MSG_ERR ? err : out).println(message.toString());
		}
	}

	protected void throwableMessage(StringBuilder m, Throwable error) {
		while (error instanceof BuildException) {
			Throwable cause = error.getCause();
			if (cause == null) {
				break;
			}
			String msg1 = error.toString();
			String msg2 = cause.toString();
			if (msg1.endsWith(msg2)) {
				m.append(msg1.substring(0, msg1.length() - msg2.length()));
				error = cause;
			} else {
				break;
			}
		}
		if (Project.MSG_VERBOSE <= msgOutputLevel || !(error instanceof BuildException)) {
			m.append(StringUtils.getStackTrace(error));
		} else {
			m.append(error).append(lSep);
		}
	}
}
