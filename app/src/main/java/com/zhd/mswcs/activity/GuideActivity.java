package com.zhd.mswcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.user.view.LoginActivity;
import com.zhd.mswcs.views.CircleIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public class GuideActivity extends AppCompatActivity {

    //存放资源
    private List<Integer> imageResList;
    private List<ImageView> imageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initImageRes();
        initImageView();
        initView();
    }

    private void initImageRes(){
        imageResList = new ArrayList<>();
        imageResList.add(R.mipmap.img_guide_one);
        imageResList.add(R.mipmap.img_guide_two);
        imageResList.add(R.mipmap.img_guide_three);
    }

    private void initImageView(){
        this.imageViewList = new ArrayList<>();
        for (Integer imageRes : imageResList){
            this.imageViewList.add(new ImageView(this));
        }
    }

    //第一步:初始化ViewPager
    //第一种方式:直接在Activity里面实现
    //第二种方式:采用Fragment实现(课后同学们实现)
    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new GuideAdpater());

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        final View view = findViewById(R.id.tv_main);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,HomeActivity.class));
                finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageResList.size() - 1) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //引导页适配器
    class GuideAdpater extends PagerAdapter {

        @Override
        public int getCount() {
            return imageResList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //绑定视图
            ImageView imageView = imageViewList.get(position);
            imageView.setImageResource(imageResList.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if(position==imageResList.size()-1){
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GuideActivity.this,HomeActivity.class));
                        finish();
                    }
                });
            }
            return imageView;
        }
    }


}

