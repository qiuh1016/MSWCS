package com.zhd.mswcs.moduls.message.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/22.
 */

@Table(name = "messageRecord", onCreated = "CREATE UNIQUE INDEX index_message ON messageRecord(token)")
  public class MessageLocalBean implements Serializable {
    @Column(name = "id",isId = true)
    private int id;
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
    @Column(name = "hasNewMsg")
    private boolean hasNewMsg;
    @Column(name = "time")
    float time;//录音时长
    @Column(name = "filePath")
    String filePath;//文件路径

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }

    public void setHasNewMsg(boolean hasNewMsg) {
        this.hasNewMsg = hasNewMsg;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

