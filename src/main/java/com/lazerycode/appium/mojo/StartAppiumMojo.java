package com.lazerycode.appium.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.Os;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.Processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.lazerycode.appium.configuration.AppiumArguments.*;
import static com.lazerycode.appium.utility.UtilityFunctions.checkFileExists;
import static com.lazerycode.appium.utility.UtilityFunctions.waitForAppiumToStart;
import static java.nio.charset.StandardCharsets.UTF_8;

@Mojo(name = "start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class StartAppiumMojo extends AbstractAppiumMojo {

    /**
     * The location of Node JS on disk
     * Default: ${project.basedir}/src/test/resources/node
     */
    @Parameter(defaultValue = "${project.basedir}/src/test/resources/node")
    File nodeDefaultLocation;
    /**
     * The location of Appium on disk
     * Default: ${project.basedir}/src/test/resources/node_modules/appium
     */
    @Parameter(defaultValue = "${project.basedir}/src/test/resources/node_modules/appium")
    File appiumLocation;

    /**
     * How long to wait for Appium to start up.
     * Each tick is a 500ms wait, so 30 ticks is equal to a maxium startup wait time of 15 seconds
     * Default: 30
     */
    @Parameter(defaultValue = "30")
    int appiumStartupTicks;
    /**
     * The IP address of the appium server that is being started up
     * Default: 0.0.0.0
     */
    @Parameter(defaultValue = "0.0.0.0")
    String appiumIpAddress;
    /**
     * The port of the Appium server that is being started up
     * Default: 4723
     */
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

    @Override
    public void controlAppiumServer() throws MojoExecutionException {
        getLog().info(" ");
        getLog().info("-------------------------------------------------------");
        getLog().info(" S T A R T I N G   A P P I U M   S E R V E R");
        getLog().info("-------------------------------------------------------");
        getLog().info(" ");
        getLog().debug("Using Node location: " + nodeDefaultLocation);
        getLog().debug("Using Appium location: " + appiumLocation);

        String nodeBinaryName = Os.isFamily(Os.FAMILY_WINDOWS) ? NODE_EXECUTABLE_WINDOWS : NODE_EXECUTABLE_NIX;
        File nodeExecutable = new File(nodeDefaultLocation, nodeBinaryName);
        checkFileExists(nodeExecutable);
        checkFileExists(appiumLocation);

        ArrayList<String> appiumCommandLineArguments = new ArrayList<>();
        appiumCommandLineArguments.add(nodeExecutable.getAbsolutePath());
        appiumCommandLineArguments.add(appiumLocation.getAbsolutePath());
        appiumCommandLineArguments.add(ADDRESS.getCommandLineArgument());
        appiumCommandLineArguments.add(appiumIpAddress);
        appiumCommandLineArguments.add(PORT.getCommandLineArgument());
        appiumCommandLineArguments.add(appiumPort);
        appiumCommandLineArguments.add(SHOW_LOG_TIMESTAMPS.getCommandLineArgument());
        appiumCommandLineArguments.add(LOG_TO_FILE.getCommandLineArgument());
        appiumCommandLineArguments.add(new File(projectBuildDirectory, "appium.log").getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(appiumCommandLineArguments);
        processBuilder.redirectError(new File(projectBuildDirectory, "appiumServerErrors.log"));
        processBuilder.redirectOutput(new File(projectBuildDirectory, "appiumServerOutput.log"));

        try {
            PidProcess appiumProcess = Processes.newPidProcess(processBuilder.start());
            if (!appiumProcess.isAlive()) {
                throw new MojoExecutionException("Unable to start Appium server!");
            }

            String processPID = Integer.toString(appiumProcess.getPid());
            FileUtils.writeStringToFile(new File(projectBuildDirectory, APPIUM_PID), processPID, UTF_8);
            waitForAppiumToStart(appiumStartupTicks, appiumIpAddress, appiumPort);
        } catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException("Unable to start Appium server!", ex);
        }
    }


}
