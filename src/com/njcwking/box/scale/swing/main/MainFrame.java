package com.njcwking.box.scale.swing.main;

import com.njcwking.box.scale.model.Config;
import com.njcwking.box.scale.swing.setting.SettingFrame;
import com.njcwking.box.scale.utils.ConfigUtils;

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
    private JMenu jm = null;

    private String srcFile;
    private String distFile;
    private String designSize;

    private MainFrameGUI mainFrameGUI;


    public MainFrame() throws HeadlessException {
        super();
        init();

    }

    public MainFrame(String srcFile,String distFile,String designSize) throws HeadlessException {
        super();
        init();
        mainFrameGUI.autoScale(srcFile, distFile, designSize);
    }

    private void init() {
        Config config = null;
        try {
            config = ConfigUtils.getConfigByDefault();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("切图缩放工具");
        mainFrameGUI = new MainFrameGUI(this, config);
        setContentPane(mainFrameGUI.rootPanel);
        //添加菜单栏
        jm = new JMenu("切图缩放工具选项");
        JMenuItem useItem = new JMenuItem("使用手册");
        useItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/njcwking/JavaToolBox/blob/master/Resources/scale-tool-manual.md").toURI());
                } catch (IOException err) {
                    err.printStackTrace();
                } catch (URISyntaxException err) {
                    err.printStackTrace();
                }
            }
        });
        jm.add(useItem);
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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        setLocationRelativeTo(getOwner());
        setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
