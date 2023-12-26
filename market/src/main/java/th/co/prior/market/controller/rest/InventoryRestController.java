package th.co.prior.market.controller.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InventoryRestController {

    public InventoryRestController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    private final InventoryService inventoryService;

    @PostMapping("/inventory")
    public ResponseModel<Void> insertInventory(@RequestBody InventoryModel inventoryModel)  {
        return this.inventoryService.insertInventoryThenResponse(inventoryModel);
    }

    @PostMapping("/find/inventory")
    public ResponseModel<List<InventoryModel>> findInventoriesByInventory(@RequestBody InventoryCriteriaModel inventoryCriteriaModel){
        return this.inventoryService.findInventoryByInventoryThenResponse(inventoryCriteriaModel);
    }
}
