package com.android.img.crop.swing.setting.size;

import com.android.img.crop.model.Config;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.bus.RxBus;
import com.android.img.crop.utils.ValidationUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/13
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class SizeAddOrUpdateGUI {
    public JPanel rootPanel;
    private JTextField nameField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField folderNameField;
    private JTextField iconSizeField;
    private JButton cancelButton;
    private JButton addButton;
    private JTextField suffixField;

    private ConfigItem mConfigItem;
    private JFrame mFrame;
    private Config mConfig;

    public SizeAddOrUpdateGUI(JFrame frame, Config config, ConfigItem configItem) {
        this.mConfigItem = configItem;
        this.mConfig = config;
        this.mFrame = frame;
        if (mConfigItem != null) {
            nameField.setText(configItem.getName());
            widthField.setText(String.valueOf(configItem.getWidth()));
            heightField.setText(String.valueOf(configItem.getHeight()));
            folderNameField.setText(configItem.getFolderName());
            iconSizeField.setText(String.valueOf(configItem.getIconWidth()));
            suffixField.setText(configItem.getSuffix()==null?"":configItem.getSuffix());
            addButton.setText("确认修改");
        }
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mFrame.dispose();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String strWidth = widthField.getText().trim();
                String strHeight = heightField.getText().trim();
                String folderName = folderNameField.getText().trim();
                String strIconSize = iconSizeField.getText().trim();
                String suffix = suffixField.getText().trim();
                if (name.equals("")) {
                    JOptionPane.showMessageDialog(mFrame, "名称不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (strWidth.equals("")) {
                    JOptionPane.showMessageDialog(mFrame, "宽度不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (strHeight.equals("")) {
                    JOptionPane.showMessageDialog(mFrame, "高度不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (folderName.equals("")) {
                    JOptionPane.showMessageDialog(mFrame, "目录名称不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (strIconSize.equals("")) {
                    JOptionPane.showMessageDialog(mFrame, "图标大小不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (!ValidationUtils.isNumeric(strWidth) && Integer.valueOf(strWidth) > 0) {
                    JOptionPane.showMessageDialog(mFrame, "宽度格式不正确", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (!ValidationUtils.isNumeric(strHeight) && Integer.valueOf(strHeight) > 0) {
                    JOptionPane.showMessageDialog(mFrame, "高度格式不正确", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (!ValidationUtils.isNumeric(strIconSize) && Integer.valueOf(strIconSize) > 0) {
                    JOptionPane.showMessageDialog(mFrame, "图标大小格式不正确", "提示", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (mConfigItem != null) {
                        mConfigItem.setName(name);
                        mConfigItem.setFolderName(folderName);
                        mConfigItem.setWidth(Integer.valueOf(strWidth));
                        mConfigItem.setHeight(Integer.valueOf(strHeight));
                        mConfigItem.setIconWidth(Integer.valueOf(strWidth));
                        mConfigItem.setSuffix(suffix);
                        JOptionPane.showMessageDialog(mFrame, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                        mFrame.dispose();
                        RxBus.getDefault().post(mConfig);
                    }
                    else{
                        ConfigItem item = new ConfigItem();
                        item.setName(name);
                        item.setFolderName(folderName);
                        item.setWidth(Integer.valueOf(strWidth));
                        item.setHeight(Integer.valueOf(strHeight));
                        item.setIconWidth(Integer.valueOf(strWidth));
                        item.setSuffix(suffix);
                        mConfig.getSizeItem().add(item);
                        JOptionPane.showMessageDialog(mFrame, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                        mFrame.dispose();
                        RxBus.getDefault().post(mConfig);
                    }
                }
            }
        });
    }
}
