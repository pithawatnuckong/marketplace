package th.co.prior.market.repository;

import th.co.prior.market.model.MarketPlaceCriteriaModel;
import th.co.prior.market.model.MarketPlaceModel;
import th.co.prior.market.model.UpdateMarketPlaceModel;

import java.util.List;

public interface MarketPlaceNativeRepository {
    int insertMarketplaces(List<MarketPlaceModel> marketPlaceModels);
    int updateMarketplaces(List<UpdateMarketPlaceModel> marketPlaceModels);

    List<MarketPlaceModel> findMarketPlaceByMarketPlace(MarketPlaceCriteriaModel marketPlaceCriteriaModels);
}
