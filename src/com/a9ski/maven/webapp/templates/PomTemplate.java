package com.a9ski.maven.webapp.templates;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PomTemplate extends AbstractTemplate {

	private static final String IDENT = "\t\t";
	private static final String NL = "\n";

	public PomTemplate(final File pomXml) throws IOException {
		super(pomXml);
	}

	public void createPom(final String groupId, final String artifactId, final String version, final Map<String, String> facets, final List<Map<String, String>> dependencies, final File destFile) throws IOException {
		final Map<String, String> params = new TreeMap<String, String>();

		params.put("groupId", groupId);
		params.put("artifactId", groupId);
		params.put("version", version);
		params.put("facets", createFacetTags(facets));
		params.put("dependencies", createDependenciesTag(dependencies));
		renderToFile(params, destFile);
	}

	private String createDependenciesTag(final List<Map<String, String>> dependencies) {
		final StringBuilder allTags = new StringBuilder();		
		boolean first = true;
		for (final Map<String, String> dependency : dependencies) {
			final StringBuilder tags = new StringBuilder();
			if (!first) {
				tags.append(NL);
			} else {
				first = false;
			}
			tags.append("<dependency>").append(NL);
			for (final Map.Entry<String, String> entry : dependency.entrySet()) {
				final String tag = entry.getKey();
				tags.append("\t");
				tags.append("<").append(tag).append(">");
				tags.append(entry.getValue());
				tags.append("</").append(tag).append(">");
				tags.append(NL);
			}
			tags.append("</dependency>");
			allTags.append(tags.toString());
		}
		return allTags.toString();
	}

	private String createFacetTags(final Map<String, String> facets) {
		final StringBuilder tags = new StringBuilder();
		boolean first = true;
		for (final Map.Entry<String, String> entry : facets.entrySet()) {
			final String facet = entry.getKey();
			if (!first) {
				tags.append(NL);
			} else {
				first = false;
			}
			tags.append(String.format("<%s>%s</%s>", facet, entry.getValue(), facet));
		}
		return tags.toString();
	}

}
