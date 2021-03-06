package com.zhd.mswcs.network;

/**
 * Created by think-1 on 2017/11/29.
 */

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.zhd.mswcs.common.utils.NetworkUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BaseInterceptor
 * Created by Tamic on 2016-7-15.
 */
public class BaseInterceptor implements Interceptor {
    private Map<String, String> headers;
    private Context context;
    public BaseInterceptor(Map<String, String> headers, Context context) {
        this.headers = headers;
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request()
                .newBuilder();
        builder.cacheControl(CacheControl.FORCE_CACHE).url(chain.request().url())
                .build();

        if (!NetworkUtils.isConnected(context)) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "当前无网络!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }

        if (NetworkUtils.isConnected(context)) {
            int maxAge = 60; // read from cache for 60 s
            builder
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 14; // tolerate 2-weeks stale
            builder
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return chain.proceed(builder.build());

    }
}
