package com.wobiancao.getluckymomenyeasy.base;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.iview.IAliPayView;
import com.wobiancao.getluckymomenyeasy.iview.IHongBaoView;
import com.wobiancao.getluckymomenyeasy.utils.HongbaoSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 16/1/27.
 */
public abstract class BasePresenter<IV extends BaseIView> {
    private static final String WECHAT_OPEN_EN = "Open";
    private static final String WECHAT_OPENED_EN = "You've opened";
    protected IHongBaoView iv;
    protected IAliPayView aliView;
    protected HongbaoSignature signature = new HongbaoSignature();
    protected String lastFetchedHongbaoId = null;
    protected long lastFetchedTime = 0;
    protected static final int MAX_CACHE_TOLERANCE = 5000;
    protected abstract void accessibilityEvent(AccessibilityEvent event);
    protected abstract void checkNodeInfo();
    protected abstract void doAction();
    protected String getHongbaoText(AccessibilityNodeInfo node) {
        /* 获取红包上的文本 */
        String content;
        try {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npe) {
            return null;
        }

        return content;
    }

    public void attachIView(IHongBaoView honbaoView) {
        iv = honbaoView;
    }
    /**支付宝**/
    public void attachAliIView(IAliPayView iAliPayView) {
        aliView = iAliPayView;
    }
    /**
     * 判断是否返回,减少点击次数
     * 现在的策略是当红包文本和缓存不一致时,戳
     * 文本一致且间隔大于MAX_CACHE_TOLERANCE时,戳
     *
     * @param id
     * @param duration 红包到达与缓存的间隔
     * @return 是否应该返回
     */
    protected boolean shouldReturn(String id, long duration) {
        // ID为空
        if (id == null) return true;

        // 名称和缓存不一致
        if (duration < MAX_CACHE_TOLERANCE && id.equals(lastFetchedHongbaoId)) {
            return true;
        }
        return false;
    }
    /**
     * 批量化执行AccessibilityNodeInfo.findAccessibilityNodeInfosByText(text).
     * 由于这个操作影响性能,将所有需要匹配的文字一起处理,尽早返回
     *
     * @param nodeInfo 窗口根节点
     * @param texts    需要匹配的字符串们
     * @return 匹配到的节点数组
     */
    protected List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            if (text == null) continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {
                return nodes;
            }
        }
        return new ArrayList<>();
    }
}
