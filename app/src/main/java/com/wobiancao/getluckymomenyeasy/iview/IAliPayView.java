package com.wobiancao.getluckymomenyeasy.iview;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BaseIView;

/**
 * Created by xy on 16/1/30.
 */
public interface IAliPayView extends BaseIView{
    void setAliNodeInfo(AccessibilityNodeInfo nodeInfo);
    AccessibilityNodeInfo getAliNode();
    AccessibilityNodeInfo getAliRootInActiveWindows();

}
