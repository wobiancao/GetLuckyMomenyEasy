package com.wobiancao.getluckymomenyeasy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.wandoujia.ads.sdk.Ads;
import com.wobiancao.getluckymomenyeasy.utils.StaticFiled;

/**
 * Created by xy on 16/1/26.
 */
public class StartActivity extends Activity {
    private final int DISPLAY_TIME = 1500;//延迟时间
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, DISPLAY_TIME );
    }


}
