package apiClient.crypto;

/**
 * Created by classic1999 on 16/9/8.
 */

import org.spongycastle.crypto.generators.SCrypt;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.Locale;

/**
 * Created by songchenwen on 15/9/21.
 */
public class CryptoUtils {
    public static final String QR_CODE_SPLIT = "/";

    final protected static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Returns the given byte array hex encoded.
     */
    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0;
             j < bytes.length;
             j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase(Locale.US);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0;
             i < len;
             i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s
                    .charAt(i + 1), 16));
        }
        return data;
    }

    public static String[] splitOfPasswordSeed(String str) {
        return str.split(QR_CODE_SPLIT);
    }



}
