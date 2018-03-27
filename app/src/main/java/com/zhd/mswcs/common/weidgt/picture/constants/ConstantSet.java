package com.zhd.mswcs.common.weidgt.picture.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by yinwei on 2015-11-17.
 */
public class ConstantSet {
    //文件
    public static final String LOCALFILE = Environment.
            getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "mswcs" + File.separator + "userPic" + File.separator;

    public static final String USERTEMPPIC="userTemp.jpg";

    public static final String USERPIC="user.jpg";

    public static final String IMAGE_CACHE_DIRECTORY="images";

    //图片裁剪

    public static final int TAKEPICTURE = 0X5;

    public static final int SELECTPICTURE = 0X6;

    public static final int CROPPICTURE = 0X7;
}
