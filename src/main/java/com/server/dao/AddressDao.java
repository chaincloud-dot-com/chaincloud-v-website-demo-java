package com.server.dao;


import com.server.model.AddressStatus;
import com.server.model.AddressType;
import com.server.model.BtcAddress;

import java.util.List;

public interface AddressDao {

	BtcAddress findByAddress(String name);
	
	List<BtcAddress> findAll();

	List<BtcAddress> findByStatus(AddressStatus init);

	int update(Long aLong, AddressStatus addressStatus, AddressType addressType);
}