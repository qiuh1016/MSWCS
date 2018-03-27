package com.zhd.mswcs.moduls.setting.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by think-1 on 2017/11/21.
 */
@Table(name = "sendTelePhone")
public class SendTelephoneListBean {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "contactName")
    private String contactName;
    @Column(name = "contactTelephone")
    private String contactTelephone;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "updateTime")
    private String updateTime;
    @Column(name = "isDelete")
    private String isDelete;//0表示未删除，1表示已删除
    @Column(name = "currentUser")
    private String currentUser;//防止不同人在同一个手机上登录,会将别人的SOS发送号码查出来

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SendTelephoneListBean{" +
                "id=" + id +
                ", contactName='" + contactName + '\'' +
                ", contactTelephone='" + contactTelephone + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
