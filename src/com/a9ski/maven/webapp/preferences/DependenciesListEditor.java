package com.a9ski.maven.webapp.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import com.a9ski.maven.webapp.preferences.dialogs.DependencyDialog;
import com.a9ski.maven.webapp.utils.DependencyUtils;

public class DependenciesListEditor extends ListEditor {

	public DependenciesListEditor(final String name, final String labelText, final Composite parent) {
		init(name, labelText);
		createControl(parent);
	}

	@Override
	protected String createList(final String[] items) {
		return DependencyUtils.join(items);
	}

	@Override
	protected String getNewInputObject() {
		final DependencyDialog dlg = new DependencyDialog(getShell());
		if (IDialogConstants.OK_ID == dlg.open()) {
			// "demo.helloworld:helloworld:war:1.0-SNAPSHOT (compile)"
			return dlg.getDependency();
		}
		return null;
	}

	@Override
	protected String[] parseString(final String stringList) {
		return DependencyUtils.split(stringList);
	}
}
