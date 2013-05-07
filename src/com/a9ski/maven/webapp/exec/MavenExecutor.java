package com.a9ski.maven.webapp.exec;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;

public class MavenExecutor extends ProcessExecutor {

	public MavenExecutor() {

	}

	@Override
	public ExecutionResult execute(final File workDir, final String stdInStr, final String... cmds) throws IOException, InterruptedException {
		return super.execute(workDir, stdInStr, addMvnToCmd(cmds));
	}

	private String[] addMvnToCmd(final String[] cmds) {
		final String[] newCmds;
		if (SystemUtils.IS_OS_WINDOWS) {
			newCmds = new String[cmds.length + 3];
			newCmds[0] = "cmd.exe";
			newCmds[1] = "/C";
			newCmds[2] = "mvn";
			System.arraycopy(cmds, 0, newCmds, 3, cmds.length);
		} else {
			newCmds = new String[cmds.length + 1];
			newCmds[0] = "mvn";
			System.arraycopy(cmds, 0, newCmds, 1, cmds.length);
		}
		return newCmds;
	}
}
