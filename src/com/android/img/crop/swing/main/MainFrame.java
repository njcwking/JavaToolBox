package com.android.img.crop.swing.main;

import com.android.img.crop.model.Config;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.model.Version;
import com.android.img.crop.swing.about.AboutDialog;
import com.android.img.crop.swing.setting.SettingFrame;
import com.android.img.crop.utils.ConfigUtils;
import com.android.img.crop.utils.VersionUtils;
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
                Version remoteVersion = VersionUtils.getVersionFromGitHub();
                if (remoteVersion == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "无法获取版本信息，请检查网络连接", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    Version localVersion = VersionUtils.getVersionFromXml();
                    if (localVersion.getVersionCode() < remoteVersion.getVersionCode()) {
                        int n = JOptionPane.showConfirmDialog(MainFrame.this, String.format("最新版本：%s\n是否更新？",remoteVersion.getVersionName()), "版本更新", JOptionPane.YES_NO_OPTION);
                        if (n == JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().browse(new URL("https://github.com/njcwking/JavaToolBox/blob/master/Resources/runJar").toURI());
                            } catch (IOException err) {
                                err.printStackTrace();
                            } catch (URISyntaxException err) {
                                err.printStackTrace();
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(MainFrame.this, "当前版本已经是最新版本", "提示",
                                JOptionPane.PLAIN_MESSAGE);
                    }
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

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
