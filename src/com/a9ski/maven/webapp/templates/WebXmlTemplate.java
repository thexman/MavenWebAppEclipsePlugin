package com.a9ski.maven.webapp.templates;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class WebXmlTemplate extends AbstractTemplate {

	private static final Map<String, String> attributes = new TreeMap<String, String>();

	static {
		attributes.put("3.0",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\"");
		attributes.put("2.5",
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"");
		attributes.put("2.4", "xmlns=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd\"");
	}

	public WebXmlTemplate(final File webXml) throws IOException {
		super(webXml);
	}

	public void createWebXml(final String displayName, final String servletSpec, final File destFile) throws IOException {
		final Map<String, String> params = new TreeMap<String, String>();
		params.put("artifactId", displayName);
		params.put("version", servletSpec);

		params.put("webAttributes", getAttributes(servletSpec));

		renderToFile(params, destFile);
	}

	private String getAttributes(final String servletSpec) {
		String attr = attributes.get(servletSpec);
		if (attr == null) {
			attr = "";
		}
		return attr;
	}

}
