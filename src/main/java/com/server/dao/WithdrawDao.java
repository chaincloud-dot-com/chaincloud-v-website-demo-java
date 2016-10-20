package com.server.dao;

import com.server.model.TxStatus;
import com.server.model.Withdraw;

import java.util.List;

/**
 * Created by classic1999 on 16/9/9.
 */
public interface WithdrawDao {

    List<Withdraw> findAll();

    Withdraw findOneByStatus(TxStatus txStatus);

    Withdraw findById(Long aLong);

    void update(Withdraw withdraw);
}
