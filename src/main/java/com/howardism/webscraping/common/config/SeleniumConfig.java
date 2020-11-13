package com.howardism.webscraping.common.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class SeleniumConfig {

    static {
        System.setProperty("webdriver.gecko.driver", findFile());
    }

    private final WebDriver driver;

    public SeleniumConfig() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    private static String findFile() {
        String[] paths = {"", "bin/", "target/classes/"};
        for (String path : paths) {
            final String pathname = path + "geckodriver";
            if (new File(pathname).exists()) {
                return pathname;
            }
        }
        return "";
    }

    public WebDriver getDriver() {
        return driver;
    }
}
