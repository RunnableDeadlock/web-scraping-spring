package com.howardism.webscraping.house;

import com.howardism.webscraping.common.config.SeleniumConfig;
import com.howardism.webscraping.house.interfaces.PageService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Page591ServiceImpl implements PageService {

    private WebDriver driver;

    @Override
    public void open(String url) {
        if (driver == null) {
            this.driver = new SeleniumConfig().getDriver();
        }
        this.driver.get(url);
    }

    @Override
    public void close() {
        this.driver.quit();
        this.driver = null;
    }

    @Override
    public boolean navigateToNextPage() {
        String currentUrl = this.driver.getCurrentUrl();
        try {
            getExecuteScript("document.getElementsByClassName(\"pageNext\")[0].click()");
            this.waitUpToTenSecs().until(webDriver -> {
                String status = (String) getExecuteScript("return document.readyState");
                return !currentUrl.equals(webDriver.getCurrentUrl()) && status.equals("complete");
            });

            return true;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            log.error("Parsing index page stopped at {}", currentUrl);
            this.close();
            return false;
        }
    }

    @Override
    public List<WebElement> getElementsByClass(String className) {
        return this.driver.findElements(By.className(className));
    }

    @Override
    public WebDriverWait waitUpToTenSecs() {
        return new WebDriverWait(this.driver, 10L);
    }

    private Object getExecuteScript(String script) {
        return ((JavascriptExecutor) this.driver).executeScript(script);
    }
}
