package com.racloop.qa.infra;

import com.racloop.qa.AppiumBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AppiumServerTest {

    @BeforeClass
    public void setup() {
        AppiumBase.initProperties();
    }

    @Test
    public void testAppiumServer() {
        AppiumServer.launch();
        System.out.println("Check if running");
        boolean running = AppiumServer.checkIfRunnning();
        System.out.println("is running : " + running);
        AppiumServer.stop();
    }

    @Test
    public void testIfRunning() {
        boolean running = AppiumServer.checkIfRunnning();
        System.out.println(running);
    }
}
