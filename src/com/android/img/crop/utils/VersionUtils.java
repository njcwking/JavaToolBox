package com.android.img.crop.utils;

import com.android.img.crop.model.Version;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/13
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class VersionUtils {

    private static final class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 从Github获取版本信息
     * @return
     */
    public static Version getVersionFromGitHub() {
        Version version = null;
        InputStream inputStream = null;
        try {
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            } catch (KeyManagementException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            }
            SSLSocketFactory ssf = ctx.getSocketFactory();
            URL url = new URL("https://raw.githubusercontent.com/njcwking/JavaToolBox/master/src/xml/version.xml");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(ssf);
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            inputStream = urlConnection.getInputStream();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            // 获取根节点 version
            Element root = document.getRootElement();
            int versionCode = 0;
            String versionName = "";
            String updateTime = "";
            if (root != null) {
                Element versionCodeElement = root.element("versionCode");
                if (versionCodeElement != null) {
                    versionCode = Integer.valueOf(versionCodeElement.getText());
                }
                Element versionNameElement = root.element("versionName");
                if (versionNameElement != null) {
                    versionName = versionNameElement.getText();
                }
                Element updateTimeElement = root.element("updateTime");
                if (updateTimeElement != null) {
                    updateTime = updateTimeElement.getText();
                }
            }
            if (versionCode != 0 && versionName != null && updateTime != null) {
                version = new Version(versionCode, versionName, updateTime);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return version;
    }

    /**
     * 从资源文件中获取版本信息
     * @return
     */
    public static Version getVersionFromXml() {
        Version version = null;
        InputStream inputStream = VersionUtils.class.getResourceAsStream("/xml/version.xml");
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 获取根节点 version
        Element root = document.getRootElement();
        int versionCode = 0;
        String versionName = "";
        String updateTime = "";
        if (root != null) {
            Element versionCodeElement = root.element("versionCode");
            if (versionCodeElement != null) {
                versionCode = Integer.valueOf(versionCodeElement.getText());
            }
            Element versionNameElement = root.element("versionName");
            if (versionNameElement != null) {
                versionName = versionNameElement.getText();
            }
            Element updateTimeElement = root.element("updateTime");
            if (updateTimeElement != null) {
                updateTime = updateTimeElement.getText();
            }
        }
        if (versionCode >=0 && versionName != null && updateTime != null) {
            version = new Version(versionCode, versionName, updateTime);
        }
        return version;
    }
}
