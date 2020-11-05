package com.howardism.webscraping.house;

import com.howardism.webscraping.house.interfaces.HouseParsingService;
import com.howardism.webscraping.house.interfaces.HouseRepository;
import com.howardism.webscraping.house.interfaces.HouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseParsingService houseParsingService;

    @Override
    public HouseEntity parse(String slug) throws IOException {
        HouseEntity house = this.houseParsingService.parse(this.formatUrl(slug));
        return this.houseRepository.save(house);
    }

    @Override
    public Optional<HouseEntity> findOne(String slug) {
        Iterator<HouseEntity> house = this.houseRepository.findByUrl(this.formatUrl(slug)).iterator();
        return house.hasNext() ? Optional.of(house.next()) : Optional.empty();
    }

    @Override
    public Iterable<HouseEntity> findAll() {
        return this.houseRepository.findAll();
    }

    private String formatUrl(String slug) {
        return "https://sale.591.com.tw/home/house/detail/2/" + slug + ".html";
    }
}
