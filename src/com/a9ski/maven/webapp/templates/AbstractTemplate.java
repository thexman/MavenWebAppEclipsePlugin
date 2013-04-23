package com.a9ski.maven.webapp.templates;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.stringtemplate.v4.ST;

public abstract class AbstractTemplate {
	
	private static final String ENCODING = "UTF-8";
	
	private final File file;
	private final String templateContent;
	
	protected AbstractTemplate(final File templateFile) throws IOException {
		this.file = templateFile;
		this.templateContent = FileUtils.readFileToString(file, ENCODING);
	}
	
	protected ST getTemplate() throws IOException {
		return new ST(templateContent, '$', '$');
	}
	
	protected String render(final Map<String, String> params) throws IOException {
		final ST st = getTemplate();
		for(final Map.Entry<String,String> entry : params.entrySet()) {
			st.add(entry.getKey(), entry.getValue());
		}
		return st.render();
	}
	
	protected void renderToFile(final Map<String, String> params, final File destFile) throws IOException {
		final String content = render(params); 
		FileUtils.write(destFile, content);		
	}
}
