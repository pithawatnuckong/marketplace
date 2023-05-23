package com.example.marketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryModel {

    private Integer id;
    private Integer accountId;
    private String itemName;
    private LocalDateTime inventoryDate;

    @JsonIgnore
    private Integer buyerId;
}


