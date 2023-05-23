package com.example.client.controller.rest;

import com.example.client.model.InventoryCriteriaModel;
import com.example.client.model.InventoryModel;
import com.example.client.model.ResponseModel;
import com.example.client.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
