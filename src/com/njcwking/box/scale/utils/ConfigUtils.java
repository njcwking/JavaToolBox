package com.njcwking.box.scale.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.njcwking.box.scale.model.Config;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import com.njcwking.box.scale.model.ConfigItem;
import org.dom4j.io.XMLWriter;

public class ConfigUtils {

    public static final String INSTALL_PATH = System.getProperty("user.home") + File.separator + "CropTool" + File.separator + "config";

    /**
     * 从文件中获取配置
     * @param file
     * @return
     */
    private static Config getConfigFromFile(File file) {
        FileInputStream inputStream = null;
        Config config = null;
        try {
            inputStream = new FileInputStream(file);
            config = getConfig(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  config;
    }

    /**
     * 从资源文件中获取配置
     * @return
     */
    private static Config getConfigFromResource() {
        InputStream resourceAsStream = getConfigInputStream();
        Config config = null;
        try {
            config = getConfig(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
    /**
     * 获取配置
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    private static Config getConfig(InputStream inputStream) throws Exception {
        Config config = new Config();
        List<ConfigItem> list = null;
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);

        // 获取根节点
        Element root = document.getRootElement();

        Element image_config = root.element("img_config");
        if (image_config != null) {
            list = new ArrayList<ConfigItem>();
            List<Element> models = image_config.elements("model");
            for (Element element : models) {
                ConfigItem model = new ConfigItem();
                model.setName(element.attributeValue("name"));
                model.setWidth(Integer.parseInt(element.attributeValue("width")));
                model.setHeight(Integer.parseInt(element
                        .attributeValue("height")));
                model.setFolderName(element.attributeValue("folder"));
                model.setIconWidth(Integer.parseInt(element.attributeValue("iconWidth")));
                model.setSuffix(element.attributeValue("suffix"));
                list.add(model);
            }
        }
        config.setSizeItem(list);
        Element settingElement = root.element("setting");
        if (settingElement != null) {
            Element iconNameElement = settingElement.element("setting_default_icon_name");
            if (iconNameElement != null) {
                config.setIconName(iconNameElement.getText());
            }
            Element recursiveElement = settingElement.element("setting_recursive");
            if (recursiveElement != null) {
                config.setRecursive(Boolean.parseBoolean(recursiveElement.getText()));
            }
            Element coverElement = settingElement.element("setting_cover");
            if (coverElement != null) {
                config.setCover(Boolean.parseBoolean(coverElement.getText()));
            }
            Element suffixElement = settingElement.element("setting_auto_add_suffix");
            if (suffixElement != null) {
                config.setAutoSuffix(Boolean.parseBoolean(suffixElement.getText()));
            }
            Element toleranceElement = settingElement.element("setting_tolerance");
            if (toleranceElement != null) {
                config.setTolerance(Integer.parseInt(toleranceElement.getText()));
            }
        }
        return config;
    }

    /**
     * 保存所有配置
     *
     * @param config
     */
    private static void saveConfig(Config config, File file) throws IOException {
        Document doc = DocumentHelper.createDocument();
        Element rootElement = doc.addElement("config");
        Element imgConfig = rootElement.addElement("img_config");
        if (config.getSizeItem() != null && config.getSizeItem().size() > 0) {
            for (ConfigItem configItem : config.getSizeItem()) {
                Element sizeElement = imgConfig.addElement("model");
                sizeElement.addAttribute("name", configItem.getName());
                sizeElement.addAttribute("width", String.valueOf(configItem.getWidth()));
                sizeElement.addAttribute("height", String.valueOf(configItem.getHeight()));
                sizeElement.addAttribute("folder", configItem.getFolderName());
                sizeElement.addAttribute("iconWidth", String.valueOf(configItem.getIconWidth()));
                sizeElement.addAttribute("suffix", configItem.getSuffix());
            }
        }
        Element settingConfig = rootElement.addElement("setting");
        Element iconNameElement = settingConfig.addElement("setting_default_icon_name");
        iconNameElement.setText(config.getIconName());
        Element recursiveElement = settingConfig.addElement("setting_recursive");
        recursiveElement.setText(String.valueOf(config.isRecursive()));
        Element coverElement = settingConfig.addElement("setting_cover");
        coverElement.setText(String.valueOf(config.isCover()));
        Element suffixElement = settingConfig.addElement("setting_auto_add_suffix");
        suffixElement.setText(String.valueOf(config.isAutoSuffix()));
        Element toleranceElement = settingConfig.addElement("setting_tolerance");
        toleranceElement.setText(String.valueOf(config.getTolerance()));

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        XMLWriter writer = new XMLWriter(fileOutputStream, format);
        writer.write(doc);
        fileOutputStream.close();
    }

    /**
     * 获取配置的输入流
     * @return
     */
    public static InputStream getConfigInputStream() {
        return ConfigUtils.class.getResourceAsStream("/xml/default_config.xml");
    }

    /**
     * 以默认路径加载配置文件
     * @return
     */
    public static Config getConfigByDefault() {
        File file = new File(INSTALL_PATH);
        if (file.exists()) {
            return getConfigFromFile(file);
        }
        else{
            return getConfigFromResource();
        }
    }

    /**
     * 以默认路径保存配置文件
     * @return
     */
    public static boolean saveConfigByDefault(Config config) {
        boolean saveSuccess = false;
        File file = new File(INSTALL_PATH);
        if (!file.getParentFile().exists()) {
            FileUtils.createFolder(file.getParentFile());
        }
        try {
            saveConfig(config,file);
            saveSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveSuccess;
    }
}
