package com.zhd.mswcs;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.zhd.mswcs.common.service.NetworkStateService;
import com.zhd.mswcs.common.service.SearchLocationService;
import com.zhd.mswcs.moduls.user.bean.UserBean;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;


public class MyApplication extends Application {
    private static MyApplication mContext;
    private DbManager db;
    private UserBean user;
    private int imageRes;

    public static MyApplication getmContext() {
        return mContext;
    }

    public static void setmContext(MyApplication mContext) {
        MyApplication.mContext = mContext;
    }

    public void setDb(DbManager db) {
        this.db = db;
    }

    public int getImageRes() {
        return imageRes==0?R.mipmap.img_has_wifi:imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public DbManager getDb() {
        return db;
    }

    public UserBean getUser() {
        if(user==null){
            user = new UserBean();
        }
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        mContext = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        initDb();
        startService(new Intent(getApplicationContext(), SearchLocationService.class));
        startService(new Intent(getApplicationContext(), NetworkStateService.class));

    }


    public static MyApplication getInstance(){
        return mContext;
    }





    public void initDb(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("mswcs.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                //.setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mswcs/db")) //设置数据库.db文件存放的目录,默认为包名下databases目录下
                .setDbVersion(1)//设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })//设置数据库创建时的Listener
                //设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                        if(newVersion>oldVersion){

                        }
                    }
                });
               db = x.getDb(daoConfig);
    }

}