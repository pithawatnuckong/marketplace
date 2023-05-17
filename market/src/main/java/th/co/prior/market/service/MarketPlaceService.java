package th.co.prior.market.service;

import org.springframework.stereotype.Service;
import th.co.prior.market.component.MarketPlaceComponent;
import th.co.prior.market.entity.InventoryEntity;
import th.co.prior.market.exception.BadRequest;
import th.co.prior.market.model.*;
import th.co.prior.market.repository.InventoryRepository;
import th.co.prior.market.repository.impl.MarketPlaceNativeRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MarketPlaceService {

    public MarketPlaceService(MarketPlaceNativeRepositoryImpl marketPlaceNativeRepository, InventoryService inventoryService, MarketPlaceComponent marketPlaceComponent, InventoryRepository inventoryRepository, InboxService inboxService, AccountService accountService) {
        this.marketPlaceNativeRepository = marketPlaceNativeRepository;
        this.inventoryService = inventoryService;
        this.marketPlaceComponent = marketPlaceComponent;
        this.inventoryRepository = inventoryRepository;
        this.inboxService = inboxService;
        this.accountService = accountService;
    }

    private final MarketPlaceNativeRepositoryImpl marketPlaceNativeRepository;
    private final InventoryService inventoryService;
    private final MarketPlaceComponent marketPlaceComponent;
    private final InventoryRepository inventoryRepository;

    private final InboxService inboxService;

    private final AccountService accountService;

    public ResponseModel<Integer> insertMarketPlaceThenResponse(List<MarketPlaceModel> marketPlaceModels){
        ResponseModel<Integer> result = new ResponseModel<>();
        result.setStatus(201);
        result.setDescription("ok");
        try {
            int insertRow = this.insertMarketPlace(marketPlaceModels);

            if (insertRow == 0){
                throw new BadRequest("Your item doesn't exists");
            }

            InboxModel inboxModel = new InboxModel();

            inboxModel.setToAccountId(marketPlaceModels.get(0).getAccountId());
            inboxModel.setFromAccountId(marketPlaceModels.get(0).getAccountId());
            String message = "Your items have been selling on the marketplace at :" + LocalDateTime.now();
            inboxModel.setMessage(message);

            this.inboxService.insertInboxThenResponse(inboxModel);
            result.setData(insertRow);
        }
        catch (BadRequest ex){
            result.setStatus(404);
            result.setDescription(ex.getMessage());
        }
        catch (Exception ex){
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }


    public ResponseModel<Integer> updateMarketPlaceThenResponse(List<UpdateMarketPlaceModel> marketPlaceModels){
        ResponseModel<Integer> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {

             AccountModel buyerModel = this.accountService.findAccountById(marketPlaceModels.get(0).getBuyerId());

             double totalPriceOfItem = 0.0;

             for (UpdateMarketPlaceModel model : marketPlaceModels){
                 totalPriceOfItem += model.getSellPrice();
             }

            if(buyerModel.getCashAmount() < totalPriceOfItem){
                throw new BadRequest("Your cash is not enough");
            }

            double newBuyerCashAmount = buyerModel.getCashAmount() - totalPriceOfItem;
            buyerModel.setCashAmount(newBuyerCashAmount);

            this.accountService.insertAccountAndUpdateThenResponse(buyerModel);

            AccountModel sellerModel = this.accountService.findAccountById(marketPlaceModels.get(0).getAccountId());

            double newSellerCashAmount = sellerModel.getCashAmount() + totalPriceOfItem;
            sellerModel.setCashAmount(newSellerCashAmount);

            this.accountService.insertAccountAndUpdateThenResponse(sellerModel);

            int insertRow = this.updateMarketPlace(marketPlaceModels);

            if(insertRow == 0){
                throw new BadRequest("Items is already sold out");
            }

            InboxModel inboxModel = new InboxModel();
            inboxModel.setToAccountId(marketPlaceModels.get(0).getAccountId());
            inboxModel.setFromAccountId(marketPlaceModels.get(0).getBuyerId());
            String message = "Your items was sold out by user id " + marketPlaceModels.get(0).getBuyerId() + " at : " + LocalDateTime.now();
            inboxModel.setMessage(message);

            this.inboxService.insertInboxThenResponse(inboxModel);

            List<InventoryModel> inventoryModels = this.marketPlaceComponent.marketPlaceTransformToInventory(marketPlaceModels);

            if(inventoryModels.size() > 0){
                this.inventoryService.updateNewUser(inventoryModels);
            }

            result.setData(insertRow);

        }
        catch (BadRequest ex){
            result.setStatus(404);
            result.setDescription(ex.getMessage());
        }
        catch (Exception ex){
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }

        return result;
    }

    public ResponseModel<List<MarketPlaceModel>> findMarketPlaceByMarketPlaceThenResponse(MarketPlaceCriteriaModel marketPlaceCriteriaModel){
        ResponseModel<List<MarketPlaceModel>> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            List<MarketPlaceModel> marketPlaceModels = this.findMarketPlaceByMarketPlace(marketPlaceCriteriaModel);
            result.setData(marketPlaceModels);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }

    private int insertMarketPlace(List<MarketPlaceModel> marketPlaceModels){
        return this.marketPlaceNativeRepository.insertMarketplaces(marketPlaceModels);
    }

    private List<MarketPlaceModel> findMarketPlaceByMarketPlace(MarketPlaceCriteriaModel marketPlaceCriteriaModels) {
        return this.marketPlaceNativeRepository.findMarketPlaceByMarketPlace(marketPlaceCriteriaModels);
    }

    public int updateMarketPlace(List<UpdateMarketPlaceModel> marketPlaceModels){
        for (UpdateMarketPlaceModel model: marketPlaceModels) {
            Optional<InventoryEntity> inventoryEntity = this.inventoryRepository.findById(model.getInventoryId());
            if(inventoryEntity.isEmpty()){
                return 0;
            }
        }
        return this.marketPlaceNativeRepository.updateMarketplaces(marketPlaceModels);
    }
}
