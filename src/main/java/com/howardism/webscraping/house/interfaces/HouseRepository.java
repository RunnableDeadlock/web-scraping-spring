package com.howardism.webscraping.house.interfaces;

import com.howardism.webscraping.house.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<HouseEntity, Long> {

    List<HouseEntity> findByUrl(String url);
}
