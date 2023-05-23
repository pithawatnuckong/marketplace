package com.example.client.repository;

import com.example.client.model.InventoryHistoryModel;

import java.util.List;

public interface InventoryHistoryNativeRepository {
    int insertInventoryHistories(List<InventoryHistoryModel> inventoryHistoryModelList);
}
