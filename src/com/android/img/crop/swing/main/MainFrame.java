package com.android.img.crop.swing.main;

import com.android.img.crop.model.Config;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.swing.about.AboutDialog;
import com.android.img.crop.swing.setting.SettingFrame;
import com.android.img.crop.utils.ConfigUtils;
import sun.net.www.http.HttpClient;

import javax.net.ssl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        super();
        Config config = null;
        try {
            config = ConfigUtils.getConfigByDefault();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("JavaToolBox");
        setContentPane(new MainFrameGUI(this,config).rootPanel);
        //添加菜单栏
        JMenu jm = new JMenu("JavaToolBox");
        JMenuItem aboutItem = new JMenuItem("关于");
        jm.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog(MainFrame.this,"关于");
                dialog.setVisible(true);
                dialog.setModal(true);
            }
        });
        jm.addSeparator();
        JMenuItem useItem = new JMenuItem("使用手册");
        useItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/njcwking/JavaToolBox/blob/master/Resources/manual.md").toURI());
                } catch (IOException err) {
                    err.printStackTrace();
                } catch (URISyntaxException err) {
                    err.printStackTrace();
                }
            }
        });
        jm.add(useItem);
        JMenuItem versionItem = new JMenuItem("检查更新");
        jm.add(versionItem);
        versionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SSLContext ctx = null;
                    try {
                        ctx = SSLContext.getInstance("TLS");
                        ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
                    } catch (KeyManagementException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchAlgorithmException e2) {
                        e2.printStackTrace();
                    }
                    SSLSocketFactory ssf = ctx.getSocketFactory();
                    URL url = new URL("https://raw.githubusercontent.com/njcwking/JavaToolBox/master/Resources/version.xml");
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
                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) > 0) {
                        System.out.println(new String(buffer, 0, len, "utf-8"));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jm.addSeparator();
        JMenuItem settingItem = new JMenuItem("设置");
        jm.add(settingItem);
        settingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame settingFrame = new SettingFrame();
                settingFrame.setVisible(true);
            }
        });
        jm.addSeparator();
        JMenuItem exitItem = new JMenuItem("退出");
        jm.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(jm);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(getOwner());
        setLocationRelativeTo(null);

    }
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
    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
