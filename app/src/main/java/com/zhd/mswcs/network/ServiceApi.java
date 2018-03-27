package com.zhd.mswcs.network;

import com.zhd.mswcs.moduls.location.bean.GpsItemServerBean;
import com.zhd.mswcs.moduls.location.bean.MyGpsLocusBean;
import com.zhd.mswcs.moduls.location.bean.TrajectoryBean;
import com.zhd.mswcs.moduls.message.bean.MessageItemServerBean;
import com.zhd.mswcs.moduls.message.bean.MessageServerBean;
import com.zhd.mswcs.moduls.message.bean.SendChatBean;
import com.zhd.mswcs.moduls.message.bean.SendGetGpsBean;
import com.zhd.mswcs.moduls.message.bean.SendGpsBean;
import com.zhd.mswcs.moduls.user.bean.LineBean;
import com.zhd.mswcs.moduls.user.bean.UserBean;
import com.zhd.mswcs.moduls.user.bean.VerificationCodeBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者:zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */

public interface ServiceApi {
//    //无参数
//    @POST("users/stven0king/repos")
//    Call<List<Repo>> listRepos();
//    //少数参数
//    @FormUrlEncoded
//    @POST("users/stven0king/repos")
//    Call<List<Repo>> listRepos(@Field("time") long time);
//    //参数较多
//    @FormUrlEncoded
//    @POST("users/stven0king/repos")
//    Call<List<Repo>> listRepos(@FieldMap Map<String, String> params);


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/UserLogin")
    Observable<UserBean> login(@Body RequestBody body);


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/UserGetCheckCode")
    Observable<VerificationCodeBean> sendVertificateCode(@Body RequestBody body);



    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/SendMsg")
    Observable<SendChatBean> sendChat(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/GetSMS")
    Observable<MessageServerBean> getSMS(@Body RequestBody body);


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/GetSMSItem")
    Observable<MessageItemServerBean> getSMSItem(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/GetGpsItem")
    Observable<GpsItemServerBean> getGpsItem(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/SendGps")
    Observable<SendGpsBean> sendGps(@Body RequestBody body);


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/SendGetGps")
    Observable<SendGetGpsBean> sendGetGps(@Body RequestBody body);



    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("shipLocus/SubmitShipLocus")
    Observable<LineBean> submitShipLocus(@Body RequestBody body);


    @POST("local/GetLocalGps")
    Observable<TrajectoryBean> getLocalGps();


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("sMS/GetMyGpsLocus")
    Observable<MyGpsLocusBean> getMyGpsLocus(@Body RequestBody body);





}
