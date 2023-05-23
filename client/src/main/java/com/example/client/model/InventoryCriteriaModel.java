package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryCriteriaModel {

    private Integer id;
    private Integer accountId;
    private String itemName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inventoryDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inventoryDateEnd;
}
