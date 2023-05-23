package com.example.marketplace.model;

import lombok.Data;

@Data
public class UpdateMarketPlaceModel {
    private Integer id;
    private Integer accountId;
    private Integer inventoryId;
    private Double sellPrice;
    private Boolean isSold;
    private Integer buyerId;
}
