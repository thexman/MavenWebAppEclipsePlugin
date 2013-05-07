package com.a9ski.maven.webapp.wizards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.a9ski.maven.webapp.Activator;
import com.a9ski.maven.webapp.exec.ExecutionResult;
import com.a9ski.maven.webapp.exec.MavenExecutor;
import com.a9ski.maven.webapp.preferences.PreferenceConstants;
import com.a9ski.maven.webapp.templates.PomTemplate;
import com.a9ski.maven.webapp.templates.WebXmlTemplate;
import com.a9ski.maven.webapp.utils.DependencyUtils;
import com.a9ski.maven.webapp.wizards.pages.MavenProjectPage;
import com.a9ski.maven.webapp.zip.Unzip;

public class NewProjectWizard extends Wizard implements INewWizard {

	private final MavenProjectPage mavenPage = new MavenProjectPage();
	private final WizardNewProjectCreationPage mainPage = new WizardNewProjectCreationPage("basicNewProjectPage") { //$NON-NLS-1$
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		public void createControl(final Composite parent) {
			super.createControl(parent);
			createWorkingSetGroup((Composite) getControl(), selection, new String[] { "org.eclipse.ui.resourceWorkingSetPage" }); //$NON-NLS-1$
			Dialog.applyDialogFont(getControl());
		}
	};

	private IStructuredSelection selection;

	public NewProjectWizard() {
		super();
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public void addPages() {
		super.addPages();

		mainPage.setTitle("Create new project");
		mainPage.setDescription("Creates new project");

		mainPage.setTitle("Create new maven web application project");
		mavenPage.setTitle("Create new maven web application project");
		// TODO: add dependencies selection page

		addPage(mainPage);
		addPage(mavenPage);
	}

	private List<Map<String, String>> getDefaultDependencies() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		final String allDependencies = store.getString(PreferenceConstants.DEPENDENCIES);
		final String[] dependencyStrings = DependencyUtils.split(allDependencies);
		final List<Map<String, String>> dependencies = new ArrayList<Map<String, String>>();
		for (final String dependency : dependencyStrings) {
			dependencies.add(DependencyUtils.parseDependencyString(dependency));
		}
		return dependencies;
	}

	@Override
	public boolean performFinish() {
		final String groupId = mavenPage.getGroupId();
		final String artifactId = mavenPage.getArtifactId();
		final String versio = mavenPage.getVersion();
		final Map<String, String> facetVersions = mavenPage.getFacetsVersions();

		final String projectName = mainPage.getProjectName();
		final File location;

		if (!mainPage.useDefaults()) {
			location = mainPage.getLocationPath().toFile();
		} else {
			location = Platform.getLocation().append(projectName).toFile();
		}

		final List<Map<String, String>> dependencies = getDefaultDependencies();

		// TODO: add dependencies;

		final Job job = new Job("Creating Maven Web Applicaton project ") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask("Creating maven project...", 100);
				try {
					return createProject(monitor, projectName, location, groupId, artifactId, versio, facetVersions, dependencies);

				} catch (final IOException ex) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error executing maven", ex);
				} catch (final InterruptedException ex) {
					return new Status(IStatus.CANCEL, Activator.PLUGIN_ID, "Maven execution cancelled", ex);
				} catch (final CoreException ex) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error executing maven", ex);
				} finally {
					monitor.done();
				}

			}

			private IStatus createProject(final IProgressMonitor monitor, final String projectName, final File destDir, final String groupId, final String artifactId, final String version, final Map<String, String> facetVersions, final List<Map<String, String>> dependencies) throws IOException,
					InterruptedException, CoreException {
				Unzip.extract("content.zip", destDir);
				/* mvn archetype:generate -DgroupId=my-group-id -DartifactId=my-projectid -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false */

				monitor.worked(20);

				final File webXml = new File(destDir, "src/main/webapp/WEB-INF/web.xml");
				final WebXmlTemplate webTemplate = new WebXmlTemplate(webXml);
				webTemplate.createWebXml(artifactId, facetVersions.get("jst.web"), webXml);

				final File pomXml = new File(destDir, "pom.xml");
				final PomTemplate pomTemplate = new PomTemplate(pomXml);
				pomTemplate.createPom(groupId, artifactId, version, facetVersions, dependencies, pomXml);

				monitor.worked(40);

				final MavenExecutor mvn = new MavenExecutor();
				final ExecutionResult res = mvn.execute(destDir, "", "eclipse:eclipse",/* "-Dwtpversion=2.0", */"-DdownloadSources=true", "-Declipse.projectNameTemplate=" + projectName);

				final ILog log = Activator.getDefault().getLog();
				log.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "CMDS:" + Arrays.asList(res.getCommands())));
				log.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "STDOUT:" + res.getStdOut()));
				log.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "STDERR:" + res.getStdErr()));

				monitor.worked(80);

				final File projectFile = new File(destDir, ".project");

				final IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(projectFile.getCanonicalPath()));
				final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
				project.create(description, null);
				project.open(null);

				monitor.worked(100);
				monitor.done();

				if (res.getExitCode() == 0) {
					return new Status(IStatus.OK, Activator.PLUGIN_ID, "Maven project created");
				} else {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error creating Maven project");
				}
			}
		};
		job.setUser(true);
		job.schedule();

		return true;
	}

}
