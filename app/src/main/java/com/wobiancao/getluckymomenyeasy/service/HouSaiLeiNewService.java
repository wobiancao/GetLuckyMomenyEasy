package com.wobiancao.getluckymomenyeasy.service;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BaseAccessibilityService;
import com.wobiancao.getluckymomenyeasy.presenter.AliPayHouSaiLeiPresenter;
import com.wobiancao.getluckymomenyeasy.presenter.QQHouSaiLeiPresenter;
import com.wobiancao.getluckymomenyeasy.presenter.WeChatHouSaiLeiPresenter;
import com.wobiancao.getluckymomenyeasy.utils.L;

/**
 * Created by xy on 16/1/27.
 */
public class HouSaiLeiNewService extends BaseAccessibilityService {
    private boolean mMutex , mLuckyMoneyReceived, mNeedUnpack, mNeedBack, mLuckyMoneyPicked;
    private AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode, aliRootNodeInfo;
    private String lastContentDescription = "";
    private WeChatHouSaiLeiPresenter weChatPresenter = new WeChatHouSaiLeiPresenter();
    private QQHouSaiLeiPresenter qqHouSaiLeiPresenter = new QQHouSaiLeiPresenter();
    private AliPayHouSaiLeiPresenter aliPayHouSaiLeiPresenter = new AliPayHouSaiLeiPresenter();
    @Override
    protected void initWeChatPresenter(AccessibilityEvent event) {
        weChatPresenter.attachIView(this);
        weChatPresenter.accessibilityEvent(event);
        weChatPresenter.checkNodeInfo();
        weChatPresenter.doAction();
    }

    @Override
    protected void initQQPresenter(AccessibilityEvent event) {
        qqHouSaiLeiPresenter.attachIView(this);
        qqHouSaiLeiPresenter.accessibilityEvent(event);
        qqHouSaiLeiPresenter.checkNodeInfo();
        qqHouSaiLeiPresenter.doAction();

    }

    @Override
    protected void initAliPresenter(AccessibilityEvent event) {
        aliPayHouSaiLeiPresenter.attachAliIView(this);
        aliPayHouSaiLeiPresenter.accessibilityEvent(event);
//        aliPayHouSaiLeiPresenter.checkNodeInfo();
        aliPayHouSaiLeiPresenter.doAction();

    }


    @Override
    public void setLastContentDescription(String contentDescription) {
        this.lastContentDescription = contentDescription;
    }

    @Override
    public String getLastContentDescription() {
        return lastContentDescription;
    }

    @Override
    public void setRootNodeInfo(AccessibilityNodeInfo rootNodeInfo) {
        this.rootNodeInfo = rootNodeInfo;
    }

    @Override
    public AccessibilityNodeInfo getRootNodeInfo() {
        return rootNodeInfo;
    }

    @Override
    public void setReceiveNode(AccessibilityNodeInfo mReceiveNode) {
        this.mReceiveNode = mReceiveNode;
    }

    @Override
    public AccessibilityNodeInfo getReceiveNode() {
        return mReceiveNode;
    }


    @Override
    public void setUnpackNode(AccessibilityNodeInfo mUnpackNode) {
        this.mUnpackNode = mUnpackNode;
    }

    @Override
    public AccessibilityNodeInfo getUnpackNode() {
        return mUnpackNode;
    }

    @Override
    public void setLuckyMoneyReceived(boolean mLuckyMoneyReceived) {
        this.mLuckyMoneyReceived = mLuckyMoneyReceived;
    }

    @Override
    public boolean isLuckyMoneyReceived() {
        return mLuckyMoneyReceived;
    }

    @Override
    public void setNeedUnpack(boolean mNeedUnpack) {
        this.mNeedUnpack = mNeedUnpack;
    }

    @Override
    public boolean isNeedUnpack() {
        return mNeedUnpack;
    }

    @Override
    public void setNeedBack(boolean mNeedBack) {
        this.mNeedBack = mNeedBack;
    }

    @Override
    public boolean isNeedBack() {
        return mNeedBack;
    }

    @Override
    public void setLuckyMoneyPicked(boolean mLuckyMoneyPicked) {
        this.mLuckyMoneyPicked = mLuckyMoneyPicked;
    }

    @Override
    public boolean isLuckyMoneyPicked() {
        return mLuckyMoneyPicked;
    }

    @Override
    public void setMutex(boolean mMutex) {
        this.mMutex = mMutex;
    }

    @Override
    public boolean isMutex() {
        return mMutex;
    }

    @Override
    public void needBack() {
        performGlobalAction(GLOBAL_ACTION_BACK);
        mMutex = false;
        mNeedBack = false;
    }

    @Override
    public AccessibilityNodeInfo getRootInActiveWindows() {
        return getRootInActiveWindow();
    }


    @Override
    public void setAliRootNodeInfo(AccessibilityNodeInfo rootNodeInfo) {
        L.e("aliRootNodeInfo", rootNodeInfo.toString() + "");
        this.aliRootNodeInfo = rootNodeInfo;
    }

    @Override
    public AccessibilityNodeInfo getAliRootNodeInfo() {
        return aliRootNodeInfo;
    }

    @Override
    public AccessibilityNodeInfo getAliRootInActiveWindows() {
        return getRootInActiveWindow();
    }
}
