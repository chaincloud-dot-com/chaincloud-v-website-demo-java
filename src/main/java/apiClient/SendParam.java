package apiClient;

import java.util.List;

/**
 * Created by classic1999 on 16/9/8.
 */
public class SendParam {
    private String token;
    private List<CoinSpend> coinSpendList;

    public SendParam() {
    }
    public SendParam(String token, List<CoinSpend> coinSpendList) {
        this.token = token;
        this.coinSpendList = coinSpendList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CoinSpend> getCoinSpendList() {
        return coinSpendList;
    }

    public void setCoinSpendList(List<CoinSpend> coinSpendList) {
        this.coinSpendList = coinSpendList;
    }

    public String toOuts() {
        if (coinSpendList == null || coinSpendList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (CoinSpend coinSpend : coinSpendList) {
            sb.append(coinSpend).append(";");
        }
        String ret = sb.toString();
        if (ret.endsWith(";")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }
}
