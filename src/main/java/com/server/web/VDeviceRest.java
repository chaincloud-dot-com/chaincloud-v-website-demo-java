package com.server.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.server.dao.AddressDao;
import com.server.dao.WithdrawDao;
import com.server.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by classic1999 on 16/9/9.
 */
@RestController
@RequestMapping("/api/v1/open")
public class VDeviceRest {
    private static final Logger logger = LoggerFactory.getLogger(VDeviceRest.class);

    @Autowired
    private WithdrawDao withdrawDao;
    @Autowired
    private AddressDao addressDao;

    @RequestMapping(value = {"/vtest"}, method = RequestMethod.GET)
    public JSONObject get() {
        Withdraw withdraw = withdrawDao.findOneByStatus(TxStatus.init);
        JSONObject jsonObject = new JSONObject();
        if (withdraw == null) {
            return jsonObject;
        }
        jsonObject.put("vtest_info", withdraw.getEncryptOuts());
        jsonObject.put("vtest_at", DateFormatUtils.ISO_DATETIME_FORMAT.format(withdraw.getCreateTime()));
        jsonObject.put("vtest_id", withdraw.getId());
        return jsonObject;
    }

    @RequestMapping(value = {"/vtest"}, method = RequestMethod.POST)
    public JSONObject post(@RequestParam(value = "vtest_id", required = true) String vtestIdStr,
                           @RequestParam(value = "tx_hash", required = false) String txHash) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(vtestIdStr) || !StringUtils.isNumeric(vtestIdStr)) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "vtest_id param error:" + vtestIdStr);
            return jsonObject;
        }
        Withdraw withdraw = withdrawDao.findById(Long.valueOf(vtestIdStr));

        if (withdraw == null) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "withdraw is null, vtest_id:" + vtestIdStr);
            return jsonObject;
        }

        TxStatus txStatus = withdraw.getStatus();
        if (txStatus.isFinish()) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "withdraw is completed, status:" + txStatus + ", vtest_id:" + vtestIdStr);
            return jsonObject;
        }

        if (StringUtils.isEmpty(txHash)) {
            txStatus = TxStatus.fail;
        } else {
            txStatus = TxStatus.success;
        }
        withdraw.setTxHash(txHash);
        withdraw.setStatus(txStatus);
        withdrawDao.update(withdraw);

        jsonObject.put("result", true);
        return jsonObject;
    }


    @RequestMapping(value = {"/batch"}, method = RequestMethod.GET)
    public JSONObject batch() {
        List<BtcAddress> btcAddressList = addressDao.findByStatus(AddressStatus.init);
        JSONObject jsonObject = new JSONObject();
        if (btcAddressList == null || btcAddressList.isEmpty()) {
            return jsonObject;
        }
        JSONArray address_list = new JSONArray();
        Long batchNo = null;
        AddressType addressType = null;
        for (BtcAddress btcAddress : btcAddressList) {
            if (batchNo == null) {
                batchNo = btcAddress.getBatchNo();
            } else if (!batchNo.equals(btcAddress.getBatchNo())) {
                break;
            }
            if (addressType == null) {
                addressType = btcAddress.getAddressType();
            }
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("index", btcAddress.getBatchIndex());
            jsonObject1.put("address", btcAddress.getAddress());
            address_list.add(jsonObject1);
        }


        jsonObject.put("batch_no", batchNo);
        jsonObject.put("address_list", address_list);
        if (addressType != null) {
            jsonObject.put("address_type", addressType.getNum());
        }
        return jsonObject;
    }


    @RequestMapping(value = {"/batch"}, method = RequestMethod.POST)
    public JSONObject post(@RequestParam(value = "batch_no", required = true) String batchNoStr,
                           @RequestParam(value = "status", required = true) String statusStr,
                           @RequestParam(value = "type", required = true) String typeStr) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(batchNoStr) || !StringUtils.isNumeric(batchNoStr)) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "batch_no param error:" + batchNoStr);
            return jsonObject;
        }
        if (StringUtils.isEmpty(statusStr) || (!"2".equals(statusStr) && !"3".equals(statusStr))) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "status param error:" + statusStr);
            return jsonObject;
        }

        if (StringUtils.isEmpty(typeStr) || (!"1".equals(typeStr) && !"2".equals(typeStr))) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "type param error:" + typeStr);
            return jsonObject;
        }
        AddressStatus addressStatus = AddressStatus.verified;
        if ("3".equals(statusStr)) {
            addressStatus = AddressStatus.failed;
        }
        AddressType addressType = AddressType.hotSend;
        if ("2".equals(typeStr)) {
            addressType = AddressType.coldReceive;
        }

        addressDao.update(Long.valueOf(batchNoStr), addressStatus, addressType);

        jsonObject.put("result", true);
        return jsonObject;
    }


}
