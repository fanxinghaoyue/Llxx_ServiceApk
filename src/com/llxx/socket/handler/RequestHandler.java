/**
 * 
 */
package com.llxx.socket.handler;

import org.json.JSONException;
import org.json.JSONObject;

import com.llxx.command.Command;
import com.llxx.socket.service.Ll_SocketService;
import com.llxx.socket.wrap.Ll_ClientSocketWrap;

import android.content.Context;

/**
 * @author 繁星
 * @describe 协议数据协议分装
 */
public abstract class RequestHandler extends Command
{
    private String classname = "";
    private String packageName = "";

    public abstract void doAction(Ll_ClientSocketWrap wrap, Ll_SocketService service);

    @Override
    public JSONObject getJsonObject()
    {
        JSONObject object = super.getJsonObject();
        if (object != null)
        {
            try
            {
                object.put("classname", getClassname());
                object.put("packagename", getPackageName());
                object.put("sucess", isRunOk());
                if (getCommandResult() != null)
                {
                    object.put("params", getCommandResult());
                }
                object.put("reason", getReason());
                return object;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getResult(Context context)
    {
        JSONObject object = getJsonObject();
        if (object != null)
        {
            return object.toString();
        }
        return null;
    }

    /**
     * @return the classname
     */
    public String getClassname()
    {
        return classname;
    }

    /**
     * @param classname the classname to set
     */
    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    /**
     * @return the packageName
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
}