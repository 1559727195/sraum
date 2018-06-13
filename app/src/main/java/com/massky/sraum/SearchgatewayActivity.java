package com.massky.sraum;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.ByteUtils;
import com.adapter.ShowUdpServerAdapter;
import com.adapter.ShowUdpServerDetailAdapter;
import com.base.Basecactivity;
import com.j256.ormlite.stmt.query.In;
import com.mob.commons.iosbridge.UDPServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.InjectView;

import static android.R.attr.codes;
import static android.R.attr.data;
import static android.R.attr.fadingEdgeLength;
import static android.R.attr.label;
import static android.R.id.list;
import static cn.jpush.android.d.f;
import static com.Util.ByteUtils.init_crc_weight_udp;
import static com.Util.ByteUtils.testCrc;
import static com.massky.sraum.R.drawable.sh;
import static com.mob.tools.gui.BitmapProcessor.start;

/**
 * Created by masskywcy on 2016-09-12.
 */
//用于搜索网关界面
public class SearchgatewayActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;

    @InjectView(R.id.goimage_id)
    ImageView goimage_id;
    @InjectView(R.id.detailimage_id)
    ImageView detailimage_id;
    @InjectView(R.id.stopimage_id)
    ImageView stopimage_id;

    @InjectView(R.id.scanone)
    ImageView scanone;
    @InjectView(R.id.scantwo)
    ImageView scantwo;
    @InjectView(R.id.scanthree)
    ImageView scanthree;
    @InjectView(R.id.scanfour)
    ImageView scanfour;
    @InjectView(R.id.scanfive)
    ImageView scanfive;
    @InjectView(R.id.scansix)
    ImageView scansix;
    @InjectView(R.id.scanseven)
    ImageView scanseven;
    @InjectView(R.id.scaneight)
    ImageView scaneight;
    @InjectView(R.id.scannine)
    ImageView scannine;
    @InjectView(R.id.scanten)
    ImageView scanten;
    @InjectView(R.id.scaneleven)
    ImageView scaneleven;
    @InjectView(R.id.scantwelve)
    ImageView scantwelve;
    @InjectView(R.id.list_show_rev_item)
    ListView list_show_rev_item;
    @InjectView(R.id.search_txt)
    TextView search_txt;
    @InjectView(R.id.list_show_rev_item_detail)
    ListView list_show_rev_item_detail;
    @InjectView(R.id.rel_list_show)
    RelativeLayout rel_list_show;
    boolean isFirst = true;
    WThread t;
    private int n = 0;
    private boolean flag = true;
    private static final int MAX_DATA_PACKET_LENGTH = 1400;//报文长度
    private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
    private boolean serSocFlag;
    //添加一个boolean变量测量那个UDPServerSocket是否已经死掉，只有死掉的时候才能重新去发送UDPSocket
    private boolean UDPServerSocket_is_death;
    CopyOnWriteArrayList<String> list_rev_udp = new CopyOnWriteArrayList<String>();//存储来自udp服务器端的数据列表
    private ShowUdpServerAdapter showUdpServerAdapter;
    private List<String> list = new ArrayList<>();
    private int REQUEST_SCAN_WANGGUAN = 0x006;
    private final int REQUEST_SCAN_NO_WANGGUAN = 0x007;
    private int REQUEST_SUBMIT_WANGGUAN = 0x009;
    private boolean tiaozhuan_bool;//跳转判断
    private boolean istiaozhuan;

    @Override
    protected int viewId() {
        return R.layout.searchmac;
    }

    @Override
    protected void onView() {
        serSocFlag = true;
        startanim();
        search_txt.setText("正在搜索局域网");
        new BroadCastUdp("021001000021F403").start();
        showUdpServerAdapter = new ShowUdpServerAdapter(SearchgatewayActivity.this
                , list);

        /*
          这个是局部listview的监听事件
         */
        list_show_rev_item.setAdapter(showUdpServerAdapter);
        list_show_rev_item.setOnItemClickListener(new MyOnItemCLickListener());

        /**
         * 下面是全部Listview的监听事件
         */
        list_show_rev_item_detail.setAdapter(showUdpServerAdapter);
        list_show_rev_item_detail.setOnItemClickListener(new MyOnItemCLickListener());
    }

    private class MyOnItemCLickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //销毁activity返回到AddGateWayActivity
            Intent intent = getIntent();
            intent.putExtra("gateWayMac", list.get(position).toString());
            intent.putExtra("act_flag", "Search");//我是SearchgatewayActivity
            setResult(RESULT_OK, intent);
            SearchgatewayActivity.this.finish();
            serSocFlag = false;
            t.onStop();
        }
    }

    /*监听并开启动画展示*/
    private void startanim() {
        titlecen_id.setVisibility(View.GONE);
        backrela_id.setOnClickListener(this);
        clear();
        goimage_id.setOnClickListener(this);
        detailimage_id.setOnClickListener(this);
        stopimage_id.setOnClickListener(this);
        if (isFirst) {
            isFirst = false;
            t = new WThread();
            t.start();
        }
    }

    //清空所有展示
    private void clear() {
        scanone.setVisibility(View.GONE);
        scantwo.setVisibility(View.GONE);
        scanthree.setVisibility(View.GONE);
        scanfour.setVisibility(View.GONE);
        scanfive.setVisibility(View.GONE);
        scansix.setVisibility(View.GONE);
        scanseven.setVisibility(View.GONE);
        scaneight.setVisibility(View.GONE);
        scannine.setVisibility(View.GONE);
        scanten.setVisibility(View.GONE);
        scaneleven.setVisibility(View.GONE);
        scantwelve.setVisibility(View.GONE);
    }

    /*自定义线程类实现开启，暂停等操作*/
    public class WThread extends Thread {

        private final Object lock;
        boolean isPause;
        boolean isStop;
        int c;

        public WThread() {
            lock = new Object();
            isPause = false;
            isStop = false;
            c = 1;
        }

        //暂停
        public void onPause() {
            if (!isPause)
                isPause = true;
        }

        public void onWait() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onResume() {
            synchronized (lock) {
                isPause = false;
                lock.notifyAll();
            }
        }

        public void onStop() {
            //如果线程处于wait状态，那么会唤醒它，但该中断也就被消耗了，无法捕捉到,退出操作会在下一个循环时实现
            //但如果线程处于running状态，那么该中断便可以被捕捉到
            isStop = true;
            this.interrupt();
        }


        @Override
        public void run() {
            super.run();
            try {
                while (!isStop) {
                    //捕获中断
                    if (Thread.interrupted()) {
                        //结束中断
                        if (isStop) {
                            return;
                            //如果中断不是由我们手动发出，那么就不予处理，直接交由更上层处理
                            //不应该生吞活剥
                        } else {
                            this.interrupt();
                            continue;
                        }

                    }
                    n++;
                    if (n == 13) {
                        n = 0;
                    }
                    Log.e("robin debug", "n:" + n);
                    if (isPause) onWait();
                    //lock.wait();需要在run中才能起作用
                    //模拟耗时操作
                    Thread.sleep(200);
                    Message m = h.obtainMessage();
                    m.what = n;
                    h.sendMessage(m);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public class BroadCastUdp extends Thread {
        private String dataString;
        private DatagramSocket udpSocket;

        public BroadCastUdp(String dataString) {
            this.dataString = dataString;
        }

        public void run() {
            DatagramPacket dataPacket = null;

            try {
                udpSocket = new DatagramSocket(9991);

                dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
//				byte[] data = dataString.getBytes();
                byte[] data = ByteUtils.hexStringToBytes(dataString);//字符串转换为byte
                dataPacket.setData(data);
                dataPacket.setLength(data.length);
                dataPacket.setPort(9991);
                InetAddress broadcastAddr;
                broadcastAddr = InetAddress.getByName("255.255.255.255");
                dataPacket.setAddress(broadcastAddr);
            } catch (Exception e) {
                Log.e("robin debug", e.toString());
            }
            // while( start ){
            try {
                udpSocket.send(dataPacket);
                udpSocket.close();
                //客户端UDP发送之后就开始监听，服务器端UDP返回数据

                new ReceivBroadCastUdp().start();
//				sleep(10);
            } catch (Exception e) {
                Log.e("robin debug", e.toString());
            }
            // }
        }
    }

    //处理接收到的UDPServer的数据
    Handler handler_udp_recev = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String wanggguan_mac = (String) msg.obj;//021001001262a001ff1008313030385352677700000000643603
            //实时显示列表数据
            list_rev_udp.add(wanggguan_mac);
            //去除list集合中重复项的几种方法
            HashSet<String> hs = new HashSet<String>(list_rev_udp); //此时已经去掉重复的数据保存在hashset中
            Iterator<String> stringIterator = hs.iterator();
            list = new ArrayList<>();
            for (int i = 0; i < hs.size(); i++) {
                list.add(stringIterator.next());
            }
            showUdpServerAdapter.clear();
            showUdpServerAdapter.addAll(list);
        }
    };

    /**
     * 接收来自UDP服务器端发送过来,UDP 通信是应答模式，你发我收
     */
    public class ReceivBroadCastUdp extends Thread {
        private DatagramSocket udpSocket;
        DatagramPacket udpPacket = null;
        public int add;
        private Timer timer;
        private TimerTask task;
        private boolean UDPServer;

        @Override
        public void run() {
            //搞个定时器10s后,关闭该UDPReceverSocket
            UDPServer = true;
            UDPServerSocket_is_death = false;
            initTimer();
            byte[] data = new byte[256];
            try {
                udpSocket = new DatagramSocket(8881);//服务器端UDP端口号，网关端口9991
                udpPacket = new DatagramPacket(data, data.length);
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
            while (UDPServer) {
                try {
                    udpSocket.receive(udpPacket);
                } catch (Exception e) {

                }
                if (udpPacket != null) {
                    if (null != udpPacket.getAddress()) {
                        //02 1001 0012 62a001ff1008 313030385352677700000000643603
                        String codeString = ByteUtils.bytesToHexString(data, udpPacket.getLength());
                        if (codeString.length() > 22) {
                            String wanggguan_mac = codeString.substring(10, 22);//62a001ff1008
                            Log.e("zhu", "wanggguan_mac:" + wanggguan_mac);
                            Message message = new Message();
                            message.obj = wanggguan_mac;
                            handler_udp_recev.sendMessage(message);
                        }
                    }
                }
            }
            if (udpSocket != null)
                udpSocket.close();
        }


        /**
         * 初始化定时器
         */
        private void initTimer() {
            if (timer == null)
                timer = new Timer();
            //3min
            //关闭clientSocket即客户端socket
            if (task == null)
                task = new TimerTask() {

                    @Override
                    public void run() {
                        add++;
                        Log.e("robin debug", "add: " + add);
                        if (add > 400) {//10s= 1000 * 10
                            try {
                                add = 0;
                                closeTimerAndClientSocket();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (!serSocFlag) {//长连接倒计时3分钟未到，TcpServer退出，要停掉该定时器，并关闭clientSocket即客户端socket
                            try {
                                closeTimerAndClientSocket();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 关闭定时器和socket客户端
                     *
                     * @throws IOException
                     */
                    private void closeTimerAndClientSocket() throws IOException {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if (task != null) {
                            task.cancel();
                            task = null;
                        }
                        closeUDPServerSocket();//关闭clientSocket即客户端socket
                    }
                };
            timer.schedule(task, 100, 25); // 1s后执行task,经过1ms再次执行
        }

        /**
         * 关闭UDPServerSocket服务器端
         */
        private void closeUDPServerSocket() {
            UDPServer = false;
            UDPServerSocket_is_death = true;
            if (udpSocket != null)
                udpSocket.close();
            t.onPause();
            if (list.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!istiaozhuan) {
                            search_txt.setText("未搜索到局域网");
                            //没有搜到局域网时，就跳进没有搜到局域网的界面
                            Intent intent = new Intent(SearchgatewayActivity.this, GatedetailActivity.class);
                            startActivityForResult(intent, REQUEST_SCAN_NO_WANGGUAN);
                            serSocFlag = false;
                            t.onPause();
                        }
                    }
                });
            } else {
                list = new ArrayList<>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        search_txt.setText("");
                    }
                });
            }
        }
    }

    Handler h = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    clear();
                    scanone.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    clear();
                    scantwo.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    clear();
                    scanthree.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    clear();
                    scanfour.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    clear();
                    scanfive.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    clear();
                    scansix.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    clear();
                    scanseven.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    clear();
                    scaneight.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    clear();
                    scannine.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    clear();
                    scanten.setVisibility(View.VISIBLE);
                    break;
                case 11:
                    clear();
                    scaneleven.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    clear();
                    scantwelve.setVisibility(View.VISIBLE);
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goimage_id:
                t.onResume();
                serSocFlag = true;
                if (UDPServerSocket_is_death) {
                    list = new ArrayList<>();
                    showUdpServerAdapter.clear();
                    search_txt.setText("正在搜索局域网");
                    list_rev_udp = new CopyOnWriteArrayList<>();//只有搜索完毕后，才能清空
                    new BroadCastUdp("021001000021F403").start();//重新去搜
                }
                break;
            case R.id.detailimage_id:
                tiaozhuan_bool = true;//跳转标记
                list_show_rev_item_detail.setVisibility(View.VISIBLE);
                rel_list_show.setVisibility(View.GONE);
                titlecen_id.setVisibility(View.VISIBLE);
                titlecen_id.setText("网关列表");
                serSocFlag = false;
                t.onPause();
                break;
            case R.id.stopimage_id://停止搜索
                serSocFlag = false;
                t.onPause();
                break;
            case R.id.backrela_id:
                if (!tiaozhuan_bool) {
                    Intent intent = getIntent();
                    istiaozhuan = true;//表明正在跳转到其他界面
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    tiaozhuan_bool = false;
                    list_show_rev_item_detail.setVisibility(View.INVISIBLE);
                    rel_list_show.setVisibility(View.VISIBLE);
                    titlecen_id.setVisibility(View.GONE);
                    titlecen_id.setText("");
                    serSocFlag = true;
                    t.onResume();
                    search_txt.setText("正在搜索局域网");
                    list_rev_udp = new CopyOnWriteArrayList<>();//只有搜索完毕后，才能清空
                    new BroadCastUdp("021001000021F403").start();//重新去搜
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serSocFlag = false;
        t.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_SCAN_WANGGUAN && resultCode == RESULT_OK) {
            t.onResume();
            serSocFlag = true;
            if (UDPServerSocket_is_death) {
                list_rev_udp = new CopyOnWriteArrayList<>();//只有搜索完毕后，才能清空
                search_txt.setText("正在搜索局域网");
                new BroadCastUdp("021001000021F403").start();//重新去搜
            }
        } else if (requestCode == REQUEST_SUBMIT_WANGGUAN && resultCode == RESULT_OK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == REQUEST_SCAN_NO_WANGGUAN && resultCode == RESULT_OK) {
            search_txt.setText("正在搜索局域网");
            t.onResume();
            serSocFlag = true;
            list = new ArrayList<>();
            list_rev_udp = new CopyOnWriteArrayList<>();//只有搜索完毕后，才能清空
            showUdpServerAdapter.clear();
            new BroadCastUdp("021001000021F403").start();//重新去搜
            list_show_rev_item_detail.setVisibility(View.INVISIBLE);
            rel_list_show.setVisibility(View.VISIBLE);
            titlecen_id.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!tiaozhuan_bool) {
            istiaozhuan = true;//表明正在跳转到其他界面
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            tiaozhuan_bool = false;
            list_show_rev_item_detail.setVisibility(View.INVISIBLE);
            rel_list_show.setVisibility(View.VISIBLE);
            titlecen_id.setVisibility(View.GONE);
            titlecen_id.setText("");
            serSocFlag = true;
            t.onResume();
            list = new ArrayList<>();
            showUdpServerAdapter.clear();
            search_txt.setText("正在搜索局域网");
            list_rev_udp = new CopyOnWriteArrayList<>();//只有搜索完毕后，才能清空
            new BroadCastUdp("021001000021F403").start();//重新去搜
        }
    }
}
