package com.baracuda.piepet.pominterceptor;

import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Project;
import hudson.model.StringParameterValue;
import jenkins.model.Jenkins;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FPPE12 on 2015-11-22.
 */
public class M2ReleasePostAction {
    public static void trigger(String systemId, String artifactVersion, String buildNumber, String targetEnvironments, String postReleaseAction){
        Project project = (Project) Jenkins.getInstance().getItemByFullName(postReleaseAction);
        String[]targetEnvironemntsArray=targetEnvironments.split(",");
        for (String targetEnv : targetEnvironemntsArray) {
            List<ParameterValue> values=new ArrayList<ParameterValue>();
            values.add(new StringParameterValue("systemId", systemId));
            values.add(new StringParameterValue("artifactVersion", artifactVersion));
            values.add(new StringParameterValue("buildNumber", buildNumber));
            values.add(new StringParameterValue("targetEnvironment", targetEnv));
            // schedule post maven release step
            ParametersAction parameters = new ParametersAction(values);
            project.scheduleBuild(0,new ReleaseCause(), parameters);
        }

    }
}
