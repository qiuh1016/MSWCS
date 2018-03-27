package com.zhd.mswcs.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.weidgt.DownLoadOfflinePackageDialog;
import com.zhd.mswcs.moduls.base.view.adapter.MyFragmentPagerAdapter;
import com.zhd.mswcs.moduls.friend.view.fragment.FriendFragment;
import com.zhd.mswcs.moduls.location.view.fragment.LocationFragment;
import com.zhd.mswcs.moduls.message.view.fragment.NewMessageFragment;
import com.zhd.mswcs.moduls.setting.view.fragment.SettingFragment;
import com.zhd.mswcs.moduls.sos.view.fragment.SOSFragment;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends FragmentActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new DownLoadOfflinePackageDialog(this);
        initView();
    }

    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sos:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_location:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_message:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_friend:
                        viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SOSFragment         sosFragment         = new SOSFragment();
        LocationFragment    locationFragment    = new LocationFragment();
        NewMessageFragment  messageFragment     = new NewMessageFragment();
        FriendFragment      friendFragment      = new FriendFragment();
        SettingFragment     settingFragment     = new SettingFragment();
        List<Fragment> allFragment = new ArrayList<>();
        allFragment.add(sosFragment);
        allFragment.add(locationFragment);
        allFragment.add(messageFragment);
        allFragment.add(friendFragment);
        allFragment.add(settingFragment);
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), allFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_sos);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_location);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_message);
                        break;
                    case 3:
                        radioGroup.check(R.id.rb_friend);
                        break;
                    case 4:
                        radioGroup.check(R.id.rb_setting);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
