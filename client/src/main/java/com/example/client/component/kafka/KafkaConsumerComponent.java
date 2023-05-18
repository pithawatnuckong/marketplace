package com.example.client.component.kafka;

import com.example.client.component.InventoryComponent;
import com.example.client.model.*;
import com.example.client.service.AccountService;
import com.example.client.service.InboxService;
import com.example.client.service.InventoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class KafkaConsumerComponent {

    public KafkaConsumerComponent(InboxService inboxService, @Qualifier("mapper")ObjectMapper mapper, AccountService accountService, InventoryComponent inventoryComponent, InventoryService inventoryService) {
        this.inboxService = inboxService;
        this.mapper = mapper;
        this.accountService = accountService;
        this.inventoryComponent = inventoryComponent;
        this.inventoryService = inventoryService;
    }
    private InboxService inboxService;
    private ObjectMapper mapper;
    private AccountService accountService;
    private InventoryComponent inventoryComponent;
    private InventoryService inventoryService;


    @KafkaListener(topics = "${kafka.topics.marketplace.create}", groupId = "${kafka.groupid.inbox}")
    public void insertInboxWhenInsertMarketPlaceByKafkaMessage(@Payload String message) throws Exception {
        List<MarketPlaceModel> marketPlaceModels = mapper.readValue(message, new TypeReference<List<MarketPlaceModel>>() {});

        InboxModel inboxModel = new InboxModel();
        inboxModel.setToAccountId(marketPlaceModels.get(0).getAccountId());
        inboxModel.setFromAccountId(marketPlaceModels.get(0).getAccountId());
        String inboxMessage = "Your items have been selling on the marketplace at :" + LocalDateTime.now();
        inboxModel.setMessage(inboxMessage);

        this.inboxService.insertInboxThenResponse(inboxModel);
    }

    @KafkaListener(topics = "${kafka.topics.marketplace.update}", groupId = "${kafka.groupid.buyer_account}")
    public void UpdateBuyerAccountWhenUpdateMarketPlaceByKafkaMessage(@Payload String message) throws Exception {
        List<UpdateMarketPlaceModel> marketPlaceModels = mapper.readValue(message, new TypeReference<List<UpdateMarketPlaceModel>>() {});

        double totalCashAmount = 0.0;

        for(UpdateMarketPlaceModel marketPlaceModel: marketPlaceModels){
            totalCashAmount += marketPlaceModel.getSellPrice();
        }

        AccountModel buyerModel = this.accountService.findAccountById(marketPlaceModels.get(0).getBuyerId());
        double newBuyerCashAmount = buyerModel.getCashAmount() - totalCashAmount;
        buyerModel.setCashAmount(newBuyerCashAmount);

        this.accountService.insertAndUpdateAccount(buyerModel);
    }


    @KafkaListener(topics = "${kafka.topics.marketplace.update}", groupId = "${kafka.groupid.seller_account}")
    public void UpdateBuyerSellerWhenUpdateMarketPlaceByKafkaMessage(@Payload String message) throws Exception {
        List<UpdateMarketPlaceModel> marketPlaceModels = mapper.readValue(message, new TypeReference<List<UpdateMarketPlaceModel>>() {});

        double totalCashAmount = 0.0;

        for(UpdateMarketPlaceModel marketPlaceModel: marketPlaceModels){
            totalCashAmount += marketPlaceModel.getSellPrice();
        }

        AccountModel sellerAccount = this.accountService.findAccountById(marketPlaceModels.get(0).getAccountId());
        double newSellerCashAmount = sellerAccount.getCashAmount() + totalCashAmount;
        sellerAccount.setCashAmount(newSellerCashAmount);

        this.accountService.insertAndUpdateAccount(sellerAccount);
    }

    @KafkaListener(topics = "${kafka.topics.marketplace.update}", groupId = "${kafka.groupid.inbox}")
    public void insertInboxWhenUpdateMarketPlaceByKafkaMessage(@Payload String message) throws Exception {
        List<UpdateMarketPlaceModel> marketPlaceModels = mapper.readValue(message, new TypeReference<List<UpdateMarketPlaceModel>>() {});

        InboxModel inboxModel = new InboxModel();
        inboxModel.setToAccountId(marketPlaceModels.get(0).getAccountId());
        inboxModel.setFromAccountId(marketPlaceModels.get(0).getBuyerId());
        String inboxMessage = "Your items was sold out by user id " + marketPlaceModels.get(0).getBuyerId() + " at : " + LocalDateTime.now();
        inboxModel.setMessage(inboxMessage);

        this.inboxService.insertInbox(inboxModel);
    }

    @KafkaListener(topics = "${kafka.topics.marketplace.update}", groupId = "${kafka.groupid.inventory}")
    public void updateInventoryWhenUpdateMarketplaceByKafkaMessage(@Payload String message) throws Exception {
        List<UpdateMarketPlaceModel> marketPlaceModels = mapper.readValue(message, new TypeReference<List<UpdateMarketPlaceModel>>() {});

        List<InventoryModel> inventoryModels = this.inventoryComponent.marketPlaceTransformToInventory(marketPlaceModels);
        this.inventoryService.updateNewUser(inventoryModels);

    }
}
