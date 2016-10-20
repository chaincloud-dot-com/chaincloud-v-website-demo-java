package apiClient.crypto;

/**
 * Created by classic1999 on 16/9/8.
 */
public class KeyCrypterException extends RuntimeException {
    private static final long serialVersionUID = -4441989608332681377L;

    public KeyCrypterException(String s) {
        super(s);
    }

    public KeyCrypterException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
