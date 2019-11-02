package com.racloop.qa.infra;

import com.racloop.qa.AppiumBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class EmulatorTest {

    @BeforeClass
    public void setup() {
        AppiumBase.initProperties();
    }

    @Test
    public void testEmulator() {
        Emulator.launch();
        Emulator.waitForBeReady();
        Emulator.stop();
    }

    @Test
    public void testDevices() {
        Emulator.isAnyRunningDevices();
    }
}
