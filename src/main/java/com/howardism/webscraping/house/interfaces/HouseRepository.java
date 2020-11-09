package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HouseRepository extends CrudRepository<HouseEntity, Long> {

    List<HouseEntity> findByUrl(String url);
}
