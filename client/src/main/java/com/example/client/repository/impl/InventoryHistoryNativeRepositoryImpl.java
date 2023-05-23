package com.example.client.repository.impl;

import com.example.client.model.InventoryHistoryModel;
import com.example.client.repository.InventoryHistoryNativeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class InventoryHistoryNativeRepositoryImpl implements InventoryHistoryNativeRepository {

    private final JdbcTemplate jdbcTemplate;

    public InventoryHistoryNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertInventoryHistories(List<InventoryHistoryModel> inventoryHistoryModelList) {
        List<Object> paramList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(" , ");
        sb.append(" INSERT INTO inventory_history (id, account_id, inventory_id, create_date) ");
        sb.append(" VALUES ");

        for (InventoryHistoryModel model: inventoryHistoryModelList){
            String value = (" ((SELECT MAX(id) + 1 FROM inventory_history ih), ?, ?, ?) ");
            System.out.println(model.getAccountId());
            paramList.add(model.getAccountId());
            paramList.add(model.getInventoryId());
            paramList.add(model.getCreateDate());
            stringJoiner.add(value);
        }

        sb.append(stringJoiner.toString());

        int insertRow = this.jdbcTemplate.update(sb.toString(), paramList.toArray());
        return insertRow;
    }
}
