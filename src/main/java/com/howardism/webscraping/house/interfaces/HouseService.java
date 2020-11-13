package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Transactional
public interface HouseService {

    Optional<HouseEntity> findOne(String slug);

    List<HouseEntity> findAll();

    HouseEntity parseAndSave(String slug) throws IOException;

    Long count();

    boolean parseIndexPage(String startingUrl) throws InterruptedException;

    boolean parseIndexPages(String startingUrl, int pageCount) throws InterruptedException;
}

