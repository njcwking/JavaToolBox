package com.njcwking.box.scale.swing.loading;

import com.njcwking.box.scale.bus.RxBus;
import com.njcwking.box.scale.utils.GenerateTool;
import io.reactivex.functions.Consumer;

import javax.swing.*;
import java.awt.event.*;

/**
 * 正在截图的加载框
 * 监听的Bus任务
 * 1. 当前进度 : 改变进度条
 * 2. 切图日志 : 打印日志
 * 3. 缩放成功 : 切图成功，打印日志，按钮改成确认后关闭
 * 4. 缩放失败 : 打出失败日志，按钮改成关闭
 */
public class LoadingDialog extends JDialog {

    public static final String MSG_CROP_CANCEL = "msg.image.scale.cancel";

    private JPanel contentPane;
    private JButton buttonCancel;
    private JProgressBar progressBar;
    private JTextArea logTextArea;

    private boolean cropComplete = false;

    public LoadingDialog(final JFrame frame) {
        super(frame, "缩放图片", false);
        setContentPane(contentPane);
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!cropComplete) {
                    int n = JOptionPane.showConfirmDialog(frame, "确定取消当前任务吗？", "取消任务", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        if (!cropComplete) {
                            RxBus.getDefault().post(MSG_CROP_CANCEL, new Object());
                        }
                        onCancel();
                    }
                } else {
                    onCancel();
                }

            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        RxBus.getDefault().toObservable(GenerateTool.MSG_CROP_LOG, String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addMessage(s);
            }
        });

        RxBus.getDefault().toObservable(GenerateTool.MSG_CROP_PROGRESS, Integer.class).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer progress) throws Exception {
                setProgress(progress);
            }
        });

        RxBus.getDefault().toObservable(GenerateTool.MSG_CROP_SUCCESS, Object.class).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                addMessage("-------------------------");
                addMessage("缩放完成");
                buttonCancel.setText("完成");
                cropComplete = true;
            }
        });

        RxBus.getDefault().toObservable(GenerateTool.MSG_CROP_FAIL, Throwable.class).subscribe(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                addMessage("-------------------------");
                addMessage("缩放错误");
                addMessage(throwable.getMessage());
                addMessage(throwable.getLocalizedMessage());
                addMessage(throwable.toString());
                buttonCancel.setText("关闭");
                cropComplete = true;
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getMessage() {
        return logTextArea.getText();
    }

    public void setMessage(String message) {
        logTextArea.setText(message);
    }

    public void addMessage(String message) {
        logTextArea.append("\n" + message);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
    }

    public int getProgress() {
        return progressBar.getValue();
    }


    public static void main(String[] args) {
        LoadingDialog dialog = new LoadingDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
