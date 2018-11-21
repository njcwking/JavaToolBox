package com.njcwking.box.scale.swing.about;

import com.njcwking.box.AbstractDialog;
import com.njcwking.box.scale.utils.VersionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : 关于
 *     version: 1.0
 * </pre>
 */
public class AboutDialog extends AbstractDialog<AboutDialogGUI> {

    /**
     * 保存标签的默认颜色
     */
    private Color preColor = null;

    private URL link;

    private final String githubText = "Github : JavaToolBox";

    public AboutDialog(JFrame owner) {
        super(owner, "关于");

    }

    @Override
    protected AboutDialogGUI getGUIFromForm() {
        return new AboutDialogGUI();
    }

    @Override
    protected void initWindow() {

    }

    @Override
    protected void initMenu() {

    }

    @Override
    protected void initView() {
        gui.versionLabel.setText("版本：" + VersionUtils.getVersionFromXml().getVersionName());
        gui.linkLabel.setText(githubText);
        try {
            link = new URL("https://github.com/njcwking/JavaToolBox");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initAction() {
        gui.linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                gui.linkLabel.setCursor(Cursor
                        .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (preColor != null)
                    gui.linkLabel.setForeground(preColor);
                gui.linkLabel.setText("<html>" + githubText + "</html>");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                gui.linkLabel.setCursor(Cursor
                        .getPredefinedCursor(Cursor.HAND_CURSOR));
                preColor = gui.linkLabel.getForeground();
                gui.linkLabel.setForeground(Color.BLUE);
                gui.linkLabel.setText("<html><u>" + githubText + "</u></html>");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(link.toURI());
                } catch (IOException err) {
                    err.printStackTrace();
                } catch (URISyntaxException err) {
                    err.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new AboutDialog(null).setVisible(true);
    }
}
