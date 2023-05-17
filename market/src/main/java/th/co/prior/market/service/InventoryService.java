package th.co.prior.market.service;

import org.springframework.stereotype.Service;
import th.co.prior.market.component.InventoryComponent;
import th.co.prior.market.entity.InventoryEntity;
import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.repository.InventoryRepository;
import th.co.prior.market.repository.impl.InventoryNativeRepositoryImpl;

import java.util.List;

@Service
public class InventoryService {

    public InventoryService(InventoryRepository inventoryRepository, InventoryComponent inventoryComponent, InventoryNativeRepositoryImpl inventoryNativeRepositoryImpl) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryComponent = inventoryComponent;
        this.inventoryNativeRepositoryImpl = inventoryNativeRepositoryImpl;
    }

    private InventoryRepository inventoryRepository;
    private InventoryComponent inventoryComponent;
    private InventoryNativeRepositoryImpl inventoryNativeRepositoryImpl;

    public ResponseModel<Void> insertInventoryThenResponse(InventoryModel inventoryModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            InventoryEntity inventoryEntity = new InventoryEntity();
            this.inventoryComponent.inventoryTransformModelToEntity(inventoryModel, inventoryEntity);
            System.out.println(inventoryEntity);
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
            this.inventoryNativeRepositoryImpl.updateNewUser(inventoryModels);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void insertAndUpdateInventory(InventoryEntity inventoryEntity){
        this.inventoryRepository.save(inventoryEntity);
    }

    private List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel){
        return this.inventoryNativeRepositoryImpl.findInventoriesByInventory(inventoryCriteriaModel);
    }
}
