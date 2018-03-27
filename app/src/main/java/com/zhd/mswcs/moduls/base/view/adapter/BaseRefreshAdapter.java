package com.zhd.mswcs.moduls.base.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseRefreshAdapter<M,L,VH extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<VH> {

    private Context context;
    private List<M> list;
    private LayoutInflater inflater;

    public BaseRefreshAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<M>();
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public List<M> getList() {
        return list;
    }

    public void refreshAdapter(boolean isDownRefresh, List<M> list, L data){
        if (isDownRefresh){
            getList().clear();
        }
        if (list != null){
            getList().addAll(list);
        }
        //刷新
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        return getList().size();
    }

}