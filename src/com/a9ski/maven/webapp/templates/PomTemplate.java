package com.a9ski.maven.webapp.templates;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PomTemplate extends AbstractTemplate {

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
		for (final Map<String, String> dependency : dependencies) {
			final StringBuilder tags = new StringBuilder();
			tags.append("<dependency>");
			for (final Map.Entry<String, String> entry : dependency.entrySet()) {
				final String tag = entry.getKey();
				tags.append("<").append(tag).append(">");
				tags.append(entry.getValue());
				tags.append("</").append(tag).append(">");
			}
			tags.append("</dependency>");
			allTags.append(tags.toString());
		}
		return allTags.toString();
	}

	private String createFacetTags(final Map<String, String> facets) {
		final StringBuilder tags = new StringBuilder();
		for (final Map.Entry<String, String> entry : facets.entrySet()) {
			final String facet = entry.getKey();
			tags.append(String.format("<%s>%s</%s>", facet, entry.getValue(), facet));
		}
		return tags.toString();
	}

}
