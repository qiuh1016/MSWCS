package com.zhd.mswcs.moduls.message.view.activity;

import android.os.Bundle;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.GPSUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.location.presenter.TrajectoryPresenter;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghanjun on 2017/9/13.
 * 当前好友对话所对应的位置信息
 */
@ContentView(R.layout.activity_chat_location)
public class ChatLocationActivity extends BaseMvpActivity<MvpView,TrajectoryPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.map_view)
    private MapView mapView;
    @ViewInject(R.id.ll_detail_info)
    private LinearLayout ll_detail_info;
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
    private List<OverlayOptions> searchMarkerOptionsList = new ArrayList<>();



    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("位置信息")
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
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker==null){
                    return false;
                }else if(marker.getExtraInfo().get("data")!=null&&marker.getExtraInfo().get("data") instanceof MessageLocalBean){
                    MessageLocalBean bean = (MessageLocalBean)marker.getExtraInfo().get("data");
                    ll_detail_info.setVisibility(View.VISIBLE);
                    tv_gps_time.setText("GPS时间："+bean.getGpsTime());
                    tv_date_time.setText("定位时间："+bean.getDateTime());
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
                    return true;
                }
                return false;
            }
        });
        initLocationData();

    }

           private void initLocationData() {
                try {
                    MessageLocalBean bean = (MessageLocalBean)getIntent().getSerializableExtra("data");
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
                    addMark(bean);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void addMark(MessageLocalBean bean) {
        baiduMap.clear();
        if(searchMarkerOptionsList!=null&&searchMarkerOptionsList.size()>0){
            searchMarkerOptionsList.clear();
        }
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(Double.parseDouble(bean.getLat()),Double.parseDouble(bean.getLon())));
            // 创建图标对象
            if(StringUtils.equals(getIntent().getStringExtra("type"),"1")) {
                //好友位置
                markerOptions.icon(searchGreenFromResource);
            }else{
                //自己位置
                markerOptions.icon(searchRedFromResource);
            }
            // 设置层级关系
            markerOptions.zIndex(9);
            //绑定数据
            markerOptions.extraInfo(bundle);
            // 设置允许拖拽覆盖物
            markerOptions.draggable(false);
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.5f,0.5f);
            searchMarkerOptionsList.add(markerOptions);
        // 绑定覆盖物
        baiduMap.addOverlays(searchMarkerOptionsList);
        ll_detail_info.setVisibility(View.VISIBLE);
        tv_gps_time.setText("GPS时间："+bean.getGpsTime());
        tv_date_time.setText("定位时间："+bean.getDateTime());
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
        ChangeMapCenter(new LatLng(Double.parseDouble(bean.getLat()),Double.parseDouble(bean.getLon())),9.0f);
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


    @Event(value = R.id.iv_close)
    private void closeDialog(View btn){
        ll_detail_info.setVisibility(View.GONE);
    }


}
