package com.njcwking.box.crop.swing.main;

import com.njcwking.box.crop.model.Config;
import com.njcwking.box.crop.model.Version;
import com.njcwking.box.crop.swing.about.AboutDialog;
import com.njcwking.box.crop.swing.setting.SettingFrame;
import com.njcwking.box.crop.utils.ConfigUtils;
import com.njcwking.box.crop.utils.VersionUtils;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
    private JMenuItem versionItem = null;
    private JMenu jm = null;


    public MainFrame() throws HeadlessException {
        super();
        Config config = null;
        try {
            config = ConfigUtils.getConfigByDefault();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("JavaToolBox");
        setContentPane(new MainFrameGUI(this, config).rootPanel);
        //添加菜单栏
        jm = new JMenu("更多选项");
        JMenuItem aboutItem = new JMenuItem("关于");
        jm.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog(MainFrame.this, "关于");
                dialog.setVisible(true);
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
        versionItem = new JMenuItem("检查更新");
        jm.add(versionItem);
        versionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Version remoteVersion = VersionUtils.getVersionFromGitHub();
                if (remoteVersion == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "无法获取版本信息，请检查网络连接", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Version localVersion = VersionUtils.getVersionFromXml();
                    if (localVersion.getVersionCode() < remoteVersion.getVersionCode()) {
                        versionItem.setText("最新版本V"+remoteVersion.getVersionName());
                        jm.setIcon(new ImageIcon(this.getClass().getResource(
                                "/png/red_point.png")));
                        jm.setHorizontalTextPosition(JMenu.LEFT);
                        versionItem.setIcon(new ImageIcon(this.getClass().getResource(
                                "/png/red_point.png")));
                        versionItem.setHorizontalTextPosition(JMenu.LEFT);
                        int n = JOptionPane.showConfirmDialog(MainFrame.this, String.format("最新版本：%s\n是否更新？", remoteVersion.getVersionName()), "版本更新", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null);
                        if (n == JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().browse(new URL("https://github.com/njcwking/JavaToolBox/blob/master/Resources/runJar").toURI());
                            } catch (IOException err) {
                                err.printStackTrace();
                            } catch (URISyntaxException err) {
                                err.printStackTrace();
                            }
                        }
                    } else {
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

        checkVersion();

    }

    private void checkVersion() {
        Observable.create(new ObservableOnSubscribe<Version>() {
            @Override
            public void subscribe(ObservableEmitter<Version> observableEmitter) throws Exception {
                observableEmitter.onNext(VersionUtils.getVersionFromGitHub());
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation()).subscribe(new Observer<Version>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Version version) {
                Version localVersion = VersionUtils.getVersionFromXml();
                if (localVersion.getVersionCode() < version.getVersionCode()) {
                    versionItem.setText("最新版本V"+version.getVersionName());
                    jm.setIcon(new ImageIcon(this.getClass().getResource(
                            "/png/red_point.png")));
                    jm.setHorizontalTextPosition(JMenu.LEFT);
                    versionItem.setIcon(new ImageIcon(this.getClass().getResource(
                            "/png/red_point.png")));
                    versionItem.setHorizontalTextPosition(JMenu.LEFT);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
