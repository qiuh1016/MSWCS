package com.zhd.mswcs.moduls.base.model;

import android.content.Context;
import android.util.Log;

import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mvp.framework.base.model.MvpModel;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */

public class BaseModel implements MvpModel {
    private Context context;
    public BaseModel(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getServerUrl(){
        String url = "";
        if(StringUtils.isEmpty(PreferencesUtils.getString(context,"ipAddress"))){
            url = "http://192.168.1.101"+Constant.PREFIX_URL;
        }else{
            url =  "http://"+PreferencesUtils.getString(context,"ipAddress") + Constant.PREFIX_URL;
        }
        Log.e("CommonUrl==",url);
        return url;
    }


    public <T> T buildService(Class<T> service){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) //设置请求超时
                .readTimeout(5, TimeUnit.MINUTES) //设置读取数据超时时间
                .build();
        //client.interceptors().add(new HttpInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServerUrl())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }


}
