package com.llxx.client.command;

import org.json.JSONException;
import org.json.JSONObject;

import com.llxx.command.Command;
import com.llxx.socket.service.Ll_AccessibilityService;

public abstract class CommandRun extends Command
{
    public abstract boolean runCommand(Ll_AccessibilityService accessibilityService);

    @Override
    public boolean prase()
    {
        try
        {
            JSONObject object = new JSONObject(getMessage().getMessage());
            setClientHash(object.optInt("clientHash", 0));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject getJsonObject()
    {
        JSONObject object = super.getJsonObject();
        if (object != null)
        {
            try
            {
                object.put("sucess", isRunOk());
                object.put("clientHash", getClientHash());
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
    public JSONObject getErrorResult()
    {
        JSONObject object = super.getErrorResult();
        if (object != null)
        {
            try
            {
                object.put("sucess", false);
                object.put("clientHash", getClientHash());
                return object;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
