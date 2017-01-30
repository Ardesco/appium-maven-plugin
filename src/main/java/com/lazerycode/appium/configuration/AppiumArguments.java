package com.lazerycode.appium.configuration;

public enum AppiumArguments {

    ADDRESS("-a"),
    PORT("-p"),
    SELENDROID_PORT("--selendroid-port"),
    CHROMEDRIVER_PORT("--chromedriver-port"),
    ROBOT_ADDRESS("-ra"),
    ROBOT_PORT("-rp"),
    SHOW_LOG_TIMESTAMPS("--log-timestamp"),
    LOG_TO_FILE("-g");


    private final String commandLineArgument;

    AppiumArguments(String commandLineArgument) {
        this.commandLineArgument = commandLineArgument;
    }

    public String getCommandLineArgument() {
        return commandLineArgument;
    }

}