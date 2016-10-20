package apiClient;

/**
 * Created by classic1999 on 16/9/8.
 */
public class CoinSpend {
    private String address;
    private String value;

    public CoinSpend(String address, String value) {
        this.address = address;
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return address + "," + value;
    }

}
