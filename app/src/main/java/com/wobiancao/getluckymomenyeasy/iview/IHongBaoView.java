package com.wobiancao.getluckymomenyeasy.iview;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BaseIView;

import java.util.List;

/**
 * Created by xy on 16/1/27.
 * 接口
 */
public interface IHongBaoView extends BaseIView{
    void setLastContentDescription(String contentDescription);
    String getLastContentDescription();

    void setRootNodeInfo(AccessibilityNodeInfo rootNodeInfo);
    AccessibilityNodeInfo getRootNodeInfo();

    void setReceiveNode(AccessibilityNodeInfo mReceiveNode);
    AccessibilityNodeInfo getReceiveNode();

    void setUnpackNode(AccessibilityNodeInfo mUnpackNode);
    AccessibilityNodeInfo getUnpackNode();


    void setLuckyMoneyReceived(boolean mLuckyMoneyReceived);
    boolean isLuckyMoneyReceived();

    void setNeedUnpack(boolean mNeedUnpack);
    boolean isNeedUnpack();

    void setNeedBack(boolean mNeedBack);
    boolean isNeedBack();

    void setLuckyMoneyPicked(boolean mLuckyMoneyPicked);
    boolean isLuckyMoneyPicked();

    void setMutex(boolean mMutex);
    boolean isMutex();

    void setChatMutex(boolean mChatMutex);
    boolean isChatMutex();

    void needBack();

    AccessibilityNodeInfo getRootInActiveWindows();
}
