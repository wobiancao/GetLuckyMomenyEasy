package com.wobiancao.getluckymomenyeasy.presenter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wobiancao.getluckymomenyeasy.base.BasePresenter;
import com.wobiancao.getluckymomenyeasy.iview.IHongBaoView;

import java.util.List;

/**
 * Created by xy on 16/1/27.
 */
public class WeChatHouSaiLeiPresenter extends BasePresenter<IHongBaoView> {
    private static final String WECHAT_VIEW_SELF_CH = "查看红包";
    private static final String WECHAT_VIEW_OTHERS_CH = "领取红包";
    private static final String WECHAT_BETTER_LUCK_CH = "手慢了";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_DETAILS_EN = "Details";
    private static final String WECHAT_DETAILS_CH = "红包详情";
    private static final String WECHAT_EXPIRES_OVER = "红包派完了";
    private final static String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_EXPIRES_CH = "已超过24小时";
    private static final String WECHAT_RECEIVE_CH = "你领取了";
    private static final String WECHAT_LUCKMONEY_RECEIVE_ACTIVITY = "LuckyMoneyReceiveUI";
    private static final String WECHAT_LUCKMONEY_DETAIL_ACTIVITY = "LuckyMoneyDetailUI";
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_CHATTING_ACTIVITY = "ChattingUI";

    /**服务接入**/
    @Override
    public void accessibilityEvent(AccessibilityEvent event) {
        iv.setRootNodeInfo(event.getSource());
        if (iv.getRootNodeInfo() == null) {
            return;
        }
        iv.setNeedBack(false);
        if (!iv.isMutex()) {
            if (watchNotifications(event)) {
                return;
            }
            if (watchList(event)){
                return;
            }
            iv.setMutex(false);
        }
        iv.setReceiveNode(null);
        iv.setUnpackNode(null);
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void checkNodeInfo() {
        AccessibilityNodeInfo rootNodeInfo = iv.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
         /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        List<AccessibilityNodeInfo> nodes1 = findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                WECHAT_VIEW_OTHERS_CH,
                WECHAT_VIEW_SELF_CH});

        if (!nodes1.isEmpty()) {
            AccessibilityNodeInfo targetNode = nodes1.get(nodes1.size() - 1);
            if (signature.generateSignature(targetNode)) {
                iv.setLuckyMoneyReceived(true);
                iv.setReceiveNode(targetNode);
            }
            return;
        }
        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        AccessibilityNodeInfo node2 = (rootNodeInfo.getChildCount() > 3) ? rootNodeInfo.getChild(3) : null;
        if (node2 != null && node2.getClassName().equals("android.widget.Button")) {
            iv.setUnpackNode(node2);
            iv.setNeedUnpack(true);
            return;
        }
//
//        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        if (iv.isLuckyMoneyPicked()) {
            List<AccessibilityNodeInfo> nodes3 = findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                    WECHAT_BETTER_LUCK_CH,
                    WECHAT_DETAILS_CH,
                    WECHAT_BETTER_LUCK_EN,
                    WECHAT_DETAILS_EN,
                    WECHAT_EXPIRES_OVER,
                    WECHAT_EXPIRES_CH});
            if (!nodes3.isEmpty()) {
                iv.setNeedBack(true);
                iv.setLuckyMoneyPicked(false);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void doAction() {
        /* 如果已经接收到红包并且还没有戳开 */
        AccessibilityNodeInfo mReceiveNode = iv.getReceiveNode();
        AccessibilityNodeInfo mUnpackNode  = iv.getUnpackNode();
        if (iv.isLuckyMoneyReceived() && mReceiveNode != null){
            String id = getHongbaoText(mReceiveNode);
            long now = System.currentTimeMillis();
            if (shouldReturn(id, now - lastFetchedTime)){
                return;
            }
            lastFetchedHongbaoId = id;
            lastFetchedTime = now;
            AccessibilityNodeInfo cellNode = mReceiveNode;
            if(signature.generateSignature(cellNode)){
                return;
            }
            if (cellNode.getText().toString().equals(WECHAT_RECEIVE_CH)) {
                return;
            }
            cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            iv.setMutex(true);
            iv.setLuckyMoneyReceived(false);
            iv.setLuckyMoneyPicked(true);
        }
        /* 如果戳开但还未领取 */
        if (iv.isNeedUnpack() && (mUnpackNode != null)) {
            AccessibilityNodeInfo cellNode = mUnpackNode;
            cellNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            iv.setNeedUnpack(false);
        }
        if (iv.isNeedBack()) {
            iv.needBack();
        }
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // Not a notification
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return false;

        // Not a hongbao
        String tip = event.getText().toString();
        if (!tip.contains(WECHAT_NOTIFICATION_TIP)) return true;

        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean watchList(AccessibilityEvent event) {
        // Not a message
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || event.getSource() == null)
            return false;

        List<AccessibilityNodeInfo> nodes = event.getSource().findAccessibilityNodeInfosByText(WECHAT_NOTIFICATION_TIP);
        if (!nodes.isEmpty()) {
            AccessibilityNodeInfo nodeToClick = nodes.get(0);
            CharSequence contentDescription = nodeToClick.getContentDescription();
            if (contentDescription != null && !iv.getLastContentDescription().equals(contentDescription)) {
                nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                iv.setLastContentDescription(contentDescription.toString());
                return true;
            }
        }
        return false;
    }

}
