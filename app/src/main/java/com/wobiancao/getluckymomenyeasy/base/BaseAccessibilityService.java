package com.wobiancao.getluckymomenyeasy.base;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.wobiancao.getluckymomenyeasy.iview.IAliPayView;
import com.wobiancao.getluckymomenyeasy.iview.IHongBaoView;
import com.wobiancao.getluckymomenyeasy.utils.L;

/**
 * Created by xy on 16/1/27.
 */
public abstract class BaseAccessibilityService extends AccessibilityService implements IHongBaoView ,IAliPayView{
    private final static String PACKAGENAME_QQ = "com.tencent.mobileqq";//qq
    private final static String PACKAGENAME_WECAHT = "com.tencent.mm";//微信
    protected abstract void initWeChatPresenter(AccessibilityEvent event);
    protected abstract void initQQPresenter(AccessibilityEvent event);
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        L.e("event", event.toString() + "");
        String packageName = event.getPackageName().toString();
        L.e("packageName", packageName);
        if (PACKAGENAME_QQ.equals(packageName)){
            initQQPresenter(event);
        }
        if (PACKAGENAME_WECAHT.equals(packageName)){
            initWeChatPresenter(event);
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "猴赛雷抢红包服务已启动，蹲群守候吧", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "猴赛雷抢红包服务已中断", Toast.LENGTH_SHORT).show();
    }

}
