package com.howardism.webscraping.house;

import com.howardism.webscraping.house.interfaces.HouseParsingService;
import com.howardism.webscraping.house.interfaces.HouseRepository;
import com.howardism.webscraping.house.interfaces.HouseService;
import com.howardism.webscraping.house.interfaces.PageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Set<String> existedUrls = new HashSet<>();

    private final HouseRepository houseRepository;
    private final HouseParsingService houseParsingService;
    private final PageService pageService;

    @Override
    public HouseEntity parseAndSave(String slug) throws IOException {
        HouseEntity house = this.houseParsingService.parseSinglePage(this.formatUrl(slug));
        return this.houseRepository.save(house);
    }

    @Override
    public Optional<HouseEntity> findOne(String slug) {
        Iterator<HouseEntity> house = this.houseRepository.findByUrl(this.formatUrl(slug)).iterator();
        return house.hasNext() ? Optional.of(house.next()) : Optional.empty();
    }

    @Override
    public List<HouseEntity> findAll() {
        return this.houseRepository.findAll();
    }

    @Override
    public Long count() {
        return this.houseRepository.count();
    }

    @Override
    public boolean parseIndexPage(String startingSlug) throws InterruptedException {
        init();
        this.pageService.open(startingSlug);
        createParsingTask();

        return true;
    }

    @Override
    public boolean parseIndexPages(String starting, int pageCount) throws InterruptedException {
        this.parseIndexPage(starting);
        int count = 1;
        while (pageCount > count && this.pageService.navigateToNextPage()) {
            createParsingTask();
            count++;
            log.info("Parsing count={}", count);
        }
        return true;
    }

    private String formatUrl(String slug) {
        return "https://sale.591.com.tw/home/house/detail/2/" + slug + ".html";
    }

    private void init() {
        if (this.existedUrls.size() == 0) {
            this.houseRepository.findAll().forEach(houseEntity -> this.existedUrls.add(houseEntity.getUrl()));
        }
    }

    private void createParsingTask() throws InterruptedException {
        String listClassname = "houseList-item-title";
        this.pageService
                .waitUpToTenSecs()
                .until(driver -> driver.findElement(By.className(listClassname)).isDisplayed());
        TimeUnit.MILLISECONDS.sleep(2000 + (int) (1000 * Math.random()));

        List<WebElement> elements = this.pageService.getElementsByClass(listClassname);
        List<String> newUrls = this.houseParsingService.parseIndexPage(elements, this.existedUrls);
        log.info("Parsing {} new urls", newUrls.size());
        newUrls.forEach(url -> executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                HouseEntity house = this.houseParsingService.parseSinglePage(url);
                this.houseRepository.save(house);
            } catch (IOException | InterruptedException e) {
                log.error(e.getMessage());
            }
        }));
    }
}
