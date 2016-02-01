package com.wobiancao.getluckymomenyeasy.presenter;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BasePresenter;
import com.wobiancao.getluckymomenyeasy.iview.IAliPayView;
import com.wobiancao.getluckymomenyeasy.utils.L;

/**
 * Created by xy on 16/1/30.
 */
public class AliPayHouSaiLeiPresenter extends BasePresenter<IAliPayView> {
    private static final String  XIUXIU_CLASSNAME = "com.alipay.android.wallet.newyear.activity.MonkeyYearActivity";
    private static final String  XIUXIU_TEXT_VIEW = "android.widget.TextView";
    private static final String  XIUXIU_BUTTON_VIEW = "android.widget.Button";
    private boolean isXiu = false;
    @Override
    public void accessibilityEvent(AccessibilityEvent event) {
//        aliView.setAliRootNodeInfo(event.getSource());
        final int eventType = event.getEventType();
        String className = String.valueOf(event.getClassName());
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                XIUXIU_CLASSNAME.equalsIgnoreCase(className)){
            isXiu = true;
        }else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
                XIUXIU_TEXT_VIEW.equalsIgnoreCase(className)){
            isXiu = true;
        }else{
            isXiu = false;
        }
    }

    @Override
    public void checkNodeInfo() {
//        AccessibilityNodeInfo rootNodeInfo = aliView.getAliRootNodeInfo();
//        if (rootNodeInfo == null){
//            return;
//        }
    }

    @Override
    public void doAction() {
        final AccessibilityNodeInfo rootNodeInfo = aliView.getAliRootInActiveWindows();
            if (rootNodeInfo != null && rootNodeInfo.getChildCount() > 10){
                final int lastNodeIndex = rootNodeInfo.getChildCount() - 1;
                String lastClassName = String.valueOf(rootNodeInfo.getChild(lastNodeIndex).getClassName());
                if (XIUXIU_BUTTON_VIEW.equalsIgnoreCase(lastClassName)){
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            do {
                                rootNodeInfo.getChild(lastNodeIndex).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    L.e("package", "有异常？");
                                }
                            } while (isXiu);
                        }
                    }) .start();
                }
            }else {
                L.e("package", "看起来不在咻页面");
            }

    }
}
