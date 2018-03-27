package com.zhd.mswcs.moduls.user.bean;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/28.
 */

public class SelectLineBean implements Serializable {
    private double latitude;
    private double lontitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLontitude() {
        return lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }




}
