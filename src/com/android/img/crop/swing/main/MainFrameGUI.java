package com.android.img.crop.swing.main;

import com.android.img.crop.model.Config;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.swing.setting.SettingFrameGUI;
import com.android.img.crop.utils.ConfigUtils;
import com.android.img.crop.utils.RxBus;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/10
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class MainFrameGUI {
    private JTextField srcDirField;
    private JButton chooseInput;
    private JTextField outDirField;
    private JButton chooseOutput;
    private JComboBox designBox;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField iconNameField;
    private JButton okButton;
    public JPanel rootPanel;
    private JLabel iconLabel;
    private JLabel srcLabel;
    private JLabel outLabel;
    private JLabel designLabel;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JLabel cropLabel;
    private JPanel sizePanel;

    //目录文件拖拽Handler
    TransferHandler dirTransferHandler = new TransferHandler() {
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            boolean success = false;
            try {
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                // 此处输出文件/文件夹的名字以及路径
                if (o != null) {
                    String path = o.toString();
                    path = path.replace("[", "");
                    path = path.replace("]", "");
                    File file = new File(path);
                    if (file.isDirectory()) {
                        ((JTextField) comp).setText(path);
                        success = true;
                    }
                }
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!success) {
                JOptionPane.showMessageDialog(null, "请拖入文件夹", "操作失败",
                        JOptionPane.ERROR_MESSAGE);
            }
            return success;
        }

        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return true;
        }
    };
    //图标文件拖拽Handler
    TransferHandler iconTransferHandler = new TransferHandler() {
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            try {
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                // 此处输出文件/文件夹的名字以及路径
                if (o != null) {
                    String path = o.toString();
                    path = path.replace("[", "");
                    path = path.replace("]", "");
                    File file = new File(path);
                    if (file.isFile()) {
                        if (file.getName().endsWith(".jpg")
                                || file.getName().endsWith(".png")
                                || file.getName().endsWith(".JPG")
                                || file.getName().endsWith(".PNG")) {
                            iconNameField.setText(file.getName());
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "请拖入正确的图片文件，当前只支持jpg和png图片", "失败",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "请拖入文件", "失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return true;
        }
    };

    private Config mConfig;

    private JFrame mFrame;

    private Disposable subscribe = null;

    public MainFrameGUI(JFrame frame,Config config) {
        this.mConfig = config;
        this.mFrame = frame;

        //设置可拖拽目录或文件填充至控件
        srcDirField.setDragEnabled(true);
        srcDirField.setEditable(false);
        srcDirField.setTransferHandler(dirTransferHandler);
        outDirField.setDragEnabled(true);
        outDirField.setEditable(false);
        outDirField.setTransferHandler(dirTransferHandler);
        iconNameField.setDragEnabled(true);
        iconNameField.setTransferHandler(iconTransferHandler);
        iconNameField.setText(config.getIconName());

        //设置目录选择按钮事件
        chooseInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    srcDirField.setText(file.getAbsolutePath());
                }
            }
        });
        chooseOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    outDirField.setText(file.getAbsolutePath());
                }
            }
        });

        widthField.setEnabled(false);
        heightField.setEnabled(false);
        designBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = designBox.getSelectedIndex();
                    String content = designBox.getSelectedItem().toString();
                    widthField.setText(mConfig.getSizeItem().get(index).getWidth() + " px");
                    heightField.setText(mConfig.getSizeItem().get(index).getHeight() + " px");
                }
            }
        });
        initSize();


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        subscribe = RxBus.get().toObservable(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (SettingFrameGUI.MSG_SETTING_SAVE.equals(s)) {
                    mConfig = ConfigUtils.getConfigByDefault();
                    initSize();
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

    private void initSize() {
        designBox.removeAllItems();
        sizePanel.removeAll();
        if (mConfig.getSizeItem() != null) {
            sizePanel.setLayout(new GridLayout(mConfig.getSizeItem().size() / 4 + (mConfig.getSizeItem().size() % 4 == 0 ? 0 : 1), 4));
            sizePanel.setBorder(new EmptyBorder(0, 5, 0, 5));
            for (ConfigItem configModel : mConfig.getSizeItem()) {
                designBox.addItem(configModel.getName());
                JCheckBox checkBox = new JCheckBox(configModel.getName());
                checkBox.setSelected(true);
                sizePanel.add(checkBox);
            }
        }
    }
}
