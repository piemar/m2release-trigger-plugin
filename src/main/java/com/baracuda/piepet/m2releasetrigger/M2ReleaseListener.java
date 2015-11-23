package com.baracuda.piepet.m2releasetrigger;


import hudson.Extension;
import hudson.Launcher;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.*;
import hudson.model.listeners.RunListener;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by FPPE12 on 2015-11-22.
 */
@Extension
public class M2ReleaseListener extends RunListener<AbstractBuild> {
    private static final Logger LOGGER = Logger.getLogger(M2ReleaseListener.class.getName());

    @Override
    public void onStarted(AbstractBuild abstractBuild, TaskListener listener) {
        super.onStarted(abstractBuild, listener);
    }

    @Override
    public Environment setUpEnvironment(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException, Run.RunnerAbortedException {
        return super.setUpEnvironment(build, launcher, listener);
    }

    @Override
    public void onDeleted(AbstractBuild abstractBuild) {
        super.onDeleted(abstractBuild);
    }

    public M2ReleaseListener() {
        super();
    }

    @Override
    public void onCompleted(AbstractBuild abstractBuild, TaskListener listener) {
        MavenModuleSetBuild mavenModuleSetBuild = ((MavenModuleSetBuild) abstractBuild);

        List<? extends Action> actions = mavenModuleSetBuild.getAllActions();
        for (Action action : actions) {
            if (action.getClass().getName().equals("org.jvnet.hudson.plugins.m2release.M2ReleaseArgumentsAction")) {
                Object[] configurationList = (mavenModuleSetBuild.getProject().getPostbuilders().toMap()).values().toArray();

                if (configurationList.length > 0) {
                    LOGGER.log(Level.INFO, "Invoking M2Release post action");
                    abstractBuild.addAction(M2ReleaseTriggerBadge.createShortText(mavenModuleSetBuild.getBuildVariables().get("MVN_RELEASE_VERSION")));
                    M2ReleasePostPlugin pomInterceptorPlugin = (M2ReleasePostPlugin) configurationList[0];
                    M2ReleasePostAction.trigger(pomInterceptorPlugin.getSystemId(), mavenModuleSetBuild.getBuildVariables().get("MVN_RELEASE_VERSION"), abstractBuild.getId(), pomInterceptorPlugin.getTargetEnvironments(),pomInterceptorPlugin.getPostReleaseAction());
                    break;
                }
            }
            super.onCompleted(abstractBuild, listener);
        }
    }

    @Override
    public void onFinalized(AbstractBuild abstractBuild) {
        super.onFinalized(abstractBuild);
    }

    @Override
    public void unregister() {
        super.unregister();
    }
}
