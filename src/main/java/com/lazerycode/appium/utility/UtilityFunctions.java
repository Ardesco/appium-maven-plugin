package com.lazerycode.appium.utility;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UtilityFunctions {
    public static void waitForAppiumToStart(int appiumStartupTicks, String appiumIpAddress, String appiumPort) throws IOException, InterruptedException, MojoExecutionException {
        for (int attempts = 0; attempts < appiumStartupTicks; attempts++) {
            Thread.sleep(500);
            if (getAppiumStatus(appiumIpAddress, appiumPort) == 200) {
                return;
            }
        }

        throw new MojoExecutionException("Unable to start Appium server!");
    }

    public static void checkFileExists(File someFile) throws MojoExecutionException {
        if (!someFile.exists()) {
            throw new MojoExecutionException("Unable to find file: " + someFile.getAbsolutePath());
        }
    }

    private static int getAppiumStatus(String appiumIpAddress, String appiumPort) throws IOException {

        OkHttpClient client = new OkHttpClient();
        URL appiumURL = new URL("http://" + appiumIpAddress + ":" + appiumPort + "/wd/hub/status");

        Request request = new Request.Builder()
                .url(appiumURL)
                .build();

        return client.newCall(request).execute().code();
    }
}
