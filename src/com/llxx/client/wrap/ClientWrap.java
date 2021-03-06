package com.llxx.client.wrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.llxx.socket.loger.Ll_Loger;
import com.llxx.socket.wrap.Ll_MessageListener;
import com.llxx.socket.wrap.bean.Ll_Message;

import android.text.TextUtils;

public class ClientWrap implements Runnable
{
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8082;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public static final String TAG = "ClientWrap";

    Ll_MessageListener mListener;

    // 
    private StringBuilder input = new StringBuilder();
    private boolean isKeepListener = true;
    
    private boolean isConnect = false;

    public ClientWrap(Ll_MessageListener listener)
    {
        mListener = listener;
    }

    /**
     * 发送消息
     * @param msg
     */
    public void send(String msg)
    {
        if (socket.isConnected())
        {
            if (!socket.isOutputShutdown())
            {
                out.println(msg);
            }
        }
    }

    public void run()
    {
        try
        {

            while (socket == null)
            {
                try
                {
                    socket = new Socket(HOST, PORT);
                    Thread.sleep(2000);
                }
                catch (Exception e)
                {
                    Ll_Loger.w(TAG, this + "wait for service");
                }
            }

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            isConnect = true;
            while (isKeepListener)
            {
                input.setLength(0); // clear
                int a;
                // (char) -1 is not equal to -1.
                // ready is checked to ensure the read call doesn't block.
                while ((a = in.read()) != -1 && in.ready())
                {
                    input.append((char) a);
                }
                
                String msg = input.toString();
                if (!TextUtils.isEmpty(msg) && mListener != null)
                {
                    mListener.onMessage(null, new Ll_Message(msg));
                }
                // 客户端已经断开连接
                if (a == -1)
                {
                    isKeepListener = false;
                }
                // Ll_Loger.i(TAG, socket + " wait for message" + a);
            }
            isConnect = false;
            socket.close();
            Ll_Loger.d(TAG, socket + " close  -->" + socket);
            Ll_Loger.d(TAG, socket + "--> isClose() " + isClose());
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Socket is close
     * @return
     */
    public boolean isClose()
    {
        return socket.isClosed();
    }

    /**
     * 给服务端发送消息
     * @param msg
     */
    public void sendmsg(String msg)
    {
        Ll_Loger.d("", msg);
        Socket mSocket = this.socket;
        PrintWriter pout = null;
        try
        {
            pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
            pout.println(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        isKeepListener = false;
    }
    
    /**
     * 是否连接
     * @return
     */
    public boolean isConnect()
    {
        return isConnect;
    }

}
