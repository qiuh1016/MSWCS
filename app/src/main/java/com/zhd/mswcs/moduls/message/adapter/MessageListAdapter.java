package com.zhd.mswcs.moduls.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.message.bean.MessageItemLocalBean;
import com.zhd.mswcs.moduls.message.view.activity.ChatActivity;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class MessageListAdapter extends BaseRecylerAdapter<MessageItemLocalBean>{
    private Context mContext;
    private List<MessageItemLocalBean> dataList;
    public MessageListAdapter(Activity context, List<MessageItemLocalBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new MessageHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, int position) {
        super.onBindViewHolder(baseHolder, position);
        MessageHolder messageHolder = (MessageHolder) baseHolder;
        final MessageItemLocalBean data = dataList.get(position);
        messageHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
        messageHolder.tv_content.setText(data.getMessage());
        messageHolder.tv_send_time.setText(data.getDateTime());
        messageHolder.tv_user_name.setText(data.getUserName());
//        if(data.isHasNewMsg()){
//            messageHolder.iv_new_msg.setVisibility(View.VISIBLE);
//        }else{
//            messageHolder.iv_new_msg.setVisibility(View.GONE);
//        }
        messageHolder.rl_rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatName",data.getUserName());
                intent.putExtra("chatTelephone",data.getSender());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_message_list,null,false);
    }

    private class MessageHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_user_name;
        private TextView tv_send_time;
        private  TextView tv_content;
        //private  ImageView iv_new_msg;
        private RelativeLayout rl_rootView;
        public MessageHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            tv_send_time = (TextView)itemView.findViewById(R.id.tv_send_time);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            rl_rootView = (RelativeLayout)itemView.findViewById(R.id.rl_rootView);
            //iv_new_msg = (ImageView)itemView.findViewById(R.id.iv_new_msg);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
