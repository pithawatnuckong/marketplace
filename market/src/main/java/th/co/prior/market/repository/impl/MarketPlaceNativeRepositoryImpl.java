package th.co.prior.market.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.co.prior.market.model.MarketPlaceCriteriaModel;
import th.co.prior.market.model.MarketPlaceModel;
import th.co.prior.market.model.UpdateMarketPlaceModel;
import th.co.prior.market.repository.MarketPlaceNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
@Slf4j
public class MarketPlaceNativeRepositoryImpl implements MarketPlaceNativeRepository {

    public MarketPlaceNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;

    @Override
    public int insertMarketplaces(List<MarketPlaceModel> marketPlaceModels) {
        StringBuilder sb = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(",");
        List<Object> paramList = new ArrayList<>();
        sb.append(" INSERT INTO market_place (account_id, inventory_id, sell_price, create_date, is_sold) ");
        sb.append(" VALUES ");
        for (MarketPlaceModel model: marketPlaceModels){
            String value = " (?, ?, ?, ?, ?) ";
            paramList.add(model.getAccountId());
            paramList.add(model.getInventoryId());
            paramList.add(model.getSellPrice());
            paramList.add(LocalDateTime.now());
            paramList.add(false);
            stringJoiner.add(value);
        }

        sb.append(stringJoiner.toString());

        int insertRow = this.jdbcTemplate.update(sb.toString(), paramList.toArray());
        return insertRow;
    }

    @Override
    public int updateMarketplaces(List<UpdateMarketPlaceModel> marketPlaceModels) {
        StringBuilder sb = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(",","(",")");
        List<Object> paramList = new ArrayList<>();
        sb.append(" UPDATE market_place ");
        sb.append(" SET is_sold = true ");
        sb.append(" WHERE is_sold = false AND id IN ");
        for (UpdateMarketPlaceModel model : marketPlaceModels) {
            stringJoiner.add(" ? ");
            paramList.add(model.getId());
        }

        sb.append(stringJoiner.toString());

        int updateRow = this.jdbcTemplate.update(sb.toString(), paramList.toArray());
        return updateRow;
    }

    @Override
    public List<MarketPlaceModel> findMarketPlaceByMarketPlace(MarketPlaceCriteriaModel marketPlaceCriteriaModels) {
        log.info("This is a model\n{}", marketPlaceCriteriaModels);
        StringBuilder sb = new StringBuilder();
        List<Object> paramList = new ArrayList<>();
        sb.append(" SELECT m.id, m.account_id, m.inventory_id, m.sell_price, m.create_date, m.is_sold ");
        sb.append(" FROM market_place m");
        sb.append(" WHERE m.is_sold = false ");
        if(marketPlaceCriteriaModels.getAccountId() != null) {
            sb.append(" AND account_id = ? ");
            paramList.add(marketPlaceCriteriaModels.getAccountId());
        }
        if(marketPlaceCriteriaModels.getId() != null) {
            sb.append(" AND id = ? ");
            paramList.add(marketPlaceCriteriaModels.getId());
        }
        if(marketPlaceCriteriaModels.getInventoryId() != null){
            sb.append(" AND inventory_id = ?");
            paramList.add(marketPlaceCriteriaModels.getInventoryId());
        }
        if(marketPlaceCriteriaModels.getCreateDateStart() != null && marketPlaceCriteriaModels.getCreateDateEnd() == null){
            sb.append(" AND create_date >= ?");
            paramList.add(marketPlaceCriteriaModels.getCreateDateStart());
        }
        else if(marketPlaceCriteriaModels.getCreateDateStart() != null && marketPlaceCriteriaModels.getCreateDateEnd() != null){
            sb.append(" AND create_date BETWEEN ? AND ? ");
            paramList.add(marketPlaceCriteriaModels.getCreateDateStart());
            paramList.add(marketPlaceCriteriaModels.getCreateDateEnd());
        }

        log.info("This is a query\n{}", sb.toString());

            List<MarketPlaceModel> marketPlaceModels = this.jdbcTemplate.query(sb.toString(), new RowMapper<MarketPlaceModel>() {
                @Override
                public MarketPlaceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                    MarketPlaceModel model = new MarketPlaceModel();
                    int col = 1;
                    model.setId(rs.getInt(col++));
                    model.setAccountId(rs.getInt(col++));
                    model.setInventoryId(rs.getInt(col++));
                    model.setSellPrice(rs.getDouble(col++));
                    model.setCreateDate(rs.getTimestamp(col++).toLocalDateTime());
                    model.setIsSold(rs.getBoolean(col));
                    return model;
                }
            }, paramList.toArray());
            return marketPlaceModels;

    }


}
