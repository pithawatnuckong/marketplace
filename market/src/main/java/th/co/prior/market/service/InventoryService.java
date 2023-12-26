package th.co.prior.market.service;

import org.springframework.stereotype.Service;
import th.co.prior.market.component.InventoryComponent;
import th.co.prior.market.entity.InventoryEntity;
import th.co.prior.market.exception.BadRequest;
import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.repository.InventoryNativeRepository;
import th.co.prior.market.repository.InventoryRepository;

import java.util.List;

@Service
public class InventoryService {

    public InventoryService(InventoryRepository inventoryRepository, InventoryComponent inventoryComponent, InventoryNativeRepository inventoryNativeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryComponent = inventoryComponent;
        this.inventoryNativeRepositoryImpl = inventoryNativeRepository;
    }

    private final InventoryRepository inventoryRepository;
    private final InventoryComponent inventoryComponent;
    private final InventoryNativeRepository inventoryNativeRepositoryImpl;

    public ResponseModel<Void> insertInventoryThenResponse(InventoryModel inventoryModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            if (inventoryModel.getItemName().equals("")) {
                throw new BadRequest("Inventory name cannot empty");
            }
            InventoryEntity inventoryEntity = new InventoryEntity();
            this.inventoryComponent.inventoryTransformModelToEntity(inventoryModel, inventoryEntity);
            this.insertAndUpdateInventory(inventoryEntity);
        }
        catch (BadRequest ex) {
            result.setStatus(400);
            result.setDescription(ex.getMessage());
        }
        catch (Exception ex) {
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
