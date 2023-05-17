package th.co.prior.market.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import th.co.prior.market.model.MarketPlaceCriteriaModel;
import th.co.prior.market.model.MarketPlaceModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.model.UpdateMarketPlaceModel;
import th.co.prior.market.service.MarketPlaceService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MarketPlaceRestController {

    public MarketPlaceRestController(MarketPlaceService marketPlaceService) {
        this.marketPlaceService = marketPlaceService;
    }
    private MarketPlaceService marketPlaceService;

    @PostMapping("/marketplace")
    public ResponseModel<Integer> insertMarketPlace(@RequestBody List<MarketPlaceModel> marketPlaceModels) {
        return this.marketPlaceService.insertMarketPlaceThenResponse(marketPlaceModels);
    }

    @PostMapping("/find/marketplace")
    public ResponseModel<List<MarketPlaceModel>> findMarketPlaceByMarketPlace(@RequestBody MarketPlaceCriteriaModel marketPlaceCriteriaModel){
        return this.marketPlaceService.findMarketPlaceByMarketPlaceThenResponse(marketPlaceCriteriaModel);
    }

    @PutMapping("/marketplace")
    public ResponseModel<Integer> updateMarketPlace(@RequestBody List<UpdateMarketPlaceModel> marketPlaceModels) {
        log.info("\n{}", marketPlaceModels);
        return this.marketPlaceService.updateMarketPlaceThenResponse(marketPlaceModels);
    }

}
