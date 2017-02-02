package com.lazerycode.appium.utility;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static com.lazerycode.appium.utility.UtilityFunctions.checkFileExists;

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
}
