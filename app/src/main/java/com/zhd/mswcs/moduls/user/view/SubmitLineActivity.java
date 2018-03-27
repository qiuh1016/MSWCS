package com.zhd.mswcs.moduls.user.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.zhd.mswcs.R;
import com.zhd.mswcs.activity.HomeActivity;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.common.weidgt.DownLoadOfflinePackageDialog;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.user.bean.LineBean;
import com.zhd.mswcs.moduls.user.bean.SelectLineBean;
import com.zhd.mswcs.moduls.user.presenter.LinePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_submit_line)
public class SubmitLineActivity extends BaseMvpActivity<MvpView,LinePresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.map_view)
    private MapView mapView;
    private BaiduMap baiduMap;
    private List<SelectLineBean> selectLineList  = new ArrayList<>();
    private BitmapDescriptor searchGreenFromResource = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_green_search);
    private List<OverlayOptions> lineOptionList = new ArrayList<>();
    private List<OverlayOptions> markerOptionsList = new ArrayList<>();
    private List<LatLng> lineList = new ArrayList<>();
    private MapStatusUpdate mMapStatusUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownLoadOfflinePackageDialog(this);
    }

    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("设置航线")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setRightIcon(R.drawable.btn_green_bg)
                .setRightText("发送")
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectLineList!=null&&selectLineList.size()>0){
                            LoadingDialog.getInstance().showDialog(SubmitLineActivity.this,"正在提交路线，请稍后...");
                            JSONObject params = new JSONObject();
                            try {
                                params.put("phone", PreferencesUtils.getString(SubmitLineActivity.this,"telephone"));
                                params.put("shipName", "");
                                params.put("peopleCount",0);
                                params.put("points",new Gson().toJson(selectLineList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getPresenter().submitShipLocus(params);
                            getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
                                @Override
                                public void bindData(Object data, int requestCode) {
                                    LoadingDialog.getInstance().closeDialog();
                                    switch (requestCode){
                                        case 1:
                                            LineBean lineBean = ((LineBean)data);
                                            if(lineBean==null){
                                                ToastUtils.showLongToast(SubmitLineActivity.this, R.string.code_system_error);
                                            }else{
                                                LogUtils.d("lineBean=",new Gson().toJson(lineBean));
                                                if(lineBean.isResult()){
                                                    ToastUtil.showToast(SubmitLineActivity.this,"预订航线发送成功");
                                                    baiduMap.clear();
                                                    if(lineOptionList!=null&&lineOptionList.size()>0){
                                                        lineOptionList.clear();
                                                    }
                                                    if(markerOptionsList!=null&&markerOptionsList.size()>0){
                                                        markerOptionsList.clear();
                                                    }

                                                    if(lineList!=null&&lineList.size()>0){
                                                        lineList.clear();
                                                    }

                                                    if(selectLineList!=null&&selectLineList.size()>0){
                                                        selectLineList.clear();
                                                    }
                                                    startActivity(new Intent(SubmitLineActivity.this, HomeActivity.class));
                                                    finish();
                                                }else{
                                                    ToastUtil.showToast(SubmitLineActivity.this,"预订航线发送失败");
                                                    baiduMap.clear();
                                                    if(lineOptionList!=null&&lineOptionList.size()>0){
                                                        lineOptionList.clear();
                                                    }
                                                    if(markerOptionsList!=null&&markerOptionsList.size()>0){
                                                        markerOptionsList.clear();
                                                    }

                                                    if(lineList!=null&&lineList.size()>0){
                                                        lineList.clear();
                                                    }
                                                    if(selectLineList!=null&&selectLineList.size()>0){
                                                        selectLineList.clear();
                                                    }
                                                    startActivity(new Intent(SubmitLineActivity.this, HomeActivity.class));
                                                    finish();
                                                }
                                            }
                                            break;
                                    }
                                }
                            });
                        }else{
                            ToastUtil.showToast(SubmitLineActivity.this,"请先在地图上选择预订航线");
                        }

                    }
                })
                .build()
                .builder();
    }


    @Override
    public LinePresenter createPresenter() {
        return new LinePresenter(this);
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
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SelectLineBean bean = new SelectLineBean();
                bean.setLatitude(latLng.latitude);
                bean.setLontitude(latLng.longitude);
                selectLineList.add(bean);
                lineList.add(latLng);
                if(selectLineList!=null&&selectLineList.size()<2){
                    addMark(selectLineList,true);
                }else{
                    addLine(selectLineList,lineList);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                SelectLineBean bean = new SelectLineBean();
                bean.setLatitude(mapPoi.getPosition().latitude);
                bean.setLontitude(mapPoi.getPosition().longitude);
                selectLineList.add(bean);
                lineList.add(mapPoi.getPosition());
                if(selectLineList!=null&&selectLineList.size()<2){
                    addMark(selectLineList,true);
                }else{
                    addLine(selectLineList,lineList);
                }
                return true;
            }
        });
    }



    public void addLine(List<SelectLineBean> list, List<LatLng> lineList) {
        OverlayOptions ooPolyline = new PolylineOptions().width(3).color(Color.RED).points(lineList);
        lineOptionList.add(ooPolyline);
        baiduMap.addOverlays(lineOptionList);
        addMark(list,false);
    }


    private void addMark(List<SelectLineBean> list,boolean isClearMap) {
        if(isClearMap){
            baiduMap.clear();
        }
        for(SelectLineBean bean : list) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            // 创建覆盖物
            MarkerOptions markerOptions = new MarkerOptions();
            // 指定覆盖物的位置
            markerOptions.position(new LatLng(bean.getLatitude(),bean.getLontitude()));
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
            markerOptionsList.add(markerOptions);
        }
        // 绑定覆盖物
        baiduMap.addOverlays(markerOptionsList);
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


}
