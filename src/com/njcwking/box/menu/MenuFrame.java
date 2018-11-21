package com.njcwking.box.menu;

import com.njcwking.box.AbstractFrame;
import com.njcwking.box.scale.model.Version;
import com.njcwking.box.scale.swing.about.AboutDialog;
import com.njcwking.box.scale.swing.main.MainFrame;
import com.njcwking.box.scale.utils.VersionUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
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
 *     author : njcwking
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/21
 *     desc   : 功能菜单页面
 *     version: 1.0
 * </pre>
 */
public class MenuFrame extends AbstractFrame<MenuGUI> implements ActionListener {
    private JMenuItem versionItem = null;
    private JMenu jm = null;

    private MainFrame mainFrame;


    public MenuFrame() {
        super("Java工具箱");
    }

    @Override
    protected MenuGUI getGUIFromForm() {
        return new MenuGUI();
    }

    @Override
    protected void initWindow() {

    }

    @Override
    protected void initMenu() {
        //添加菜单栏
        jm = new JMenu("Java工具箱选项");
        JMenuItem aboutItem = new JMenuItem("关于");
        jm.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog(MenuFrame.this);
                dialog.setVisible(true);
            }
        });
        jm.addSeparator();
        versionItem = new JMenuItem("检查更新");
        jm.add(versionItem);
        versionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Version remoteVersion = VersionUtils.getVersionFromGitHub();
                if (remoteVersion == null) {
                    JOptionPane.showMessageDialog(MenuFrame.this, "无法获取版本信息，请检查网络连接", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Version localVersion = VersionUtils.getVersionFromXml();
                    if (localVersion.getVersionCode() < remoteVersion.getVersionCode()) {
                        versionItem.setText("最新版本V" + remoteVersion.getVersionName());
                        jm.setIcon(new ImageIcon(this.getClass().getResource(
                                "/png/red_point.png")));
                        jm.setHorizontalTextPosition(JMenu.LEFT);
                        versionItem.setIcon(new ImageIcon(this.getClass().getResource(
                                "/png/red_point.png")));
                        versionItem.setHorizontalTextPosition(JMenu.LEFT);
                        int n = JOptionPane.showConfirmDialog(MenuFrame.this, String.format("最新版本：%s\n是否更新？", remoteVersion.getVersionName()), "版本更新", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
                        if (n == JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().browse(new URL(remoteVersion.getDownloadUrl()).toURI());
                            } catch (IOException err) {
                                err.printStackTrace();
                            } catch (URISyntaxException err) {
                                err.printStackTrace();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(MenuFrame.this, "当前版本已经是最新版本", "提示",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
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
    }

    @Override
    protected void initView() {
        checkVersion();
    }

    @Override
    protected void initAction() {
        gui.scaleToolInto.addActionListener(this);
        gui.androidToolInto.addActionListener(this);
        gui.iconToolInto.addActionListener(this);
        gui.mergeToolInto.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == gui.scaleToolInto) {
            if (mainFrame == null || !mainFrame.isDisplayable()) {
                mainFrame = new MainFrame();
            }
            mainFrame.setVisible(true);
        } else if (source == gui.androidToolInto) {

        } else if (source == gui.iconToolInto) {

        } else if (source == gui.mergeToolInto) {

        }
    }

    /**
     * 检查软件版本
     */
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
                    versionItem.setText("最新版本V" + version.getVersionName());
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
}
