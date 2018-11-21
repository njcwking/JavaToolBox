package com.njcwking.box;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/21
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public abstract class AbstractFrame<T extends BaseGUI> extends JFrame implements WindowListener {

    protected T gui;

    public AbstractFrame(String title) {
        this(title, JFrame.EXIT_ON_CLOSE);
    }

    public AbstractFrame(String title, int operation) {
        super();
        setTitle(title);
        gui = getGUIFromForm();
        setContentPane(gui.getRootPanel());
        setDefaultCloseOperation(operation);
        pack();

        setLocationRelativeTo(getOwner());
        setLocationRelativeTo(null);

        addWindowListener(this);
    }

    /**
     * 初始化GUI文件
     */
    protected abstract T getGUIFromForm();


    /**
     * 初始化窗口
     */
    protected abstract void initWindow();

    /**
     * 初始化菜单栏
     */
    protected abstract void initMenu();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化事件
     */
    protected abstract void initAction();

    @Override
    public void windowOpened(WindowEvent e) {
        initWindow();
        initMenu();
        initView();
        initAction();
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
