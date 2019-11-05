package com.racloop.qa;

import com.racloop.qa.config.AppiumDriverType;
import com.racloop.qa.config.AppiumFactory;
import com.racloop.qa.infra.AppiumServer;
import com.racloop.qa.infra.Emulator;
import com.racloop.qa.listeners.ScreenshotListener;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


@Listeners(ScreenshotListener.class)
public class AppiumBase {

    private static List<AppiumFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<AppiumFactory>());
    private static ThreadLocal<AppiumFactory> appiumFactory;

    @BeforeSuite(alwaysRun = true)
    public static void instantiateDriverObject() {
        initProperties();
        System.out.println(System.getProperty("device"));
        if(System.getProperty("device").toUpperCase().equals(AppiumDriverType.ANDROID_EMULATOR.name())) {
            boolean startAutomaticallyEmulator = Boolean.getBoolean(System.getProperty("auto.start.emulator"));
            if(startAutomaticallyEmulator) {
                Emulator.launch();
                Emulator.waitForBeReady();
            }
        }
        boolean startAutomaticallyAppium = Boolean.getBoolean(System.getProperty("auto.start.appium"));
        if(startAutomaticallyAppium) {
            AppiumServer.launch();
        }
        appiumFactory = new ThreadLocal<AppiumFactory>() {
            @Override
            protected AppiumFactory initialValue() {
                AppiumFactory appiumFactory = new AppiumFactory();
                webDriverThreadPool.add(appiumFactory);
                return appiumFactory;
            }
        };
        System.out.println(appiumFactory.get());
    }

    public static void initProperties() {
        Properties props = new Properties();
        try (InputStream input = AppiumBase.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
            System.getProperties().putAll(props);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static AppiumDriver getDriver() throws Exception {
        return (AppiumDriver) appiumFactory.get().getDriver();
    }

    @AfterSuite(alwaysRun = true)
    public static void closeDriverObjects() {
        for (AppiumFactory appiumFactory : webDriverThreadPool) {
            appiumFactory.quitDriver();
        }
        stop();
    }

    private static void stop() {
        boolean startAutomaticallyAppium = Boolean.getBoolean(System.getProperty("auto.start.appium"));
        if(startAutomaticallyAppium) {
            AppiumServer.stop();
        }
        if(System.getProperty("device").toUpperCase().equals(AppiumDriverType.ANDROID_EMULATOR.name())) {
            boolean startAutomaticallyEmulator = Boolean.getBoolean(System.getProperty("auto.start.emulator"));
            if(startAutomaticallyEmulator) {
                Emulator.stop();
            }
        }
    }
}