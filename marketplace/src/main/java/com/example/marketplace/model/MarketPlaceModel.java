package com.example.marketplace.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MarketPlaceModel {
    private Integer id;
    private Integer accountId;
    private LocalDateTime createDate;
    private Integer inventoryId;
    private Double sellPrice;
    private Boolean isSold;
}
