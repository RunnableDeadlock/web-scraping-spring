package com.howardism.webscraping.house;

import com.googlecode.jmapper.JMapper;
import com.howardism.webscraping.house.dtos.HouseResponseDto;
import com.howardism.webscraping.house.dtos.ParseHouseDto;
import com.howardism.webscraping.house.interfaces.HouseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("houses")
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("{slug}")
    public ResponseEntity<HouseResponseDto> findHouseBySlug(@PathVariable String slug) {
        Optional<HouseEntity> house = this.houseService.findOne(slug);

        if (house.isEmpty()) {
            try {
                HouseEntity newHouse = this.houseService.parseAndSave(slug);
                return this.transform(this.convertToDto(newHouse));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource", e);
            }
        }

        return this.transform(this.convertToDto(house.get()));
    }

    @GetMapping()
    public ResponseEntity<Long> getHouses() {
        return this.transform(this.houseService.count());
    }

    @PostMapping()
    public ResponseEntity<Boolean> parseHouse(@RequestBody ParseHouseDto parseHouseDto) {
        int pageCounts = parseHouseDto.getCount();

        boolean success;

        try {
            if (pageCounts == 0) {
                success = this.houseService.parseIndexPage(parseHouseDto.getUrl());
            } else {
                success = this.houseService.parseIndexPages(parseHouseDto.getUrl(), parseHouseDto.getCount());
            }
        } catch (InterruptedException e) {
            success = false;
        }

        return this.transform(success);
    }


    private HouseResponseDto convertToDto(HouseEntity house) {
        JMapper<HouseResponseDto, HouseEntity> mapper = new JMapper<>(HouseResponseDto.class, HouseEntity.class);
        return mapper.getDestination(house);
    }

    private <T> ResponseEntity<T> transform(T arg) {
        return new ResponseEntity<>(arg, HttpStatus.OK);
    }
}
