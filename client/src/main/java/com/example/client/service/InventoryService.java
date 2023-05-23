package com.example.client.service;

import com.example.client.component.InventoryComponent;
import com.example.client.entity.InventoryEntity;
import com.example.client.model.InventoryCriteriaModel;
import com.example.client.model.InventoryHistoryModel;
import com.example.client.model.InventoryModel;
import com.example.client.model.ResponseModel;
import com.example.client.repository.InventoryHistoryNativeRepository;
import com.example.client.repository.InventoryNativeRepository;
import com.example.client.repository.InventoryRepository;
import com.example.client.repository.impl.InventoryNativeRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    public InventoryService(InventoryRepository inventoryRepository, InventoryComponent inventoryComponent, InventoryNativeRepository inventoryNativeRepository, InventoryHistoryNativeRepository inventoryHistoryNativeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryComponent = inventoryComponent;
        this.inventoryNativeRepository = inventoryNativeRepository;
        this.inventoryHistoryNativeRepository = inventoryHistoryNativeRepository;
    }

    private final InventoryRepository inventoryRepository;
    private final InventoryComponent inventoryComponent;
    private final InventoryNativeRepository inventoryNativeRepository;
    private final InventoryHistoryNativeRepository inventoryHistoryNativeRepository;

    public ResponseModel<Void> insertInventoryThenResponse(InventoryModel inventoryModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            InventoryEntity inventoryEntity = new InventoryEntity();
            this.inventoryComponent.inventoryTransformModelToEntity(inventoryModel, inventoryEntity);
            this.insertAndUpdateInventory(inventoryEntity);
        } catch (Exception ex) {
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }

    public ResponseModel<List<InventoryModel>> findInventoryByInventoryThenResponse(InventoryCriteriaModel inventoryCriteriaModel){
        ResponseModel<List<InventoryModel>> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            List<InventoryModel> inventoryModels = this.findInventoriesByInventory(inventoryCriteriaModel);
            result.setData(inventoryModels);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }

    public void updateNewUser(List<InventoryModel> inventoryModels){
        try {

            List<InventoryHistoryModel> inventoryHistoryModelList = new ArrayList<>();

            for (InventoryModel inventoryModel : inventoryModels) {
                InventoryHistoryModel inventoryHistoryModel = new InventoryHistoryModel();
                inventoryHistoryModel.setInventoryId(inventoryModel.getId());
                inventoryHistoryModel.setAccountId(inventoryModel.getAccountId());
                inventoryHistoryModel.setCreateDate(LocalDateTime.now());
                inventoryHistoryModelList.add(inventoryHistoryModel);
            }

            this.inventoryHistoryNativeRepository.insertInventoryHistories(inventoryHistoryModelList);

            this.inventoryNativeRepository.updateNewUser(inventoryModels);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void insertAndUpdateInventory(InventoryEntity inventoryEntity){
        this.inventoryRepository.save(inventoryEntity);
    }

    private List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel){
        return this.inventoryNativeRepository.findInventoriesByInventory(inventoryCriteriaModel);
    }
}
