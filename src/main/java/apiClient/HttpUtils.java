package apiClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.net.SocketTimeoutException;
import java.util.Map;

public class HttpUtils {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0";

    public static Connection getConnectionForPost(String url, Map<String, String> datas) {
        url = appendHttpString(url);
        Connection connection = Jsoup.connect(url)
                .userAgent(USER_AGENT).timeout(5000)
                .method(Connection.Method.POST);
        if (datas != null && !datas.isEmpty()) {
            connection.data(datas);
        }
        return connection;
    }

    private static String appendHttpString(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    public static Connection getConnectionForGetNoCookies(String url) {
        return getConnectionForGetNoCookies(url, null);
    }

    public static Connection getConnectionForGetNoCookies(String url, Map<String, String> datas) {
        url = appendHttpString(url);
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).ignoreContentType(true).timeout(5000);
        if (datas != null) {
            connection.data(datas);
        }

        return connection;
    }

    public static Connection getConnectionForGet(String url) {
        return getConnectionForGet(url, null);
    }


    public static Connection getConnectionForGet(String url, Map<String, String> datas) {
        trust();
        return getConnectionForGetNoCookies(url, datas);
    }

    protected static void trust() {
        try {
            trustAllHttpsCertificates();
        } catch (Exception e) {
            // ignore
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    static HostnameVerifier hv = (urlHostName, session) -> true;

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
        }
    }

    public static String getContentForGet(String url, int timeout) {
        try {
            Document objectDoc;
            try {
                Connection connection = getConnectionForGetNoCookies(url).timeout(timeout);
                objectDoc = connection.get();
            } catch (SocketTimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    // ignore
                }
                Connection connection = getConnectionForGetNoCookies(url).timeout(timeout);
                objectDoc = connection.get();
            }
            return objectDoc.body().text();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
