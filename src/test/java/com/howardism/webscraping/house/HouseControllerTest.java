package com.howardism.webscraping.house;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(HouseController.class)
public class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseServiceImpl houseService;

    private List<HouseEntity> houses;

    @BeforeEach
    void setUp() {
        this.houses = new ArrayList<>();
        HouseEntity house = new HouseEntity();
        house.setName("Test House");
        this.houses.add(house);
    }

    @Test
    void findHouseByUrlShouldReturn404() throws Exception {
        Mockito.when(houseService.findOne(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(houseService.parse(Mockito.anyString())).thenThrow(IOException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/houses/Unknown"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getHousesShouldReturn200() throws Exception {
        Mockito.when(houseService.count()).thenReturn(Long.valueOf(houses.size()));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/houses"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
