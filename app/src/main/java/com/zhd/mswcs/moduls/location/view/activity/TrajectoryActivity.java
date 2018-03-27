package com.zhd.mswcs.moduls.location.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.GPSUtils;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.NetworkUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.location.bean.GpsItemLocalBean;
import com.zhd.mswcs.moduls.location.bean.MyGpsLocusBean;
import com.zhd.mswcs.moduls.location.bean.TrajectoryBean;
import com.zhd.mswcs.moduls.location.presenter.TrajectoryPresenter;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_trajectory)
public class TrajectoryActivity extends BaseMvpActivity<MvpView,TrajectoryPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.map_view)
    private MapView mapView;
    @ViewInject(R.id.ll_detail_info)
    private LinearLayout ll_detail_info;
    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;
    @ViewInject(R.id.tv_gps_time)
    private TextView tv_gps_time;
    @ViewInject(R.id.tv_date_time)
    private TextView tv_date_time;
    @ViewInject(R.id.tv_lon)
    private TextView tv_lon;
    @ViewInject(R.id.tv_lat)
    private TextView tv_lat;
    private BaiduMap baiduMap;
    private MapStatusUpdate mMapStatusUpdate;
    private BitmapDescriptor searchGreenFromResource = BitmapDescriptorFactory.fromResource(R.mipmap.img_green_flag);
    private BitmapDescriptor searchRedFromResource = BitmapDescriptorFactory.fromResource(R.mipmap.img_red_flag);
    private List<OverlayOptions> searchLineOptionList = new ArrayList<>();
    private Overlay startMarkOverlay;
    private Overlay endMarkOverlay;
    private BitmapDescriptor icon_start;
    private BitmapDescriptor icon_end;
    private List<OverlayOptions> searchMarkerOptionsList = new ArrayList<>();
    private List<LatLng> friendLineList = new ArrayList<>();
    private List<LatLng> myLineList = new ArrayList<>();
    private List<OverlayOptions> mySearchMarkerOptionsList = new ArrayList<>();
    private List<OverlayOptions> mySearchLineOptionList = new ArrayList<>();
    private Overlay myStartMarkOverlay;
    private Overlay myEndMarkOverlay;



    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("轨迹")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build()
                .builder();
    }


    @Override
    public TrajectoryPresenter createPresenter() {
        return new TrajectoryPresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        baiduMap = mapView.getMap();
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        UiSettings settings=baiduMap.getUiSettings();
        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setRotateGesturesEnabled(false);//屏蔽旋转
        ChangeMapCenter(new LatLng(31.2359290000,121.4805390000),9.0f);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker==null||marker.getExtraInfo()==null){
                    return false;
                }else if(marker.getIcon() == icon_start){
                    return false;
                }else if(marker.getIcon() == icon_end){
                    return false;
                }else if(marker.getExtraInfo().get("data")!=null&&marker.getExtraInfo().get("data") instanceof MessageLocalBean){
                    MessageLocalBean bean = (MessageLocalBean)marker.getExtraInfo().get("data");
                    ll_detail_info.setVisibility(View.VISIBLE);
                    tv_gps_time.setText("GPS时间："+bean.getGpsTime().replace("T"," "));
                    tv_date_time.setText("定位时间："+bean.getDateTime().replace("T"," "));
                    if(Double.parseDouble(bean.getLon())<0){
                        tv_lon.setText("经        度："+"西经"+Math.abs((Double.parseDouble(bean.getLon())))+"度");
                    }else{
                        tv_lon.setText("经        度："+"东经"+bean.getLon()+"度");
                    }
                    if(Double.parseDouble(bean.getLat())<0){
                        tv_lat.setText("纬        度："+"南纬"+Math.abs((Double.parseDouble(bean.getLat())))+"度");
                    }else{
                        tv_lat.setText("纬        度："+"北纬"+bean.getLat()+"度");
                    }
                    tv_user_name.setVisibility(View.GONE);
                    return true;
                }else if(marker.getExtraInfo().get("data")!=null&&marker.getExtraInfo().get("data") instanceof TrajectoryBean.ResultBean){
                    TrajectoryBean.ResultBean bean = (TrajectoryBean.ResultBean)marker.getExtraInfo().get("data");
                    ll_detail_info.setVisibility(View.VISIBLE);
                    tv_gps_time.setText("GPS时间："+bean.getGpsTime().replace("T"," "));
                    tv_date_time.setText("定位时间："+bean.getDateTime().replace("T"," "));
                    if(Double.parseDouble(bean.getLongitude())<0){
                        tv_lon.setText("经        度："+"西经"+Math.abs((Double.parseDouble(bean.getLongitude())))+"度");
                    }else{
                        tv_lon.setText("经        度："+"东经"+bean.getLongitude()+"度");
                    }
                    if(Double.parseDouble(bean.getLatitude())<0){
                        tv_lat.setText("纬        度："+"南纬"+Math.abs((Double.parseDouble(bean.getLatitude())))+"度");
                    }else{
                        tv_lat.setText("纬        度："+"北纬"+bean.getLatitude()+"度");
                    }
                    tv_user_name.setText("用户信息："+ PreferencesUtils.getString(TrajectoryActivity.this,"telephone"));
                    return true;
                }else if(marker.getExtraInfo().get("data")!=null&&marker.getExtraInfo().get("data") instanceof GpsItemLocalBean){
                    GpsItemLocalBean bean = (GpsItemLocalBean)marker.getExtraInfo().get("data");
                    ll_detail_info.setVisibility(View.VISIBLE);
                    tv_gps_time.setText("GPS时间："+bean.getGpsTime().replace("T"," "));
                    tv_date_time.setText("定位时间："+bean.getDateTime().replace("T"," "));
                    if(Double.parseDouble(bean.getLon())<0){
                        tv_lon.setText("经        度："+"西经"+Math.abs((Double.parseDouble(bean.getLon())))+"度");
                    }else{
                        tv_lon.setText("经        度："+"东经"+bean.getLon()+"度");
                    }
                    if(Double.parseDouble(bean.getLat())<0){
                        tv_lat.setText("纬        度："+"南纬"+Math.abs((Double.parseDouble(bean.getLat())))+"度");
                    }else{
                        tv_lat.setText("纬        度："+"北纬"+bean.getLat()+"度");
                    }
                    tv_user_name.setText("用户信息："+bean.getUserName());
                    return true;
                }else if(marker.getExtraInfo().get("data")!=null&&marker.getExtraInfo().get("data") instanceof MyGpsLocusBean.ResultBean){
                    MyGpsLocusBean.ResultBean bean = (MyGpsLocusBean.ResultBean)marker.getExtraInfo().get("data");
                    ll_detail_info.setVisibility(View.VISIBLE);
                    tv_gps_time.setText("GPS时间："+bean.getGpsTime().replace("T"," "));
                    tv_date_time.setText("定位时间："+bean.getGpsTime().replace("T"," "));
                    if(Double.parseDouble(bean.getLongitude())<0){
                        tv_lon.setText("经        度："+"西经"+Math.abs((Double.parseDouble(bean.getLongitude())))+"度");
                    }else{
                        tv_lon.setText("经        度："+"东经"+bean.getLongitude()+"度");
                    }
                    if(Double.parseDouble(bean.getLatitude())<0){
                        tv_lat.setText("纬        度："+"南纬"+Math.abs((Double.parseDouble(bean.getLatitude())))+"度");
                    }else{
                        tv_lat.setText("纬        度："+"北纬"+bean.getLatitude()+"度");
                    }
                    tv_user_name.setText("用户信息："+ PreferencesUtils.getString(TrajectoryActivity.this,"telephone"));
                    return true;
                }
                return false;
            }
        });
        if(getIntent().getIntExtra("type",-1)==1){
            baiduMap.clear();
            //添加所有好友位置
            addFriendLocation();
            if(NetworkUtils.isConnected(TrajectoryActivity.this)){
                //获取自己位置信息
                getPresenter().getLocalGps();
                getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
                    @Override
                    public void bindData(Object data, int requestCode) {
                        switch (requestCode){
                            case 1:
                                TrajectoryBean trajectoryBean = ((TrajectoryBean)data);
                                if(trajectoryBean==null){
                                    return;
                                }else{
                                    try {
                                        LogUtils.d("trajectoryBean=",new Gson().toJson(trajectoryBean));
                                        if(trajectoryBean.getResult()!=null){
//                                            MyLocalGpsBean bean = MyApplication.getInstance().getDb().selector(MyLocalGpsBean.class).where("localiId","=",trajectoryBean.getResult().getId()).findFirst();
//                                            MyLocalGpsBean myLocalGpsBean = new MyLocalGpsBean();
//                                            myLocalGpsBean.setCoordinate(trajectoryBean.getResult().getCoordinate());
//                                            myLocalGpsBean.setDateTime(trajectoryBean.getResult().getDateTime());
//                                            myLocalGpsBean.setGpsTime(trajectoryBean.getResult().getGpsTime());
//                                            myLocalGpsBean.setLocalId(trajectoryBean.getResult().getId());
//                                            myLocalGpsBean.setLatitude(trajectoryBean.getResult().getLatitude());
//                                            myLocalGpsBean.setLongitude(trajectoryBean.getResult().getLongitude());
//                                            myLocalGpsBean.setSmsVersion(trajectoryBean.getResult().getSmsVersion());
//                                            if(bean!=null){
//                                                myLocalGpsBean.setId(bean.getId());
//                                                MyApplication.getInstance().getDb().saveOrUpdate(myLocalGpsBean);
//                                            }else{
//                                                MyApplication.getInstance().getDb().saveBindingId(myLocalGpsBean);
//                                            }

                                          //添加自己位置
                                          addMyLocation(trajectoryBean.getResult());

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                        }
                    }
                });
            }else{
//                try {
//                    List<MyLocalGpsBean> list = MyApplication.getInstance().getDb().selector(MyLocalGpsBean.class).orderBy("dateTime",true).findAll();
//                    if(list!=null&&list.size()>0){
//                        MyLocalGpsBean data = list.get(0);
//                        TrajectoryBean.ResultBean resultBean = new TrajectoryBean.ResultBean();
//                        resultBean.setCoordinate(data.getCoordinate());
//                        resultBean.setDateTime(data.getDateTime());
//                        resultBean.setGpsTime(data.getGpsTime());
//                        resultBean.setId(data.getLocalId());
//                        resultBean.setLatitude(data.getLatitude());
//                        resultBean.setLongitude(data.getLongitude());
//                        resultBean.setSmsVersion(data.getSmsVersion());
//                        //添加自己位置
//                        addMyLocation(resultBean);
//                    }
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }

                ToastUtil.showToast(TrajectoryActivity.this,"网络连接异常，无法获取自己位置");
            }
        }else{
            String sender = getIntent().getStringExtra("sender");
            String receiver = getIntent().getStringExtra("receiver");
            String startTime = getIntent().getStringExtra("startTime");
            String endTime = getIntent().getStringExtra("endTime");
            baiduMap.clear();
            initTrajectoryData(sender,receiver,startTime,endTime);
            getMyGpsLocusData(startTime,endTime);
        }

    }

    /**
     * 添加所有好友位置
     */
    private void addFriendLocation(){
        String currentUser = PreferencesUtils.getString(TrajectoryActivity.this, "telephone");
        ArrayList<String> friendPhoneList = getIntent().getStringArrayListExtra("data");
        for (String phone : friendPhoneList) {
            DbModel model = null;
            try {
                model = MyApplication.getInstance().getDb().findDbModelFirst(new SqlInfo("select * from GpsItem where ( sender = '" + phone + "' and receiver = '" + currentUser + "') and isDelete = 0 order by dateTime desc"));
                if (model == null) {
                    continue;
                }
                GpsItemLocalBean gpsItemLocalBean = new GpsItemLocalBean();
                gpsItemLocalBean.setLat(model.getString("lat"));
                gpsItemLocalBean.setLon(model.getString("lon"));
                gpsItemLocalBean.setGpsTime(model.getString("gpsTime").replace("T"," "));
                gpsItemLocalBean.setDateTime(model.getString("dateTime").replace("T"," "));
                FriendBean friendBean = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone", "=", phone).findFirst();
                if (friendBean != null) {
                    gpsItemLocalBean.setUserName(friendBean.getUserName());
                }else{
                    gpsItemLocalBean.setUserName(model.getString("sender"));
                }
                addFriendMark(gpsItemLocalBean);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取当前好友轨迹
     * @param sender
     * @param receiver
     * @param startTime
     * @param endTime
     */
    private void initTrajectoryData(String sender,String receiver,String startTime,String endTime) {
        try {
           List<MessageLocalBean> dataList = MyApplication.getInstance().getDb()
                    .selector(MessageLocalBean.class)
                    .where("sender","=",sender)
                    .and("receiver","=",receiver)
                    .and("smsType","=",10)
                    .and("gpsTime",">=",startTime)
                    .and("gpsTime","<=",endTime)
                    .orderBy("dateTime")
                    .findAll();

//           List<DbModel> list =  MyApplication.getInstance().getDb().findDbModelAll(new SqlInfo(" select * from messageRecord where sender = " +
//                    sender +" and receiver = "+ receiver +" and smsType = 10 and gpsTime between "
//                   + "'"+startTime +"'"+" and " + "'"+endTime+"'"+" ORDER BY dateTime"));

            if(dataList!=null&&dataList.size()>0){
                if(friendLineList!=null&&friendLineList.size()>0){
                    friendLineList.clear();
                }
                for(MessageLocalBean bean:dataList){
                    double lat = 0.0;
                    double lon = 0.0;
                    switch (Integer.parseInt(bean.getCo())){
                        case 0:
                            lat = Double.parseDouble(bean.getLat());
                            lon = Double.parseDouble(bean.getLon());
                            break;
                        case 1:
                            lat = Double.parseDouble(bean.getLat());
                            lon = -1*Double.parseDouble(bean.getLon());
                            break;
                        case 2:
                            lat = -1*Double.parseDouble(bean.getLat());
                            lon = Double.parseDouble(bean.getLon());
                            break;
                        case 3:
                            lat = -1*Double.parseDouble(bean.getLat());
                            lon = -1*Double.parseDouble(bean.getLon());
                            break;
                    }
                    Map<String,Double> dis_map = GPSUtils.gcj_encrypt(lat,lon);
                    Map<String,Double> dist = GPSUtils.bd_encrypt(dis_map.get("lat"),dis_map.get("lon"));
                    bean.setLat(String.valueOf(dist.get("lat")));
                    bean.setLon(String.valueOf(dist.get("lon")));
                    friendLineList.add(new LatLng(dist.get("lat"),dist.get("lon")));
                }
                if(dataList.size()<2){
                    addMark(dataList);
                }else{
                    addLine(dataList,friendLineList,dataList.get(0).getDateTime().replace("T"," "),dataList.get(dataList.size()-1).getDateTime().replace("T"," "));
                }

            }else{
                ToastUtils.showLongToast(getContext(), "暂无好友轨迹信息.");
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

    }



    public void addLine(List<MessageLocalBean> list,List<LatLng> lineList, String startTime, String endTime) {
        if(searchLineOptionList!=null&&searchLineOptionList.size()>0){
            searchLineOptionList.clear();
        }
         OverlayOptions ooPolyline = new PolylineOptions().width(3).color(Color.RED).points(lineList);
         searchLineOptionList.add(ooPolyline);
         baiduMap.addOverlays(searchLineOptionList);
         addTrajectory(list,startTime, endTime);
    }


    private void addTrajectory(List<MessageLocalBean> list ,String startTime, String endTime) {
        if(startMarkOverlay!=null){
            startMarkOverlay.remove();
        }
        if(endMarkOverlay!=null){
            endMarkOverlay.remove();
        }
        LatLng StartLatLng = new LatLng(Double.parseDouble(list.get(0).getLat()),Double.parseDouble(list.get(0).getLon()));
        LatLng EndLatLng =  new LatLng(Double.parseDouble(list.get(list.size() - 1).getLat()),Double.parseDouble(list.get(list.size() - 1).getLon()));
        View startView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mark_start,null,false);
        TextView tv_start = (TextView)startView.findViewById(R.id.tv_mark_start);
        tv_start.setText(startTime);
        icon_start = BitmapDescriptorFactory.fromView(startView);
        MarkerOptions startMarker = new MarkerOptions().position(StartLatLng).icon(icon_start).zIndex(9).draggable(false).anchor(0.5f,1.0f);
        startMarker.animateType(MarkerOptions.MarkerAnimateType.grow);
        startMarkOverlay = baiduMap.addOverlay(startMarker);

        View endView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mark_end,null,false);
        TextView tv_end = (TextView) endView.findViewById(R.id.tv_mark_end);
        tv_end.setText(endTime);
        icon_end = BitmapDescriptorFactory.fromView(endView);
        MarkerOptions endMarker = new MarkerOptions().position(EndLatLng).icon(icon_end).zIndex(9).draggable(false).anchor(0.5f,1.0f);
        endMarker.animateType(MarkerOptions.MarkerAnimateType.grow);
        endMarkOverlay =baiduMap.addOverlay(endMarker);

        if(searchMarkerOptionsList!=null&&searchMarkerOptionsList.size()>0){
            searchMarkerOptionsList.clear();
        }
        for(MessageLocalBean bean : list) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(Double.parseDouble(bean.getLat()),Double.parseDouble(bean.getLon())));
            // 创建图标对象
            markerOptions.icon(searchGreenFromResource);
            //绑定数据
            markerOptions.extraInfo(bundle);
            // 设置层级关系
            markerOptions.zIndex(9);
            // 设置允许拖拽覆盖物
            markerOptions.draggable(false);
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.2f,0.9f);
            searchMarkerOptionsList.add(markerOptions);
        }
        // 绑定覆盖物
        baiduMap.addOverlays(searchMarkerOptionsList);
        ChangeMapCenter(StartLatLng,4.0f);
    }


    /**
     * 当只有一个位置时显示一个绿旗
     * @param list
     */
    private void addMark(List<MessageLocalBean> list) {
        if(startMarkOverlay!=null){
            startMarkOverlay.remove();
        }
        if(endMarkOverlay!=null){
            endMarkOverlay.remove();
        }

        if(searchMarkerOptionsList!=null&&searchMarkerOptionsList.size()>0){
            searchMarkerOptionsList.clear();
        }
        for(MessageLocalBean bean : list) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(Double.parseDouble(bean.getLat()),Double.parseDouble(bean.getLon())));
            // 创建图标对象
            markerOptions.icon(searchGreenFromResource);
            // 设置层级关系
            markerOptions.zIndex(9);
            //绑定数据
            markerOptions.extraInfo(bundle);
            // 设置允许拖拽覆盖物
            markerOptions.draggable(false);
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.5f,1.0f);
            searchMarkerOptionsList.add(markerOptions);
        }
        // 绑定覆盖物
        baiduMap.addOverlays(searchMarkerOptionsList);
        ChangeMapCenter(new LatLng(Double.parseDouble(list.get(0).getLat()),Double.parseDouble(list.get(0).getLon())),9.0f);
    }

    /**
     * 添加自己位置
     * @param bean
     */
    private void addMyLocation(TrajectoryBean.ResultBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        // 创建覆盖物
        MarkerOptions markerOptions = new MarkerOptions();
        // 指定覆盖物的位置
        markerOptions.position(new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude())));
        // 创建图标对象
        markerOptions.icon(searchRedFromResource);
        // 设置层级关系
        markerOptions.zIndex(9);
        //绑定数据
        markerOptions.extraInfo(bundle);
        // 设置允许拖拽覆盖物
        markerOptions.draggable(false);
        //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.visible(true);
        // 绑定覆盖物
        baiduMap.addOverlay(markerOptions);

        ll_detail_info.setVisibility(View.VISIBLE);
        tv_gps_time.setText("GPS时间：" + bean.getGpsTime().replace("T"," "));
        tv_date_time.setText("定位时间：" + bean.getDateTime().replace("T"," "));
        if(Double.parseDouble(bean.getLongitude())<0){
            tv_lon.setText("经        度："+"西经"+Math.abs((Double.parseDouble(bean.getLongitude())))+"度");
        }else{
            tv_lon.setText("经        度："+"东经"+bean.getLongitude()+"度");
        }
        if(Double.parseDouble(bean.getLatitude())<0){
            tv_lat.setText("纬        度："+"南纬"+Math.abs((Double.parseDouble(bean.getLatitude())))+"度");
        }else{
            tv_lat.setText("纬        度："+"北纬"+bean.getLatitude()+"度");
        }
        tv_user_name.setText("用户信息："+ PreferencesUtils.getString(TrajectoryActivity.this,"telephone"));
        ChangeMapCenter(new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude())), 9.0f);
    }

    /**
     * 当好友有多个轨迹时显示所有的绿旗
     */

    private void addFriendMark(GpsItemLocalBean bean){
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        // 创建覆盖物
        MarkerOptions markerOptions = new MarkerOptions();
        // 指定覆盖物的位置
        markerOptions.position(new LatLng(Double.parseDouble(bean.getLat()),Double.parseDouble(bean.getLon())));
        // 创建图标对象
        markerOptions.icon(searchGreenFromResource);
        // 设置层级关系
        markerOptions.zIndex(9);
        //绑定数据
        markerOptions.extraInfo(bundle);
        // 设置允许拖拽覆盖物
        markerOptions.draggable(false);
        //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
        markerOptions.anchor(0.5f,0.5f);
        // 绑定覆盖物
        baiduMap.addOverlay(markerOptions);

    }








    /**
     * 更新当前地图中心点
     * @param latLng
     */
    private  void ChangeMapCenter(LatLng latLng,float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(zoom);
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        baiduMap.animateMapStatus(mMapStatusUpdate);
    }


    /**
     * 从网络获取自己所有轨迹数据
     * @param startTime
     * @param endTime
     */
    private void getMyGpsLocusData(String startTime,String endTime){
        if(NetworkUtils.isConnected(TrajectoryActivity.this)){
            JSONObject requestParams = new JSONObject();
            try {
                requestParams.put("stTime",startTime);
                requestParams.put("edTime",endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getPresenter().getMyGpsLocus(requestParams);
            getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
                @Override
                public void bindData(Object data, int requestCode) {
                    switch (requestCode){
                        case 1:
                            MyGpsLocusBean myGpsLocusBean = ((MyGpsLocusBean)data);
                            if(myGpsLocusBean==null){
                                return;
                            }else{
                                try {
                                    LogUtils.d("myGpsLocusBean=",new Gson().toJson(myGpsLocusBean));
                                    if(myGpsLocusBean.getResult()!=null){
                                        addMyGpsLocusData(myGpsLocusBean.getResult());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                    }
                }
            });
        }else{
            ToastUtil.showToast(TrajectoryActivity.this,getResources().getString(R.string.code_system_error));
        }
    }


    /**
     * 在地图上添加自己所有轨迹信息
     * @param dataList
     */
    private void addMyGpsLocusData(List<MyGpsLocusBean.ResultBean> dataList) {
            if(dataList!=null&&dataList.size()>0){
                if(myLineList!=null&&myLineList.size()>0){
                    myLineList.clear();
                }
                for(MyGpsLocusBean.ResultBean bean:dataList){
                    myLineList.add(new LatLng(Double.parseDouble(bean.getLatitude()),Double.parseDouble(bean.getLongitude())));
                }
                if(dataList.size()<2){
                    addMyGpsLocusMark(dataList);
                }else{
                    addMyGpsLocusLine(dataList,myLineList,dataList.get(0).getGpsTime().replace("T"," "),dataList.get(dataList.size()-1).getGpsTime().replace("T"," "));
                }

            }else{
                ToastUtils.showLongToast(getContext(), "暂无自己轨迹信息.");
            }

    }


    /**
     * 添加自己所有轨迹中的红旗
     * @param list
     */
    private void addMyGpsLocusMark(List<MyGpsLocusBean.ResultBean> list) {
        if(myStartMarkOverlay!=null){
            myStartMarkOverlay.remove();
        }
        if(myEndMarkOverlay!=null){
            myEndMarkOverlay.remove();
        }

        if(mySearchMarkerOptionsList!=null&&mySearchMarkerOptionsList.size()>0){
            mySearchMarkerOptionsList.clear();
        }
        for(MyGpsLocusBean.ResultBean bean : list) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(Double.parseDouble(bean.getLatitude()),Double.parseDouble(bean.getLongitude())));
            // 创建图标对象
            markerOptions.icon(searchRedFromResource);
            // 设置层级关系
            markerOptions.zIndex(9);
            //绑定数据
            markerOptions.extraInfo(bundle);
            // 设置允许拖拽覆盖物
            markerOptions.draggable(false);
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.5f,1.0f);
            mySearchMarkerOptionsList.add(markerOptions);
        }
        // 绑定覆盖物
        baiduMap.addOverlays(mySearchMarkerOptionsList);
        ChangeMapCenter(new LatLng(Double.parseDouble(list.get(0).getLatitude()),Double.parseDouble(list.get(0).getLongitude())),4.0f);
    }




    /**
     * 给自己所有轨迹中的红旗之间连线
     * @param list
     */
    public void addMyGpsLocusLine(List<MyGpsLocusBean.ResultBean> list,List<LatLng> lineList, String startTime, String endTime) {
        if(mySearchLineOptionList!=null&&mySearchLineOptionList.size()>0){
            mySearchLineOptionList.clear();
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(3).color(Color.RED).points(lineList);
        mySearchLineOptionList.add(ooPolyline);
        baiduMap.addOverlays(mySearchLineOptionList);
        addMyGpsLocusTrajectory(list,startTime, endTime);
    }


    private void addMyGpsLocusTrajectory(List<MyGpsLocusBean.ResultBean> list ,String startTime, String endTime) {
        if(myStartMarkOverlay!=null){
            myStartMarkOverlay.remove();
        }
        if(myEndMarkOverlay!=null){
            myEndMarkOverlay.remove();
        }
        LatLng StartLatLng = new LatLng(Double.parseDouble(list.get(0).getLatitude()),Double.parseDouble(list.get(0).getLongitude()));
        LatLng EndLatLng =  new LatLng(Double.parseDouble(list.get(list.size() - 1).getLatitude()),Double.parseDouble(list.get(list.size() - 1).getLongitude()));
        View startView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mark_start,null,false);
        TextView tv_start = (TextView)startView.findViewById(R.id.tv_mark_start);
        tv_start.setText(startTime);
        icon_start = BitmapDescriptorFactory.fromView(startView);
        MarkerOptions startMarker = new MarkerOptions().position(StartLatLng).icon(icon_start).zIndex(9).draggable(false).anchor(0.5f,1.0f);
        startMarker.animateType(MarkerOptions.MarkerAnimateType.grow);
        myStartMarkOverlay = baiduMap.addOverlay(startMarker);

        View endView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mark_end,null,false);
        TextView tv_end = (TextView) endView.findViewById(R.id.tv_mark_end);
        tv_end.setText(endTime);
        icon_end = BitmapDescriptorFactory.fromView(endView);
        MarkerOptions endMarker = new MarkerOptions().position(EndLatLng).icon(icon_end).zIndex(9).draggable(false).anchor(0.5f,1.0f);
        endMarker.animateType(MarkerOptions.MarkerAnimateType.grow);
        myEndMarkOverlay =baiduMap.addOverlay(endMarker);

        if(mySearchMarkerOptionsList!=null&&mySearchMarkerOptionsList.size()>0){
            mySearchMarkerOptionsList.clear();
        }
        for(MyGpsLocusBean.ResultBean bean : list) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(Double.parseDouble(bean.getLatitude()),Double.parseDouble(bean.getLongitude())));
            // 创建图标对象
            markerOptions.icon(searchRedFromResource);
            //绑定数据
            markerOptions.extraInfo(bundle);
            // 设置层级关系
            markerOptions.zIndex(9);
            // 设置允许拖拽覆盖物
            markerOptions.draggable(false);
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.2f,0.9f);
            mySearchMarkerOptionsList.add(markerOptions);
        }
        // 绑定覆盖物
        baiduMap.addOverlays(mySearchMarkerOptionsList);
        ChangeMapCenter(StartLatLng,9.0f);
    }

    @Event(value = R.id.iv_close)
    private void closeDialog(View btn){
        ll_detail_info.setVisibility(View.GONE);
    }




}
