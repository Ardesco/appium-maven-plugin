package com.lazerycode.appium.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
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

    /**
     * The amount of time, in seconds, that the plugin will wait for appium to shutdown before it tries to just kill the process.
     * Default: 15
     */
    @Parameter(defaultValue = "15")
    int shutdownTimeout;
    /**
     * The amount of time, in seconds,  to wait for a force shutdown to complete.
     * If the plugin has not managed to kill appium by the time this timeout is complete the build will fail
     * Default: 5
     */
    @Parameter(defaultValue = "5")
    int forceShutdownTimeout;

    @Override
    public void controlAppiumServer() throws MojoExecutionException {

        getLog().info(" ");
        getLog().info("-------------------------------------------------------");
        getLog().info(" S T O P P I N G   A P P I U M   S E R V E R");
        getLog().info("-------------------------------------------------------");
        getLog().info(" ");

        try {
            int appiumProcessPID = Integer.parseInt(readFileToString(new File(projectBuildDirectory, APPIUM_PID), UTF_8));
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
