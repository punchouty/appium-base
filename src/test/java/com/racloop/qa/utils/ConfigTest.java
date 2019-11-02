package com.racloop.qa.utils;

import com.racloop.qa.AppiumBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class ConfigTest {

    @BeforeClass
    public void setup() {
        AppiumBase.initProperties();
    }

    @Test
    public void testProperties() {
        String smtpHost = System.getProperty("mail.smtp.host");
        Assert.assertNotNull(smtpHost);
    }

    @Test
    public void testApkFileExists() {
        String appFileName = System.getProperty("user.dir") + File.separator + "apps" + File.separator + System.getProperty("appFileName");
        File file = new File(appFileName);
        Assert.assertTrue(file.exists());
    }
}
