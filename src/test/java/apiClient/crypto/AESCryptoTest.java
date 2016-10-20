package apiClient.crypto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by classic1999 on 16/9/8.
 */
public class AESCryptoTest {

    @Test
    public void test() throws Exception {
        final String password = "20160721";
        /*Map<String,Object> jsonObject=new LinkedHashMap<>();
        jsonObject.put("outs","1NbbvxBYxGGCBhaM8mow1HFWA7dB5yukmY,10000;1MCs9SZwLg9JvLo6pzvVBWtmSV1dakwyM1,10000");
        jsonObject.put("dynamic",0);*/

        JSONObject jsonObject=new JSONObject(true);
        jsonObject.put("outs","1PsdHa2geM5jzHgFbndzCyyDyh8J1eYzbN,10000;1CD8w6tzJrWBie8JTJLai8v6YhJem5swHj,20000");
        jsonObject.put("dynamic",0);
        final String str = jsonObject.toJSONString();
        String dd = AESCrypto.encrypt(str, password);
        System.out.println(dd);

        String dd2="A155A1300B6E41EE6DD672620A7FE5D34F038EE65197C88C9A57E224AB88C0A410A896503E9B953F92FC8052168058024A5DEFE8D88C95E1F52DF77DCFA5C252531A8041700A5BA514CB3C4BBA445286E986410865455AD1CC5B8377C379D0A13A2B62ADCE22CB3C512BE723F9A45D17/6DAFF891831AD1CF64C3A9B1617B3B4F/22AD1DC0AAB18773";
        //assertEquals(dd2, dd);

        String str2 = AESCrypto.decrypt(dd, password);
        System.out.println(str2);

        assertEquals(str, str2);

    }

    @Test
    public void test2() throws Exception {
        final String password = "123";
        final String str = "a";
        AESCrypto encryptedHandler = AESCrypto.buildToEncrypted(str.getBytes(), password);
        String dd = encryptedHandler.toEncryptedString();
        AESCrypto encryptedHandler2 = AESCrypto.buildFromEncrypted(dd);
        String str2 = new String(encryptedHandler2.decrypt(password));
        assertEquals(str2, str);

    }

    @Test
    public void testBuildToEncrypted() throws Exception {
        AESCrypto encryptedHandler = AESCrypto.buildToEncrypted("a".getBytes(), "123");
        String dd = encryptedHandler.toEncryptedString();
        System.out.println(dd);
        assertTrue(dd != null);
    }

    @Test
    public void testDecrypt() throws Exception {
        String dd = "1E30CB3A46C7E60A3745CC3EE8842677/1FC965B3AABBBC97A1EA0ED9FBF70829/BAC7B94C81EEEF26";
        AESCrypto encryptedHandler = AESCrypto.buildFromEncrypted(dd);
        final String dd2 = new String(encryptedHandler.decrypt("123"));
        System.out.println(dd2);
    }

}