package com.a9ski.maven.webapp.exec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.a9ski.maven.webapp.exec.pipes.Pipe;

public class ProcessExecutor {
	
	private final Executor executor = Executors.newFixedThreadPool(3);
	
	public ExecutionResult execute(final File workDir, final String stdInStr, final String... cmds) throws IOException, InterruptedException {
		final ByteArrayOutputStream stdErr = new ByteArrayOutputStream();
		final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
		//final ByteArrayInputStream stdIn = new ByteArrayInputStream(stdInStr.getBytes(Charset.forName("ISO_8859_1")))
		final ByteArrayInputStream stdIn = new ByteArrayInputStream(stdInStr.getBytes());
		
		final ProcessBuilder b = new ProcessBuilder(cmds);
		b.directory(workDir);		
		final Process p = b.start();
		executor.execute(new Pipe(p.getErrorStream(), stdErr));
		executor.execute(new Pipe(p.getInputStream(), stdOut));
		executor.execute(new Pipe(stdIn, p.getOutputStream()));
		int exitCode = p.waitFor();
		
		return new ExecutionResult(exitCode, stdOut.toByteArray(), stdErr.toByteArray(), cmds);
	}
	

}
