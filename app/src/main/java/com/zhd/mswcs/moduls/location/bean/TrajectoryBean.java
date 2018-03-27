package com.zhd.mswcs.moduls.location.bean;

import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/22.
 */

public class TrajectoryBean implements Serializable{
    /**
     * result : {"longitude":"111.185185","latitude":"11.185185","coordinate":"2","gpsTime":"27/11/2017 17:21:35","smsVersion":"2ab514ca-28ab-4ebf-9d63-7b5e28b5d379","id":1}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private String targetUrl;
    private boolean success;
    private String error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public static class ResultBean implements Serializable{
        /**
         * longitude : 111.185185
         * latitude : 11.185185
         * coordinate : 2
         * gpsTime : 27/11/2017 17:21:35
         * smsVersion : 2ab514ca-28ab-4ebf-9d63-7b5e28b5d379
         * id : 1
         */

        private String longitude;
        private String latitude;
        private String coordinate;
        private String gpsTime;
        private String dateTime;
        private String smsVersion;
        private int id;

        public String getDateTime() {
            return dateTime;
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
}
