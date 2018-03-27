package com.zhd.mswcs.moduls.location.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/24.
 */
@Table(name = "GpsItem")
public class GpsItemLocalBean implements Serializable {
    /**
     * smsType : 10
     * sender : 15206215859
     * receiver : 15206215859
     * message : null
     * lon : 1
     * lat : 1
     * co : ne
     * gpsTime : 2019-05-18 00:00:00.000
     * state : 0
     * dateTime : 2017/11/23 20:52:45
     * token : 5c9a303a-edbb-4e3d-bdd5-4a5a762b83a3
     */
    @Column(name = "ID",isId = true)
    private int id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "smsType")
    private int smsType;
    @Column(name = "sender")
    private String sender;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "message")
    private String message;
    @Column(name = "lon")
    private String lon;
    @Column(name = "lat")
    private String lat;
    @Column(name = "co")
    private String co;
    @Column(name = "gpsTime")
    private String gpsTime;
    @Column(name = "state")
    private String state;
    @Column(name = "dateTime")
    private String dateTime;
    @Column(name = "token")
    private String token;

    @Column(name = "isDelete")
    private String isDelete = "0";//0表示未删除，1表示已删除


    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSmsType() {
        return smsType;
    }

    public void setSmsType(int smsType) {
        this.smsType = smsType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
