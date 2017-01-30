package com.lazerycode.appium.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.Os;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.Processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.lazerycode.appium.configuration.AppiumArguments.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Mojo(name = "start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class StartAppiumMojo extends AbstractAppiumMojo {


    @Override
    public void execute() throws MojoExecutionException {
        getLog().info(" ");
        getLog().info("-------------------------------------------------------");
        getLog().info(" S T A R T I N G   A P P I U M   S E R V E R");
        getLog().info("-------------------------------------------------------");
        getLog().info(" ");
        getLog().debug("Using Node location: " + nodeDefaultLocation);
        getLog().debug("Using Appium location: " + appiumLocation);

        String nodeBin = Os.isFamily(Os.FAMILY_WINDOWS) ? NODE_EXECUTABLE_WINDOWS : NODE_EXECUTABLE_NIX;
        File nodeExecutable = new File(nodeDefaultLocation, nodeBin);
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


            Thread.sleep(3000);  //TODO wait for server to boot properly

        } catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException("Unable to start Appium server!", ex);
        }
    }

    private void checkFileExists(File someFile) throws MojoExecutionException {
        if (!someFile.exists()) {
            throw new MojoExecutionException("Unable to find file: " + someFile.getAbsolutePath());
        }
    }


}
