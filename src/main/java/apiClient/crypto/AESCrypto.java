package apiClient.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by classic1999 on 16/9/8.
 */
public class AESCrypto {
    private static final Logger LOG = LoggerFactory.getLogger(AESCrypto.class);

    private final byte[] encryptedData;
    private final byte[] initialisationVector;
    private final SaltWrapper saltObject;

    public static String encrypt(String data, CharSequence password) {
        final AESCrypto aesCrypto = AESCrypto.buildToEncrypted(data.getBytes(), password);
        return aesCrypto.toEncryptedString();
    }

    public static String decrypt(String encryptData, CharSequence password) {
        final AESCrypto aesCrypto = AESCrypto.buildFromEncrypted(encryptData);
        return new String(aesCrypto.decrypt(password));
    }

    public static AESCrypto buildFromEncrypted(String str) {
        return new AESCrypto(str);
    }

    public static AESCrypto buildToEncrypted(byte[] dataToEncrypt, CharSequence password) {
        return new AESCrypto(dataToEncrypt, password);
    }


    private AESCrypto(String str) {
        String[] strs = CryptoUtils.splitOfPasswordSeed(str);
        if (strs.length != 3) {
            LOG.error("AESCrypto", "decryption: AESCrypto format error");
        }
        initialisationVector = CryptoUtils.hexStringToByteArray
                (strs[1]);
        encryptedData = CryptoUtils.hexStringToByteArray(strs[0]);
        byte[] saltQRCodes = CryptoUtils.hexStringToByteArray(strs[2]);
        saltObject = new SaltWrapper(saltQRCodes);
    }

    private AESCrypto(byte[] dataToEncrypt, CharSequence password) {
        this(dataToEncrypt, password, true, false);
    }

    private AESCrypto(byte[] dataToEncrypt, CharSequence password,
                      boolean isFromXRandom) {
        this(dataToEncrypt, password, true, isFromXRandom);
    }

    private AESCrypto(byte[] dataToEncrypt, CharSequence password, boolean isCompress,
                      boolean isFromXRandom) {
        KeyCryptoHandler crypter = new KeyCryptoHandler();
        byte[] salt = crypter.getSalt();
        PrivateKeyWrapper k = crypter.encrypt(dataToEncrypt, crypter.deriveKey(password));
        encryptedData = k.getEncryptedBytes();
        initialisationVector = k.getInitialisationVector();
        saltObject = new SaltWrapper(salt, isCompress, isFromXRandom);
    }

    public byte[] decrypt(CharSequence password) {
        KeyCryptoHandler crypter = new KeyCryptoHandler(saltObject.getSalt());
        return crypter.decrypt(new PrivateKeyWrapper(initialisationVector, encryptedData), crypter.deriveKey(password));
    }

    public String toEncryptedString() {
        return CryptoUtils.bytesToHexString(encryptedData).toUpperCase()
                + CryptoUtils.QR_CODE_SPLIT + CryptoUtils.bytesToHexString(initialisationVector).toUpperCase()
                + CryptoUtils.QR_CODE_SPLIT + CryptoUtils.bytesToHexString(saltObject.getSalt()).toUpperCase();
    }

    public boolean isXRandom() {
        return saltObject.isFromXRandom();
    }

    public boolean isCompressed() {
        return saltObject.isCompressed();
    }

}
