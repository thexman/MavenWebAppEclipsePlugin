package com.a9ski.maven.webapp.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.a9ski.maven.webapp.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(PreferenceConstants.DEPENDENCIES, "javax:javaee-api:jar:6.0 (provided)|junit:junit:jar:4.11 (test)|javax.ws.rs:jsr311-api:jar:1.1.1 (provided)");		
	}

}
