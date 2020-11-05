package com.howardism.webscraping.house;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "house")
@AllArgsConstructor
@NoArgsConstructor
public class HouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    //    有效期
    private LocalDateTime effectiveDate;
    private Float price;

    /**
     * info-floor
     * 格局,屋齡,權狀坪數
     */
    private String format;
    private Float age = 0f;
    private Float legalSize = 0f;

    /**
     * info-addr
     * 樓層,朝向,社區,地址
     */
    private String floor;
    private String facingDirection = "未知";
    private String apartmentComplex = "未知";
    private String address;

    /**
     * detail-house
     * 現況,型態,裝潢程度,管理費
     * 帶租約,法定用途,車位,公設比
     * 主建物,共用部分,附屬建物,土地坪數
     */
    private String style;
    private String type;
    private String decorationLevel;
    private Float monthlyAdminCost=0f;

    private Boolean withRent;
    private String legalUsage = "未知";
    private String parking;
    private Float sharedRatio=0f;
    private Float mainSize=0f;
    private Float sharedSize=0f;
    private Float extraSize=0f;
    private Float landSize=0f;

    /**
     * other system info
     */

    private LocalDateTime timestamp;
    private Long duration;

    @Column(unique = true)
    private String url;
}
