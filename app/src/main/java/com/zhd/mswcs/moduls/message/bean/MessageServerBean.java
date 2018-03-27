package com.zhd.mswcs.moduls.message.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class MessageServerBean implements Serializable{

    /**
     * result : {"smsVersion":"d9a45575-244e-4c2a-ae3d-21a2fd2f51e5","sms":[{"smsType":0,"sender":"15206215859","receiver":"15206215862","message":"test","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:32:00","token":"e1bc7d3a-2e36-4a59-83d6-f323f2876418"},{"smsType":0,"sender":"15206215859","receiver":"15206215862","message":"test","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:32:00","token":"adcce52a-63c7-4523-9ecf-acf83f1dc747"},{"smsType":0,"sender":"15206215862","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:35:00","token":"385d7d33-4047-407e-89fb-9e42ed58b095"},{"smsType":0,"sender":"15206215822","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"62988378-44f6-48be-ad3a-3670855c5208"},{"smsType":0,"sender":"15206215822","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"0f7131df-da15-4608-ad33-d58a9daacdc9"},{"smsType":0,"sender":"15206215811","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"75c33ca9-edaf-40b3-a503-90595852c079"}]}
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
         * smsVersion : d9a45575-244e-4c2a-ae3d-21a2fd2f51e5
         * sms : [{"smsType":0,"sender":"15206215859","receiver":"15206215862","message":"test","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:32:00","token":"e1bc7d3a-2e36-4a59-83d6-f323f2876418"},{"smsType":0,"sender":"15206215859","receiver":"15206215862","message":"test","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:32:00","token":"adcce52a-63c7-4523-9ecf-acf83f1dc747"},{"smsType":0,"sender":"15206215862","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:35:00","token":"385d7d33-4047-407e-89fb-9e42ed58b095"},{"smsType":0,"sender":"15206215822","receiver":"15206215859","message":"1111","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"62988378-44f6-48be-ad3a-3670855c5208"},{"smsType":0,"sender":"15206215822","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"0f7131df-da15-4608-ad33-d58a9daacdc9"},{"smsType":0,"sender":"15206215811","receiver":"15206215859","message":"ssss","lon":null,"lat":null,"co":null,"gpsTime":null,"state":"0","dateTime":"2017/11/22 17:44:00","token":"75c33ca9-edaf-40b3-a503-90595852c079"}]
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
             * smsType : 0
             * sender : 15206215859
             * receiver : 15206215862
             * message : test
             * lon : null
             * lat : null
             * co : null
             * gpsTime : null
             * state : 0
             * dateTime : 2017/11/22 17:32:00
             * token : e1bc7d3a-2e36-4a59-83d6-f323f2876418
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
