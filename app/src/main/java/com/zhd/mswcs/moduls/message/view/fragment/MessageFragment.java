package com.zhd.mswcs.moduls.message.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.NetworkUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.moduls.base.view.BaseRefreshFragment;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.message.adapter.MessageListAdapter;
import com.zhd.mswcs.moduls.message.bean.MessageItemLocalBean;
import com.zhd.mswcs.moduls.message.bean.MessageItemServerBean;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mswcs.moduls.message.presenter.MessagePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Dream on 16/9/22 23:23
 * QQ:510278658
 * E-mail:510278658@qq.com
 */

public class MessageFragment extends BaseRefreshFragment<MvpView,MessagePresenter> implements MvpView {
    private RecyclerView message_recyclerView;
    private LinearLayout ll_rootView;
    private List<MessageItemLocalBean> messageList = new ArrayList<>();
    private MessageListAdapter messageListAdapter;
    private String smsVersion = "";




    @Override
    public int getContentView() {
        return R.layout.fragment_message;
    }

    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(getContext(),ll_rootView)
                .setTitle("消息")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_wifi)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .build()
                .builder();

    }

    @Override
    public void initContentView(View contentView) {
        super.initContentView(contentView);
        setRefresh(true,false);
        message_recyclerView = (RecyclerView)contentView.findViewById(R.id.message_recyclerView);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
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
        if(!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"smsVersion"))){
            smsVersion = PreferencesUtils.getString(getActivity(),"smsVersion");
        }
        if(NetworkUtils.isConnected(getActivity())){
            JSONObject params = new JSONObject();
            try {
                params.put("smsVersion",smsVersion);
                params.put("phone",PreferencesUtils.getString(getActivity(),"telephone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getPresenter().getSMSItem(params);
            getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
                @Override
                public void bindData(Object data, int requestCode) {
                    switch (requestCode){
                        case 1:
                            MessageItemServerBean messageItemServerBean = ((MessageItemServerBean)data);
                            List<String> phoneList = new ArrayList<>();
                            if(messageItemServerBean==null){
                                return;
                            }else{
                                try {
                                    LogUtils.d("messageItemServerBean=",new Gson().toJson(messageItemServerBean));
                                    if(messageItemServerBean.getResult()!=null&&messageItemServerBean.getResult().getSms()!=null&&messageItemServerBean.getResult().getSms().size()>0){
                                        PreferencesUtils.putString(getActivity(),"smsVersion",messageItemServerBean.getResult().getSmsVersion());
                                        if(MyApplication.getInstance().getDb().selector(MessageItemLocalBean.class).count()>0){
                                            MyApplication.getInstance().getDb().delete(MessageItemLocalBean.class);
                                        }
                                        for(MessageItemServerBean.ResultBean.SmsBean smsBean :messageItemServerBean.getResult().getSms()){
                                            MessageItemLocalBean messageItemLocalBean = new MessageItemLocalBean();
                                            messageItemLocalBean.setSmsType(smsBean.getSmsType());
                                            messageItemLocalBean.setSender(smsBean.getSender());
                                            messageItemLocalBean.setReceiver(smsBean.getReceiver());
                                            messageItemLocalBean.setMessage(smsBean.getMessage());
                                            messageItemLocalBean.setLon(smsBean.getLon());
                                            messageItemLocalBean.setLat(smsBean.getLat());
                                            messageItemLocalBean.setCo(smsBean.getCo());
                                            messageItemLocalBean.setGpsTime(smsBean.getGpsTime());
                                            messageItemLocalBean.setState(smsBean.getState());
                                            messageItemLocalBean.setDateTime(smsBean.getDateTime());
                                            messageItemLocalBean.setToken(smsBean.getToken());
                                            MyApplication.getInstance().getDb().saveBindingId(messageItemLocalBean);
                                        }

                                        List<MessageItemLocalBean> list = MyApplication.getInstance().getDb().selector(MessageItemLocalBean.class).findAll();
                                        if(list!=null&&list.size()>0){
                                            for(MessageItemLocalBean bean:list){
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
                                                MessageItemLocalBean itemLocalBean = new MessageItemLocalBean();
                                                MessageLocalBean bean =  MyApplication.getInstance().getDb().selector(MessageLocalBean.class).where("sender","=",phone).where(WhereBuilder.b().or("receiver","=",phone)).orderBy("dateTime",true).findFirst();
                                                FriendBean friend = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone","=",phone).findFirst();
                                                if(friend!=null){
                                                    itemLocalBean.setUserName(friend.getUserName());
                                                }else{
                                                    itemLocalBean.setUserName(phone);
                                                }
                                                itemLocalBean.setDateTime(bean.getDateTime());
                                                itemLocalBean.setMessage(bean.getMessage());
                                                itemLocalBean.setSender(phone);
                                                messageList.add(itemLocalBean);
                                            }
                                            Log.e("TAG",new Gson().toJson(messageList));
                                            messageListAdapter.notifyDataSetChanged();
                                        }

                                    }else{
                                           List<FriendBean> friendBeanList = MyApplication.getInstance().getDb().selector(FriendBean.class).findAll();
                                           for(FriendBean bean:friendBeanList){
                                               MessageItemLocalBean itemLocalBean = new MessageItemLocalBean();
                                               MessageLocalBean bean2 =  MyApplication.getInstance().getDb().selector(MessageLocalBean.class).where("sender","=",bean.getTelephone()).where(WhereBuilder.b().or("receiver","=",bean.getTelephone())).orderBy("dateTime",true).findFirst();
                                               FriendBean friend = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone","=",bean.getTelephone()).findFirst();
                                               if(friend!=null){
                                                   itemLocalBean.setUserName(friend.getUserName());
                                               }else{
                                                   itemLocalBean.setUserName(bean.getTelephone());
                                               }
                                               itemLocalBean.setDateTime(bean2.getDateTime());
                                               itemLocalBean.setMessage(bean2.getMessage());
                                               itemLocalBean.setSender(bean.getTelephone());
                                               messageList.add(itemLocalBean);
                                           }
                                        Log.e("TAG",new Gson().toJson(messageList));
                                        messageListAdapter.notifyDataSetChanged();
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                    }
                }
            });
        }else{
            try {
                List<FriendBean> friendBeanList = MyApplication.getInstance().getDb().selector(FriendBean.class).findAll();
                for(FriendBean bean:friendBeanList){
                    MessageItemLocalBean itemLocalBean = new MessageItemLocalBean();
                    MessageLocalBean bean2 =  MyApplication.getInstance().getDb().selector(MessageLocalBean.class).where("sender","=",bean.getTelephone()).where(WhereBuilder.b().or("receiver","=",bean.getTelephone())).orderBy("dateTime",true).findFirst();
                    FriendBean friend = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone","=",bean.getTelephone()).findFirst();
                    if(friend!=null){
                        itemLocalBean.setUserName(friend.getUserName());
                    }else{
                        itemLocalBean.setUserName(bean.getTelephone());
                    }
                    itemLocalBean.setDateTime(bean2.getDateTime());
                    itemLocalBean.setMessage(bean2.getMessage());
                    itemLocalBean.setSender(bean.getTelephone());
                    messageList.add(itemLocalBean);
                }
                messageListAdapter.notifyDataSetChanged();
                Log.e("TAG",new Gson().toJson(messageList));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void refreshData(boolean isDownRefresh) {
        initMessageData();
    }
}
