package com.zhd.mswcs.moduls.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.setting.bean.SelectFriendListBean;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class SelectSendFriendListAdapter extends BaseRecylerAdapter<SelectFriendListBean>{
    private Context mContext;
    private List<SelectFriendListBean> dataList;
    public List<SelectFriendListBean> getDataList() {
        return dataList;
    }

    public SelectSendFriendListAdapter(Activity context, List<SelectFriendListBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new SelectFriendHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, final int position) {
        super.onBindViewHolder(baseHolder, position);
        SelectFriendHolder selectFriendHolder = (SelectFriendHolder) baseHolder;
        final SelectFriendListBean data = dataList.get(position);
        selectFriendHolder.tv_user_telephone.setText(data.getTelephone());
        selectFriendHolder.tv_user_name.setText(data.getUserName());
//        if(data.isSelected()){
//            selectFriendHolder.cb_user.setChecked(true);
//        }else{
//            selectFriendHolder.cb_user.setChecked(false);
//        }
//
//        selectFriendHolder.cb_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    data.setSelected(true);
//                }else{
//                    data.setSelected(false);
//                }
//            }
//        });


        if(position ==PreferencesUtils.getInt(mContext,"sos_position")){
            selectFriendHolder.iv_selected.setVisibility(View.VISIBLE);
        }else{
            selectFriendHolder.iv_selected.setVisibility(View.INVISIBLE);
        }
        selectFriendHolder.rl_rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putInt(mContext,"sos_position",position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_select_send_friend,null,false);
    }

    private class SelectFriendHolder extends BaseRecylerHolder{
        private TextView tv_user_name;
        private TextView tv_user_telephone;
        //private CheckBox cb_user;
        private ImageView iv_selected;
        private RelativeLayout rl_rootView;
        public SelectFriendHolder(View itemView) {
            super(itemView);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            tv_user_telephone = (TextView)itemView.findViewById(R.id.tv_user_telephone);
            //cb_user = (CheckBox)itemView.findViewById(R.id.cb_user);
            iv_selected = (ImageView) itemView.findViewById(R.id.iv_selected);
            rl_rootView = (RelativeLayout) itemView.findViewById(R.id.rl_rootView);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
