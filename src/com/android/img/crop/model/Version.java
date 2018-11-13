package com.android.img.crop.model;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/13
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class Version {
    private int versionCode;
    private String versionName;
    private String updateTime;

    public Version(int versionCode, String versionName, String updateTime) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.updateTime = updateTime;
    }

    public Version() {
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
