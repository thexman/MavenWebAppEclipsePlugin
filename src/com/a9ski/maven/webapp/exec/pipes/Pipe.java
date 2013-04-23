package com.a9ski.maven.webapp.exec.pipes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class Pipe implements Runnable {

	private final InputStream is;
	private final OutputStream os;
	
	private IOException exception;	
	
	
	public Pipe(final InputStream is, final OutputStream os) {
		this.is = is;
		this.os = os;
	}
	
	@Override
	public void run() {		
		try {
			IOUtils.copy(is, os);
		} catch (final IOException ex) {
			exception = ex;			
		}		
	}
	
	public IOException getException() {
		return exception;
	}

}
