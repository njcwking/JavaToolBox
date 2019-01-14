package com.njcwking.box;

import com.njcwking.box.menu.MenuFrame;
import com.njcwking.box.scale.swing.main.MainFrame;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/14
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class Main {
    /**
     * 1. 缩放图片
     * --scale src dist designSize
     *
     * @param args
     */
    public static void main(String[] args) {
//        new MainFrame().setVisible(true);
        HashMap<String, String[]> argMap = new HashMap<>();
        String commandName = null;
        List<String> argsValue = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                if (commandName != null) {
                    argMap.put(commandName, argsValue.toArray(new String[argsValue.size()]));
                    argsValue.clear();
                }
                commandName = arg;
            } else {
                if (commandName != null) {
                    argsValue.add(arg);
                }
            }
        }
//        JOptionPane.showMessageDialog(null, Arrays.asList(args).toString(), "提示",
//                JOptionPane.ERROR_MESSAGE);
        if (argsValue.size() > 0) {
            argMap.put(commandName, argsValue.toArray(new String[argsValue.size()]));
        }

        if (argMap.containsKey("--scale") && argMap.get("--scale").length >= 3) {
            String[] subArgs = argMap.get("--scale");
            String filePath = subArgs[0];
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                String distPath = subArgs[1];
                String designSize = subArgs[2];
                new MainFrame(filePath, distPath, designSize).setVisible(true);
                return;
            }
        }
        new MenuFrame().setVisible(true);
    }
}
