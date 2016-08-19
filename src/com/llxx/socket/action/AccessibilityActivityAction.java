/**
 * 
 */
package com.llxx.socket.action;

import com.llxx.socket.loger.Ll_Loger;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author 李万隆
 * @date   2016年8月19日
 * @qq 	461051353
 * @describe 获取点击事件
 */
public class AccessibilityActivityAction extends AccessibilityAction
{
    public static final String TAG = "AccessibilityClickAction";

    @Override
    protected boolean processEvent(Context context, AccessibilityEvent event,
            AccessibilityNodeInfo nodeInfo)
    {
        // Ll_Loger.i(TAG, event.getBeforeText().toString());
        if (event.getClassName() != null)
            Ll_Loger.i(TAG, "class name: " + event.getClassName().toString());

        // Ll_Loger.i(TAG, event.getContentDescription().toString());
        // Ll_Loger.i(TAG, event.getText().toString());
        if (event.getPackageName() != null)
            Ll_Loger.i(TAG,
                    "package name: " + event.getPackageName().toString());
        try
        {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString());

            ActivityInfo activityInfo = tryGetActivity(context, componentName);
            if (activityInfo != null)
            {
                Ll_Loger.i(TAG, componentName.flattenToShortString());
                setResult("start|activity|" + event.getPackageName() + "|"
                        + event.getClassName());
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getEventType()
    {
        return AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
    }

    private ActivityInfo tryGetActivity(Context context,
            ComponentName componentName)
    {
        try
        {
            return context.getPackageManager().getActivityInfo(componentName,
                    0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

}