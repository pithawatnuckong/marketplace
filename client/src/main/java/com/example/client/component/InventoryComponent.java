package com.example.client.component;

import com.example.client.entity.InventoryEntity;
import com.example.client.model.InventoryModel;
import com.example.client.model.UpdateMarketPlaceModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryComponent {

    public void inventoryTransformModelToEntity(InventoryModel inventoryModel, InventoryEntity inventoryEntity) {
        inventoryEntity.setId(inventoryModel.getId());
        inventoryEntity.setItemName(inventoryModel.getItemName());
        inventoryEntity.setAccountId(inventoryModel.getAccountId());
        inventoryEntity.setInventoryDate(LocalDateTime.now());
    }


    public List<InventoryModel> marketPlaceTransformToInventory(List<UpdateMarketPlaceModel> marketPlaceModels) {
        List<InventoryModel> inventoryModels = new ArrayList<>();
        for(UpdateMarketPlaceModel model : marketPlaceModels){
            InventoryModel inventoryModel = new InventoryModel();
            inventoryModel.setId(model.getInventoryId());
            inventoryModel.setBuyerId(model.getBuyerId());
            inventoryModel.setAccountId(model.getAccountId());
            inventoryModels.add(inventoryModel);
        }

        return inventoryModels;
    }
}
