package com.android.img.crop.model;

import java.util.List;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/12
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class Config {
    private List<ConfigItem> sizeItem;
    private String iconName;
    private boolean recursive;
    private boolean cover;
    private boolean autoSuffix;
    /**
     * 容差值
     */
    private int tolerance;

    public List<ConfigItem> getSizeItem() {
        return sizeItem;
    }

    public void setSizeItem(List<ConfigItem> sizeItem) {
        this.sizeItem = sizeItem;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public boolean isAutoSuffix() {
        return autoSuffix;
    }

    public void setAutoSuffix(boolean autoSuffix) {
        this.autoSuffix = autoSuffix;
    }

    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }
}
