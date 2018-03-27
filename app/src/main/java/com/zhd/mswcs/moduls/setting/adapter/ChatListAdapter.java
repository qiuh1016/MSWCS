package com.zhd.mswcs.moduls.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.weidgt.audio.MediaPlayerManager;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mswcs.moduls.message.view.activity.ChatLocationActivity;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class ChatListAdapter extends BaseRecylerAdapter<MessageLocalBean>{
    private Context mContext;
    private List<MessageLocalBean> dataList;
    //item的最小宽度
    private int mMinWidth;
    //item的最大宽度
    private int mMaxWidth;
    public ChatListAdapter(Activity context, List<MessageLocalBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;

        //获取屏幕的宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        //最大宽度为屏幕宽度的百分之七十
        mMaxWidth = (int) (outMetrics.widthPixels * 0.7f);
        //最大宽度为屏幕宽度的百分之十五
        mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        if(viewType==1){
            return new ChatLeftNormalHolder(view);
        } else if(viewType==2){
            return new ChatRightNormalHolder(view);
        } else if(viewType==3){
            return new ChatLeftSpecialHolder(view);
        }else if(viewType==4){
            return new ChatRightSpecialHolder(view);
        }else if(viewType==5){
            return new ChatLeftRequestHolder(view);
        }else if(viewType==6){
            return new ChatRightRequestHolder(view);
        }else if(viewType==7){
            return new ChatLeftRecordHolder(view);
        }else{
            return new ChatRightRecordHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, int position) {
        super.onBindViewHolder(baseHolder, position);
        final MessageLocalBean data = dataList.get(position);
        switch (getItemViewType(position)){
            case 1:
                ChatLeftNormalHolder chatLeftNormalHolder = (ChatLeftNormalHolder) baseHolder;
                chatLeftNormalHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
                chatLeftNormalHolder.tv_content.setText(data.getMessage());
                break;
            case 2:
                ChatRightNormalHolder chatRightNormalHolder = (ChatRightNormalHolder) baseHolder;
                chatRightNormalHolder.iv_user_head.setImageResource(R.mipmap.img_user_head_two);
                chatRightNormalHolder.tv_content.setText(data.getMessage());
                break;
            case 3:
                ChatLeftSpecialHolder chatLeftSpecialHolder = (ChatLeftSpecialHolder) baseHolder;
                chatLeftSpecialHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
                chatLeftSpecialHolder.tv_content.setText(Html.fromHtml("经度："+data.getLon()+"<br/>"+"纬度："+data.getLat()));
                chatLeftSpecialHolder.rl_detail_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatLocationActivity.class);
                        intent.putExtra("type","1");
                        intent.putExtra("data",data);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 4:
                ChatRightSpecialHolder chatRightSpecialHolder = (ChatRightSpecialHolder) baseHolder;
                chatRightSpecialHolder.iv_user_head.setImageResource(R.mipmap.img_user_head_two);
                chatRightSpecialHolder.tv_content.setText(Html.fromHtml("经度："+data.getLon()+"<br/>"+"纬度："+data.getLat()));
                chatRightSpecialHolder.rl_detail_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatLocationActivity.class);
                        intent.putExtra("data",data);
                        intent.putExtra("type","2");
                        mContext.startActivity(intent);
                    }
                });
                break;

            case 5:
                ChatLeftRequestHolder chatLeftRequestHolder = (ChatLeftRequestHolder) baseHolder;
                chatLeftRequestHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
                chatLeftRequestHolder.tv_content.setText(data.getMessage());
                break;
            case 6:
                ChatRightRequestHolder chatRightRequestHolder = (ChatRightRequestHolder) baseHolder;
                chatRightRequestHolder.iv_user_head.setImageResource(R.mipmap.img_user_head_two);
                chatRightRequestHolder.tv_content.setText(data.getMessage());
                break;

            case 7:
                final ChatLeftRecordHolder chatLeftRecordHolder = (ChatLeftRecordHolder)baseHolder;
                chatLeftRecordHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
                chatLeftRecordHolder.id_record_time.setText(Math.round(data.getTime()) + "\"");
                ViewGroup.LayoutParams lp_left = chatLeftRecordHolder.id_record_length.getLayoutParams();
                lp_left.width = (int) (mMinWidth + (mMaxWidth / 60f) * data.getTime());
                chatLeftRecordHolder.rl_detail_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 声音播放动画
                        if (chatLeftRecordHolder.id_record_anim != null) {
                            chatLeftRecordHolder.id_record_anim.setBackgroundResource(R.drawable.img_adj_left);
                        }
                        chatLeftRecordHolder.id_record_anim.setBackgroundResource(R.drawable.play_anim_left);
                        AnimationDrawable animation = (AnimationDrawable)chatLeftRecordHolder.id_record_anim.getBackground();
                        animation.start();
                        // 播放录音
                        MediaPlayerManager.playSound(data.getFilePath(), new MediaPlayer.OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                //播放完成后修改图片
                                chatLeftRecordHolder.id_record_anim.setBackgroundResource(R.drawable.img_adj_left);
                            }
                        });
                    }
                });
                break;

            case 8:
                final ChatRightRecordHolder chatRightRecordHolder = (ChatRightRecordHolder)baseHolder;
                chatRightRecordHolder.iv_user_head.setImageResource(R.mipmap.img_user_head_two);
                chatRightRecordHolder.id_record_time.setText(Math.round(data.getTime()) + "\"");
                ViewGroup.LayoutParams lp_right = chatRightRecordHolder.id_record_length.getLayoutParams();
                lp_right.width = (int) (mMinWidth + (mMaxWidth / 60f) * data.getTime());
                chatRightRecordHolder.rl_detail_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 声音播放动画
                        if (chatRightRecordHolder.id_record_anim != null) {
                            chatRightRecordHolder.id_record_anim.setBackgroundResource(R.drawable.img_adj_right);
                        }
                        chatRightRecordHolder.id_record_anim.setBackgroundResource(R.drawable.play_anim_right);
                        AnimationDrawable animation = (AnimationDrawable)chatRightRecordHolder.id_record_anim.getBackground();
                        animation.start();
                        // 播放录音
                        MediaPlayerManager.playSound(data.getFilePath(), new MediaPlayer.OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                //播放完成后修改图片
                                chatRightRecordHolder.id_record_anim.setBackgroundResource(R.drawable.img_adj_right);
                            }
                        });
                    }
                });
                break;

        }

    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        if(viewType==1){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_left_normal,null,false);
        } else if(viewType==2){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_right_normal,null,false);
        } else if(viewType==3){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_left_special,null,false);
        }else if(viewType==4){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_right_special,null,false);
        }else if(viewType==5){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_left_request,null,false);
        }else if(viewType==6){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_right_request,null,false);
        }else if(viewType==7){
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_left_recoder,null,false);
        }else{
            return LayoutInflater.from(mContext).inflate(R.layout.item_chat_right_recoder,null,false);
        }
    }

    private class ChatLeftNormalHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        public ChatLeftNormalHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
        }
    }


    private class ChatLeftRequestHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        public ChatLeftRequestHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
        }
    }


    private class ChatRightNormalHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        public ChatRightNormalHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
        }
    }


    private class ChatRightRequestHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        public ChatRightRequestHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
        }
    }


    private class ChatLeftSpecialHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        private RelativeLayout rl_detail_info;
        public ChatLeftSpecialHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            rl_detail_info = (RelativeLayout)itemView.findViewById(R.id.rl_detail_info);
        }
    }


    private class ChatRightSpecialHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_content;
        private RelativeLayout rl_detail_info;
        public ChatRightSpecialHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            rl_detail_info = (RelativeLayout)itemView.findViewById(R.id.rl_detail_info);
        }
    }




    private class ChatLeftRecordHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private FrameLayout id_record_length;
        private View id_record_anim;
        private TextView id_record_time;
        private RelativeLayout rl_detail_info;
        public ChatLeftRecordHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            id_record_length = (FrameLayout)itemView.findViewById(R.id.id_recoder_length);
            id_record_anim = (View)itemView.findViewById(R.id.id_recoder_anim);
            id_record_time = (TextView)itemView.findViewById(R.id.id_recoder_time);
            rl_detail_info = (RelativeLayout)itemView.findViewById(R.id.rl_detail_info);
        }
    }


    private class ChatRightRecordHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private FrameLayout id_record_length;
        private View id_record_anim;
        private TextView id_record_time;
        private RelativeLayout rl_detail_info;
        public ChatRightRecordHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            id_record_length = (FrameLayout)itemView.findViewById(R.id.id_recoder_length);
            id_record_anim = (View)itemView.findViewById(R.id.id_recoder_anim);
            id_record_time = (TextView)itemView.findViewById(R.id.id_recoder_time);
            rl_detail_info = (RelativeLayout)itemView.findViewById(R.id.rl_detail_info);
        }
    }






    @Override
    public int getItemViewType(int position) {
        if(dataList.get(position).getSmsType()==0){
            if(!StringUtils.equals(dataList.get(position).getSender(), PreferencesUtils.getString(mContext,"telephone"))){
                return 1;
            }else{
                return 2;
            }
        } else if(dataList.get(position).getSmsType()==10){
            if(!StringUtils.equals(dataList.get(position).getSender(), PreferencesUtils.getString(mContext,"telephone"))){
                return 3;
            }else{
                return 4;
            }
        }else if(dataList.get(position).getSmsType()==20){
            if(!StringUtils.equals(dataList.get(position).getSender(), PreferencesUtils.getString(mContext,"telephone"))){
                return 5;
            }else{
                return 6;
            }
        }else{
            if(!StringUtils.equals(dataList.get(position).getSender(), PreferencesUtils.getString(mContext,"telephone"))){
                return 7;
            }else{
                return 8;
            }
        }
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

}
