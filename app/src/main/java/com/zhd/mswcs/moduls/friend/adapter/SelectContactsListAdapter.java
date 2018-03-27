package com.zhd.mswcs.moduls.friend.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.friend.bean.ContactsInfoBean;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class SelectContactsListAdapter extends BaseRecylerAdapter<ContactsInfoBean>{
    private Context mContext;
    private List<ContactsInfoBean> dataList;
    public List<ContactsInfoBean> getDataList() {
        return dataList;
    }

    public SelectContactsListAdapter(Activity context, List<ContactsInfoBean> data) {
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
        final ContactsInfoBean data = dataList.get(position);
        selectFriendHolder.tv_user_telephone.setText(data.getContactTelephone());
        selectFriendHolder.tv_user_name.setText(data.getContactName());
        if(data.isSelected()){
            selectFriendHolder.cb_user.setChecked(true);
        }else{
            selectFriendHolder.cb_user.setChecked(false);
        }

        selectFriendHolder.cb_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    data.setSelected(true);
                }else{
                    data.setSelected(false);
                }
            }
        });

    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_select_contacts,null,false);
    }

    private class SelectFriendHolder extends BaseRecylerHolder{
        private TextView tv_user_name;
        private TextView tv_user_telephone;
        private CheckBox cb_user;
        public SelectFriendHolder(View itemView) {
            super(itemView);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            tv_user_telephone = (TextView)itemView.findViewById(R.id.tv_user_telephone);
            cb_user = (CheckBox)itemView.findViewById(R.id.cb_user);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
