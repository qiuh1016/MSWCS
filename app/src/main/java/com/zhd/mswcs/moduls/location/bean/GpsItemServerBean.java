package com.zhd.mswcs.moduls.location.bean;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class GpsItemServerBean {


    /**
     * result : {"smsVersion":"de1d4b61-72cd-468c-aefd-69905859e33a","sms":[{"smsType":10,"sender":"15206215859","receiver":"15206215859","message":null,"lon":"1","lat":"1","co":"ne","gpsTime":"2019-05-18 00:00:00.000","state":"0","dateTime":"2017/11/23 20:52:45","token":"5c9a303a-edbb-4e3d-bdd5-4a5a762b83a3"}]}
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

    public static class ResultBean {
        /**
         * smsVersion : de1d4b61-72cd-468c-aefd-69905859e33a
         * sms : [{"smsType":10,"sender":"15206215859","receiver":"15206215859","message":null,"lon":"1","lat":"1","co":"ne","gpsTime":"2019-05-18 00:00:00.000","state":"0","dateTime":"2017/11/23 20:52:45","token":"5c9a303a-edbb-4e3d-bdd5-4a5a762b83a3"}]
         */

        private String smsVersion;
        private List<SmsBean> sms;

        public String getSmsVersion() {
            return smsVersion;
        }

        public void setSmsVersion(String smsVersion) {
            this.smsVersion = smsVersion;
        }

        public List<SmsBean> getSms() {
            return sms;
        }

        public void setSms(List<SmsBean> sms) {
            this.sms = sms;
        }

        public static class SmsBean {
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

            private int smsType;
            private String sender;
            private String receiver;
            private String message;
            private String lon;
            private String lat;
            private String co;
            private String gpsTime;
            private String state;
            private String dateTime;
            private String token;

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
    }
}
