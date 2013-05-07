package com.a9ski.maven.webapp.exec;

import java.io.Serializable;
import java.nio.charset.Charset;

public class ExecutionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6530701940184939146L;

	private static final Charset ENCODING = Charset.defaultCharset();

	private final int exitCode;
	private final byte[] stdOut;
	private final byte[] stdErr;
	private final String[] cmds;

	public ExecutionResult(final int exitCode, final byte[] stdOut, final byte[] stdErr, final String[] cmds) {
		super();
		this.exitCode = exitCode;
		this.stdOut = stdOut;
		this.stdErr = stdErr;
		this.cmds = cmds;
	}

	public int getExitCode() {
		return exitCode;
	}

	public byte[] getStdOutBytes() {
		return stdOut;
	}

	public byte[] getStdErrBytes() {
		return stdErr;
	}

	public String getStdErr() {
		return new String(stdErr, ENCODING);
	}

	public String getStdOut() {
		return new String(stdOut, ENCODING);
	}

	public String[] getCommands() {
		return cmds;
	}
}
