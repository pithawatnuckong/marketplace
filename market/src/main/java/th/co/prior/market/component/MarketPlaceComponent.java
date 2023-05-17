package th.co.prior.market.component;

import org.springframework.stereotype.Component;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.model.UpdateMarketPlaceModel;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarketPlaceComponent {

    public List<InventoryModel> marketPlaceTransformToInventory(List<UpdateMarketPlaceModel> marketPlaceModels) {
        List<InventoryModel> inventoryModels = new ArrayList<>();
        for(UpdateMarketPlaceModel model : marketPlaceModels){
            InventoryModel inventoryModel = new InventoryModel();
            inventoryModel.setId(model.getInventoryId());
            inventoryModel.setBuyerId(model.getBuyerId());
            inventoryModels.add(inventoryModel);
        }

        return inventoryModels;
    }
}
