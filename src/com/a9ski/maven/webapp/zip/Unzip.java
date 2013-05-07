package com.a9ski.maven.webapp.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Unzip {

	private final String resource;
	private final File destDir;

	private Unzip(final String resource, final File destDir) {
		this.resource = resource;
		this.destDir = destDir;
	}

	public void extract() throws IOException {
		this.destDir.mkdirs();
		final ZipInputStream zip = new ZipInputStream(getClass().getResourceAsStream(resource));
		boolean success = false;
		try {
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					final File dir = new File(destDir, entry.getName());
					dir.mkdirs();
				} else {
					final File file = new File(destDir, entry.getName());
					file.getParentFile().mkdirs();
					copyInputStreamToFile(zip, file);
				}
			}
			success = true;
		} finally {
			if (success) {
				zip.close();
			} else {
				IOUtils.closeQuietly(zip);
			}
		}
	}

	private void copyInputStreamToFile(final InputStream source, final File destination) throws IOException {
		final FileOutputStream output = FileUtils.openOutputStream(destination);
		try {
			IOUtils.copy(source, output);
			output.close(); // don't swallow close Exception if copy completes normally
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	public static void extract(final String resource, final File destDir) throws IOException {
		final Unzip u = new Unzip(resource, destDir);
		u.extract();
	}
}
