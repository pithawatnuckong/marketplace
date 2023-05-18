package com.example.marketplace.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MarketPlaceCriteriaModel {
        private Integer id;
        private Integer accountId;
        private Integer inventoryId;
        private Double sellPrice;
        private Boolean isSold;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate createDateStart;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate createDateEnd;
}
