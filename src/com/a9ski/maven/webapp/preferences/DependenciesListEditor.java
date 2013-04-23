package com.a9ski.maven.webapp.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import com.a9ski.maven.webapp.preferences.dialogs.DependencyDialog;
import com.a9ski.maven.webapp.utils.DependencyUtils;

public class DependenciesListEditor extends ListEditor {

	public DependenciesListEditor(String name, String labelText, Composite parent) {
		init(name, labelText);        
        createControl(parent);
	}
	
	@Override
	protected String createList(String[] items) {
		return DependencyUtils.join(items);
	}

	@Override
	protected String getNewInputObject() {
		DependencyDialog dlg = new DependencyDialog(getShell());
		if (IDialogConstants.OK_ID == dlg.open()) {
			//"demo.helloworld:helloworld:war:1.0-SNAPSHOT (compile)"
			return dlg.getDependency();
		} 
		return null;
	}

	@Override
	protected String[] parseString(String stringList) {
		return DependencyUtils.split(stringList);
	}	
}
