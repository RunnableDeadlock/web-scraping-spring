package com.howardism.webscraping.house;

import com.howardism.webscraping.house.interfaces.HouseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class HouseServiceImplTest {

    @InjectMocks
    private HouseServiceImpl houseService;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseParsingServiceImpl houseParsingService;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        String testUrl = "https://sale.591.com.tw/home/house/detail/2/123.html";

        HouseEntity house = new HouseEntity();
        house.setName("Test House");
        List<HouseEntity> houses = new ArrayList<>();
        houses.add(house);
        Mockito.when(houseRepository.findByUrl(testUrl)).thenReturn(houses);
        Mockito.when(houseRepository.save(house)).thenReturn(house);
        Mockito.when(houseRepository.count()).thenReturn(1L);
        Mockito.when(houseRepository.findAll()).thenReturn(houses);
        Mockito.when(houseParsingService.parseSinglePage(testUrl)).thenReturn(house);
    }

    @Test
    void parseShouldReturnHouse() throws IOException {
        HouseEntity house = this.houseService.parse("123");
        Assertions.assertEquals(house.getName(), "Test House");
    }

    @Test()
    void parseShouldThrowGivenUnknownUrl() throws IOException {
        Mockito.when(houseParsingService.parseSinglePage(Mockito.anyString())).thenThrow(IOException.class);
        Assertions.assertThrows(IOException.class, () -> this.houseService.parse("unknown"));
    }

    @Test
    void findOneShouldReturnHouse() {
        Optional<HouseEntity> house = this.houseService.findOne("123");
        Assertions.assertFalse(house.isEmpty());
        Assertions.assertEquals(house.get().getName(), "Test House");
    }

    @Test
    void findOneShouldReturnNoHouse() {
        Optional<HouseEntity> house = this.houseService.findOne("Unknown");
        Assertions.assertTrue(house.isEmpty());
    }

    @Test
    void countShouldReturnOne() {
        Long count = this.houseService.count();
        Assertions.assertEquals(count, 1L);
    }

    @Test
    void findAllShouldReturnHouses() {
        List<HouseEntity> houses = this.houseService.findAll();
        houses.forEach(house -> Assertions.assertEquals(house.getName(), "Test House"));
    }
}