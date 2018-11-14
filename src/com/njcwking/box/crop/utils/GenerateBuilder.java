package com.njcwking.box.crop.utils;

import java.io.File;
import java.util.List;

import com.njcwking.box.crop.model.ConfigItem;

public class GenerateBuilder {
    private String rootPath;
    private ConfigItem currentModel;// 当前模式
    private List<ConfigItem> generateModels;// 需要生成的模式
    private String iconName;
    private File file;
    /**
     * 是否递归
     */
    private boolean isRecursion = false;
    /**
     * 是否覆盖
     */
    private boolean isCover = false;

    /**
     * 容差值
     */
    private int tolerance = 0;

    /**
     * 自动添加后缀
     */
    private boolean autoAddSuffix = false;

    public GenerateBuilder setRootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public GenerateBuilder setCurrentMode(ConfigItem model) {
        this.currentModel = model;
        return this;
    }

    public GenerateBuilder setRecursion(boolean isRecursion) {
        this.isRecursion = isRecursion;
        return this;
    }

    public GenerateBuilder setIconName(String iconName) {
        this.iconName = iconName;
        return this;
    }

    public GenerateBuilder setGenerateModels(List<ConfigItem> models) {
        this.generateModels = models;
        return this;
    }

    public GenerateBuilder setSaveFile(File file) {
        this.file = file;
        return this;
    }

    public GenerateBuilder setCover(boolean cover) {
        isCover = cover;
        return this;
    }

    public GenerateBuilder setTolerance(int tolerance) {
        this.tolerance = tolerance;
        return this;
    }

    public GenerateBuilder setAutoAddSuffix(boolean autoAddSuffix) {
        this.autoAddSuffix = autoAddSuffix;
        return this;
    }


    public String getRootPath() {
        return rootPath;
    }

    public ConfigItem getCurrentModel() {
        return currentModel;
    }

    public List<ConfigItem> getGenerateModels() {
        return generateModels;
    }

    public String getIconName() {
        return iconName;
    }

    public File getFile() {
        return file;
    }

    public boolean isRecursion() {
        return isRecursion;
    }

    public boolean isCover() {
        return isCover;
    }

    public int getTolerance() {
        return tolerance;
    }

    public boolean isAutoAddSuffix() {
        return autoAddSuffix;
    }

    public GenerateTool createTool() {
        return new GenerateTool(this);
    }
}
