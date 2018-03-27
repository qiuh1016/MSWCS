package com.zhd.mswcs.moduls.tab.iterator.tabitem;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhd.mswcs.R;

/**
 * 作者: Dream on 16/8/16 16:28
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class DefaultTabItem extends AbsTabItem<DefaultTabItem.DefaultTabItemParams>{

    private TextView textView;
    private ImageView imageView;

    public DefaultTabItem(DefaultTabItemParams params){
        super(params);
    }

    @Override
    public String getTabString() {
        return getParams().title;
    }

    @Override
    public Class<? extends Fragment> getFragmentClazz() {
        return getParams().fragmentClazz;
    }

    @Override
    public void setChecked(boolean isChecked) {
        if (isChecked){
            setStyle(getParams().imagePress,getParams().titlePressColor);
        }else{
            setStyle(getParams().imageNormal,getParams().titleNormalColor);
        }
    }

    @Override
    public View builder() {
        View view = super.builder();
        setStyle(getParams().imageNormal,getParams().titleNormalColor);
        return view;
    }

    private void setStyle(int image,int color){
        setImageView(image);
        setTextView(color);
    }

    private void setTextView(int color){
        if (textView == null){
            View view = getParams().findViewById(R.id.tv_tab);
            if (view != null && view instanceof TextView){
                textView = (TextView)view;
                if (TextUtils.isEmpty(getParams().title)){
                    textView.setVisibility(View.GONE);
                }else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(getParams().title);
                }
            }
        }
        if (textView != null){
            textView.setTextColor(getParams().getColor(color));
        }
    }

    private void setImageView(int res){
        if (imageView == null){
            View view = getParams().findViewById(R.id.iv_tab);
            if (view != null && view instanceof ImageView){
                imageView = (ImageView) getParams().findViewById(R.id.iv_tab);
            }
        }
        if (imageView != null){
            if (res == DEFAULT_IMAGE_ID){
                imageView.setVisibility(View.GONE);
            }else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(res);
            }
        }
    }

    public static class DefaultTabItemParams extends AbsTabItem.TabItemParams{

        public String title;
        public int titleNormalColor;
        public int titlePressColor;
        public int imageNormal;
        public int imagePress;



        public DefaultTabItemParams(Context context,int layoutId){
            super(context,layoutId);
            this.title = DEFAULT_STRING;
            this.titleNormalColor = DEFAULT_COLOR_ID;
            this.titlePressColor = DEFAULT_COLOR_ID;
            this.imageNormal = DEFAULT_IMAGE_ID;
            this.imagePress = DEFAULT_IMAGE_ID;
        }
    }

    public static class DefaultTabItemBuilder extends AbsTabItem.TabItemBuilder<DefaultTabItemParams,DefaultTabItem>{
        public DefaultTabItemBuilder(Context context,int layoutId){
            super(context,layoutId);
        }

        @Override
        protected DefaultTabItemParams bindTabItemParams(Context context,int layoutId) {
            return new DefaultTabItemParams(context,layoutId);
        }

        public DefaultTabItemBuilder setTitle(int id){
            getParams().title = getParams().getString(id);
            return this;
        }

        public DefaultTabItemBuilder setTitle(String title){
            getParams().title = title;
            return this;
        }

        public DefaultTabItemBuilder setTitleNormalColor(int normalColor){
            getParams().titleNormalColor = normalColor;
            return this;
        }

        public DefaultTabItemBuilder setTitlePressColor(int titlePressColor){
            getParams().titlePressColor = titlePressColor;
            return this;
        }

        public DefaultTabItemBuilder setImageNormal(int imageNormal){
            getParams().imageNormal = imageNormal;
            return this;
        }

        public DefaultTabItemBuilder setImagePress(int imagePress){
            getParams().imagePress = imagePress;
            return this;
        }

        @Override
        public DefaultTabItem build() {
            return new DefaultTabItem(this.getParams());
        }
    }

}
