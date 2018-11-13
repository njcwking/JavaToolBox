package com.android.img.crop.swing.about;

import com.android.img.crop.swing.setting.SettingFrameGUI;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : 关于
 *     version: 1.0
 * </pre>
 */
public class AboutDialog extends JDialog {
    public AboutDialog(Frame owner, String title) {
        super(owner, title,true);
        setContentPane(new AboutDialogGUI().rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
    }
}
