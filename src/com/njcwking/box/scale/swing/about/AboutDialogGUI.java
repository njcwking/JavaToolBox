package com.njcwking.box.scale.swing.about;

import com.njcwking.box.BaseGUI;

import javax.swing.*;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class AboutDialogGUI implements BaseGUI {
    public JPanel rootPanel;
    protected JLabel iconLabel;
    protected JLabel linkLabel;
    protected JLabel versionLabel;

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
