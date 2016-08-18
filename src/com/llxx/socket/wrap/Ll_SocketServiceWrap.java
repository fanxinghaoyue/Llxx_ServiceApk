package com.llxx.socket.wrap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.llxx.socket.loger.Ll_Loger;

import android.util.Log;

/**
 * 
 * @author 李万隆
 * @qq 461051343
 * @date 2016年8月18日
 * @describe 封装Socket服务端
 */
public class Ll_SocketServiceWrap
{
    static final String TAG = "SocketServiceWrap";
    private static final int PORT = 8082;
    private List<Ll_ClientSocketWrap> mList = new ArrayList<Ll_ClientSocketWrap>();
    private ServerSocket server = null;
    private ExecutorService mExecutorService = null; //thread pool

    Ll_MessageListener mMessageListener;

    public Ll_SocketServiceWrap(Ll_MessageListener listener)
    {
        mMessageListener = listener;
    }

    /**
     * 获取端口
     * @return
     */
    public int getPort()
    {
        return PORT;
    }

    public void run()
    {
        try
        {
            server = new ServerSocket(PORT);
            mExecutorService = Executors.newCachedThreadPool(); //create a thread pool
            Log.e(TAG, "服务器已启动...");
            Socket client = null;
            while (true)
            {
                client = server.accept();
                //把客户端放入客户端集合中
                Ll_ClientSocketWrap wrap = new Ll_ClientSocketWrap(client, mMessageListener);
                mList.add(wrap);
                mExecutorService.execute(wrap); //start a new thread to handle the connection
                Ll_Loger.LogD(TAG, "client " + wrap + " connect to services");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 给所有的客户端发送消息
     * @param msg
     */
    public void sendMessage(String msg)
    {
        int num = mList.size();
        for (int index = 0; index < num; index++)
        {
            Ll_ClientSocketWrap mSocket = mList.get(index);
            PrintWriter pout = null;
            try
            {
                pout = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(mSocket.getSocket().getOutputStream())), true);
                pout.println(msg);
                Ll_Loger.LogD(TAG, "sendMessage" + msg);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}