package com.zhd.mswcs.moduls.message.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseRefreshFragment;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.message.adapter.MessageListAdapter;
import com.zhd.mswcs.moduls.message.bean.MessageItemLocalBean;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mswcs.moduls.message.presenter.MessagePresenter;
import com.zhd.mswcs.moduls.sos.view.activity.EditUserTelephoneActivity;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Dream on 16/9/22 23:23
 * QQ:510278658
 * E-mail:510278658@qq.com
 */

public class NewMessageFragment extends BaseRefreshFragment<MvpView,MessagePresenter> implements MvpView {
    private RecyclerView message_recyclerView;
    private LinearLayout ll_rootView;
    private List<MessageItemLocalBean> messageList = new ArrayList<>();
    private MessageListAdapter messageListAdapter;
    private TextView tv_title;
    private ImageView iv_left;
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private LinearLayout ll_no_data;
    private ImageView iv_right;

    @Override
    public int getContentView() {
        return R.layout.fragment_message;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initContentView(View contentView) {
        super.initContentView(contentView);
        setRefresh(true,false);
        ll_no_data = (LinearLayout) contentView.findViewById(R.id.ll_no_data);
        message_recyclerView = (RecyclerView)contentView.findViewById(R.id.message_recyclerView);
        tv_title =  (TextView)contentView.findViewById(R.id.tv_title);
        iv_left =  (ImageView) contentView.findViewById(R.id.iv_left);
        iv_right =  (ImageView) contentView.findViewById(R.id.iv_right);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
        tv_title.setText("消息");
        iv_left.setBackgroundResource(MyApplication.getInstance().getImageRes());
        iv_right.setBackgroundResource(R.mipmap.img_refresh);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
                    ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
                    Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                    startActivity(intent);
                    return;
                }
                initMessageData();
            }
        });
    }


    @Override
    public MessagePresenter createPresenter() {
        return new MessagePresenter(getContext());
    }

    @Override
    public void initData() {
        messageListAdapter = new MessageListAdapter(getActivity(),messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        message_recyclerView.setLayoutManager(linearLayoutManager);
        message_recyclerView.addItemDecoration(new SimpleDividerDecoration(getActivity()));
        message_recyclerView.setAdapter(messageListAdapter);
    }

    private void initMessageData() {
        messageListAdapter.clearData();
        messageList.clear();
        try {
            List<MessageLocalBean> list = MyApplication.getInstance().getDb().selector(MessageLocalBean.class).findAll();
            List<String> phoneList = new ArrayList<>();
            if(list!=null&&list.size()>0){
                for(MessageLocalBean bean:list){
                    if(!phoneList.contains(bean.getReceiver())){
                        phoneList.add(bean.getReceiver());
                    }else{
                        if(!phoneList.contains(bean.getSender())){
                            phoneList.add(bean.getSender());
                        }else{
                            continue;
                        }
                    }
                }

                String currentUser = PreferencesUtils.getString(getActivity(),"telephone");
                if(phoneList.contains(currentUser)){
                    phoneList.remove(currentUser);
                }
                for(String phone:phoneList){
                    if (StringUtils.isEmpty(phone)) {
                        continue;
                    }
                    MessageItemLocalBean itemLocalBean = new MessageItemLocalBean();
                    Log.e("tag","select * from messageRecord where ( sender = '"+phone+ "' and receiver = '"+currentUser+"') or (receiver = '"+ phone + "' and sender = '"+currentUser+"' )order by dateTime desc");
                    DbModel bean =  MyApplication.getInstance().getDb().findDbModelFirst(new SqlInfo("select * from messageRecord where ( sender = '"+phone+ "' and receiver = '"+currentUser+"') or (receiver = '"+ phone + "' and sender = '"+currentUser+"')order by dateTime desc"));
                    if(bean==null){
                        continue;
                    }
                    DbModel friend = MyApplication.getInstance().getDb().findDbModelFirst(new SqlInfo("select * from friend where telephone = " + phone +" and currentUser = "+ currentUser +" order by updateTime desc "));
                    //FriendBean friend = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone","=",phone).where(WhereBuilder.b().and("currentUser","=",currentUser)).findFirst();
                    if(friend!=null){
                        itemLocalBean.setUserName(friend.getString("userName"));
                    }else{
                        if(!StringUtils.equals(phone,currentUser)){
                            itemLocalBean.setUserName(phone);
                            FriendBean newFriend = new FriendBean();
                            newFriend.setUserName(phone);
                            newFriend.setNicknNme(phone);
                            newFriend.setCreateTime(TimeUtils.getCurTimeString());
                            newFriend.setUpdateTime(TimeUtils.getCurTimeString());
                            newFriend.setTelephone(phone);
                            try {
                                MyApplication.getInstance().getDb().saveBindingId(newFriend);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("tag","保存好友失败");
                            }
                        }

                    }
                    if(StringUtils.isEmpty(bean.getString("dateTime"))){
                        itemLocalBean.setDateTime("");
                    }else{
                        itemLocalBean.setDateTime(bean.getString("dateTime"));
                    }

                    if(StringUtils.isEmpty(bean.getString("message"))){
                        itemLocalBean.setMessage("");
                    }else{
                        itemLocalBean.setMessage(bean.getString("message"));
                    }
                    itemLocalBean.setSender(phone);
                    messageList.add(itemLocalBean);
                }
            }
            Log.e("TAG",new Gson().toJson(messageList));
            messageListAdapter.notifyDataSetChanged();

            if(messageList!=null&&messageList.size()>0){
                getRefreshView().setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
            }else{
                getRefreshView().setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void refreshData(boolean isDownRefresh) {
        registerReciver();
        if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
            ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
            Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
            startActivity(intent);
            return;
        }
        initMessageData();
    }



    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NEW_MESSAGE_BRO);
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
            }else{
                initMessageData();
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
}
