package com.howardism.webscraping.house;

import com.howardism.webscraping.common.utils.SSLHelper;
import com.howardism.webscraping.house.interfaces.HouseParsingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class HouseParsingServiceImpl implements HouseParsingService {

    private final Pattern DATETIME_PATTERN = Pattern.compile("\\d{1,4}-\\d{1,2}-\\d{1,2}");
    private final Pattern FLOAT_PATTERN = Pattern.compile("^\\d*\\.\\d+|\\d+\\.\\d*$|\\d+");
    private final Pattern PATH_PATTERN = Pattern.compile("/home/house/detail/2/(\\d+)\\.html");

    @Override
    public HouseEntity parseSinglePage(String url) throws IOException {

        LocalDateTime currentTime = LocalDateTime.now();

        Document doc = SSLHelper
                .getConnection(url)
                .userAgent("Mozilla")
                .get();

        Element error = doc.selectFirst("dl.error_img");
        if (error != null && error.text() != null) {
            throw new IOException("Page does not exist");
        }

        Element title = doc.selectFirst("h1.detail-title-content");
        Element price = doc.selectFirst("span.info-price-num");
        Element effectiveDate = doc.selectFirst("span.detail-info-span.pull-right");

        Elements floorInfo = doc.select("div.info-floor-left");
        Elements addressInfo = doc.select("div.info-addr-content");
        Elements houseDetail = doc.select("div.detail-house-item");

        HouseEntity house = new HouseEntity();

        house.setName(title.text());

        Matcher priceMatcher = this.FLOAT_PATTERN.matcher(price.text());
        if (priceMatcher.find()) {
            house.setPrice(Float.parseFloat(priceMatcher.group()));
        }

        Matcher matcher = this.DATETIME_PATTERN.matcher(effectiveDate.text());
        if (matcher.find())
            house.setEffectiveDate(LocalDate.parse(matcher.group()).atStartOfDay());

        parseFloorInfo(floorInfo, house);
        parseAddressInfo(addressInfo, house);
        parseHouseDetail(houseDetail, house);

        house.setUrl(url);

        LocalDateTime finishTime = LocalDateTime.now();
        house.setTimestamp(finishTime);

        Duration duration = Duration.between(currentTime, finishTime);
        house.setDuration(duration.toMillis());

        return house;
    }

    @Override
    public List<String> parseIndexPage(List<WebElement> webElements, Set<String> existedUrls) {
        Iterator<WebElement> webElementIterator = webElements.iterator();
        List<String> newUrls = new ArrayList<>();
        while (webElementIterator.hasNext()) {
            String outerHtml = webElementIterator.next().getAttribute("outerHTML");

            Matcher pathMatcher = PATH_PATTERN.matcher(outerHtml);
            if (pathMatcher.find()) {
                String url = "https://sale.591.com.tw" + pathMatcher.group();
                if (existedUrls.contains(url)) {
                    continue;
                }

                newUrls.add(url);
                existedUrls.add(url);
            }
        }
        return newUrls;
    }

    private void parseHouseDetail(Elements elements, HouseEntity house) {
        elements.forEach(element -> {
            String key = getText(element, "detail-house-key");
            String value = getText(element, "detail-house-value");
            switch (key) {
                case "現況":
                    house.setStyle(value);
                    break;

                case "型態":
                    house.setType(value);
                    break;

                case "裝潢程度":
                    house.setDecorationLevel(value);
                    break;

                case "管理費": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setMonthlyAdminCost(Float.parseFloat(matcher.group()));
                    break;
                }

                case "帶租約":
                    house.setWithRent(value.equals("是"));
                    break;

                case "法定用途":
                    house.setLegalUsage(value);
                    break;

                case "車位":
                    house.setParking(value);
                    break;

                case "公設比": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setSharedRatio(Float.parseFloat(matcher.group()));
                    break;
                }

                case "主建物": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setMainSize(Float.parseFloat(matcher.group()));
                    break;
                }

                case "共用部分": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setSharedSize(Float.parseFloat(matcher.group()));
                    break;
                }

                case "附屬建物": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setExtraSize(Float.parseFloat(matcher.group()));
                    break;
                }

                case "土地坪數": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setLandSize(Float.parseFloat(matcher.group()));
                    break;
                }

                case "現況坪數": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setCurrentSize(Float.parseFloat(matcher.group()));
                    break;
                }

                default:
                    if (key.length() > 0) {
                        this.logMissingInfo(key, value);
                    }
                    break;
            }
        });
    }

    private void parseAddressInfo(Elements elements, HouseEntity house) {
        elements.forEach(element -> {
            String key = getText(element, "info-addr-key");
            String value = getText(element, "info-addr-value");
            switch (key) {
                case "樓層":
                    house.setFloor(value);
                    break;

                case "朝向":
                    house.setFacingDirection(value);
                    break;

                case "社區":
                    house.setApartmentComplex(value);
                    break;

                case "地址":
                    house.setAddress(value);
                    break;

                default:
                    this.logMissingInfo(key, value);
            }
        });
    }

    private void parseFloorInfo(Elements elements, HouseEntity house) {
        elements.forEach(element -> {
            String value = getText(element, "info-floor-key");
            String key = getText(element, "info-floor-value");
            switch (key) {
                case "格局":
                    house.setFormat(value);
                    break;

                case "屋齡": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setAge(Float.parseFloat(matcher.group()));
                    break;
                }

                case "權狀坪數": {
                    Matcher matcher = this.FLOAT_PATTERN.matcher(value);
                    if (matcher.find())
                        house.setLegalSize(Float.parseFloat(matcher.group()));
                    break;
                }

                case "車位種類": {
                    throw new RuntimeException("This a parking slot instead of a house");
                }

                default:
                    this.logMissingInfo(key, value);
            }
        });
    }

    private String getText(Element element, String classname) {
        return element.getElementsByClass(classname).text().replace(" ", "");
    }

    private void logMissingInfo(String key, String value) {
        log.error("key={} is not set in HouseEntity, with value={}", key, value);
    }
}
