package com.a9ski.maven.webapp.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class DependencyUtils {
		
	
	private DependencyUtils() {
		
	}
	
	public static Map<String, String> parseDependencyString(String dependency) {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		dependency = dependency.replace(" (", ":").replace(")", "");		
		
		final String[] items = dependency.split(":"); 
		map.put("groupId", items[0]);
		map.put("artifactId", items[1]);
		if (!items[2].isEmpty()) {
			map.put("type", items[2]);
		}
		map.put("version", items[3]);
		if (!items[4].isEmpty()) {
			map.put("scope", items[4]);
		}
		return map;
	}
	
	public static String createDependencyString(final String groupId, final String artifactId, final String type, final String version, final String scope) {
		final String dependency = String.format("%s:%s:%s:%s", groupId, artifactId, type, version);		
		if (!scope.isEmpty()) {
			return String.format("%s (%s)", dependency, scope);
		} else {
			return dependency;
		}
	}
	
	public static String join(String... dependencyStrings) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(final String item : dependencyStrings) {
			if (!first) {
				sb.append("|");
			} else {
				first = false;
			}
			sb.append(item);
		}
		return sb.toString();
	}
	
	public static String[] split(String dependencies) {
		return dependencies.split("\\|");
	}
}
