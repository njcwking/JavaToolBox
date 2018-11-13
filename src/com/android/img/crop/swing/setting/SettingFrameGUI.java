package com.android.img.crop.swing.setting;

import com.android.img.crop.model.Config;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.swing.setting.size.SizeAddOrUpdateFrame;
import com.android.img.crop.utils.ConfigUtils;
import com.android.img.crop.utils.FileUtils;
import com.android.img.crop.utils.RxBus;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

import static com.android.img.crop.utils.ConfigUtils.INSTALL_PATH;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class SettingFrameGUI {
    public JPanel rootPanel;
    private JCheckBox recursiveCheckBox;
    private JButton recursiveButton;
    private JCheckBox coverCheckBox;
    private JButton coverButton;
    private JCheckBox suffixCheckBox;
    private JButton suffixButton;
    private JButton cancelButton;
    private JButton saveButton;
    private JButton addButton;
    private JButton delButton;
    private JTable sizeTable;
    private JButton restoreButton;
    private JButton updateButton;

    private String recursiveDesc = "<html>递归文件夹指在进行图片处理时，<br/>如果选择的文件夹内包含更多的图片文<br/>件夹，如果选择递归则会将子文件夹<br/>中的图片一并进行处理。</html>";

    private String coverDesc = "<html>在指定生成的文件中，<br/>如果有相同的文件名，<br/>如果设置覆盖，原文件将直接被删除后覆盖。</html>";

    private String suffixDesc = "<html>在配置文件中对生成的文件名添加后缀，<br/>如“图片原名_后缀名.png”</html>";

    JFrame mFrame;

    Config mConfig;

    private Disposable subscribe;

    public final static String MSG_SETTING_SAVE = "setting_save";

    public SettingFrameGUI(JFrame frame, final Config config) {
        this.mConfig = config;
        mFrame = frame;

        initData(config);

        initListener(config);

        subscribe = RxBus.get().toObservable(Config.class).subscribe(new Consumer<Config>() {
            @Override
            public void accept(Config config) throws Exception {
                if (config != null) {
                    initTable(mConfig);
                }
            }
        });

    }

    private void initListener(final Config config) {
        recursiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, recursiveDesc, "递归文件夹", JOptionPane.QUESTION_MESSAGE);
            }
        });

        recursiveCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setRecursive(recursiveCheckBox.isSelected());
            }
        });

        coverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, coverDesc, "覆盖文件", JOptionPane.QUESTION_MESSAGE);
            }
        });

        coverCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setCover(coverCheckBox.isSelected());
            }
        });

        suffixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, suffixDesc, "文件后缀", JOptionPane.QUESTION_MESSAGE);
            }
        });

        suffixCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setAutoSuffix(suffixCheckBox.isSelected());
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SizeAddOrUpdateFrame addFrame = new SizeAddOrUpdateFrame(mConfig, null);
                addFrame.setVisible(true);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowcount = sizeTable.getSelectedRow();
                if (rowcount >= 0) {
                    SizeAddOrUpdateFrame addFrame = new SizeAddOrUpdateFrame(mConfig, mConfig.getSizeItem().get(rowcount));
                    addFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mFrame, "请先选择一个尺寸", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowcount = sizeTable.getSelectedRow();
                if (rowcount >= 0) {
                    DefaultTableModel tableModel = (DefaultTableModel) sizeTable.getModel();
                    tableModel.removeRow(rowcount);
                    config.getSizeItem().remove(rowcount);
                } else {
                    JOptionPane.showMessageDialog(mFrame, "请先选择一个尺寸", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mFrame.dispose();
                if (subscribe != null && !subscribe.isDisposed()) {
                    subscribe.dispose();
                }
            }
        });
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(mFrame, "确定恢复默认值？", "恢复默认", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    boolean success = false;
                    InputStream resourceAsStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        resourceAsStream = ConfigUtils.getConfigInputStream();
                        File file = new File(INSTALL_PATH);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = resourceAsStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, length);
                        }
                        success = true;
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            if (resourceAsStream != null) {
                                resourceAsStream.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (success) {
                        mConfig = ConfigUtils.getConfigByDefault();
                        JOptionPane.showMessageDialog(mFrame, "恢复默认设置成功", "提示", JOptionPane.PLAIN_MESSAGE);
                        initData(mConfig);
                        RxBus.get().post(MSG_SETTING_SAVE);
                    } else {
                        JOptionPane.showMessageDialog(mFrame, "恢复默认设置失败", "提示", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ConfigUtils.saveConfigByDefault(mConfig)) {
                    JOptionPane.showMessageDialog(mFrame, "保存成功", "提示", JOptionPane.PLAIN_MESSAGE);
                    RxBus.get().post(MSG_SETTING_SAVE);
                } else {
                    JOptionPane.showMessageDialog(mFrame, "配置保存失败", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (subscribe != null && !subscribe.isDisposed()) {
                    subscribe.dispose();
                }
                super.windowClosing(e);
            }
        });
    }

    private void initData(Config config) {
        recursiveCheckBox.setSelected(mConfig.isRecursive());
        coverCheckBox.setSelected(mConfig.isCover());
        suffixCheckBox.setSelected(mConfig.isAutoSuffix());
        initTable(config);
    }

    private void initTable(Config config) {
        DefaultTableModel tableModel = (DefaultTableModel) sizeTable.getModel();
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }
        //table设置
        if (config != null) {
            Vector rowData = new Vector();
            for (ConfigItem configItem : config.getSizeItem()) {
                Vector data = new Vector();
                data.addElement(configItem.getName());
                data.addElement(String.format("%d px x %d px", configItem.getWidth(), configItem.getHeight()));
                data.addElement(configItem.getFolderName());
                data.addElement(String.format("%d px x %d px", configItem.getIconWidth(), configItem.getIconWidth()));
                data.addElement(configItem.getSuffix() == null ? "" : configItem.getSuffix());
                rowData.add(data);
            }
            Vector columnNames = new Vector();
            columnNames.addElement("名称");
            columnNames.addElement("尺寸");
            columnNames.addElement("文件夹名");
            columnNames.addElement("应用图标大小");
            columnNames.addElement("文件后缀");
            tableModel = new DefaultTableModel(rowData, columnNames);
            sizeTable.setModel(tableModel);
            sizeTable.getTableHeader().setPreferredSize(new Dimension(0, 30));
            sizeTable.setRowHeight(30);
        }
    }
}
