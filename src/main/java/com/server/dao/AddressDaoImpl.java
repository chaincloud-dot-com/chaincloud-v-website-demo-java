package com.server.dao;

import com.server.model.AddressStatus;
import com.server.model.AddressType;
import com.server.model.BtcAddress;
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
public class AddressDaoImpl implements AddressDao {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public BtcAddress findByAddress(String address) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("address", address);

        String sql = "SELECT * FROM btc_address WHERE address=:address";

        BtcAddress result = namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                new AddressMapper());

        //new BeanPropertyRowMapper(Customer.class));

        return result;

    }

    @Override
    public List<BtcAddress> findAll() {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT * FROM btc_address";

        List<BtcAddress> result = namedParameterJdbcTemplate.query(sql, params, new AddressMapper());

        return result;

    }

    @Override
    public List<BtcAddress> findByStatus(AddressStatus status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status.name());
        String sql = "SELECT * FROM btc_address WHERE status=:status ORDER BY batchNo";
        List<BtcAddress> result = namedParameterJdbcTemplate.query(sql, params, new AddressMapper());
        return result;
    }

    @Override
    public int update(Long aLong, AddressStatus addressStatus, AddressType addressType) {
        String sql = "UPDATE btc_address SET status=:status,addressType=:addressType WHERE batchNo=:batchNo";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", addressStatus.name());
        paramMap.put("addressType", addressType.name());
        paramMap.put("batchNo", aLong);
        return namedParameterJdbcTemplate.update(sql, paramMap);
    }

    private static final class AddressMapper implements RowMapper<BtcAddress> {

        public BtcAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
            BtcAddress user = new BtcAddress();
            user.setId(rs.getLong("id"));
            user.setBatchNo(rs.getLong("batchNo"));
            user.setAddressType(AddressType.valueOf(rs.getString("addressType")));
            user.setAddress(rs.getString("address"));
            user.setBatchIndex(rs.getLong("batchIndex"));
            user.setCreateTime(rs.getLong("createTime"));
            user.setStatus(AddressStatus.valueOf(rs.getString("status")));
            return user;
        }
    }

}