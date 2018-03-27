package com.zhd.mswcs.moduls.location.bean;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/27.
 */

public class CityBean implements Serializable {
    public int cityId;
    public String cityName;
    public String dataSize;

    public CityBean(int cityId, String cityName, String dataSize) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.dataSize = dataSize;
    }

}
