package com.lazerycode.appium.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.Os;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.ProcessUtil;
import org.zeroturnaround.process.Processes;
import org.zeroturnaround.process.WindowsProcess;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;

@Mojo(name = "stop", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class StopAppiumMojo extends AbstractAppiumMojo {

    @Override
    public void execute() throws MojoExecutionException {

        getLog().info(" ");
        getLog().info("-------------------------------------------------------");
        getLog().info(" S T O P P I N G   A P P I U M   S E R V E R");
        getLog().info("-------------------------------------------------------");
        getLog().info(" ");

        try {
            int appiumProcessPID = Integer.parseInt(readFileToString(new File(projectBuildDirectory, "appium.pid"), UTF_8));
            PidProcess appiumProcess = Processes.newPidProcess(appiumProcessPID);

            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                WindowsProcess appiumWindowsProcess = (WindowsProcess) appiumProcess;
                appiumWindowsProcess.setIncludeChildren(true);
                appiumProcess = appiumWindowsProcess;
            }

            if (!appiumProcess.isAlive()) {
                throw new MojoExecutionException("Could not find a process running on " + appiumProcessPID);
            }

            ProcessUtil.destroyGracefullyOrForcefullyAndWait(appiumProcess, shutdownTimeout, TimeUnit.SECONDS, forceShutdownTimeout, TimeUnit.SECONDS);

            if (appiumProcess.isAlive()) {
                throw new MojoExecutionException("Unable to stop Appium server...");
            }
        } catch (InterruptedException | TimeoutException | IOException ex) {
            throw new MojoExecutionException("Unable to stop Appium server...", ex);
        }

    }
}
