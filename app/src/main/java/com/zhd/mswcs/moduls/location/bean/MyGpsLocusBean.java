package com.zhd.mswcs.moduls.location.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by think-1 on 2017/12/22.
 */

public class MyGpsLocusBean implements Serializable{


    /**
     * result : [{"longitude":"111.385185","latitude":"11.585185","coordinate":"2","gpsTime":"2017-11-21T10:12:52","id":3},{"longitude":"111.385285","latitude":"11.585285","coordinate":"2","gpsTime":"2017-11-22T11:13:17","id":8}]
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private String targetUrl;
    private boolean success;
    private String error;
    private boolean unAuthorizedRequest;
    private boolean __abp;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * longitude : 111.385185
         * latitude : 11.585185
         * coordinate : 2
         * gpsTime : 2017-11-21T10:12:52
         * id : 3
         */

        private String longitude;
        private String latitude;
        private String coordinate;
        private String gpsTime;
        private int id;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
