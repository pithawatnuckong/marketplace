package com.example.marketplace.service;

import com.example.marketplace.component.AccountComponent;
import com.example.marketplace.component.kafka.KafkaProducerComponent;
import com.example.marketplace.entity.AccountEntity;
import com.example.marketplace.entity.InventoryEntity;
import com.example.marketplace.exception.BadRequest;
import com.example.marketplace.model.*;
import com.example.marketplace.repository.AccountRepository;
import com.example.marketplace.repository.InventoryRepository;
import com.example.marketplace.repository.impl.MarketPlaceNativeRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarketPlaceService {

    @Value("${kafka.topics.marketplace.create}")
    private String createTopicKafka;

    @Value("${kafka.topics.marketplace.update}")
    private String updateTopicKafka;

    public MarketPlaceService(
            MarketPlaceNativeRepositoryImpl marketPlaceNativeRepository,
            InventoryRepository inventoryRepository,
            AccountRepository accountRepository,
            AccountComponent accountComponent,
            @Qualifier("mapper") ObjectMapper mapper,
            KafkaProducerComponent kafkaProducerComponent)
    {
        this.marketPlaceNativeRepository = marketPlaceNativeRepository;
        this.inventoryRepository = inventoryRepository;
        this.accountRepository = accountRepository;
        this.accountComponent = accountComponent;
        this.mapper = mapper;
        this.kafkaProducerComponent = kafkaProducerComponent;
    }

    private final MarketPlaceNativeRepositoryImpl marketPlaceNativeRepository;
    private final InventoryRepository inventoryRepository;
    private final AccountRepository accountRepository;
    private final AccountComponent accountComponent;
    private final ObjectMapper mapper;
    private final KafkaProducerComponent kafkaProducerComponent;

    public ResponseModel<Integer> insertMarketPlaceThenResponse(List<MarketPlaceModel> marketPlaceModels){
        ResponseModel<Integer> result = new ResponseModel<>();
        result.setStatus(201);
        result.setDescription("ok");
        try {
            int insertRow = this.insertMarketPlace(marketPlaceModels);

            if (insertRow == 0){
                throw new BadRequest("Your item doesn't exists");
            }

            String message = this.mapper.writeValueAsString(marketPlaceModels);
            this.kafkaProducerComponent.sendMessageToKafka(message, createTopicKafka);

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

             AccountModel buyerModel = this.findAccountById(marketPlaceModels.get(0).getBuyerId());

             double totalPriceOfItem = 0.0;

             for (UpdateMarketPlaceModel model : marketPlaceModels){
                 totalPriceOfItem += model.getSellPrice();
             }

            if(buyerModel.getCashAmount() < totalPriceOfItem){
                throw new BadRequest("Your cash is not enough");
            }

            int insertRow = this.updateMarketPlace(marketPlaceModels);

            if(insertRow == 0){
                throw new BadRequest("Items is already sold out");
            }

            String message = this.mapper.writeValueAsString(marketPlaceModels);
            this.kafkaProducerComponent.sendMessageToKafka(message, updateTopicKafka);

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

    private AccountModel findAccountById(Integer id) {
        Optional<AccountEntity> accountEntityOptional = this.accountRepository.findById(id);
        if(accountEntityOptional.isPresent()){
            AccountModel accountModel = this.accountComponent.accountTransformEntityToModel(accountEntityOptional.get());
            return accountModel;
        }
        return new AccountModel();
    }
}
