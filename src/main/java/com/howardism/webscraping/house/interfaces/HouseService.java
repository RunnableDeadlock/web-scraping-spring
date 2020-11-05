package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Transactional
public interface HouseService {

    Optional<HouseEntity> findOne(String slug);

    Iterable<HouseEntity> findAll();

    HouseEntity parse(String slug) throws IOException;
}

