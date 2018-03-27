package com.zhd.mswcs.moduls.tab.iterator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 作者: Dream on 16/8/16 16:14
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public interface ITabItem {

    public static final int DEFAULT_LAYOUT_ID = 0;
    public static final int DEFAULT_STRING_ID = 0;
    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_COLOR_ID = Color.BLACK;
    public static final int DEFAULT_IMAGE_ID = 0;

    public View builder();

    public String getTabString();

    public Class<? extends Fragment> getFragmentClazz();

    public void setChecked(boolean isChecked);

    public abstract static class TabItemBuilder<P extends TabItemParams,TabItem extends ITabItem>{

        private P params;

        public TabItemBuilder(Context context,int layoutId){
            this.params = bindTabItemParams(context,layoutId);
        }

        protected abstract P bindTabItemParams(Context context,int layoutId);

        public abstract TabItem build();

        public TabItemBuilder setFragmentClazz(Class<? extends Fragment> clazz){
            getParams().fragmentClazz = clazz;
            return this;
        }

        public P getParams() {
            return params;
        }
    }

    public static class TabItemParams{
        public Context context;
        public LayoutInflater inflater;
        public View view;
        public int layoutId;

        public Class<? extends Fragment> fragmentClazz;

        public TabItemParams(Context context,int layoutId){
            this.context = context;
            this.layoutId = layoutId;
            inflater = LayoutInflater.from(context);
        }

        public View findViewById(int viewId){
            return view.findViewById(viewId);
        }

        public View inflate(int layoutId){
            if (layoutId == DEFAULT_LAYOUT_ID){
                return null;
            }
            view = inflater.inflate(layoutId, null);
            return view;
        }

        public String getString(int id){
            if (id == DEFAULT_STRING_ID){
                return "";
            }
            return context.getResources().getString(id);
        }

        public int getColor(int id){
            if (id == DEFAULT_COLOR_ID){
                return DEFAULT_COLOR_ID;
            }
            return context.getResources().getColor(id);
        }
    }

}
