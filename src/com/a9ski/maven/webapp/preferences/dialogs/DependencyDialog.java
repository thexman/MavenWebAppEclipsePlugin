package com.a9ski.maven.webapp.preferences.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.a9ski.maven.webapp.utils.DependencyUtils;

public class DependencyDialog extends TitleAreaDialog {

	private final GridData horizontalFillData = new GridData(GridData.FILL_HORIZONTAL);
	private Text textGroupId;
	private Text textArtifactId;
	private Text textVersion;
	private Text textType;
	private Text textScope;
	private String dependency;
	private Listener modificationListener  = new Listener() {		
		@Override
		public void handleEvent(Event event) {
			validatePage();
		}
	};
	
	public DependencyDialog(Shell parentShell) {
		super(parentShell);
		horizontalFillData.horizontalSpan = 1;
	}
	
	@Override
	public void create() {		
		super.create();
		setTitle("New dependency");
		setMessage("Create new dependency");		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);		
		
		GridLayout layout = new GridLayout();			
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());
		
		super.createDialogArea(composite);
		
		Composite grid = new Composite(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();		
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 5;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		gridLayout.verticalSpacing = 10;		
		gridLayout.numColumns = 2;
		grid.setLayout(gridLayout);
		grid.setLayoutData(new GridData(GridData.FILL_BOTH));
		grid.setFont(parent.getFont());
		
		/*org.mortbay.jetty:jetty:jar:7.0 (test)*/				
		textGroupId = createText(grid, "Group Id");
		textArtifactId = createText(grid, "Artifact Id");
		textVersion = createText(grid, "Version");
		textType = createText(grid, "Type");
		textScope = createText(grid, "Scope");
				
		return composite;
	}
	
	private Text createText(final Composite group, final String label) {
		final Label textLabel = new Label(group, SWT.NONE);
        textLabel.setText(label);
        textLabel.setFont(group.getFont());        
        
        final Text text = new Text(group, SWT.BORDER);
        text.setLayoutData(horizontalFillData);
        text.setFont(group.getFont());
        
        text.addListener(SWT.Modify, modificationListener);
        
        return text;
	}
	
	private String createDependencyString() {
		/*org.mortbay.jetty:jetty:jar:1.6-SNAPSHOT (test)*/
		final String groupId = textGroupId.getText().trim();
		final String artifactId = textArtifactId.getText().trim();
		final String type = textType.getText().trim();
		final String version = textVersion.getText().trim();
		final String scope = textScope.getText().trim();
		
		return DependencyUtils.createDependencyString(groupId, artifactId, type, version, scope);
	}

	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID && !validatePage()) {
			return;
		}
		dependency = createDependencyString();
		super.buttonPressed(buttonId);
	}
	
	public String getDependency() {
		return dependency;
	}

	private boolean validatePage() {
		return validateGroupId() && validateArtifactId() && validateVersion() && validateType() && validateScope();
		
	}

	private boolean validateGroupId() {
		final String groupId = textGroupId.getText().trim();;
		if (groupId.isEmpty()) {
			setErrorMessage(null);
			setMessage("Maven groupId must be specified. E.g.: com.mycompany.xxx");
			return false;
		} else if (groupId.startsWith(".")){
			setErrorMessage("Maven groupId cannot starts with '.'");			
			return false;
		} else if (groupId.endsWith(".")) {
			setErrorMessage("Maven groupId cannot ends with '.'");			
			return false;
		} else if (groupId.endsWith(".")) {
			setErrorMessage("Maven groupId cannot contains '|'");			
			return false;
		}
		return true;
	}
	
	private boolean validateArtifactId() {
		final String artifactId = textArtifactId.getText().trim();;
		if (artifactId.isEmpty()) {
			setErrorMessage(null);
			setMessage("Maven artifactId must be specified. E.g.: MyProject");
			return false;
		} else if (artifactId.startsWith(".")) {
			setErrorMessage("Maven artifactId cannot starts with '.'");			
			return false;
		} else if (artifactId.endsWith(".")) {
			setErrorMessage("Maven artifactId cannot ends with '.'");			
			return false;
		} else if (artifactId.contains("|")) {
			setErrorMessage("Maven artifactId cannot contains '|'");			
			return false;
		}
		return true;
	}
	
	private boolean validateVersion() {
		final String versionId = textVersion.getText().trim();;
		if (versionId.isEmpty()) {
			setErrorMessage(null);
			setMessage("Maven artifact version must be specified. E.g.: 1.0-SNAPSHOT");
			return false;
		} else if (versionId.startsWith(".")){
			setErrorMessage("Maven artifact version cannot starts with '.'");			
			return false;
		} else if (versionId.endsWith(".")) {
			setErrorMessage("Maven artifact version cannot ends with '.'");			
			return false;
		} else if (versionId.contains("|")) {
			setErrorMessage("Maven artifact version cannot contains '|'");			
			return false;
		}
		return true;
	}
	
	private boolean validateType() {
		final String type = textType.getText().trim();
		if (type.contains("|")) {
			setErrorMessage("Maven artifact type cannot contains '|'");			
			return false;
		}
		return true;
	}
	
	private boolean validateScope() {
		final String scope = textScope.getText().trim();
		if (scope.contains("|")) {
			setErrorMessage("Maven artifact scope cannot contains '|'");			
			return false;
		}
		return true;
	}
}
