package com.zhd.mswcs.moduls.base.view.adapter.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 基类的recyclerview adaper
 * Created by zhanghanjun on 2017/8/16.
 */

public abstract class BaseRecylerAdapter<T> extends RecyclerView.Adapter<BaseRecylerHolder> {
    public List<T> mList;
    public Activity mContext;
    public RecyclerViewItemClickListener mListener;

    public BaseRecylerAdapter(Activity context, List<T> data) {
        this.mList = data;
        this.mContext = context;
    }

    @Override
    public BaseRecylerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getView(parent,viewType);
        return createViewHolder(view,viewType);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public void setOnItemClickListner(RecyclerViewItemClickListener listner) {
        this.mListener = listner;
    }

    public abstract BaseRecylerHolder createViewHolder(View view, int viewType);

    public abstract View getView(ViewGroup parent, int viewType);
}
