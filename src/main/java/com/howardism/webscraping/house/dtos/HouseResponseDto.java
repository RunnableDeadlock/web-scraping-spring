package com.howardism.webscraping.house.dtos;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class HouseResponseDto {

    @JMap
    private String name;

    @JMap
    private LocalDateTime effectiveDate;

    @JMap
    @Column(nullable = false)
    private Float price;

    @JMap
    private String format;

    @JMap
    private Float age;

    @JMap
    private Float legalSize;

    @JMap
    private String floor;

    @JMap
    private String facingDirection;

    @JMap
    private String apartmentComplex;

    @JMap
    private String address;

    @JMap
    @Column(nullable = false)
    private String style;

    @JMap
    private String type;

    @JMap
    private String decorationLevel;

    @JMap
    private Float monthlyAdminCost;

    @JMap
    private Boolean withRent;

    @JMap
    private String legalUsage;

    @JMap
    private String parking;

    @JMap
    private Float sharedRatio;

    @JMap
    private Float mainSize;

    @JMap
    private Float sharedSize;

    @JMap
    private Float extraSize;

    @JMap
    private Float landSize;

    @JMap
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @JMap
    @Column(nullable = false)
    private Long duration;

    @JMap
    @Column(nullable = false)
    private String url;
}
