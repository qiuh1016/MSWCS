package com.zhd.mswcs.common.weidgt.picture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.weidgt.picture.constants.ConstantSet;
import com.zhd.mswcs.common.weidgt.picture.utils.BitmapUtils;
import com.zhd.mswcs.common.weidgt.picture.utils.SDCardUtils;
import com.zhd.mswcs.common.weidgt.picture.view.CuttingFrameView;
import com.zhd.mswcs.common.weidgt.picture.view.PerfectControlImageView;
import com.zhd.mswcs.common.weidgt.picture.view.SpinnerProgressDialoag;
import java.io.ByteArrayOutputStream;

/**
 * Created by yinwei on 2015-11-12.
 */
public class ClippingPageActivity extends AppCompatActivity {


    private PerfectControlImageView imageView;
    private CuttingFrameView cuttingFrameView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipping_page);
        initTitle();
        cuttingFrameView = (CuttingFrameView) findViewById(R.id.cutingFrame);
        imageView = (PerfectControlImageView) findViewById(R.id.targetImage);
        if (getIntent().getStringExtra("type").equals("takePicture")) {
            bitmap = BitmapUtils.DecodLocalFileImage(ConstantSet.LOCALFILE + ConstantSet.USERTEMPPIC, this);
        } else {
            String path=getIntent().getStringExtra("path");
            bitmap = BitmapUtils.DecodLocalFileImage(path, this);
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 初始化title
     */
    private void initTitle() {
        ImageView rightImage = (ImageView) findViewById(R.id.id_img_right);
        TextView title = (TextView) findViewById(R.id.id_title);
        TextView righttext=(TextView)findViewById(R.id.id_text_right);
        ImageView back = (ImageView) findViewById(R.id.id_back);
        rightImage.setVisibility(View.GONE);
        righttext.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.picture_cutting));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClippingPageActivity.this.finish();
            }
        });

        righttext.setText(getResources().getString(R.string.sure));
        righttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerProgressDialoag sp=new SpinnerProgressDialoag(ClippingPageActivity.this);
                sp.show();
                Bitmap bitmap=cuttingFrameView.takeScreenShot(ClippingPageActivity.this);
                SDCardUtils.saveMyBitmap(ConstantSet.LOCALFILE, ConstantSet.USERPIC, bitmap);
                Intent it=new Intent();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();
                it.putExtra("result", bitmapByte);
                setResult(RESULT_OK, it);
                sp.dismiss();
                ClippingPageActivity.this.finish();
            }
        });
    }


}
