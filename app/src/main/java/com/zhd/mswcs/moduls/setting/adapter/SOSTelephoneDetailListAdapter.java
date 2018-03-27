package com.zhd.mswcs.moduls.setting.adapter;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.setting.bean.SendTelephoneListBean;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by think-1 on 2017/11/27.
 */

public class SOSTelephoneDetailListAdapter extends RecyclerSwipeAdapter<SOSTelephoneDetailListAdapter.SOSTelephoneHolder> {
    private Context mContext;
    private List<SendTelephoneListBean> dataList;

    public SOSTelephoneDetailListAdapter(Context context, List<SendTelephoneListBean> list){
        this.mContext = context;
        this.dataList = list;
    }

    @Override
    public SOSTelephoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sos_send_tel,null,false);
        return new SOSTelephoneHolder(view);
    }

    @Override
    public void onBindViewHolder(final SOSTelephoneHolder viewHolder, int position) {
        final SendTelephoneListBean data = dataList.get(position);
        viewHolder.tv_contactName.setText("姓名："+data.getContactName());
        viewHolder.tv_contactTelephone.setText("手机号："+data.getContactTelephone());
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        data.setIsDelete("1");
                        MyApplication.getInstance().getDb().saveOrUpdate(data);
                        ToastUtil.showToast(mContext,"删除成功");
                        dataList.remove(data);
                        notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mContext,"删除失败");
                }
            }
        });

        viewHolder.swipeLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }


     class SOSTelephoneHolder extends BaseRecylerHolder {
         private TextView tv_contactName;
         private TextView tv_contactTelephone;
         private SwipeLayout swipeLayout;
         private TextView btn_delete;
         private RelativeLayout rl_rootView;
         public SOSTelephoneHolder(View itemView) {
             super(itemView);
             tv_contactName = (TextView)itemView.findViewById(R.id.tv_contactName);
             tv_contactTelephone = (TextView)itemView.findViewById(R.id.tv_contactTelephone);
             swipeLayout = (SwipeLayout)itemView.findViewById(R.id.layout_swipe_tel_detail);
             btn_delete = (TextView)itemView.findViewById(R.id.btn_delete);
             rl_rootView = (RelativeLayout) itemView.findViewById(R.id.rl_rootView);
        }
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
