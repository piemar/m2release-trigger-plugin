package com.baracuda.piepet.m2releasetrigger;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * POMInterceptor plugin will intercept POM elements and save them as build paramteters
 * XPATH to select which parameters to be used.
 *
 * An badge will be added to build history with the maven artifact version e.g 4.2.0-SNAPSHOT
 *
 * Paramteters that always will be created are
 * POM_VERSION = artifactVersion
 * POM_STAGING_PROFILE_ID=stagingProfileId retrieved from nexus-staging-maven plugin configuration
 */
public class M2ReleasePostPlugin extends Builder implements SimpleBuildStep {

    private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private String postReleaseAction ="";
    private String targetEnvironments="";
    private String systemId="";



    @DataBoundConstructor
    public M2ReleasePostPlugin(String systemId, String postReleaseAction, String targetEnvironments) {
        this.systemId = systemId;
        this.postReleaseAction =postReleaseAction;
        this.targetEnvironments=targetEnvironments;

    }

    public String getPostReleaseAction() {
        return postReleaseAction;
    }

    public String getTargetEnvironments() {
        return targetEnvironments;
    }

    public String getSystemId() {
        return systemId;
    }

    @Override
    public void perform(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener) {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.

    }


    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link M2ReleasePostPlugin}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     * <p/>
     * <p/>
     * See <tt>src/main/resources/hudson/plugins/hello_world/M2ReleasePostPlugin/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }


        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable xpath is used in the configuration screen.
         */
        public String getDisplayName() {
            return "M2Release Job Trigger";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req, formData);
        }

    }
}

