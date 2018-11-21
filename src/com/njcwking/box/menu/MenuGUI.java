package com.njcwking.box.menu;

import com.njcwking.box.BaseGUI;

import javax.swing.*;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/21
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class MenuGUI implements BaseGUI {
    public JButton mergeToolInto;
    public JPanel rootPanel;
    public JPanel scalePanel;
    public JButton scaleToolInto;
    public JButton iconToolInto;
    public JButton androidToolInto;


    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
