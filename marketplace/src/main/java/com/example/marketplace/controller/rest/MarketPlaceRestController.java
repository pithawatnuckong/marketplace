package com.example.marketplace.controller.rest;

import com.example.marketplace.model.MarketPlaceCriteriaModel;
import com.example.marketplace.model.MarketPlaceModel;
import com.example.marketplace.model.ResponseModel;
import com.example.marketplace.model.UpdateMarketPlaceModel;
import com.example.marketplace.service.MarketPlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MarketPlaceRestController {

    public MarketPlaceRestController(MarketPlaceService marketPlaceService) {
        this.marketPlaceService = marketPlaceService;
    }
    private final MarketPlaceService marketPlaceService;

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
        return this.marketPlaceService.updateMarketPlaceThenResponse(marketPlaceModels);
    }

}
