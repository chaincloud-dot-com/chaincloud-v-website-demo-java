package apiClient.crypto;

import com.sun.istack.internal.Nullable;

import java.util.Arrays;

/**
 * Created by classic1999 on 16/9/8.
 */
public class PrivateKeyWrapper {

    private byte[] initialisationVector = null;
    private byte[] encryptedPrivateBytes = null;

    /**
     * Cloning constructor.
     *
     * @param encryptedPrivateKey PrivateKeyWrapper to clone.
     */
    public PrivateKeyWrapper(PrivateKeyWrapper encryptedPrivateKey) {
        setInitialisationVector(encryptedPrivateKey.getInitialisationVector());
        setEncryptedPrivateBytes(encryptedPrivateKey.getEncryptedBytes());
    }

    /**
     */
    public PrivateKeyWrapper(byte[] initialisationVector, byte[] encryptedPrivateKeys) {
        setInitialisationVector(initialisationVector);
        setEncryptedPrivateBytes(encryptedPrivateKeys);
    }

    public byte[] getInitialisationVector() {
        return initialisationVector;
    }

    /**
     * Set the initialisationVector, cloning the bytes.
     *
     */
    public void setInitialisationVector(byte[] initialisationVector) {
        if (initialisationVector == null) {
            this.initialisationVector = null;
            return;
        }

        byte[] cloneIV = new byte[initialisationVector.length];
        System.arraycopy(initialisationVector, 0, cloneIV, 0, initialisationVector.length);

        this.initialisationVector = cloneIV;
    }

    public byte[] getEncryptedBytes() {
        return encryptedPrivateBytes;
    }

    /**
     * Set the encrypted private key bytes, cloning them.
     *
     */
    public void setEncryptedPrivateBytes(byte[] encryptedPrivateBytes) {
        if (encryptedPrivateBytes == null) {
            this.encryptedPrivateBytes = null;
            return;
        }

        this.encryptedPrivateBytes = Arrays.copyOf(encryptedPrivateBytes, encryptedPrivateBytes.length);
    }

    @Override
    public PrivateKeyWrapper clone() {
        return new PrivateKeyWrapper(getInitialisationVector(), getEncryptedBytes());
    }

    @Override
    public int hashCode() {
        return hashCode(encryptedPrivateBytes, initialisationVector);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrivateKeyWrapper other = (PrivateKeyWrapper) obj;

        return equal(this.initialisationVector, other.initialisationVector)
                && equal(this.encryptedPrivateBytes, other.encryptedPrivateBytes);
    }

    @Override
    public String toString() {
        return "PrivateKeyWrapper [initialisationVector=" + Arrays.toString(initialisationVector) + ", encryptedPrivateKey=" + Arrays.toString(encryptedPrivateBytes) + "]";
    }

    /**
     * Clears all the PrivateKeyWrapper contents from memory (overwriting all data including PRIVATE KEYS).
     * WARNING - this method irreversibly deletes the private key information.
     */
    public void clear() {
        if (encryptedPrivateBytes != null) {
            Arrays.fill(encryptedPrivateBytes, (byte) 0);
        }
        if (initialisationVector != null) {
            Arrays.fill(initialisationVector, (byte) 0);
        }
    }


    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
