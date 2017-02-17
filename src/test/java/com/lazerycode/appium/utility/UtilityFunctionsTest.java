package com.lazerycode.appium.utility;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;

import static com.lazerycode.appium.utility.UtilityFunctions.checkFileExists;
import static com.lazerycode.appium.utility.UtilityFunctions.getAppiumStatus;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilityFunctionsTest {


    @Test
    public void checkFileExistsReturnsCorrectResultForFileThatExists() throws Exception {
        //Exception will be thrown if the file does not exist
        URL fileThatExists = this.getClass().getResource("/foo.txt");
        checkFileExists(new File(fileThatExists.toURI()));
    }

    @Test(expected = MojoExecutionException.class)
    public void checkFileExistsReturnsCorrectResultForFileThatDoesNotExist() throws Exception {
        checkFileExists(new File("/foo/bar/invalid"));
    }

    @Test
    public void checkThatAStatusIsReturnedEvenIfServerConnectIsImpossible() throws Exception {
        int appiumStatus = getAppiumStatus("0.0.0.0", findRandomOpenLocalPort());

        assertThat(appiumStatus, is(equalTo(0)));
    }

    private String findRandomOpenLocalPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return Integer.toString(socket.getLocalPort());
        }
    }
}
