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

    @Parameter(defaultValue = "${project.basedir}/src/test/resources/node")
    File nodeDefaultLocation;
    @Parameter(defaultValue = "${project.basedir}/src/test/resources/node_modules/appium")
    File appiumLocation;
    @Parameter(defaultValue = "15")
    int shutdownTimeout;
    @Parameter(defaultValue = "5")
    int forceShutdownTimeout;
    @Parameter(defaultValue = "0.0.0.0")
    String appiumIpAddress;
    @Parameter(defaultValue = "4723")
    String appiumPort;
    @Parameter
    String selendroidPort;
    @Parameter
    String chromedriverPort;
    @Parameter
    String robotIpAddress;
    @Parameter
    String robotPort;

    static final String NODE_EXECUTABLE_WINDOWS = "node.exe";
    static final String NODE_EXECUTABLE_NIX = "node";
    static final String APPIUM_PID = "appium.pid";
}