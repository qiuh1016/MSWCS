package com.zhd.mswcs.common.weidgt.picture.bean;

/**
 * Created by yinwei on 2015-11-16.
 */
public class ImageBean {

    private String ImagePath;

    public ImageBean(String imagePath) {

        this.ImagePath=imagePath;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
