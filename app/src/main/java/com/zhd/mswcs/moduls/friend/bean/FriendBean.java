package com.zhd.mswcs.moduls.friend.bean;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import java.io.Serializable;

/**
 * Created by think-1 on 2017/11/20.
 */
@Table(name = "friend")
public class FriendBean implements Serializable{
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "nicknNme")
    private String nicknNme;
    @Column(name = "head")
    private String head;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "longtitude")
    private double longtitude;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "locationTime")
    private String locationTime;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "updateTime")
    private String updateTime;
    @Column(name = "address")
    private String address;
    @Column(name = "currentUser")
    private String currentUser;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNicknNme() {
        return nicknNme;
    }

    public void setNicknNme(String nicknNme) {
        this.nicknNme = nicknNme;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "FriendBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nicknNme='" + nicknNme + '\'' +
                ", currentUser='" + currentUser + '\'' +
                ", head='" + head + '\'' +
                ", telephone='" + telephone + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", locationTime='" + locationTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
