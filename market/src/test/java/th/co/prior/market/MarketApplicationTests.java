package th.co.prior.market;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import th.co.prior.market.component.InventoryComponent;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.repository.InventoryNativeRepository;
import th.co.prior.market.repository.InventoryNativeRepositoryTest;
import th.co.prior.market.repository.InventoryRepository;
import th.co.prior.market.repository.InventoryRepositoryTest;
import th.co.prior.market.service.InventoryService;

class MarketApplicationTests {


    @Test
    public void test_insertItemToInventory_expect_200() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        InventoryNativeRepository inventoryNativeRepositoryTest = new InventoryNativeRepositoryTest();
        InventoryRepository inventoryRepository = new InventoryRepositoryTest();
        InventoryService inventoryService = new InventoryService(inventoryRepository, inventoryComponent, inventoryNativeRepositoryTest);

        InventoryModel inventoryModel = new InventoryModel();
        inventoryModel.setAccountId(1);
        inventoryModel.setItemName("pencils");
        ResponseModel<Void> result = inventoryService.insertInventoryThenResponse(inventoryModel);
        System.out.println(result.getStatus());
        Assertions.assertTrue(result.getStatus() == 200);
    }
    @Test
    public void test_insertItemToInventory_expect_400() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        InventoryNativeRepository inventoryNativeRepositoryTest = new InventoryNativeRepositoryTest();
        InventoryRepository inventoryRepository = new InventoryRepositoryTest();
        InventoryService inventoryService = new InventoryService(inventoryRepository, inventoryComponent, inventoryNativeRepositoryTest);

        InventoryModel inventoryModel = new InventoryModel();
        inventoryModel.setAccountId(1);
        inventoryModel.setItemName("");
        ResponseModel<Void> result = inventoryService.insertInventoryThenResponse(inventoryModel);
        System.out.println(result.getStatus());
        Assertions.assertTrue(result.getStatus() == 400);
    }

    @Test
    public void test_insertItemToInventory_expect_500() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        InventoryNativeRepository inventoryNativeRepositoryTest = new InventoryNativeRepositoryTest();
        InventoryService inventoryService = new InventoryService(null, inventoryComponent, inventoryNativeRepositoryTest);

        try{
            InventoryModel inventoryModel = new InventoryModel();
            ResponseModel<Void> result = inventoryService.insertInventoryThenResponse(inventoryModel);
            Assertions.assertTrue(result.getStatus() == 500);
        } catch (Exception ex) {
            Assertions.assertTrue(null != ex.getMessage());
        }
    }

}
