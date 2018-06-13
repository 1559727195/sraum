package com.massky.sraum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by masskywcy on 2017-04-13.
 */
//用于tcp测试
public class TCPActivity extends Activity {
    private TextView txtReceiveInfo;
    private EditText edtRemoteIP, edtRemotePort, edtSendInfo, edtLocalPort, edtSeverSendInfo;
    private Button btnConnect, btnSend, btnListen, btnServerSend;
    private boolean isConnected = false, isListened = false;
    private Socket socketClient = null, socket = null;
    private ServerSocket socketServer = null;
    private String receiveInfoClient, receiveInfoServer;
    static BufferedReader bufferedReaderClient = null, bufferedReaderServer = null;
    static PrintWriter printWriterClient = null, printWriterServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcp);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnSend = (Button) findViewById(R.id.btnSend);
        txtReceiveInfo = (TextView) findViewById(R.id.txtReceiveInfo);
        edtRemoteIP = (EditText) findViewById(R.id.edtRemoteIP);
        edtRemotePort = (EditText) findViewById(R.id.edtRemotePort);
        edtSendInfo = (EditText) findViewById(R.id.edtSendInfo);
        btnListen = (Button) findViewById(R.id.btnListen);
        edtLocalPort = (EditText) findViewById(R.id.edtLocalPort);
        btnServerSend = (Button) findViewById(R.id.btnServerSend);
        edtSeverSendInfo = (EditText) findViewById(R.id.edtSeverSendInfo);
    }

    //连接按钮单击事件
    public void ConnectButtonClick(View source) {
        if (isConnected) {
            isConnected = false;
            if (socketClient != null) {
                try {
                    socketClient.close();
                    socketClient = null;
                    printWriterClient.close();
                    printWriterClient = null;
                } catch (IOException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
            new tcpClientThread().interrupt();
            btnConnect.setText("开始连接");
            edtRemoteIP.setEnabled(true);
            edtRemotePort.setEnabled(true);
            txtReceiveInfo.setText("ReceiveInfo:\n");
        } else {
            isConnected = true;
            btnConnect.setText("停止连接");
            edtRemoteIP.setEnabled(false);
            edtRemotePort.setEnabled(false);
            new tcpClientThread().start();
        }
    }

    //发送信息按钮单击事件
    public void SendButtonClick(View source) {
        if (isConnected && socketClient != null) {
            String sendInfo = edtSendInfo.getText().toString();//取得编辑框中我们输入的内容
            try {
                printWriterClient.print(sendInfo);//发送给服务器
                printWriterClient.flush();
                receiveInfoClient = "发送信息" + "\"" + sendInfo + "\"" + "\n";//消息换行
                Message msg = new Message();
                msg.what = 0x123;
                handler.sendMessage(msg);
            } catch (Exception e) {
                receiveInfoClient = e.getMessage() + "\n";
                Message msg = new Message();
                msg.what = 0x123;
                handler.sendMessage(msg);
            }
        }
    }

    //TCP客户端线程
    private class tcpClientThread extends Thread {
        public void run() {
            try {
                //连接服务器
                socketClient = new Socket(edtRemoteIP.getText().toString(), Integer.parseInt
                        (edtRemotePort.getText().toString()));
                //取得输入、输出流
                bufferedReaderClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream
                        ()));
                printWriterClient = new PrintWriter(socketClient.getOutputStream(), true);
                receiveInfoClient = "连接服务器成功!\n";
                Message msg = new Message();
                msg.what = 0x123;
                handler.sendMessage(msg);
            } catch (Exception e) {
                receiveInfoClient = e.getMessage() + "\n";
                Message msg = new Message();
                msg.what = 0x123;
                handler.sendMessage(msg);
                return;
            }
            char[] buffer = new char[256];
            int count = 0;
            while (isConnected) {
                try {
                    if ((count = bufferedReaderClient.read(buffer)) > 0) {
                        receiveInfoClient = "接收信息 " + "\"" + getInfoBuff(buffer, count) + "\"" + "\n";//消息换行
                        Message msg = new Message();
                        msg.what = 0x123;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    receiveInfoClient = e.getMessage() + "\n";
                    Message msg = new Message();
                    msg.what = 0x123;
                    handler.sendMessage(msg);
                    return;
                }
            }
        }
    }

    ;

    //TCP服务端线程
    private class tcpServerThread extends Thread {
        public void run() {
            try {
                socketServer = new ServerSocket(Integer.parseInt(edtLocalPort.getText().toString()));

                receiveInfoServer = "开始监听!" + GetLocalIP() + ":" + edtLocalPort.getText().toString() + "(本机地址端 口)" + "\n";
                Message msg = new Message();
                msg.what = 0x456;
                handler.sendMessage(msg);
            } catch (IOException e) {
                receiveInfoServer = e.getMessage() + "\n";
                Message msg = new Message();
                msg.what = 0x456;
                handler.sendMessage(msg);
            }
            while (isListened) {
                try {
                    socket = socketServer.accept();
                    new Thread(new ServerThread(socket)).start();
                    String clientIP = socket.getInetAddress().toString();
                    clientIP = clientIP.substring(clientIP.lastIndexOf("/") + 1);
                    receiveInfoServer = "连接客户端成功！" + clientIP + "(客户端地址)" + "\n";
                    Message msg = new Message();
                    msg.what = 0x456;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    receiveInfoServer = e.getMessage() + "\n";
                    Message msg = new Message();
                    msg.what = 0x456;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    ;

    private class ServerThread extends Thread {
        private Socket s = null;

        public ServerThread(Socket s) {
            this.s = s;
            try {
                bufferedReaderServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
                printWriterServer = new PrintWriter(s.getOutputStream(), true);

            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

        public void run() {
            char[] buffer = new char[256];
            int count = 0;
            while (isListened) {
                try {
                    if ((count = bufferedReaderServer.read(buffer)) > 0) {
                        receiveInfoServer = "接收信息" + "\"" + getInfoBuff(buffer, count) + "\"" + "\n";//消息换行
                        Message msg = new Message();
                        msg.what = 0x456;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    receiveInfoServer = e.getMessage() + "\n";
                    Message msg = new Message();
                    msg.what = 0x456;
                    handler.sendMessage(msg);
                    return;
                }
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                txtReceiveInfo.append("TCPClient: " + receiveInfoClient);    // 刷新
            }
            if (msg.what == 0x456) {
                txtReceiveInfo.append("TCPServer: " + receiveInfoServer);
            }
        }
    };

    //监听按钮单击事件
    public void ListenButtonClick(View source) {
        if (isListened) {
            isListened = false;
            if (socketServer != null) {
                try {
                    socketServer.close();
                    socketServer = null;
                    printWriterServer = null;
                } catch (IOException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
            new tcpServerThread().interrupt();
            btnListen.setText("开始监听");
            edtLocalPort.setEnabled(true);
            txtReceiveInfo.setText("ReceiveInfo:\n");
        } else {
            isListened = true;
            btnListen.setText("停止监听");
            edtLocalPort.setEnabled(false);
            new tcpServerThread().start();
        }
    }

    //server信息发送按钮单击事件
    public void ServerSendButtonClick(View source) {
        if (isListened && socketServer != null) {
            String serverSendInfo = edtSeverSendInfo.getText().toString();//取得编辑框中我们输入的内容
            try {
                printWriterServer.print(serverSendInfo);//发送给服务器
                printWriterServer.flush();
                receiveInfoServer = "发送信息" + "\"" + serverSendInfo + "\"" + "\n";//消息换行
                Message msg = new Message();
                msg.what = 0x456;
                handler.sendMessage(msg);
            } catch (Exception e) {
                receiveInfoClient = e.getMessage() + "\n";
                Message msg = new Message();
                msg.what = 0x456;
                handler.sendMessage(msg);
            }
        }
    }

    private String getInfoBuff(char[] buff, int count) {
        char[] temp = new char[count];
        for (int i = 0; i < count; i++) {
            temp[i] = buff[i];
        }
        return new String(temp);
    }

    //得到本地IP地址（WIFI）
    private String GetLocalIP() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (ipAddress == 0) return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tc, menu);
        return true;
    }
}

