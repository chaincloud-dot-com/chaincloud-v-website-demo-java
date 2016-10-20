package com.server.dao;

import com.server.model.TxStatus;
import com.server.model.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WithdrawDaoImpl implements WithdrawDao {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Withdraw> findAll() {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT * FROM withdraw";

        return namedParameterJdbcTemplate.query(sql, params, new VDeviceMapper());

    }

    @Override
    public Withdraw findOneByStatus(TxStatus txStatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", txStatus.name());
        String sql = "SELECT * FROM withdraw WHERE status=:status";

        List<Withdraw> devices = namedParameterJdbcTemplate.query(sql, params, new VDeviceMapper());
        if (devices.isEmpty()) {
            return null;
        }
        return devices.get(0);
    }

    @Override
    public Withdraw findById(Long aLong) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", aLong);
        String sql = "SELECT * FROM withdraw WHERE id=:id";

        List<Withdraw> devices = namedParameterJdbcTemplate.query(sql, params, new VDeviceMapper());
        if (devices.isEmpty()) {
            return null;
        }
        return devices.get(0);
    }

    @Override
    public void update(Withdraw withdraw) {
        String sql = "UPDATE withdraw SET status=:status,txHash=:txHash WHERE id=:id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", withdraw.getStatus().name());
        paramMap.put("txHash", withdraw.getTxHash());
        paramMap.put("id", withdraw.getId());
        int i = namedParameterJdbcTemplate.update(sql, paramMap);
    }

    private static final class VDeviceMapper implements RowMapper<Withdraw> {

        public Withdraw mapRow(ResultSet rs, int rowNum) throws SQLException {
            Withdraw user = new Withdraw();
            user.setId(rs.getLong("id"));
            user.setOuts(rs.getString("outs"));
            user.setEncryptOuts(rs.getString("encryptOuts"));
            user.setCreateTime(rs.getLong("createTime"));
            user.setIsDynamicFee(rs.getInt("isDynamicFee"));
            user.setTxHash(rs.getString("txHash"));
            user.setStatus(TxStatus.valueOf(rs.getString("status")));
            return user;
        }

    }

}