package com.zhd.mswcs.activity;

import android.os.Bundle;
import android.widget.TabHost;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.weidgt.DownLoadOfflinePackageDialog;
import com.zhd.mswcs.moduls.friend.view.fragment.FriendFragment;
import com.zhd.mswcs.moduls.location.view.fragment.LocationFragment;
import com.zhd.mswcs.moduls.message.view.fragment.NewMessageFragment;
import com.zhd.mswcs.moduls.setting.view.fragment.SettingFragment;
import com.zhd.mswcs.moduls.tab.BaseTabActivity;
import com.zhd.mswcs.moduls.tab.iterator.ITabItem;
import com.zhd.mswcs.moduls.tab.iterator.TabIterator;
import com.zhd.mswcs.moduls.tab.iterator.tabitem.DefaultTabItem;
import com.zhd.mswcs.moduls.tab.iterator.tabiterator.ListTabIterable;

public class MainActivity extends BaseTabActivity implements TabHost.OnTabChangeListener{

    private ListTabIterable listTabIterable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownLoadOfflinePackageDialog(this);
    }

    @Override
    public void initTabHostRes() {
        listTabIterable = new ListTabIterable();

        listTabIterable.addTab(new DefaultTabItem.DefaultTabItemBuilder(this, R.layout.tab_indicator)
                .setTitle(R.string.main_location_text)
                .setImageNormal(R.mipmap.tab_icon_discover_normal)
                .setImagePress(R.mipmap.tab_icon_discover_focus)
                .setTitleNormalColor(R.color.main_tab_text_normal)
                .setTitlePressColor(R.color.main_tab_text_select)
                .setFragmentClazz(LocationFragment.class).build());

        listTabIterable.addTab(new DefaultTabItem.DefaultTabItemBuilder(this,R.layout.tab_indicator)
                .setTitle(R.string.main_message_text)
                .setImageNormal(R.mipmap.tab_icon_chat_normal)
                .setImagePress(R.mipmap.tab_icon_chat_focus)
                .setTitleNormalColor(R.color.main_tab_text_normal)
                .setTitlePressColor(R.color.main_tab_text_select)
                .setFragmentClazz(NewMessageFragment.class).build());

        listTabIterable.addTab(new DefaultTabItem.DefaultTabItemBuilder(this,R.layout.tab_indicator)
                .setTitle(R.string.main_friend_text)
                .setImageNormal(R.mipmap.tab_icon_contact_normal)
                .setImagePress(R.mipmap.tab_icon_contact_focus)
                .setTitleNormalColor(R.color.main_tab_text_normal)
                .setTitlePressColor(R.color.main_tab_text_select)
                .setFragmentClazz(FriendFragment.class).build());

        listTabIterable.addTab(new DefaultTabItem.DefaultTabItemBuilder(this,R.layout.tab_indicator)
                .setTitle(R.string.main_setting_text)
                .setImageNormal(R.mipmap.tab_icon_me_normal)
                .setImagePress(R.mipmap.tab_icon_me_focus)
                .setTitleNormalColor(R.color.main_tab_text_normal)
                .setTitlePressColor(R.color.main_tab_text_select)
                .setFragmentClazz(SettingFragment.class).build());
    }

    @Override
    public TabIterator<ITabItem> getTabIterator() {
        return listTabIterable.iterator();
    }


    @Override
    public void onBackPressed() {

    }




}
