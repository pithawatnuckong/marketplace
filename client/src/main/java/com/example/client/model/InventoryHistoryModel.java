package com.example.client.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryHistoryModel {
    private int id;
    private int accountId;
    private int inventoryId;
    private LocalDateTime createDate;
}
