package com.example.marketplace.repository;

import com.example.marketplace.model.MarketPlaceCriteriaModel;
import com.example.marketplace.model.MarketPlaceModel;
import com.example.marketplace.model.UpdateMarketPlaceModel;

import java.util.List;

public interface MarketPlaceNativeRepository {
    int insertMarketplaces(List<MarketPlaceModel> marketPlaceModels);
    int updateMarketplaces(List<UpdateMarketPlaceModel> marketPlaceModels);

    List<MarketPlaceModel> findMarketPlaceByMarketPlace(MarketPlaceCriteriaModel marketPlaceCriteriaModels);
}
