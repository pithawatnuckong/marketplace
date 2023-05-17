package th.co.prior.market.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.co.prior.market.model.InventoryCriteriaModel;
import th.co.prior.market.model.InventoryModel;
import th.co.prior.market.repository.InventoryNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class InventoryNativeRepositoryImpl implements InventoryNativeRepository {

    public InventoryNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<InventoryModel> findInventoriesByInventory(InventoryCriteriaModel inventoryCriteriaModel) {
        StringBuilder sb = new StringBuilder();
        List<Object> paramList = new ArrayList<>();
        sb.append(" SELECT id, account_id, item_name, inventory_date ");
        sb.append(" FROM inventory ");
        sb.append(" WHERE 1=1 ");
        if (null != inventoryCriteriaModel.getId()) {
            sb.append(" AND id = ? ");
            paramList.add(inventoryCriteriaModel.getId());
        }
        if (!StringUtils.isEmpty(inventoryCriteriaModel.getItemName())){
            sb.append(" AND item_name = ? ");
            paramList.add(inventoryCriteriaModel.getItemName());
        }
        if (null != inventoryCriteriaModel.getAccountId()){
            sb.append(" AND account_id = ? ");
            paramList.add(inventoryCriteriaModel.getAccountId());
        }
        if (inventoryCriteriaModel.getInventoryDateStart() != null && inventoryCriteriaModel.getInventoryDateStart() == null) {
            sb.append(" AND inventory_date >= ? ");
            paramList.add(inventoryCriteriaModel.getInventoryDateStart());
        } else if (inventoryCriteriaModel.getInventoryDateStart() != null && inventoryCriteriaModel.getInventoryDateStart() != null) {
            sb.append(" AND inventory_date BETWEEN ? AND ? ");
            paramList.add(inventoryCriteriaModel.getInventoryDateStart());
            paramList.add(inventoryCriteriaModel.getInventoryDateEnd());
        }

            List<InventoryModel> results = this.jdbcTemplate.query(sb.toString(), new RowMapper<InventoryModel>() {
                @Override
                public InventoryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                    InventoryModel model = new InventoryModel();
                    int col = 1;
                    model.setId(rs.getInt(col++));
                    model.setAccountId(rs.getInt(col++));
                    model.setItemName(rs.getString(col++));
                    model.setInventoryDate(rs.getTimestamp(col).toLocalDateTime());
                    return model;
                }
            }, paramList.toArray());

            return results;
    }
    @Override
    public void updateNewUser(List<InventoryModel> inventoryModels) {
            StringBuilder sb = new StringBuilder();
            StringJoiner stringJoiner = new StringJoiner(",","(",")");
            List<Object> paramList = new ArrayList<>();
            sb.append(" UPDATE inventory ");
            sb.append(" SET account_id = ? ");
            paramList.add(inventoryModels.get(0).getBuyerId());
            sb.append(" WHERE id IN ");
            for (InventoryModel model : inventoryModels) {
                stringJoiner.add(" ? ");
                paramList.add(model.getId());
            }

            sb.append(stringJoiner.toString());

            this.jdbcTemplate.update(sb.toString(), paramList.toArray());
    }
}
