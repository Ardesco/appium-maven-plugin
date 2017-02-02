package com.lazerycode.appium.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class AbstractAppiumMojo extends AbstractMojo {

    /**
     * The project build directory
     */
    @Parameter(defaultValue = "${project.build.directory}")
    transient File projectBuildDirectory;

    static final String APPIUM_PID = "appium.pid";
}