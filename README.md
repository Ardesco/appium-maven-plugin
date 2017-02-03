#Appium Maven Plugin
=================================

[![Join the chat at https://gitter.im/jmeter-maven-plugin/jmeter-maven-plugin](https://badges.gitter.im/Ardesco/appium-maven-plugin.svg)](https://gitter.im/Ardesco/appium-maven-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/Ardesco/appium-maven-plugin.svg?branch=master)](https://travis-ci.org/Ardesco/appium-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.appium/appium-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.jmeter/jmeter-maven-plugin)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.lazerycode.appium/appium-maven-plugin/badge.svg)](http://www.javadoc.io/doc/com.lazerycode.jmeter/jmeter-maven-plugin)

A maven plugin for appium


#Basic Usage
-----

### Add the plugin to your project

* Add the plugin to the build section of your pom's project :

```
<plugin>
    <groupId>com.lazerycode.appium</groupId>
    <artifactId>appium-maven-plugin</artifactId>
    <version>0.1.0</version>
    <configuration>
        <nodeDefaultLocation>${project.basedir}/src/test/node</nodeDefaultLocation>
        <appiumLocation>${project.basedir}/src/test/node_modules/appium</appiumLocation>
    </configuration>
    <executions>
        <execution>
            <id>start appium</id>
            <phase>pre-integration-test</phase>
            <goals>
                <goal>start</goal>
            </goals>
        </execution>
        <execution>
            <id>stop appium</id>
            <phase>post-integration-test</phase>
            <goals>
                <goal>stop</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

* Use the frontend-maven-plugin to download node and appium

```
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.3</version>
    <configuration>
        <workingDirectory>src/test</workingDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <phase>process-resources</phase>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <configuration>
                <nodeVersion>${frontend-maven-plugin.nodeVersion}</nodeVersion>
                <npmVersion>${frontend-maven-plugin.npmVersion}</npmVersion>
            </configuration>
        </execution>
        <execution>
            <id>npm install</id>
            <phase>process-resources</phase>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This will require a package.json:

```
{
  "name": "mobile-appium-tests",
  "private": true,
  "license": "",
  "version": "0.0.0",
  "description": "Download appium for automated tests",
  "devDependencies": {
    "appium": "1.6.3",
    "ios-deploy":"1.9.0"
  },
  "scripts": {
    "prestart": "npm install",
    "pretest": "npm install"
  }
}
```


### Run the build

	`mvn verify`
	
Now node and appium will be downloaded and appium will stop and start as part of your build.	