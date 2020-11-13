package com.howardism.webscraping.house.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParseHouseDto {

    private String url;
    private int count = 0;
}
