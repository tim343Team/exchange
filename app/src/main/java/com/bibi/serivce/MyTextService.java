package com.bibi.serivce;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bibi.config.AppConfig;
import com.bibi.socket.HeartSocket;
import com.bibi.socket.SocketFactory;
import com.bibi.utils.WonderfulLogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.bibi.socket.ISocket;


public class MyTextService extends Service {

    private static final String TAG = "mysocket";
    private static final long sequenceId = 0;//以后用于token
    private static final int requestid = 0;//请求ID
    private static final int version = 1;
    private static final String terminal = "1001";  //安卓:1001,苹果:1002,WEB:1003,PC:1004
    private static final String ip = AppConfig.MARKET_URL;//行情
    private static final int port = 28901;//行情

    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Socket socket = null;
    private SocketThread socketThread;
    private HeartSocket heartSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(SocketMessage message) {
        if (message.getCode() == 0) { // 行情方面的推送
            getSocketOne(message);
        }
    }

    /**
     * 对行情方面推送的处理
     */
    private void getSocketOne(SocketMessage message) {
        if (socketThread != null && socketThread.isAlive()) {
            if (socket == null || !socket.isConnected()) {
                try {
                    socket = new Socket(ip, port);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    socket = null;
                }
            }

            toRequest(message.getCmd(), message.getBody());
        } else {
            socketThread = new SocketThread();
            socketThread.start();
        }
    }

    private void toRequest(ISocket.CMD cmd, byte[] body) {
        try {
            byte[] requestBytes = buildRequest(cmd, body);
            dos.write(requestBytes);
            dos.flush();
        } catch (Exception e) {
            //Log.d(TAG, "3");
            e.printStackTrace();
        }
    }

    public static byte[] buildRequest(ISocket.CMD cmd, byte[] body) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            int length = body == null ? 26 : (26 + body.length);
            dos.writeInt(length);
            dos.writeLong(sequenceId);
            dos.writeShort(cmd.getCode());
            dos.writeInt(version);
            byte[] terminalBytes = terminal.getBytes();
            dos.write(terminalBytes);
            dos.writeInt(requestid);
            if (body != null) dos.write(body);
            return bos.toByteArray();
        } catch (IOException ex) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WonderfulLogUtils.logi(TAG, "服务开启了 onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isOpen = true;
        if (socketThread == null || !socketThread.isAlive()) {
            WonderfulLogUtils.logi(TAG, "服务开启了");
            socketThread = new SocketThread();
            socketThread.start();
        }
        WonderfulLogUtils.logi(TAG, "服务开启onStartCommand");
        if (heartSocket == null) {
            heartSocket = (HeartSocket) SocketFactory.produceSocket(ISocket.HEART);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        WonderfulLogUtils.logi(TAG, "onDestroy 服务关闭了！！！！！！");
        if (socket != null) try {
            socket.close();
            dis.close();
            dos.close();
            socket = null;
            socketThread = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }

    private void dealResponse(DataInputStream dis) throws Exception {
        try {
            int length = dis.readInt();
            long sequenceId = dis.readLong();
            short code = dis.readShort();
            final int responseCode = dis.readInt();
            int requestId = dis.readInt();
            byte[] buffer = new byte[length - 22];
            final ISocket.CMD cmd = ISocket.CMD.findObjByCode(code);
//            WonderfulLogUtils.logi(TAG, code+"");

            int nIdx = 0;
            int nReadLen = 0;
            while (nIdx < buffer.length) {
                nReadLen = dis.read(buffer, nIdx, buffer.length - nIdx);
                if (nReadLen > 0) {
                    nIdx += nReadLen;
                } else {
                    break;
                }
            }
            String str = new String(buffer);
            if (responseCode == 200) {
                EventBus.getDefault().post(new SocketResponse(cmd, str));
            }
        } catch (Exception e) {
//            WonderfulLogUtils.logi(TAG, "行情socket dealResponse出错");
            if (socket != null) try {
                socket.close();
                dis.close();
                dos.close();
                socket = null;
                isOpen=false;
                socketThread = null;
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

    }

    private boolean isOpen;
    private static final String lock = "lock";

    class SocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                if (socket == null || socket.isConnected()) {
                    try {
                        socket = new Socket(ip, port);
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        isOpen = true;
                        WonderfulLogUtils.logi(TAG, "行情socket 连接成功：");
                    } catch (IOException e) {
                        isOpen = false;
                        socket = null;
                    }
                }
            }
            while (isOpen) {
                try {
                    dealResponse(dis);
                } catch (Exception e) {
                    WonderfulLogUtils.logi(TAG, "行情socket 连接出错");
                    socketThread = null;
                }
            }
        }
    }

}
