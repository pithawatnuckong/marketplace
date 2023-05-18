package com.example.client.repository;

import com.example.client.model.InventoryCriteriaModel;
import com.example.client.model.InventoryModel;

import java.util.List;

public interface InventoryNativeRepository {

    List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel);
    void updateNewUser(List<InventoryModel> inventoryModels);
}
