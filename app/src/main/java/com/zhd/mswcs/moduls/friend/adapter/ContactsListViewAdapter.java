package com.zhd.mswcs.moduls.friend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.friend.bean.ContactsInfoBean;

import java.util.List;

/**
 * Created by wangsong on 2016/4/24.
 */
public class ContactsListViewAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactsInfoBean> dataList;
    private boolean[] checks; //用于保存checkBox的选择状态
    public List<ContactsInfoBean> getDataList() {
        return dataList;
    }
    private Context context;
    private LayoutInflater inflater;

    public ContactsListViewAdapter(Context context, List<ContactsInfoBean> list) {
        this.context = context;
        this.dataList = list;
        checks = new boolean[list.size()];
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_contact, null);
            holder = new ViewHolder();
            holder.showLetter = (TextView) convertView.findViewById(R.id.show_letter);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.userTelephone = (TextView) convertView.findViewById(R.id.tv_user_telephone);
            holder.cb_user = (CheckBox) convertView.findViewById(R.id.cb_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ContactsInfoBean data = dataList.get(position);
        holder.userName.setText(data.getContactName());
        holder.userTelephone.setText(data.getContactTelephone());
        final int pos  = position; //pos必须声明为final
        holder.cb_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checks[pos] = isChecked;
                if(isChecked){
                    data.setSelected(true);
                }else{
                    data.setSelected(false);
                }
            }
        });

        holder.cb_user.setChecked(checks[pos]);

        //获得当前position是属于哪个分组
        int sectionForPosition = getSectionForPosition(position);
        //获得该分组第一项的position
        int positionForSection = getPositionForSection(sectionForPosition);
        //查看当前position是不是当前item所在分组的第一个item
        //如果是，则显示showLetter，否则隐藏
        if (position == positionForSection) {
            holder.showLetter.setVisibility(View.VISIBLE);
            holder.showLetter.setText(data.getFirstLetter());
        } else {
            holder.showLetter.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    //传入一个分组值[A....Z],获得该分组的第一项的position
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getFirstLetter().charAt(0) == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    //传入一个position，获得该position所在的分组
    @Override
    public int getSectionForPosition(int position) {
        return dataList.get(position).getFirstLetter().charAt(0);
    }

    class ViewHolder {
        TextView userName,userTelephone,showLetter;
        CheckBox cb_user;
    }
}
