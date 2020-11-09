package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;

import java.io.IOException;

public interface HouseParsingService {

    HouseEntity parse(String url) throws IOException;
}
