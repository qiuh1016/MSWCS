package com.zhd.mswcs.moduls.location.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class SelectFriendListAdapter extends BaseRecylerAdapter<FriendBean>{
    private Context mContext;
    private List<FriendBean> dataList;
    public SelectFriendListAdapter(Activity context, List<FriendBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new SelectFriendHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, int position) {
        super.onBindViewHolder(baseHolder, position);
        SelectFriendHolder selectFriendHolder = (SelectFriendHolder) baseHolder;
        final FriendBean data = dataList.get(position);
        selectFriendHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
        selectFriendHolder.tv_user_name.setText(data.getUserName());
    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_select_friend,null,false);
    }

    private class SelectFriendHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_user_name;
        public SelectFriendHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }
}
