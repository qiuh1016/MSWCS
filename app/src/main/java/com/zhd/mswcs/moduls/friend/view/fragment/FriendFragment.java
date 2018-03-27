package com.zhd.mswcs.moduls.friend.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.AddFriendTypeDialog;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseRefreshFragment;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.RecyclerViewItemClickListener;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.friend.adapter.FriendListAdapter;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.friend.view.activity.AddFriendActivity;
import com.zhd.mswcs.moduls.friend.view.activity.EditFriendInfoActivity;
import com.zhd.mswcs.moduls.friend.view.activity.ImportContactsListActivity;
import com.zhd.mswcs.moduls.location.presenter.AllUserLocationPresenter;
import com.zhd.mswcs.moduls.sos.view.activity.EditUserTelephoneActivity;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Dream on 16/9/22 23:23
 * QQ:510278658
 * E-mail:510278658@qq.com
 */

public class FriendFragment extends BaseRefreshFragment<MvpView,AllUserLocationPresenter> implements MvpView{
    private RecyclerView friend_recyclerView;
    private LinearLayout ll_rootView;
    private List<FriendBean> friendBeanList = new ArrayList<>();
    private FriendListAdapter friendListAdapter;
    private static final int EDIT_FRIEND_INFO_REQUEST_CODE = 1;
    private static final int ADD_FRIEND_REQUEST_CODE = 2;
    private static final int ADD_IMPORT_CONTACTS_REQUEST_CODE = 3;
    private TextView tv_title;
    private ImageView iv_left;
    private ImageView iv_right;
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private LinearLayout ll_no_data;
    private ImageView iv_no_data;
    private String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
    //动态获取权限监听
    private static PermissionListener mListener;

    @Override
    public int getContentView() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initNavigation() {
//        new DefaultNavigation
//                .Builder(getContext(),ll_rootView)
//                .setTitle("好友")
//                .setTitleColor(getResources().getColor(R.color.common_white),true)
//                .setHeadBgColor(getResources().getColor(R.color.common_black))
//                .setLeftIcon(R.mipmap.img_wifi)
//                .setRightIcon(R.mipmap.img_add_friend)
//                .setRightIconOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
//                        startActivityForResult(intent,ADD_FRIEND_REQUEST_CODE);
//                    }
//                })
//                .build()
//                .builder();

    }

    @Override
    public void initContentView(View contentView) {
        super.initContentView(contentView);
        setRefresh(true,false);
        ll_no_data = (LinearLayout) contentView.findViewById(R.id.ll_no_data);
        iv_no_data = (ImageView) contentView.findViewById(R.id.iv_no_data);
        friend_recyclerView = (RecyclerView)contentView.findViewById(R.id.friend_recyclerView);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
        tv_title =  (TextView)contentView.findViewById(R.id.tv_title);
        iv_left =  (ImageView) contentView.findViewById(R.id.iv_left);
        iv_right=  (ImageView) contentView.findViewById(R.id.iv_right);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
        tv_title.setText("好友");
        iv_left.setBackgroundResource(MyApplication.getInstance().getImageRes());
        iv_right.setBackgroundResource(R.mipmap.img_add_friend_two);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
                    ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
                    Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                    startActivity(intent);
                    return;
                }
                AddFriendTypeDialog addFriendTypeDialog = new AddFriendTypeDialog(getActivity());
                addFriendTypeDialog.showDialog();
                addFriendTypeDialog.setHandAndListener(new AddFriendTypeDialog.HandAndListener() {
                    @Override
                    public void handAndListener() {
                        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                        startActivityForResult(intent,ADD_FRIEND_REQUEST_CODE);
                    }
                });
                addFriendTypeDialog.setImportContactsListener(new AddFriendTypeDialog.ImportContactsListener() {
                    @Override
                    public void importContactsListener() {
                        requestRuntimePermission(permissions, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(getActivity(), ImportContactsListActivity.class);
                                startActivityForResult(intent,ADD_IMPORT_CONTACTS_REQUEST_CODE);
                            }

                            @Override
                            public void onDenied(List<String> deniedPermission) {
                                ToastUtil.showToast(getActivity(),"你拒绝了读取通讯录权限请求，无法导入通讯录");
                            }
                        });

                    }
                });

            }
        });
    }



    @Override
    public AllUserLocationPresenter createPresenter() {
        return new AllUserLocationPresenter(getContext());
    }

    @Override
    public void initData() {
        friendListAdapter = new FriendListAdapter(getActivity(),friendBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        friend_recyclerView.setLayoutManager(linearLayoutManager);
        friend_recyclerView.addItemDecoration(new SimpleDividerDecoration(getContext()));
        friend_recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EditFriendInfoActivity.class);
                intent.putExtra("friendName",friendBeanList.get(position).getUserName());
                intent.putExtra("telephone",friendBeanList.get(position).getTelephone());
                intent.putExtra("position",position);
                startActivityForResult(intent,EDIT_FRIEND_INFO_REQUEST_CODE);
            }
        });
    }

    private void initFriendListData() {
        friendListAdapter.clearData();
        try {
            List<FriendBean> friendList = MyApplication.getInstance().getDb().selector(FriendBean.class).where("currentUser","=",PreferencesUtils.getString(getActivity(),"telephone")).findAll();
            if(friendList!=null&&friendList.size()>0){
                friendBeanList.addAll(friendList);
                String currentUser = PreferencesUtils.getString(getActivity(),"telephone");
                for(int i=0;i<friendBeanList.size();i++){
                    if(StringUtils.equals(friendBeanList.get(i).getTelephone(),currentUser)){
                        friendBeanList.remove(i);
                    }
                }
            }

            if(friendBeanList!=null&&friendBeanList.size()>0){
                getRefreshView().setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
            }else{
                getRefreshView().setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                iv_no_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
                            ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
                            Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                            startActivity(intent);
                            return;
                        }
                        AddFriendTypeDialog addFriendTypeDialog = new AddFriendTypeDialog(getActivity());
                        addFriendTypeDialog.showDialog();
                        addFriendTypeDialog.setHandAndListener(new AddFriendTypeDialog.HandAndListener() {
                            @Override
                            public void handAndListener() {
                                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                                startActivityForResult(intent,ADD_FRIEND_REQUEST_CODE);
                            }
                        });
                        addFriendTypeDialog.setImportContactsListener(new AddFriendTypeDialog.ImportContactsListener() {
                            @Override
                            public void importContactsListener() {
                                requestRuntimePermission(permissions, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent intent = new Intent(getActivity(), ImportContactsListActivity.class);
                                        startActivityForResult(intent,ADD_IMPORT_CONTACTS_REQUEST_CODE);
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        ToastUtil.showToast(getActivity(),"你拒绝了读取通讯录权限请求，无法导入通讯录");
                                    }
                                });

                            }
                        });
                    }
                });
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        friendListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case EDIT_FRIEND_INFO_REQUEST_CODE:
                    if(data!=null) {
                        int position = data.getIntExtra("position", -1);
                        FriendBean friendBean = friendBeanList.get(position);
                        friendBean.setUserName(data.getStringExtra("friendName"));
                        friendBean.setNicknNme(data.getStringExtra("friendName"));
                        friendBean.setTelephone(data.getStringExtra("telephone"));
                        friendBean.setUpdateTime(data.getStringExtra("updateTime"));
                        try {
                            MyApplication.getInstance().getDb().update(friendBean);
                            ToastUtil.showToast(MyApplication.getInstance().getApplicationContext(),"好友更新成功");
                            getActivity().sendBroadcast(new Intent(Constant.NEW_MESSAGE_BRO));
                            initFriendListData();
                        } catch (DbException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(MyApplication.getInstance().getApplicationContext(),"好友更新失败");
                        }
                    }
                    break;
                case ADD_FRIEND_REQUEST_CODE:
                    initFriendListData();
                    break;

                case ADD_IMPORT_CONTACTS_REQUEST_CODE:
                    initFriendListData();
                    break;
                default:
                    break;
            }

    }


    @Override
    public void refreshData(boolean isDownRefresh) {
        registerReciver();
        initFriendListData();
    }


    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.HAS_NETWORK_BRO);
        intentFilter.addAction(Constant.NO_NETWORK_BRO);
        getActivity().registerReceiver(dynamicReceiver,intentFilter);
    }


    //通过继承 BroadcastReceiver建立动态广播接收器
    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(StringUtils.equals(intent.getAction(),Constant.HAS_NETWORK_BRO)){
                iv_left.setBackgroundResource(R.mipmap.img_has_wifi);
            }else if(StringUtils.equals(intent.getAction(),Constant.NO_NETWORK_BRO)){
                iv_left.setBackgroundResource(R.mipmap.img_no_wifi);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dynamicReceiver!=null){
            getActivity().unregisterReceiver(dynamicReceiver);
        }
    }




    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<String>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }



    public interface PermissionListener {
        /**
         * 成功获取权限
         */
        void onGranted();

        /**
         * 为获取权限
         * @param deniedPermission
         */
        void onDenied(List<String> deniedPermission);
    }


}
