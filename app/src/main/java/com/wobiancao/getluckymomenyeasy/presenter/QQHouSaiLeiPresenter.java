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
 * Created by xy on 16/1/28.
 */
public class QQHouSaiLeiPresenter extends BasePresenter<IHongBaoView> {
    private final static String QQ_NOTIFICATION_TIP = "[QQ红包]";
    private final static String QQ_DEFAULT_CLICK_OPEN = "点击拆开";
    public static final String  QQ_SPECIAL_UNCLICK_TEXT = "QQ红包个性版";
    private final static String QQ_HONG_BAO_PASSWORD = "口令红包";
    private final static String QQ_DEFAULT_CLICK_OPENED = "已拆开";
    private final static String QQ_HONG_BAO_PASSWORD_OPENED = "口令红包已拆开";
    private final static String QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令";
    private final static String QQ_AFTER_CLICK = "来晚一步";
    private final static String QQ_EXPIRES_OVER = "红包被领完了";
    private static final String QQ_BETTER_LUCK_CH = "手慢了";
    private static final String QQ_BETTER_LUCK_EN = "Better luck next time!";
    private final static String QQ_RECEIVE_CLICK = "已存入余额";
    private final static String QQ_COME_FROM = "来自";
    /**服务接入**/
    @TargetApi(Build.VERSION_CODES.KITKAT)
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void checkNodeInfo() {
        AccessibilityNodeInfo rootNodeInfo = iv.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
          /* 聊天会话窗口，遍历节点匹配“点击拆开”，“口令红包”，“点击输入口令” */
        List<AccessibilityNodeInfo> nodes1 = findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                QQ_DEFAULT_CLICK_OPEN,
                QQ_HONG_BAO_PASSWORD,
                QQ_SPECIAL_UNCLICK_TEXT,
                QQ_CLICK_TO_PASTE_PASSWORD, "发送"});

            if (!nodes1.isEmpty()) {
                AccessibilityNodeInfo targetNode = nodes1.get(nodes1.size() - 1);
                if (!signature.generateSignature(targetNode)) {
                    iv.setLuckyMoneyReceived(true);
                    iv.setReceiveNode(targetNode);
                }
                return;
            }

        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        if (iv.isLuckyMoneyPicked()) {
            List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                    QQ_AFTER_CLICK,
                    QQ_RECEIVE_CLICK,
                    QQ_EXPIRES_OVER,
                    QQ_BETTER_LUCK_CH,
                    QQ_BETTER_LUCK_EN,
                    QQ_COME_FROM

            });
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
        if (iv.isLuckyMoneyReceived() && (mReceiveNode != null)) {
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
                if (cellNode.getText().toString().equals(QQ_HONG_BAO_PASSWORD_OPENED)) {
                    return;
                }
                if (cellNode.getText().toString().equals(QQ_DEFAULT_CLICK_OPENED)) {
                    return;
                }
                cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                if (cellNode.getText().toString().equals(QQ_HONG_BAO_PASSWORD)) {
                    AccessibilityNodeInfo rowNode = iv.getRootInActiveWindows();
                    if (rowNode == null) {
                        return;
                    } else {
                        recycle(rowNode);
                    }
                }
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null && info.getText().toString().equals(QQ_CLICK_TO_PASTE_PASSWORD)) {
                info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            if (info.getClassName().toString().equals("android.widget.Button") && info.getText().toString().equals("发送")) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
    }


    private boolean watchNotifications(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            return false;
        }
        String tip = event.getText().toString();
        if (!tip.contains(QQ_NOTIFICATION_TIP)) return true;
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

        List<AccessibilityNodeInfo> nodes = event.getSource().findAccessibilityNodeInfosByText(QQ_NOTIFICATION_TIP);
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
