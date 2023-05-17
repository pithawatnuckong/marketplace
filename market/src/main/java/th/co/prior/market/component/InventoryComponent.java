package th.co.prior.market.component;

import org.springframework.stereotype.Component;
import th.co.prior.market.entity.InventoryEntity;
import th.co.prior.market.model.InventoryModel;

import java.time.LocalDateTime;

@Component
public class InventoryComponent {

    public void inventoryTransformModelToEntity(InventoryModel inventoryModel, InventoryEntity inventoryEntity) {
        inventoryEntity.setId(inventoryModel.getId());
        inventoryEntity.setItemName(inventoryModel.getItemName());
        inventoryEntity.setAccountId(inventoryModel.getAccountId());
        inventoryEntity.setInventoryDate(LocalDateTime.now());
    }
}
