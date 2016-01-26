package com.wobiancao.getluckymomenyeasy;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import com.umeng.update.UmengUpdateAgent;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button actionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        UmengUpdateAgent.update(this);
    }

    private void initView() {
        actionBtn = (Button) findViewById(R.id.button_accessible);
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
            if (info.getId().equals(getPackageName() + "/.service.HouSaiLeiService")) {
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
    public void onGithubClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(browserIntent);
    }
}
