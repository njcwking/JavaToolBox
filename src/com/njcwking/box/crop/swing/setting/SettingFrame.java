package com.njcwking.box.crop.swing.setting;

import com.njcwking.box.crop.model.Config;
import com.njcwking.box.crop.utils.ConfigUtils;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class SettingFrame extends JFrame {

    public SettingFrame() throws HeadlessException {
        super();
        Config config = null;
        try {
            config = ConfigUtils.getConfigByDefault();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("设置");
        if (config != null) {
            setContentPane(new SettingFrameGUI(this, config).rootPanel);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pack();

            setLocationRelativeTo(getOwner());
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }

    public static void main(String[] args) {
        new SettingFrame().setVisible(true);
    }
}
