package com.zhd.mswcs.moduls.message.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by think-1 on 2017/11/23.
 */

public class MessageItemServerBean implements Serializable {


    /**
     * result : {"smsVersion":"dae9ae86-21e0-4779-97c1-f169b67613c5","sms":[{"groupId":"18a2f957-d0cd-4efb-8d64-3c7fa5436097","smsType":0,"sender":"15206215862","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":0,"dateTime":"2017-11-22T17:35:04.92","token":"385d7d33-4047-407e-89fb-9e42ed58b095","id":23},{"groupId":"590753ea-3c80-40a9-bb2a-fe47766a9e5f","smsType":0,"sender":"15206215822","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":0,"dateTime":"2017-11-22T17:43:42.837","token":"0f7131df-da15-4608-ad33-d58a9daacdc9","id":25}]}
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
         * smsVersion : dae9ae86-21e0-4779-97c1-f169b67613c5
         * sms : [{"groupId":"18a2f957-d0cd-4efb-8d64-3c7fa5436097","smsType":0,"sender":"15206215862","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":0,"dateTime":"2017-11-22T17:35:04.92","token":"385d7d33-4047-407e-89fb-9e42ed58b095","id":23},{"groupId":"590753ea-3c80-40a9-bb2a-fe47766a9e5f","smsType":0,"sender":"15206215822","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":0,"dateTime":"2017-11-22T17:43:42.837","token":"0f7131df-da15-4608-ad33-d58a9daacdc9","id":25}]
         */

        private String smsVersion;
        private List<SmsBean> sms;

        public String getSmsVersion() {
            return smsVersion==null?"":smsVersion;
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
            private int smsType;
            private String sender;
            private String receiver;
            private String message;
            private String lon;
            private String lat;
            private String co;
            private String gpsTime;
            private int state;
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

        }
    }
}
