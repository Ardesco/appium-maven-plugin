package com.lazerycode.appium.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class AbstractAppiumMojo extends AbstractMojo {

    /**
     * The project build directory
     */
    @Parameter(defaultValue = "${project.build.directory}")
    transient File projectBuildDirectory;

    /**
     * Don't start up/shut down the Appium server
     */
    @Parameter(defaultValue = "${skipTests}")
    boolean skipTests;

    static final String APPIUM_PID = "appium.pid";

    public void execute() throws MojoExecutionException {
        if (skipTests) {
            getLog().info("Tests are skipped, Appium server not started/stopped.");
            return;
        }

        controlAppiumServer();
    }

    protected abstract void controlAppiumServer() throws MojoExecutionException;
}