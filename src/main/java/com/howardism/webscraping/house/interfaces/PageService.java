package com.howardism.webscraping.house.interfaces;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public interface PageService {

    void open(String url);

    void close();

    boolean navigateToNextPage();

    List<WebElement> getElementsByClass(String className);

    WebDriverWait waitUpToTenSecs();
}
