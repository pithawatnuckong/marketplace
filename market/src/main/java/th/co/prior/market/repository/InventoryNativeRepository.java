package th.co.prior.market.repository;

import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;

import java.util.List;

public interface InventoryNativeRepository {

    List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel);
    void updateNewUser(List<InventoryModel> inventoryModels);
}
