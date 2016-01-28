package com.wobiancao.getluckymomenyeasy.base;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.wobiancao.getluckymomenyeasy.iview.IWechatView;
import com.wobiancao.getluckymomenyeasy.presenter.QQHouSaiLeiPresenter;
import com.wobiancao.getluckymomenyeasy.presenter.WeChatHouSaiLeiPresenter;

import java.util.List;

/**
 * Created by xy on 16/1/27.
 */
public abstract class BaseAccessibilityService extends AccessibilityService implements IWechatView {
    private final static String PACKAGENAME_QQ = "com.tencent.mobileqq";
    private final static String PACKAGENAME_WECAHT = "com.tencent.mm";
    protected abstract void initWeChatPresenter(AccessibilityEvent event);
    protected abstract void initQQPresenter(AccessibilityEvent event);
    protected boolean mMutex , mLuckyMoneyReceived, mNeedUnpack, mNeedBack, mLuckyMoneyPicked;
    protected AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode;
    protected String lastContentDescription = "";
    protected WeChatHouSaiLeiPresenter weChatPresenter = new WeChatHouSaiLeiPresenter();
    protected QQHouSaiLeiPresenter qqHouSaiLeiPresenter = new QQHouSaiLeiPresenter();
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        if (PACKAGENAME_QQ.equals(packageName)){
            initQQPresenter(event);
        }
        if (PACKAGENAME_WECAHT.equals(packageName)){
            initWeChatPresenter(event);
        }

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
