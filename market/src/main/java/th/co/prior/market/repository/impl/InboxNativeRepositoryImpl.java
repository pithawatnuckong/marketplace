package th.co.prior.market.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.co.prior.market.model.InboxModel;
import th.co.prior.market.repository.InboxNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class InboxNativeRepositoryImpl implements InboxNativeRepository {

    public InboxNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<InboxModel> findInboxByInbox(InboxModel inboxModel) {
        StringBuilder sb = new StringBuilder();
        List<Object> paramList = new ArrayList<>();
        sb.append(" SELECT id, from_account_id, to_account_id, message, create_date, status ");
        sb.append(" FROM inbox ");
        sb.append(" WHERE 1=1 ");
        if (inboxModel.getFromAccountId() != null){
            sb.append(" AND from_account_id = ? ");
            paramList.add(inboxModel.getFromAccountId());
        }
        if (inboxModel.getToAccountId() != null) {
            sb.append(" AND to_account_id = ? ");
            paramList.add(inboxModel.getToAccountId());
        }
        if (!StringUtils.isEmpty(inboxModel.getMessage())) {
            sb.append(" AND message = ? ");
            paramList.add(inboxModel.getMessage());
        }

            List<InboxModel> results = this.jdbcTemplate.query(sb.toString(), new RowMapper<InboxModel>() {
                @Override
                public InboxModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                    InboxModel model = new InboxModel();
                    int col = 1;
                    model.setId(rs.getInt(col++));
                    model.setFromAccountId(rs.getInt(col++));
                    model.setToAccountId(rs.getInt(col++));
                    model.setMessage(rs.getString(col++));
                    model.setCreateDate(rs.getTimestamp(col++).toLocalDateTime());
                    model.setStatus(rs.getBoolean(col));
                    return model;
                }
            }, paramList.toArray());

            return results;
    }

    @Override
    public void updateToReadStatus(List<InboxModel> inboxModels) {
        StringBuilder sb = new StringBuilder();
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        List<Object> paramList = new ArrayList<>();
        sb.append(" UPDATE inbox ");
        sb.append(" SET status = true ");
        sb.append(" WHERE id IN ");
        for (InboxModel model: inboxModels) {
            stringJoiner.add(" ? ");
            paramList.add(model.getId());
        }

        sb.append(stringJoiner.toString());

        this.jdbcTemplate.update(sb.toString(), paramList.toArray());
    }
}
