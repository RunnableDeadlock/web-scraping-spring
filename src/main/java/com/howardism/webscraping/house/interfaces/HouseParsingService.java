package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HouseParsingService {

    HouseEntity parseSinglePage(String url) throws IOException;

    List<String> parseIndexPage(List<WebElement> webElements, Set<String> existedUrls);
}
