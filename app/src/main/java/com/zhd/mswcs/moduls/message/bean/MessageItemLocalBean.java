package com.zhd.mswcs.moduls.message.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/23.
 */
@Table(name = "MessageItem")
public class MessageItemLocalBean implements Serializable {
    /**
     * groupId : 18a2f957-d0cd-4efb-8d64-3c7fa5436097
     * smsType : 0
     * sender : 15206215862
     * receiver : 15206215859
     * message : 1111
     * lon : null
     * lat : null
     * co : null
     * gpsTime : null
     * state : 0
     * dateTime : 2017-11-22T17:35:04.92
     * token : 385d7d33-4047-407e-89fb-9e42ed58b095
     * id : 23
     */

    @Column(name = "ID",isId = true,autoGen = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "userName")
    private String userName;
    @Column(name = "groupId")
    private String groupId;
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
    private int state;
    @Column(name = "dateTime")
    private String dateTime;
    @Column(name = "token")
    private String token;
    @Column(name = "hasNewMsg")
    private boolean hasNewMsg;

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }

    public void setHasNewMsg(boolean hasNewMsg) {
        this.hasNewMsg = hasNewMsg;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    @Override
    public String toString() {
        return "MessageItemLocalBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", smsType=" + smsType +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", co='" + co + '\'' +
                ", gpsTime='" + gpsTime + '\'' +
                ", state=" + state +
                ", dateTime='" + dateTime + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
