package com.zhd.mswcs.moduls.friend.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.RecyclerViewItemClickListener;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class FriendListAdapter extends BaseRecylerAdapter<FriendBean>{
    private Context mContext;
    private List<FriendBean> dataList;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public FriendListAdapter(Activity context, List<FriendBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new FriendHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, final int position) {
        super.onBindViewHolder(baseHolder, position);
        FriendHolder friendHolder = (FriendHolder) baseHolder;
        final FriendBean data = dataList.get(position);
        friendHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
        friendHolder.tv_user_name.setText(data.getUserName());
        friendHolder.rl_rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(recyclerViewItemClickListener!=null){
                  recyclerViewItemClickListener.onItemClick(v,position);
              }
            }
        });
    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_friend_list,null,false);
    }

    private class FriendHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_user_name;
        private RelativeLayout rl_rootView;
        public FriendHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            rl_rootView = (RelativeLayout)itemView.findViewById(R.id.rl_rootView);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
