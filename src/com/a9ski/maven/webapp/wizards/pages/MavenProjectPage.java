package com.a9ski.maven.webapp.wizards.pages;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page for choosing the Maven group id / artifact id, etc.
 *  
 * @author Kiril Arabadzhiyski
 * 
 */
public class MavenProjectPage extends WizardPage {

	private static final String NONE = "---";

	/**
	 * The maven group id
	 */
	private Text projectGroupIdField;

	/**
	 * The maven argifact id
	 */
	private Text projectArtifactIdField;

	/**
	 * The maven artifact version
	 */
	private Text projectVersionField;
		
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
    
    final GridData horizontalFillData = new GridData(GridData.FILL_HORIZONTAL);    
    
    private Listener modificationListener = new Listener() {			
		@Override
		public void handleEvent(Event event) {			
			setPageComplete(validatePage());
		}
	};

	private Combo javaVersionField;

	private Combo webModuleVesionField;

	private Combo javaScriptVersionField;

	private Combo jaxRsVersionField;
	
	private Map<String, Combo> tag2combo = new TreeMap<String, Combo>(); 

	public MavenProjectPage() {
		super("NewProjectPage");		
		setPageComplete(false);
		horizontalFillData.widthHint = SIZING_TEXT_FIELD_WIDTH;
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);

		initializeDialogUnits(parent);

		// PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createProjectNameGroup(composite);
		createProjectFacetsGroup(composite);
		
		setPageComplete(validatePage());
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}
	
	/**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectNameGroup(Composite parent) {
        // project specification group        
        final Group projectGroup = new Group(parent, SWT.NONE);
        projectGroup.setText("Artifact");
        
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setFont(parent.getFont());
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        
        // new project group id
        projectGroupIdField = createText(projectGroup, "Group Id:");
                
        // new project artifact id
        projectArtifactIdField = createText(projectGroup, "Artifact Id:");                        
        
        // new project group id
        projectVersionField = createText(projectGroup, "Version:");                               
    }
    
    /**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectFacetsGroup(Composite parent) {
        // project specification group
        final Group facetsGroup = new Group(parent, SWT.NONE);
        facetsGroup.setText("Project facets");
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        facetsGroup.setLayout(layout);
        facetsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                
        javaVersionField = createCombo(facetsGroup, "Java version", 2, "1.5", "1.6", "1.7");
        webModuleVesionField = createCombo(facetsGroup, "Dynamic web module version", 0, "3.0"); // jst.web
        javaScriptVersionField = createCombo(facetsGroup, "Javascript", 0, "1.0"); // wst.jsdt.web
        jaxRsVersionField = createCombo(facetsGroup, "JAX-RS", 0, NONE, "1.0", "1.1"); // jst.jaxrs               
        
        tag2combo.put("java", javaVersionField);
        tag2combo.put("jst.web", webModuleVesionField);
        tag2combo.put("wst.jsdt.web", javaScriptVersionField);
        tag2combo.put("jst.jaxrs", jaxRsVersionField);        
    }

	private Combo createCombo(Composite group, String label, int defaultSelection, String... items) {		
		final Label projectGroupIdLabel = new Label(group, SWT.NONE);
        projectGroupIdLabel.setText(label);
        projectGroupIdLabel.setFont(group.getFont());
        
        final Combo combo = new Combo(group, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setItems(items);
        combo.select(defaultSelection);
        combo.setText(items[defaultSelection]);
        combo.setLayoutData(horizontalFillData);
        combo.setFont(group.getFont());
        
        combo.addListener(SWT.Modify, modificationListener);
        
        return combo;
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
	
	public boolean validatePage() {
		setErrorMessage(null);
		setMessage(null);
		return validateGroupId() && validateArtifactId() && validateVersion();
	}

	private boolean validateGroupId() {
		final String groupId = projectGroupIdField.getText().trim();;
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
		}
		return true;
	}
	
	private boolean validateArtifactId() {
		final String artifactId = projectArtifactIdField.getText().trim();;
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
		}
		return true;
	}
	
	private boolean validateVersion() {
		final String versionId = projectVersionField.getText().trim();;
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
		}
		return true;
	}
	
	public Map<String, String> getFacetsVersions() {
		final Map<String, String> facets = new TreeMap<String, String>();
		for(final Map.Entry<String, Combo> entry : tag2combo.entrySet()) {
			final String version = entry.getValue().getText();
			if (!NONE.equalsIgnoreCase(version)) {
				facets.put(entry.getKey(), version);
			}
		}
		return facets;
	}
	
	public String getGroupId() {
		return projectGroupIdField.getText();
	}
	
	public String getArtifactId() {
		return projectArtifactIdField.getText();
	}
	
	public String getVersion() {
		return projectVersionField.getText();
	}	
	

}
