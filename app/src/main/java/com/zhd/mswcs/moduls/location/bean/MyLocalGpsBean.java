package com.zhd.mswcs.moduls.location.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by think-1 on 2017/12/22.
 */
@Table(name = "LocalGps")
public class MyLocalGpsBean {
    /**
     * longitude : 111.185185
     * latitude : 11.185185
     * coordinate : 2
     * gpsTime : 27/11/2017 17:21:35
     * smsVersion : 2ab514ca-28ab-4ebf-9d63-7b5e28b5d379
     * id : 1
     */
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "coordinate")
    private String coordinate;
    @Column(name = "gpsTime")
    private String gpsTime;
    @Column(name = "dateTime")
    private String dateTime;
    @Column(name = "smsVersion")
    private String smsVersion;
    @Column(name = "localiId")
    private int localId;

    @Column(name = "ID",isId = true)
    private int id;

    public String getDateTime() {
        return dateTime;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getSmsVersion() {
        return smsVersion;
    }

    public void setSmsVersion(String smsVersion) {
        this.smsVersion = smsVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
