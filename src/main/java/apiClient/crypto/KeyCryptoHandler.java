package apiClient.crypto;

/**
 * Created by classic1999 on 16/9/8.
 */


import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.generators.SCrypt;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;


public class KeyCryptoHandler implements Serializable {
    /**
     * Key length in bytes.
     */
    public static final int KEY_LENGTH = 32; // = 256 bits.
    /**
     * The size of an AES block in bytes.
     * This is also the length of the initialisation vector.
     */
    public static final int BLOCK_LENGTH = 16;  // = 128 bits.
    /**
     * The length of the salt used.
     */
    public static final int SALT_LENGTH = 8;
    private static final Logger LOG = LoggerFactory.getLogger(KeyCryptoHandler.class);
    private static final long serialVersionUID = 949662512049152670L;
    private static final int BITCOINJ_SCRYPT_N = 16384;
    private static final int BITCOINJ_SCRYPT_R = 8;
    private static final int BITCOINJ_SCRYPT_P = 1;
    private static final SecureRandom secureRandom;

    static {
        secureRandom = new SecureRandom();
    }

    // Scrypt parameters.

    private final String TAG = "KeyCryptoHandler";
    private byte[] mSalt;

    /**
     * Encryption/ Decryption using default parameters and a random salt
     */
    public KeyCryptoHandler() {
        mSalt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(mSalt);

    }


    public KeyCryptoHandler(byte[] salt) {
        mSalt = checkNotNull(salt);

        if (salt.length == 0) {
            LOG.warn(TAG, "You are using a ScryptParameters with no salt. Your encryption may be vulnerable to a dictionary attack.");
        }
    }

    /**
     * Convert a CharSequence (which are UTF16) into a byte array.
     * <p>
     * Note: a String.getBytes() is not used to avoid creating a String of the password in the JVM.
     */
    private static byte[] convertToByteArray(CharSequence charSequence) {
        checkNotNull(charSequence);

        byte[] byteArray = new byte[charSequence.length() << 1];
        for (int i = 0; i < charSequence.length(); i++) {
            int bytePosition = i << 1;
            byteArray[bytePosition] = (byte) ((charSequence.charAt(i) & 0xFF00) >> 8);
            byteArray[bytePosition + 1] = (byte) (charSequence.charAt(i) & 0x00FF);
        }
        return byteArray;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static void wipeBytes(byte[] bytes) {
        if (bytes == null) {
            return;
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        SecureRandom r = new SecureRandom();
        r.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public KeyParameter deriveKey(CharSequence password) throws KeyCrypterException {
        byte[] passwordBytes = null;
        try {
            passwordBytes = convertToByteArray(password);
            byte[] salt = new byte[0];
            if (mSalt != null) {
                salt = mSalt;
            } else {
                // Warn the user that they are not using a salt.
                // (Some early MultiBit wallets had a blank salt).
                LOG.warn(TAG, "You are using a ScryptParameters with no salt. Your encryption may be vulnerable to a dictionary attack.");
            }

            byte[] keyBytes = SCrypt.generate(passwordBytes, salt, BITCOINJ_SCRYPT_N, BITCOINJ_SCRYPT_R, BITCOINJ_SCRYPT_P, KEY_LENGTH);
            return new KeyParameter(keyBytes);
        } catch (Exception e) {
            throw new KeyCrypterException("Could not generate key from password and salt.", e);
        } finally {
            // Zero the password bytes.
            if (passwordBytes != null) {
                java.util.Arrays.fill(passwordBytes, (byte) 0);
            }
        }
    }

    /**
     * Password based encryption using AES - CBC 256 bits.
     */
    public PrivateKeyWrapper encrypt(byte[] plainBytes, KeyParameter aesKey) throws KeyCrypterException {
        checkNotNull(plainBytes);
        checkNotNull(aesKey);

        try {
            // Generate iv - each encryption call has a different iv.
            byte[] iv = new byte[BLOCK_LENGTH];
            secureRandom.nextBytes(iv);

            ParametersWithIV keyWithIv = new ParametersWithIV(aesKey, iv);

            // Encrypt using AES.
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
            cipher.init(true, keyWithIv);
            byte[] encryptedBytes = new byte[cipher.getOutputSize(plainBytes.length)];
            int length = cipher.processBytes(plainBytes, 0, plainBytes.length, encryptedBytes, 0);

            cipher.doFinal(encryptedBytes, length);

            return new PrivateKeyWrapper(iv, encryptedBytes);
        } catch (Exception e) {
            throw new KeyCrypterException("Could not encrypt bytes.", e);
        }
    }

    /**
     * Decrypt bytes previously encrypted with this class.
     *
     * @param privateKeyToDecode The private key to decrypt
     * @param aesKey             The AES key to use for decryption
     * @return The decrypted bytes
     * @throws KeyCrypterException if bytes could not be decoded to a valid key
     */
    public byte[] decrypt(PrivateKeyWrapper privateKeyToDecode, KeyParameter aesKey) throws KeyCrypterException {
        checkNotNull(privateKeyToDecode);
        checkNotNull(aesKey);

        try {
            ParametersWithIV keyWithIv = new ParametersWithIV(new KeyParameter(aesKey.getKey()), privateKeyToDecode.getInitialisationVector());

            // Decrypt the message.
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
            cipher.init(false, keyWithIv);

            byte[] cipherBytes = privateKeyToDecode.getEncryptedBytes();
            int minimumSize = cipher.getOutputSize(cipherBytes.length);
            byte[] outputBuffer = new byte[minimumSize];
            int length1 = cipher.processBytes(cipherBytes, 0, cipherBytes.length, outputBuffer, 0);
            int length2 = cipher.doFinal(outputBuffer, length1);
            int actualLength = length1 + length2;

            byte[] decryptedBytes = new byte[actualLength];
            System.arraycopy(outputBuffer, 0, decryptedBytes, 0, actualLength);

            wipeBytes(outputBuffer);

            return decryptedBytes;
        } catch (Exception e) {
            throw new KeyCrypterException("Could not decrypt bytes", e);
        }
    }

    public byte[] getSalt() {
        return this.mSalt;
    }

    @Override
    public String toString() {
        return "Scrypt/AES";
    }

    @Override
    public int hashCode() {
        return hashCode(this.mSalt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof KeyCryptoHandler)) {
            return false;
        }
        final KeyCryptoHandler other = (KeyCryptoHandler) obj;

        return equal(this.mSalt, other.getSalt());
    }
}
