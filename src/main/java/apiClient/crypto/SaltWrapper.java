package apiClient.crypto;

/**
 * Created by classic1999 on 16/9/8.
 */
public class SaltWrapper {

    public static final int IS_COMPRESSED_FLAG = 1;
    public static final int IS_FROMXRANDOM_FLAG = 2;

    private byte[] salt;
    private boolean isFromXRandom = false;
    private boolean isCompressed = true;
    private byte[] qrCodeSalt;

    public SaltWrapper(byte[] qrCodeSalt) {

        this.qrCodeSalt = qrCodeSalt;
        salt = new byte[KeyCryptoHandler.SALT_LENGTH];
        isCompressed = true;
        isFromXRandom = false;
        if (qrCodeSalt.length == KeyCryptoHandler.SALT_LENGTH) {
            salt = qrCodeSalt;
        } else {
            System.arraycopy(qrCodeSalt, 1, salt, 0, salt.length);
            isCompressed = (((int) qrCodeSalt[0]) & IS_COMPRESSED_FLAG) == IS_COMPRESSED_FLAG;
            isFromXRandom = (((int) qrCodeSalt[0]) & IS_FROMXRANDOM_FLAG) == IS_FROMXRANDOM_FLAG;
        }
    }

    public SaltWrapper(byte[] salt, boolean isCompressed, boolean isFromXRandom) {
        this.salt = salt;
        this.isFromXRandom = isFromXRandom;
        this.isCompressed = isCompressed;
        qrCodeSalt = new byte[KeyCryptoHandler.SALT_LENGTH + 1];
        if (salt.length == KeyCryptoHandler.SALT_LENGTH) {
            int flag = 0;
            if (isCompressed) {
                flag += IS_COMPRESSED_FLAG;
            }
            if (isFromXRandom) {
                flag += IS_FROMXRANDOM_FLAG;
            }
            qrCodeSalt[0] = (byte) flag;
            System.arraycopy(salt, 0, qrCodeSalt, 1, salt.length);

        } else {
            System.arraycopy(salt, 0, qrCodeSalt, 0, salt.length);
        }

    }

    public byte[] getSalt() {
        return salt;
    }

    public boolean isFromXRandom() {
        return isFromXRandom;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public byte[] getQrCodeSalt() {
        return qrCodeSalt;
    }

}
