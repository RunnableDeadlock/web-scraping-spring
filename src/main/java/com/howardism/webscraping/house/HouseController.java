package com.howardism.webscraping.house;

import com.googlecode.jmapper.JMapper;
import com.howardism.webscraping.house.dtos.HouseResponseDto;
import com.howardism.webscraping.house.interfaces.HouseService;
import com.howardism.webscraping.utils.IterableHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("houses")
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("{slug}")
    public ResponseEntity<HouseResponseDto> findHouseByUrl(@PathVariable String slug) {
        Optional<HouseEntity> house = this.houseService.findOne(slug);

        if (house.isEmpty()) {
            try {
                HouseEntity newHouse = this.houseService.parse(slug);
                return this.transform(this.convertToDto(newHouse));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource", e);
            }
        }

        return this.transform(this.convertToDto(house.get()));
    }

    @GetMapping()
    public ResponseEntity<Long> getHouses() {
        Iterable<HouseEntity> house = this.houseService.findAll();
        Long count = IterableHelper.size(house);
        return this.transform(count);
    }

    private HouseResponseDto convertToDto(HouseEntity house) {
        JMapper<HouseResponseDto, HouseEntity> mapper = new JMapper<>(HouseResponseDto.class, HouseEntity.class);
        return mapper.getDestination(house);
    }

    private <T> ResponseEntity<T> transform(T arg) {
        return new ResponseEntity<>(arg, HttpStatus.OK);
    }
}
