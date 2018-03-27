package com.zhd.mswcs.moduls.setting.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.SettingIpAddressDialog;
import com.zhd.mswcs.common.weidgt.SettingMobileRadioInfoDialog;
import com.zhd.mswcs.common.weidgt.SettingMobileRadioPasswordDialog;
import com.zhd.mswcs.common.weidgt.picture.activity.ClippingPageActivity;
import com.zhd.mswcs.common.weidgt.picture.activity.SelectImagesFromLocalActivity;
import com.zhd.mswcs.common.weidgt.picture.constants.ConstantSet;
import com.zhd.mswcs.common.weidgt.picture.utils.SDCardUtils;
import com.zhd.mswcs.common.weidgt.picture.view.CircleImageView;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseMvpFragment;
import com.zhd.mswcs.moduls.setting.presenter.SettingPresenter;
import com.zhd.mswcs.moduls.setting.view.activity.EditEscalationCycleActivity;
import com.zhd.mswcs.moduls.setting.view.activity.EditSOSSettingActivity;
import com.zhd.mswcs.moduls.setting.view.activity.EditUserNameActivity;
import com.zhd.mswcs.moduls.sos.view.activity.EditUserTelephoneActivity;
import com.zhd.mswcs.moduls.user.view.LoginActivity;
import com.zhd.mvp.framework.base.view.MvpView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Dream on 16/9/22 23:23
 * QQ:510278658
 * E-mail:510278658@qq.com
 */

public class SettingFragment extends BaseMvpFragment<MvpView,SettingPresenter> implements MvpView{
    private RelativeLayout rl_user_head;
    private RelativeLayout rl_user_name;
    private RelativeLayout rl_telephone;
    private RelativeLayout rl_reporting_cycle;
    private RelativeLayout rl_sos_setting;
    private RelativeLayout rl_exit_login;
    private RelativeLayout rl_ip_setting;
    private RelativeLayout rl_mobile_radio;
    private TextView tv_ip_setting;
    private TextView tv_mobile_radio;
    private TextView tv_user_name;
    private TextView tv_escalation_cycle;
    private TextView tv_telephone;
    private TextView tv_sos_setting;
    private CircleImageView iv_head;
    private static final int EDIT_USER_NAME_REQUEST_CODE = 1;
    private static final int EDIT_ESCALATION_CYCLE_REQUEST_CODE = 2;
    private static final int EDIT_USER_TELEPHONE_REQUEST_CODE = 3;
    private static final int EDIT_SOS_SETTING_REQUEST_CODE = 4;
    private TextView tv_title;
    private ImageView iv_left;
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private String[] permissions_take_picture={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] permissions_slelect_photo={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //动态获取权限监听
    private  PermissionListener mListener;
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
    private File file ;
    private Uri imageUri ;
    private int flag=1;//1代表选择用户头像，2代表选择机动电台头像
    private SettingMobileRadioInfoDialog settingMobileRadioInfoDialog;

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter(getContext());
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initNavigation() {}



    @Override
    public void initContentView(View contentView) {
        iv_head = (CircleImageView) contentView.findViewById(R.id.iv_head);
        rl_user_head = (RelativeLayout) contentView.findViewById(R.id.rl_user_head);
        rl_user_name= (RelativeLayout) contentView.findViewById(R.id.rl_user_name);
        rl_telephone= (RelativeLayout) contentView.findViewById(R.id.rl_telephone);
        rl_reporting_cycle= (RelativeLayout) contentView.findViewById(R.id.rl_reporting_cycle);
        rl_sos_setting= (RelativeLayout) contentView.findViewById(R.id.rl_sos_setting);
        rl_exit_login= (RelativeLayout) contentView.findViewById(R.id.rl_exit_login);
        rl_ip_setting= (RelativeLayout) contentView.findViewById(R.id.rl_ip_setting);
        rl_mobile_radio= (RelativeLayout) contentView.findViewById(R.id.rl_mobile_radio);
        tv_ip_setting = (TextView) contentView.findViewById(R.id.tv_ip_setting);
        tv_mobile_radio= (TextView) contentView.findViewById(R.id.tv_mobile_radio);
        tv_user_name = (TextView) contentView.findViewById(R.id.tv_user_name);
        tv_escalation_cycle = (TextView) contentView.findViewById(R.id.tv_escalation_cycle);
        tv_telephone = (TextView) contentView.findViewById(R.id.tv_telephone);
        tv_sos_setting = (TextView) contentView.findViewById(R.id.tv_sos_setting);
        tv_title =  (TextView)contentView.findViewById(R.id.tv_title);
        iv_left =  (ImageView) contentView.findViewById(R.id.iv_left);
        tv_title.setText("设置");
        iv_left.setBackgroundResource(MyApplication.getInstance().getImageRes());

        file = new File(IMAGE_FILE_LOCATION);
        if(!file.exists()){
            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }
        file=new File(IMAGE_FILE_LOCATION+ConstantSet.USERTEMPPIC);
        imageUri = getUriForFile(getActivity(),file);

    }


    @Override
    public void initData() {
        super.initData();
        registerReciver();
        if(!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"userHead"))){
            String userHeadStr = PreferencesUtils.getString(getActivity(),"userHead");
            byte[] bytes = Base64.decode(userHeadStr,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            iv_head.setImageBitmap(bitmap);
        }
        if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"ipAddress"))){
            tv_ip_setting.setText("192.168.1.101");
        }else{
            tv_ip_setting.setText(PreferencesUtils.getString(getActivity(),"ipAddress"));
        }

        if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"mobileRadioHead"))||StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"mobileRadioName"))){
            tv_mobile_radio.setText("未设置");
        }else{
            tv_mobile_radio.setText("已设置");
        }

        if (StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))) {
            tv_telephone.setText("未设置");
        }else{
            tv_telephone.setText(PreferencesUtils.getString(getActivity(),"telephone"));
        }

        if (StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "userName"))) {
            tv_user_name.setText("未设置");
        }else{
            tv_user_name.setText(PreferencesUtils.getString(getActivity(), "userName"));
        }

        if (!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "sos_send_content"))&&!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"sos_send_telephone"))){
            tv_sos_setting.setText("已设置");
        }else{
            tv_sos_setting.setText("未设置");
        }



        rl_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flag = 1;
//                showSelectPickerDialog();
            }
        });

        rl_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserNameActivity.class);
                intent.putExtra("userName",tv_user_name.getText().toString().trim());
                startActivityForResult(intent,EDIT_USER_NAME_REQUEST_CODE);
            }
        });

        rl_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                intent.putExtra("telephone",tv_telephone.getText().toString().trim());
                startActivityForResult(intent,EDIT_USER_TELEPHONE_REQUEST_CODE);
            }
        });

        rl_reporting_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditEscalationCycleActivity.class);
                startActivityForResult(intent,EDIT_ESCALATION_CYCLE_REQUEST_CODE);
            }
        });

        rl_sos_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSOSSettingActivity.class);
                startActivityForResult(intent,EDIT_SOS_SETTING_REQUEST_CODE);
            }
        });

        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


        rl_ip_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingIpAddressDialog settingIpAddressDialog = new SettingIpAddressDialog(getActivity(),PreferencesUtils.getString(getActivity(),"ipAddress"));
                settingIpAddressDialog.showDialog();
                settingIpAddressDialog.setConfirmClickListener(new SettingIpAddressDialog.ConfirmClickListener() {
                    @Override
                    public void click(String ipAddress) {
                        PreferencesUtils.putString(getActivity(),"ipAddress",ipAddress);
                        tv_ip_setting.setText(ipAddress);
                    }
                });
            }
        });

        rl_mobile_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingMobileRadioPasswordDialog settingMobileRadioPasswordDialog = new SettingMobileRadioPasswordDialog(getActivity());
                settingMobileRadioPasswordDialog.showDialog();
                settingMobileRadioPasswordDialog.setConfirmClickListener(new SettingMobileRadioPasswordDialog.ConfirmClickListener() {
                    @Override
                    public void click(String pwd) {
                        PreferencesUtils.putString(getActivity(),"commanderPwd",pwd);
                        settingMobileRadioInfoDialog = new SettingMobileRadioInfoDialog(getActivity());
                        settingMobileRadioInfoDialog.showDialog();
                        settingMobileRadioInfoDialog.setSelectHeadClickListener(new SettingMobileRadioInfoDialog.SelectHeadClickListener() {
                            @Override
                            public void changeHead() {
                                flag = 2;
                                showSelectPickerDialog();
                            }
                        });
                        settingMobileRadioInfoDialog.setConfirmClickListener(new SettingMobileRadioInfoDialog.ConfirmClickListener() {
                            @Override
                            public void click(String name, String headStr) {
                                LogUtils.e("info==",name+","+headStr);
                                PreferencesUtils.putString(getActivity(),"mobileRadioName",name);
                            }
                        });

                    }
                });
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDIT_USER_NAME_REQUEST_CODE:
                tv_user_name.setText(PreferencesUtils.getString(getActivity(),"userName"));
                break;
            case  EDIT_ESCALATION_CYCLE_REQUEST_CODE:
                if(data!=null){
                    tv_escalation_cycle.setText(data.getStringExtra("cycleName"));
                }
                break;
            case EDIT_USER_TELEPHONE_REQUEST_CODE:
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"telephone"))){
                    tv_telephone.setText("未设置");
                }else{
                    tv_telephone.setText(PreferencesUtils.getString(getActivity(),"telephone"));
                }
                break;

            case EDIT_SOS_SETTING_REQUEST_CODE:
                if (!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "sos_send_content"))&&!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"sos_send_telephone"))){
                    tv_sos_setting.setText("已设置");
                }else{
                    tv_sos_setting.setText("未设置");
                }
                break;


            case ConstantSet.TAKEPICTURE:
                  Intent tcutIntent = new Intent(getActivity(), ClippingPageActivity.class);
                  tcutIntent.putExtra("type", "takePicture");
                  startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                if(data!=null){
                    Intent scutIntent = new Intent(getActivity(), ClippingPageActivity.class);
                    scutIntent.putExtra("type", "selectPicture");
                    scutIntent.putExtra("path",data.getStringExtra("path"));
                    startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                }
                break;

            case ConstantSet.CROPPICTURE:
                if(data!=null){
                    byte[] bis = data.getByteArrayExtra("result");
                    String base64Str = Base64.encodeToString(bis,0,bis.length,Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                    if(flag==1){
                        PreferencesUtils.putString(getActivity(),"userHead",base64Str);
                        iv_head.setImageBitmap(bitmap);
                    }else if(flag==2){
                        PreferencesUtils.putString(getActivity(),"mobileRadioHead",base64Str);
                        settingMobileRadioInfoDialog.setImageData(bitmap);
                    }

                }
                break;


            default:
                break;
        }
    }


    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.HAS_NETWORK_BRO);
        intentFilter.addAction(Constant.NO_NETWORK_BRO);
        intentFilter.addAction(Constant.BIND_TELEPHONE_BRO);
        intentFilter.addAction(Constant.SOS_SETTING_BRO);
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
            }else if(StringUtils.equals(intent.getAction(),Constant.BIND_TELEPHONE_BRO)){
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"telephone"))){
                    tv_telephone.setText("未设置");
                }else{
                    tv_telephone.setText(PreferencesUtils.getString(getActivity(),"telephone"));
                }
            }else if(StringUtils.equals(intent.getAction(),Constant.SOS_SETTING_BRO)){
                if (!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "sos_send_content"))&&!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"sos_send_telephone"))){
                    tv_sos_setting.setText("已设置");
                }else{
                    tv_sos_setting.setText("未设置");
                }
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




    /***
     * 头像更改监听
     */
    private class MyDialoagListener implements View.OnClickListener {
        private AlertDialog alertDialog;
        public MyDialoagListener(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Photograph:
                    requestRuntimePermission(permissions_take_picture, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            alertDialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, ConstantSet.TAKEPICTURE);//or TAKE_SMALL_PICTURE
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            ToastUtil.showToast(getActivity(),"你拒绝了相机权限请求，无法进行拍照功能");
                        }
                    });
                    break;

                case R.id.selectImage_from_local:
                    requestRuntimePermission(permissions_slelect_photo, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            Intent sintent = new Intent(getActivity(), SelectImagesFromLocalActivity.class);
                            startActivityForResult(sintent, ConstantSet.SELECTPICTURE);
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            ToastUtil.showToast(getActivity(),"你拒绝了相机权限请求，无法进行拍照功能");
                        }
                    });

                    break;

                case R.id.dimiss_dialoag:
                    alertDialog.dismiss();
                    break;

            }

        }
    }



    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.zhd.mswcs.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    //    //andrpoid 6.0 需要写运行时权限
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
                    List<String> deniedPermissions = new ArrayList<>();
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


    private void showSelectPickerDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View uv = getActivity().getLayoutInflater().from(getActivity()).inflate(R.layout.upload_user_pic, null);
        TextView photograph = (TextView) uv.findViewById(R.id.Photograph);
        TextView selectPic = (TextView) uv.findViewById(R.id.selectImage_from_local);
        TextView dimissDialoag = (TextView) uv.findViewById(R.id.dimiss_dialoag);
        builder.setView(uv);
        final AlertDialog alertDialog = builder.show();
        MyDialoagListener myDialoagListener = new MyDialoagListener(alertDialog);
        photograph.setOnClickListener(myDialoagListener);
        selectPic.setOnClickListener(myDialoagListener);
        dimissDialoag.setOnClickListener(myDialoagListener);

    }

}
