package com.wobiancao.getluckymomenyeasy;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wandoujia.ads.sdk.Ads;
import com.wobiancao.getluckymomenyeasy.utils.StaticFiled;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button actionBtn;
    private LinearLayout adLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        actionBtn = (Button) findViewById(R.id.button_accessible);
        adLayout = (LinearLayout) findViewById(R.id.main_adlayout);
        adLayout.setVisibility(View.GONE);
        initAD();
    }

    //初始化广告
    private void initAD() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Ads.init(MainActivity.this, StaticFiled.APP_ID, StaticFiled.SECRET_KEY);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                Ads.preLoad(StaticFiled.AD_LIST_ID, Ads.AdFormat.list);
                Ads.preLoad(StaticFiled.AD_BANNER_ID, Ads.AdFormat.banner);
                View adView = Ads.createBannerView(MainActivity.this, StaticFiled.AD_BANNER_ID);
                if (adView != null){
                    adLayout.addView(adView);
                }
            }}.execute();

        }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }

    private void updateServiceStatus() {
        boolean serviceEnabled = false;
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.service.HouSaiLeiNewService")) {
                serviceEnabled = true;
                break;
            }
        }
        if (serviceEnabled) {
            actionBtn.setText(getResources().getString(R.string.app_service_close));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            actionBtn.setText(getResources().getString(R.string.app_service_open));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void onServiceClick(View view) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    //点击广告
    public void onWallClick(View view) {
        Ads.showInterstitial(MainActivity.this, StaticFiled.AD_LIST_ID);
    }
    public void onPingClick(View view) {
        if (adLayout.getVisibility() == View.VISIBLE){
            adLayout.setVisibility(View.GONE);
        }else{
            adLayout.setVisibility(View.VISIBLE);
        }
    }
}
