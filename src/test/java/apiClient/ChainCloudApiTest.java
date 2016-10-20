package apiClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by classic1999 on 16/9/7.
 */
public class ChainCloudApiTest {
    private static String coldToken = ""; // FIXME
    private static String hotToken = ""; // FIXME

    @Test
    public void testOpenTime() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        String time = chainCloudApi.openTime(hotToken);
        assertTrue(time != null);
    }

    private ChainCloudApi getChainCloudApi() {
        return new ChainCloudApi();
    }

    @Test
    public void testUser() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONObject hotJsonObject = chainCloudApi.user(hotToken);
        assertTrue(hotJsonObject != null);

        JSONObject coldJsonObject = chainCloudApi.user(coldToken);
        assertTrue(coldJsonObject != null);

        assertTrue(!hotJsonObject.equals(coldJsonObject));

    }

    @Test
    public void addressHistory() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONArray jsonArray = chainCloudApi.addressHistory(coldToken, "1Gdd7xsSqJE1uH16YZca7vkKssPoShRFyw");
        assertTrue(jsonArray != null);

    }

    @Test
    public void addressNext() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONObject jsonObject = chainCloudApi.addressNext(coldToken);
        assertTrue(jsonObject != null);
        jsonObject = chainCloudApi.addressNext(coldToken);
        assertTrue(jsonObject != null);
    }

    @Test
    public void tx() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONArray jsonArray = chainCloudApi.tx(coldToken, "d79deb9419b3cf62f08badef456a75396400bfe78cc38ed5ee6cbba27caf57e6");
        assertTrue(jsonArray != null);

    }

    @Test
    public void txDetail() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONObject jsonArray = chainCloudApi.txDetail(coldToken, "d79deb9419b3cf62f08badef456a75396400bfe78cc38ed5ee6cbba27caf57e6");
        assertTrue(jsonArray != null);

    }

    @Test
    public void addressTx() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        JSONArray jsonArray = chainCloudApi.addressTx(coldToken, "1Gdd7xsSqJE1uH16YZca7vkKssPoShRFyw");
        assertTrue(jsonArray != null);
    }

    @Test
    public void sending() {
        ChainCloudApi chainCloudApi = getChainCloudApi();
        SendParam sendParam = new SendParam(hotToken, null);
        JSONObject jsonObject = chainCloudApi.send(sendParam);

    }


    @Ignore
    @Test
    public void testRetrieveAddresses() {

        ChainCloudApi chainCloudApi = getChainCloudApi();
        //  调用一次3元，慎重
        JSONArray jsonArray = chainCloudApi.retrieveAddresses(coldToken, 0);
        assertTrue(!jsonArray.isEmpty());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("index:" + jsonObject.getString("index") + ", address:" + jsonObject.getString("address"));
        }
        System.out.print(jsonArray);
    }
}
