package com.example.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "market_place")
@Data
public class MarketPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Integer accountId;

    @Column(nullable = false)
    private Integer inventoryId;

    private Double sellPrice;
    private LocalDateTime createDate;
    private Boolean isSold;
}
