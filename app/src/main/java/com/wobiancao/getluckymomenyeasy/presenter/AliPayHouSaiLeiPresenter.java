package com.wobiancao.getluckymomenyeasy.presenter;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BasePresenter;
import com.wobiancao.getluckymomenyeasy.iview.IAliPayView;

import java.util.List;

/**
 * Created by xy on 16/1/30.
 */
public class AliPayHouSaiLeiPresenter extends BasePresenter<IAliPayView> {
    private static final String  XIUXIU_CLASSNAME = "com.alipay.android.wallet.newyear.activity.MonkeyYearActivity";
    private static final String  XIUXIU_TEXT_VIEW = "android.widget.TextView";
    private static final String  XIUXIU_BUTTON_VIEW = "android.widget.Button";
    private static final String  XIUXIU_DIALOG = "android.app.Dialog";
    private boolean isXiu = false;
    @Override
    public void accessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        String className = String.valueOf(event.getClassName());
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            if (XIUXIU_CLASSNAME.equalsIgnoreCase(className)){
                isXiu = true;
                AccessibilityNodeInfo btnNode = getButtonInfo(aliView.getAliRootInActiveWindows());
                if (btnNode != null){

                }
            }
        }else if(XIUXIU_DIALOG.equalsIgnoreCase(className)){
            isXiu = false;
        }

    }

    @Override
    public void checkNodeInfo() {
        AccessibilityNodeInfo rootNodeInfo = aliView.getAliRootInActiveWindows();
        if (rootNodeInfo == null){
            isXiu = false;
            return;
        }
        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("huxi");
        if (!nodes1.isEmpty()){
            AccessibilityNodeInfo targetNode = nodes1.get(nodes1.size() - 1);
            aliView.setAliNodeInfo(targetNode);
            isXiu = true;
        }
    }

    @Override
    public void doAction() {
        final AccessibilityNodeInfo temNode = aliView.getAliNode();
        if (temNode == null){
            return;
        }
        if (XIUXIU_BUTTON_VIEW.equalsIgnoreCase(String.valueOf(temNode.getClassName()))
                && temNode.getChildCount() == 0
                && TextUtils.isEmpty(temNode.getText())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        if (temNode.isEnabled()){
                            temNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    } while (isXiu);
                }
            }) .start();
        }


    }

    //筛选出咻咻的button，进行不停的点击
    private AccessibilityNodeInfo getButtonInfo(AccessibilityNodeInfo parent){
        if(parent != null && parent.getChildCount() > 0){
            for(int i = 0 ;i < parent.getChildCount() ;i++){
                AccessibilityNodeInfo node = parent.getChild(i);
                if(XIUXIU_BUTTON_VIEW.equals(node.getClassName())){
                    return node;
                }
            }
        }
        return null;
    }
}
