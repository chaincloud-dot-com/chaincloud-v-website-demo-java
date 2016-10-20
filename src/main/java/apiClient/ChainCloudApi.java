package apiClient;

import apiClient.crypto.AESCrypto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.server.model.Withdraw;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by classic1999 on 16/9/7.
 */
public class ChainCloudApi {
    private static final Logger LOG = LoggerFactory.getLogger(ChainCloudApi.class);

    private static String URL = "https://chaincloud-api.getcai.com/api/v1/";
    private static int TIME_OUT = 4000;

    public String openTime(String token) {
        String path = "open/time";
        return sendRequest(path, token);
    }

    public JSONObject user(String token) {
        String path = "open/user";
        return sendRequestForObject(path, token);
    }

    public JSONArray retrieveAddresses(String token, int batchNo) {
        String path = "open/address/batch/" + batchNo;
        return sendRequestForArray(path, token);
    }

    public JSONArray addressHistory(String token, String sinceAddress) {
        String path = "open/address/history/0";
        Map<String, String> params = new LinkedHashMap<>();
        params.put("since_address", sinceAddress);
        String response = sendRequest(path, token, params);
        return JSON.parseArray(response);
    }

    public JSONObject addressNext(String token) {
        String path = "open/address/next";
        return sendRequestForObject(path, token);
    }

    public JSONArray tx(String token, String txHash) {
        String path = "open/tx";
        Map<String, String> params = new LinkedHashMap<>();
        params.put("tx_hash", txHash);
        String response = sendRequest(path, token, params);
        return JSON.parseArray(response);
    }

    public JSONObject txDetail(String token, String txHash) {
        String path = "open/tx/detail/" + txHash;
        return sendRequestForObject(path, token);
    }

    public JSONObject send(SendParam sendParam) {
        String path = "open/tx/request";
        Map<String, String> params = new LinkedHashMap<>();
        params.put("coin_code", "BTC");
        params.put("user_tx_no", "");
        final String outs = sendParam.toOuts();
        params.put("outs", outs);
        params.put("vc_code", "");
        final String dynameFee = "1";
        params.put("is_dynamic_fee", dynameFee);
        params.put("c_id", "");
        //info = "{\"outs\":\"%s\",\"dynamic\":%d}" % (outs_str, dynamic_fee)
        JSONObject info = new JSONObject(true);
        info.put("outs", outs);
        info.put("dynamic", dynameFee);
        String encryptInfo = AESCrypto.encrypt(info.toJSONString(), "123456");

        Withdraw withdraw = new Withdraw(1L, outs, encryptInfo, System.currentTimeMillis(), 1); // todo 保存

        return sendRequestForObject(path, sendParam.getToken(), params);
    }


    public JSONArray addressTx(String token, String address) {
        String path = "open/address/" + address + "/tx";
        return sendRequestForArray(path, token);
    }

    private JSONArray sendRequestForArray(String path, String token) {
        String response = sendRequest(path, token);
        return JSON.parseArray(response);
    }

    private JSONObject sendRequestForObject(String path, String token) {
        return sendRequestForObject(path, token, new HashMap<>());
    }

    private JSONObject sendRequestForObject(String path, String token, Map<String, String> params) {
        String response = sendRequest(path, token, params);
        return JSON.parseObject(response);
    }

    private String sendRequest(String path, String token) {
        return sendRequest(path, token, new LinkedHashMap<>());
    }

    private String sendRequest(String path, String token, Map<String, String> params) {
        LOG.info("path:{},params:{}, token:{}", path, params, token.substring(0, token.length() / 2));
        String url = URL + path;
        String text = "";
        try {
            Document objectDoc;
            try {
                Connection connection = HttpUtils.getConnectionForGet(url).timeout(TIME_OUT);
                connection.header("token", token);
                connection.data(params);
                objectDoc = connection.get();
            } catch (SocketTimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    // ignore
                }
                Connection connection = HttpUtils.getConnectionForGet(url).timeout(TIME_OUT);
                connection.header("token", token);
                objectDoc = connection.get();
            }
            text = objectDoc.body().text();
            return text;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            LOG.info("response:{}", text);
        }
    }


}
