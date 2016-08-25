/**
 * 
 */
package com.llxx.socket.action;

import com.llxx.socket.loger.Ll_Loger;
import com.llxx.socket.protocol.wrap.ProtocolActivity;
import com.llxx.socket.protocol.wrap.ProtocolClick;
import com.llxx.socket.protocol.wrap.ProtocolConstants;
import com.llxx.socket.protocol.wrap.ProtocolNotify;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author 李万隆
 * @date   2016年8月19日
 * @qq 	461051353
 * @describe 获取点击事件
 */
public class AccessibilityClickAction extends AccessibilityAction
{
    public static final String TAG = "AccessibilityClickAction";

    @Override
    protected boolean processEvent(Context context, AccessibilityEvent event,
            AccessibilityNodeInfo nodeInfo)
    {
        setResult("");
        // Ll_Loger.i(TAG, event.getBeforeText().toString());
        if (event.getClassName() != null)
            Ll_Loger.i(TAG, "class name: " + event.getClassName().toString());

        // Ll_Loger.i(TAG, event.getContentDescription().toString());
        // Ll_Loger.i(TAG, event.getText().toString());
        if (event.getPackageName() != null)
            Ll_Loger.i(TAG,
                    "package name: " + event.getPackageName().toString());

        if (nodeInfo != null)
        {
            ProtocolClick activity = new ProtocolClick();
            activity.setClassname(event.getClassName().toString());
            activity.setPackageName(event.getPackageName().toString());
            activity.setType(activity.getClassname());
            activity.setTitle(nodeInfo.getText().toString());
            setResult(activity.getResult(context));
            // Ll_Loger.i(TAG, nodeInfo.toString());
            return true;
        }
        else if (event.getText() != null && event.getText().size() > 0)
        {
            ProtocolClick activity = new ProtocolClick();
            activity.setClassname(event.getClassName().toString());
            activity.setPackageName(event.getPackageName().toString());
            activity.setType(activity.getClassname());
            activity.setTitle(event.getText().get(0).toString());
            setResult(activity.getResult(context));
            // Ll_Loger.i(TAG, "nodeInfo is null" +  event.getText());
            return true;
        }
        return false;
    }

    @Override
    public int getEventType()
    {
        return AccessibilityEvent.TYPE_VIEW_CLICKED;
    }
}
