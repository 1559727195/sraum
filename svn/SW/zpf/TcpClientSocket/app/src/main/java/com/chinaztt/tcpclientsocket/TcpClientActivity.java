package com.chinaztt.tcpclientsocket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.input;

/**
 * Created by zhu on 2017/4/17.
 */

public class TcpClientActivity extends AppCompatActivity implements View.OnClickListener{
    private TCPClient client;
    private Timer timer;
    private TimerTask task;
    private boolean isRevFromServer;
    private EditText txt_send;
    private EditText txt_show;
    private Button click_send;
    private Socket clicksSocket;
    private boolean activi_destroy;//activity有没有销毁
    private  DataInputStream dataInputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_client);
        initView ();
        initEvent ();
        new Thread(new Runnable() {
            @Override
            public void run() {
                activi_destroy = false;//说明activity被创建
                initSocket();
            }
        }).start();
    }

    private void initEvent() {
        click_send.setOnClickListener(this);
    }

    private void initView() {
        txt_send = (EditText) findViewById(R.id.txt_send);//发送的文本
        txt_show = (EditText) findViewById(R.id.txt_show);//显示接收的文本
        click_send = (Button) findViewById(R.id.click_send);//发送按钮
    }

    /**
     * socket初始化
     */
    private void initSocket() {
        clicksSocket = null;
        try {
//            clicksSocket= new Socket("192.168.169.134", 8080);
            clicksSocket = new Socket();
            SocketAddress socAddress = new InetSocketAddress("192.168.169.209", 8080);
            clicksSocket.connect(socAddress, 10000);//此处超时间为20s

            isRevFromServer = true;//说明已经连接到服务器端Socket,不需要在用心跳定时器
//            clicksSocket.setSoTimeout(5000);
            clicksSocket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信

            Receive_Thread receive_thread =  new Receive_Thread(clicksSocket);//创建客户端处理线程对象
            Thread t = new Thread(receive_thread);//创建客户端处理线程
            t.start();//启动线程
            //启动接收线程
        } catch (IOException e) {//连接超时异常，重新去连接30分钟e.printStackTrace();//如果服务器端没有连，客户端超过3min，就会抛出异常
            //开启心跳每个3min,去连接
            isRevFromServer = false;//连接服务器端超时，开启定时器
            isServerClose(clicksSocket);
        }
    }

    /**
     * 初始化定时器
     */
    private  int Add = 0;
    private void initTimer() {
        if (timer == null)
            timer = new Timer();
        //3min
        //关闭clientSocket即客户端socket
        if (task != null) {
            task.cancel();  //将原任务从队列中移除
            task = null;
            initTimeTask();
        } else {
            initTimeTask();//初始化TimeTask
        }
        timer.schedule(task, 100, 1); // 1s后执行task,经过1000ms再次执行
    }

    /**
     * 初始化TimeTask
     */
    private void initTimeTask() {
        task = new TimerTask() {
            @Override
            public void run() {

                if (isRevFromServer || activi_destroy) {//说明从服务器端接收正常了，关闭长连接定时器
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                } else {
                    // ConnectToServer();//从服务器端断开两分钟后，每两分钟主动去连接服务器端Server
                    initSocket();//tcp客户端主动连接tcpServer有20s的超时时间
                }
            }
        };
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    public  void isServerClose(Socket socket){
//            socket.sendUrgentData(0xFF)
            initTimer();//开启心跳
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_send:
                OutputStream outputStream = null;
                try {
                    outputStream = clicksSocket.getOutputStream();
                    outputStream.write(txt_send.getText().toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;//发送按钮
        }
    }

    /**
     *
     * 接收线程
     *
     */
    private  class Receive_Thread implements Runnable//实现Runnable
    {
        private  Socket clientSocket;
        private  InputStream inputStream;
       //客户端接收子线程标志位，它的生命周期随着客户端物理连接断开，或者3min心跳后，客户端
        private Boolean Recev_flag;

        //虽然连接，单3min之后依然没响应，要关掉该客户端子线程
        public Receive_Thread(final Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.Recev_flag = true;
            try {
                InputStream inputStream = clientSocket.getInputStream();
                this.inputStream = inputStream;
                DataInputStream input = new DataInputStream(inputStream);
               dataInputStream = input;
            }catch (Exception ex) {

            }
        }
        @Override
        public void run() {
            byte[] b = new byte[1024];//new byte[10000];
            while(Recev_flag && !activi_destroy)
            {
                int length = 0;
                try {
                    length = dataInputStream.read(b);
                    if (length == -1)  {//当服务器端断开时，input.read(b)就会返回 -1，//开启心跳包每个3min 去连接，
                        Recev_flag = false;//杀死该线程,然后
                        initSocket();
                    } else {
                        final String Msg = new String(b, 0, length, "UTF-8");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_show.setText(txt_show.getText() + Msg);
                            }
                        });
                        Log.v("data", Msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activi_destroy = true;//说明activity被销毁
        try {
            if (dataInputStream != null)//退出客户端socket时及时通知服务器端Socket，客户端socket已经断线
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
