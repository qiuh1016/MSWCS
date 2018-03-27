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
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.setting.bean.EscalationCycleBean;
import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class EscalationCycleListAdapter extends BaseRecylerAdapter<EscalationCycleBean>{
    private Context mContext;
    private List<EscalationCycleBean> dataList;

    public List<EscalationCycleBean> getDataList() {
        return dataList;
    }

    public EscalationCycleListAdapter(Activity context, List<EscalationCycleBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new EscalationCycleHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, final int position) {
        super.onBindViewHolder(baseHolder, position);
        EscalationCycleHolder escalationCycleHolder = (EscalationCycleHolder) baseHolder;
        final EscalationCycleBean data = dataList.get(position);
        escalationCycleHolder.tv_cycle_time.setText(data.getTime());
        if(data.isSelected()){
            escalationCycleHolder.iv_selected.setVisibility(View.VISIBLE);
        }else{
            escalationCycleHolder.iv_selected.setVisibility(View.INVISIBLE);
        }

        escalationCycleHolder.rl_rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 for(EscalationCycleBean bean:dataList){
                     if(StringUtils.equals(bean.getId(),dataList.get(position).getId())){
                         bean.setSelected(true);
                     }else{
                         bean.setSelected(false);
                     }
                 }
                  notifyDataSetChanged();
            }
        });

    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_escalation_cycle,null,false);
    }

    private class EscalationCycleHolder extends BaseRecylerHolder{
        private TextView tv_cycle_time;
        private ImageView iv_selected;
        private RelativeLayout rl_rootView;
        public EscalationCycleHolder(View itemView) {
            super(itemView);
            tv_cycle_time = (TextView)itemView.findViewById(R.id.tv_cycle_time);
            iv_selected = (ImageView)itemView.findViewById(R.id.iv_selected);
            rl_rootView = (RelativeLayout)itemView.findViewById(R.id.rl_rootView);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
