package com.racloop.qa.config;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppiumFactory {

    private WebDriver driver;
    private AppiumDriverType selectedDriverType;

    private final AppiumDriverType defaultDriverType = AppiumDriverType.ANDROID_EMULATOR;
    private final String appiumConfig = System.getProperty("device", defaultDriverType.toString()).toUpperCase();
    private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");
    private final boolean setDebugMode = Boolean.getBoolean("enableDebugMode");
    private final String pathToAppFile = System.getProperty("user.dir") + File.separator + "apps" + File.separator + System.getProperty("appFileName");
    private final String appiumServerLocation = System.getProperty("appiumServerLocation", "http://127.0.0.1:4723/wd/hub");

    public WebDriver getDriver() throws Exception {
        if (null == driver) {
            determineEffectiveDriverType();
            DesiredCapabilities desiredCapabilities = selectedDriverType.getDesiredCapabilities(pathToAppFile, setDebugMode);
            instantiateWebDriver(desiredCapabilities);
        }

        return driver;
    }

    public void quitDriver() {
        if (null != driver) {
            driver.quit();
        }
    }

    private void determineEffectiveDriverType() {
        AppiumDriverType driverType = defaultDriverType;
        try {
            driverType = AppiumDriverType.valueOf(appiumConfig);
        } catch (IllegalArgumentException ignored) {
            System.err.println("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            System.err.println("No driver specified, defaulting to '" + driverType + "'...");
        }
        selectedDriverType = driverType;
    }

    private void instantiateWebDriver(DesiredCapabilities desiredCapabilities) throws MalformedURLException {
        System.out.println("\n");
        System.out.println("Current Appium Config Selection: " + selectedDriverType);
        System.out.println("Current Appium Server Location: " + appiumServerLocation);
        System.out.println("\n");

        // For Selenium Grid e.g. Sauce Labs
        if (useRemoteWebDriver) {
            URL seleniumGridURL = new URL(System.getProperty("gridURL"));
            String desiredVersion = System.getProperty("desiredVersion");
            String desiredPlatform = System.getProperty("desiredPlatform");

            if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
                desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
            }

            if (null != desiredVersion && !desiredVersion.isEmpty()) {
                desiredCapabilities.setVersion(desiredVersion);
            }

            driver = new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
        } else {
            driver = selectedDriverType.getWebDriverObject(new URL(appiumServerLocation), desiredCapabilities);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }

    public AppiumDriverType getSelectedDriverType() {
        return selectedDriverType;
    }

    @Override
    public String toString() {
        return "AppiumFactory{" +
                "driver=" + driver +
                ", selectedDriverType=" + selectedDriverType +
                ", defaultDriverType=" + defaultDriverType +
                ", appiumConfig='" + appiumConfig + '\'' +
                ", useRemoteWebDriver=" + useRemoteWebDriver +
                ", setDebugMode=" + setDebugMode +
                ", pathToAppFile='" + pathToAppFile + '\'' +
                ", appiumServerLocation='" + appiumServerLocation + '\'' +
                '}';
    }
}