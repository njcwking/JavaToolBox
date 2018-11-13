package com.android.img.crop.swing.about;

import com.android.img.crop.utils.VersionUtils;

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
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class AboutDialogGUI {
    public JPanel rootPanel;
    private JLabel iconLabel;
    private JLabel linkLabel;
    private JLabel versionLabel;

    /** 保存标签的默认颜色 */
    private Color preColor = null;

    private URL link;

    private String text = "Github : JavaToolBox";

    public AboutDialogGUI() {

        versionLabel.setText("版本："+VersionUtils.getVersionFromXml().getVersionName());
        linkLabel.setText(text);
        try {
            link = new URL("https://github.com/njcwking/JavaToolBox");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                linkLabel.setCursor(Cursor
                        .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (preColor != null)
                    linkLabel.setForeground(preColor);
                linkLabel.setText("<html>" + text + "</html>");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                linkLabel.setCursor(Cursor
                        .getPredefinedCursor(Cursor.HAND_CURSOR));
                preColor = linkLabel.getForeground();
                linkLabel.setForeground(Color.BLUE);
                linkLabel.setText("<html><u>" + text + "</u></html>");
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
}
