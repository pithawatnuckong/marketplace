package th.co.prior.market.repository;

import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;

import java.util.List;

public class InventoryNativeRepositoryTest implements InventoryNativeRepository {
    @Override
    public List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel) {
        return null;
    }

    @Override
    public void updateNewUser(List<InventoryModel> inventoryModels) {

    }
}
