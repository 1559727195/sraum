package com.massky.sraumzigbee;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.massky.sraumzigbee.adapter.ListBaseAdapter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android_serialport_api.SerialPort;
import static java.lang.Integer.parseInt;

/**
 * Created by zhu on 2017/3/2.
 */

public class SerialPortActivity extends AppCompatActivity implements View.OnClickListener{

    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private TextView text;
    private String prot = "ttySAC3";//ttyS0默认    ttySAC3对应着端口com2 ,那哪个节点ttySAC2对应着端口com3呢
    private int baudrate = 115200;//9600
    private static int i = 0;
    private EditText et_prot;
    private EditText et_num;
    private Button btn_set;
    private Button btn_open;
    private Button btn_receive;
    private Button btn_send;
    private StringBuilder stringBuffer = new StringBuilder();
    private StringBuffer stringContent = new StringBuffer();
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                String stringContent1 = new String();
                stringContent1 = stringBuffer.toString();
                stringBuffer.setLength(0);
                if (!stringContent1.isEmpty()) {
                    Log.e("zhu", "stringbuffer:" + stringContent1);
//                    stringContent.append("," + stringContent1);
                }
            }
        }
    };

    private int TIME = 1;//1ms ,定时器
//
//    /*
//    * 1ms 定时器
//    * */
//    Handler handler_time = new Handler();
//    private int flagAdd;//每包发送完整数据，时间肯定是小于20ms
//    Runnable runnable_time = new Runnable() {
//
//        @Override
//        public void run() {
//            // handler自带方法实现定时器
//            try {
//                if (!closeSerial) {
//                    handler_time.postDelayed(this, TIME);//每隔1ms执行一次
//                    flagAdd ++ ;
//                    if (flagAdd > 500) {
//                        flagAdd = 0;
//                    }
//                    if (flagAdd >= 20 && stringBuffer.length() != 0) {//一包执行下来5ms,重置 ，5 + 20 = 25ms ,下个还有 50 - 25 = 25ms，才来，
//                        handler.sendEmptyMessage(1);
//                    }
//                } else {
//                     handler_time.removeCallbacks(runnable_time);//如果关闭的话，就停掉该定时器
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("exception...");
//            }
//        }
//    };
    private Timer timer;
    private TimerTask task;
    private int add;
    private int count = 0;
    private EditText et_send_value1;
    private byte[] data;
    private Button add_dev1;//[0xD1=网关读取ZigBee模块的信息]
    private Button device_write1;//
    private Map map_set_zigbee;
    private Button btn_reset_zigbee1;
    private Map write_zigbee_back_map;
    private Button btn_test;
    private Button accend_normal_model;

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
                    if (!closeSerial) {
                        add++;
                        if (add > 50000 * 30) {//包之间的间隔为50ms时，最好
                            add = 0;
                        }

                        if (add > 10 && stringBuffer.length() != 0)   {//3min= 1000 * 60 * 3
//                            add = 0;
                            String stringContent1 = new String();
                            stringContent1 = stringBuffer.toString();//设备在我这边 它的MAC是HEX 61A204002007
                            stringBuffer.setLength(0);
                            if (!stringContent1.isEmpty()) {
                                //在这里编写ZigBee模块与面板设备协议格式(HEX)
                                //STX ADDR CMD  LEN  DATA0~DATAn CRC ETX
                                //字节总长度为 1+ 1 + 2 +  DATA0~DATAn这个设备面板发送给网管的数据可以为null,
                                //然后接下来的一个字节为CRC-》STX~DATAn的CRC-CCITT - 16 -False (0x7101)
                                //ETX 03

                                //首先拿到前4个字符 ，如果为abcd 则为网关设置，如果为0201则为ZigBee与面板加退模式
                                String first_select = stringContent1.substring(0,4);
                                switch (first_select){
                                    case "abbc":
                                        init_set_zigbee(stringContent1);

                                        break;//abcd 则为网关设置
                                    case "0201":
                                        init_add_delete_second(stringContent1);
                                        break;//ZigBee与面板加退模式
                                }

                                Log.e("zhu", "stringbuffer:" + stringContent1);
                            }
                        }
                    } else {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if (task != null) {
                            task.cancel();
                            task = null;
                        }
                    }
                }

                /**
                 * 初始化网关zigbee设置
                 * @param stringContent1
                 */
                private void init_set_zigbee(String stringContent1) {//
                    //模块返回：
                    //拿到命令CMD ,
                    String CMD = stringContent1.substring(6,8);//CMD
                    int length = stringContent1.length();
                    String HEAD = stringContent1.substring(0,6);//HEAD
                    switch (CMD) {
                        case "d1":
                            init_read_zigbee_by_set(stringContent1, CMD, length, HEAD);
                            break;//0xD1=网关读取ZigBee模块的信息--模块返回:
                        case "d6":
                            write_back_zigbee_by_set (stringContent1, CMD, length, HEAD);
                            break;//[0xD6=网关写入ZigBee模块的信息] --模块返回:
                        case "d9":

                            break;//[0xD9=网关复位ZigBee模块]--模块返回:-》 在去0xD1=网关读取ZigBee模块的信息
                    }
                }

                /*
                * 网关写入ZigBee模块返回--[0xD6=网关写入ZigBee模块的信息]
                * */
                private void write_back_zigbee_by_set(String stringContent1, String CMD, int length, String HEAD) {
                    String short_address = stringContent1.substring(8,12);//设备网络短地址
                    String device_status = stringContent1.substring(length - 2,length);//设备状态
                write_zigbee_back_map = new HashMap();
                    write_zigbee_back_map.put("HEAD",HEAD);
                    write_zigbee_back_map.put("CMD",CMD);
                    write_zigbee_back_map.put("short_address",short_address);
                    write_zigbee_back_map.put("device_status",device_status);
            }

                /**
                 * [0xD1=网关读取ZigBee模块的信息] - 模块返回
                 * @param stringContent1
                 * @param CMD
                 * @param length
                 * @param HEAD
                 */
                private void init_read_zigbee_by_set(String stringContent1, String CMD, int length, String HEAD) {
                    String DeviceName = stringContent1.substring(8,40);//设备名称
                    String DevicePassword = stringContent1.substring(40,72);//设备密码
                    String routing = stringContent1.substring(72,74);//路由
                    String channel = stringContent1.substring(74,76);//信道
                    String PanID = stringContent1.substring(76,80);//PanID
                    String short_address = stringContent1.substring(80,84);//设备网络短地址
//                    String model_mac = stringContent1.substring(52,68);//模块MAC地址
//                    String target_address = stringContent1.substring(68,72);//目标网络短地址
//                    String target_mac = stringContent1.substring(52,68);//模块MAC地址
//                    String model_mac = stringContent1.substring(52,68);//模块MAC地址
                    //Other-字段信息
                    String others = stringContent1.substring(84,length - 10);
                    /**
                     * 将zigbee关键设备信息储存起来，方便写[0xD6=网关写入ZigBee模块的信息]
                     */
                    map_set_zigbee = new HashMap();
                    map_set_zigbee.put("HEAD",HEAD);
                    map_set_zigbee.put("CMD",CMD);
                    map_set_zigbee.put("DeviceName",DeviceName);
                    map_set_zigbee.put("DevicePassword",DevicePassword);
                    map_set_zigbee.put("routing",routing);
                    map_set_zigbee.put("channel",channel);
                    map_set_zigbee.put("PanID",PanID);
                    map_set_zigbee.put("short_address",short_address);
                    map_set_zigbee.put("others",others);
                }

                /**
                 * 处理网关与面板加退时的详细逻辑代码
                 * @param stringContent1
                 */
                private void init_add_delete_second(String stringContent1) {
                    //判断第三个字节

                    //分割字符串拿到第三两字节
                    String index_cmd=   stringContent1.substring(4,8);//A802  命令

                    //首先先进行CRC校验  --- STX~DATAn的CRC-CCITT - 16 -False (0x7101)

                    int length = stringContent1.length();
                    String CRC= stringContent1.substring(length - 6,length -2).trim();//拿到CRC值
                    //拿到STX
                    final String STX = stringContent1.substring(0,2);//拿到STX
                    final String ADDR = stringContent1.substring(2,4);//ADDR

                    final String ETX = stringContent1.substring(length - 2,length);//ETX

                    String CRC_privew = stringContent1.substring(0,length - 6);//STX~DATAn的CRC-CCITT - 16 -False (0x7101)

                    //然后开始校验CRC_priew ，并把两字节校验值和CRC比较，一样的话继续下去，不一样则说明接收数据错误
                    init_crc_weight (CRC_privew);//初始化CRC权值  --打开时获取输入的CRC -校验值
                    String checksum_CRC = testCrc();//开始校验CRC
//                    if (checksum_CRC.length() < 4) {
//                        checksum_CRC = "0" + checksum_CRC;
//                    }
                    switch (checksum_CRC.length()) {//保证CRC检验码为2个字节
                        case 1:
                            checksum_CRC = "000" + checksum_CRC;
                            break;
                        case 2:
                            checksum_CRC = "00" + checksum_CRC;
                            break;
                        case 3:
                            checksum_CRC = "0" + checksum_CRC;
                            break;
                        case 4:

                            break;
                    }

//                                String stringContent = "983030381FFD247C91983030381FFD123456";//16进制字符串
//
//                                String TYPE1 = stringContent.substring(34,36);//6Bytes面板MAC


                    //判断CRC和校验后的CRC相等不
                    if (CRC.equals(checksum_CRC.trim())) {

                        switch (index_cmd){
                            case "a802" :
                                //9开始到length - 6结束
                                initAdd_Del(stringContent1, length, STX, ADDR, ETX,"A802");

                                break; //如果为A802加网管时设备发送给网管的信息内存
                            case "a803":

                                initAdd_Del(stringContent1, length, STX, ADDR, ETX,"A803");
                                break;  //如果为A803退网管时设备发送给网管的信息内存

                            case "A805"://32 Bytes DATA -即32字节数据包,数据长度为64
                                String Data = stringContent1.substring(8,length -6).trim();//拿到Data字符串值

                                if (Data.length() == 64) { //18个字节
//                                                String stringContent = "983030381FFD247C91";//16进制字符串

                                    final String MACs = Data.substring(0, 12);//6Bytes面板MAC
                                    final String MACr = Data.substring(12, 24);//6Bytes网关MAC(面板未加入任何网关时MAC值采用610000000000)
                                    final String TYPE = Data.substring(24, 28);//2Bytes 产品类型
                                    String HW = Data.substring(28, 30);
                                    ;//1Byte 硬件版本
                                    String BW = Data.substring(30, 32);
                                    ;//1Byte BOOT版本
                                    String FW = Data.substring(32, 34);
                                    ;//1Byte 固件版本
                                    String ZW = Data.substring(34, 36);
                                    ;//1Byte ZigBee版本
                                }
                                break;//设备主动向网关发送状态
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SerialPortActivity.this,"CRC校验失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                /**
                 * 初始化网关返回给面板--设备加退 模式
                 * @param stringContent1
                 * @param length
                 * @param STX
                 * @param ADDR
                 * @param ETX
                 * @param cmd
                 */
                private void initAdd_Del(String stringContent1, int length, final String STX, final String ADDR, final String ETX
                , final String cmd) {
                    String Data = stringContent1.substring(10,length -6).trim();//拿到Data字符串值
                    Log.e("robin debug","A802面板发送给网关的:" + stringContent1);
                    if (Data.length() == 36) { //18个字节
//                                                String stringContent = "983030381FFD247C91";//16进制字符串
                        //把长度拼接到mAcs里去了
                        final String MACs = Data.substring(0,12);//6Bytes面板MAC
                        final String MACr = Data.substring(12,24);//6Bytes网关MAC(面板未加入任何网关时MAC值采用610000000000)
                        final String TYPE = Data.substring(24,28);//2Bytes 产品类型
                        String HW = Data.substring(28,30);;//1Byte 硬件版本
                        String BW = Data.substring(30,32);;//1Byte BOOT版本
                        String FW = Data.substring(32,34);;//1Byte 固件版本
                        String ZW = Data.substring(34,36);;//1Byte ZigBee版本

                        switch (cmd) {
                            case "A802" ://网关返回给面板:A802 设备加网关

                                a802_add_a803_delete_wang_guan_back(STX, ADDR, ETX, cmd, MACs);

                                break;
                            case "A803" ://网关返回给面板:A803 设备减网关
                                a802_add_a803_delete_wang_guan_back(STX, ADDR, ETX, cmd, MACs);
                                break;
                        }

//
//                        //在这里是网关立刻返回给面板
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                final StringBuffer stringBuffer = new StringBuffer();
//                                stringBuffer.append(STX.trim());
//                                stringBuffer.append(ADDR.trim());
//                                stringBuffer.append(cmd);//命令
//                                //添加data长度
//                                stringBuffer.append("12");//长度12-对应10进制为18个字节
//                                //添加data
//                                stringBuffer.append(MACs.trim());
//                                stringBuffer.append(MACr.trim());
//                                stringBuffer.append(TYPE.trim());
//                                stringBuffer.append("19");//信道
//                                stringBuffer.append("0678");//加退模式时用PANID=0x0678
//                                stringBuffer.append("01");//同意
//                                //然后进行CRC校验的到CRC
//                                init_crc_weight (stringBuffer.toString().trim().toUpperCase());//初始化CRC权值  --打开时获取输入的CRC -校验值
//                                String checksum_CRC = testCrc();//开始校验CRC
//                                stringBuffer.append(checksum_CRC.trim().toUpperCase());//添加CRC
//                                stringBuffer.append(ETX);//添加ETX
//                                //网关返回给面板
//
//                                sendThread = new Thread() {
//                                    @Override
//                                    public void run() {
//                                        try {
////
//                                            byte[] bytes = StringtoBytes(stringBuffer.toString().toUpperCase().trim());
//                                            mOutputStream.write(bytes);
//                                            Log.i("test", "发送成功:1" + i);
//                                        } catch (Exception e) {
//                                            Log.i("test", "发送失败");
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                };
//                                sendThread.start();
//                            }
//                        });
                    }
                }

                /**
                 * A802加网关返回给面板: A803减网关返回给面板:
                 * @param STX
                 * @param ADDR
                 * @param ETX
                 * @param cmd
                 * @param MACs
                 */
                private void a802_add_a803_delete_wang_guan_back(String STX, String ADDR, String ETX, String cmd, String MACs) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(STX);
                    stringBuffer.append(ADDR);
                    stringBuffer.append(cmd);
                    /**
                     * 接下来添加Data
                     */
                    //Data 的长度
                    stringBuffer.append("12");//添加Data长度
                    stringBuffer.append(MACs);//6Bytes面板MAC
                    stringBuffer.append("61A001FF0004");//6Bytes网关MAC(面板FLASH中断电保存) 61A001FF0004
                    stringBuffer.append("A001");//2Bytes 产品类型 A001
                    stringBuffer.append("14");//0B~19，即11~25信道，默认20信道 0x14
                    stringBuffer.append("0004");//PanID，1001~EFFF(面板FLASH中断电保存) 2个字节  0004
                    stringBuffer.append("01");//Agr， 01同意，00拒绝

                    String CRC_privew = stringBuffer.toString().toUpperCase().trim();
                    //STX~DATAn的CRC-CCITT - 16 -False (0x7101)
                    //然后开始校验CRC_priew ，并把两字节校验值和CRC比较，一样的话继续下去，不一样则说明接收数据错误
                    init_crc_weight (CRC_privew);//初始化CRC权值  --打开时获取输入的CRC -校验值
                    String checksum_CRC = testCrc();//开始校验CRC

//                    if (checksum_CRC.length() < 4) {//009D ->校验码必须为2个字节
//                        checksum_CRC = "0" + checksum_CRC;
//                    }


                    switch (checksum_CRC.length()) {//保证CRC检验码为2个字节
                        case 1:
                            checksum_CRC = "000" + checksum_CRC;
                            break;
                        case 2:
                            checksum_CRC = "00" + checksum_CRC;
                            break;
                        case 3:
                            checksum_CRC = "0" + checksum_CRC;
                            break;
                        case 4:

                            break;
                    }


                    stringBuffer.append(checksum_CRC);
                    stringBuffer.append(ETX);

                    //发送
                    String write_zigbee_add_net = stringBuffer.toString().trim().toUpperCase();
                   Log.e("robin debug","A802网关发送给面板的:" + write_zigbee_add_net);
                    //[0xD6=网关写入ZigBee模块的信息]
                    byte[] bytes = StringtoBytes(write_zigbee_add_net);
                    try {
                        mOutputStream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        timer.schedule(task, 100, 1); // 1s后执行task,经过1ms再次执行
    }

    private Thread receiveThread;
    private Thread sendThread;
    private EditText et_send1;
    private Button btn_clear1;
    private List<String> list;
    private ListView list_view;
    private ListBaseAdapter listBaseAdapter;
    private boolean closeSerial;// guan bi chuan kou biao zhi
    private TextView txt_show;
    private  int crc;//初始化crc值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_port_lay);
        initView ();
        initEvent ();
        initData ();
    }

    /**
     * 初始化CRC权值
     * @param CRC_privew
     */
    private void init_crc_weight(String CRC_privew) {
        String crc_s = TextUtils.isEmpty(et_send_value1.getText().toString().trim()) ? "7101" : et_send_value1.getText().toString().trim();
        //将字符串的CRC校验权值转化为int 型 ---0x7101
//        int a = 28929;//要转换的数!
//        int l=(a&0x00ff)<<8;
//        int h=(a&0xff00)>>8;
//        a=h|l;

        crc =   Integer.parseInt(crc_s, 16);//java标签（label）求16进制字符串的整数和 把一个整数转为4个16进制字符表示
//        crc =   Integer.parseInt("7101", 16);
     String stringContent1 = "983030381FFD247C91";//16进制字符串


//        int length = stringContent1.length();
//        String CRC= stringContent1.substring(length - 6,length -2).trim();//拿到CRC值
//        //拿到STX
//        String STX = stringContent1.substring(0,2);//拿到STX
//        String ADDR = stringContent1.substring(2,4);//ADDR
//
//        String  ETX = stringContent1.substring(length - 2,length);//ETX
        data = hexStringToBytes (CRC_privew);
    }


    private String testCrc() {//添加修改权值的控件

        //AA 0C 01 00 01 00 00 04 05 17 05 01 A0 86 01 00
        //结果为：F2E3
        return CrcUtil.setParamCRC(data,crc);
//        if (CrcUtil.isPassCRC(crcData, 2,crc)) {
//            System.out.println("验证通过");
//        } else {
//            System.out.println("验证失败");
//        }
    }

    /**
     * Convert hex string to byte[] --实现将"0x98"-转换为byte(0x98)即  即16进制字符串转换为byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public  byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private void initData() {
        listBaseAdapter = new ListBaseAdapter(SerialPortActivity.this,new ArrayList<String>());
        list_view.setAdapter(listBaseAdapter);
//        handler_time.postDelayed(runnable_time,TIME); //搞个1ms的定时器 ，一行代码执行只需1us
        initTimer();//初始化定时器
    }

    private void initEvent() {
        accend_normal_model.setOnClickListener(this);//退出加退模式之后，进入正常模式
        btn_test.setOnClickListener(this);
        //[0xD1=网关读取ZigBee模块的信息]
        add_dev1.setOnClickListener(this);
        device_write1.setOnClickListener(this);//[0xD6=网关写入ZigBee模块的信息]
        btn_reset_zigbee1.setOnClickListener(this);//[0xD9=网关复位ZigBee模块]
        btn_clear1.setOnClickListener(this);
        btn_open.setOnClickListener(new View.OnClickListener() {//打开，里面包含设置

            @Override
            public void onClick(View v) {

//                init_crc_weight ();//初始化CRC权值  --打开时获取输入的CRC -校验值
//
//                testCrc();

                closeSerial = false;// open serialPort cheng gong
                //端口和波特率设置
                prot = TextUtils.isEmpty(et_prot.getText().toString().trim()) ? "2"
                        : et_prot.getText().toString().trim();
                baudrate = parseInt(TextUtils.isEmpty(et_num.getText()
                        .toString().trim()) ? "115200" : et_num.getText()
                        .toString().trim());
                switch (prot) {
                    case  "2" :
                        prot = "ttySAC3";
                        break;//com2口
                    case  "3" :
                        prot = "ttySAC2";
                        break;//com3口
                }
                // 打开
                try {
                    mSerialPort = new SerialPort(new File("/dev/" + "ttySAC3"), 115200,
                            0);
                    mInputStream = mSerialPort.getInputStream();
                    mOutputStream = mSerialPort.getOutputStream();
                    list = new ArrayList<String>();//300ms 后清空,大于等于300ms，发送比较好 ,即PC端两次发送间隔在300ms以上为好，两次数据好分割
                    receiveThread();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("test", "打开失败");
                    e.printStackTrace();
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {//往PC端发送

            @Override
            public void onClick(View v) {
                // 发送
                sendThread = new Thread() {
                    @Override
                    public void run() {
                        try {
//
                           byte[] bytes = StringtoBytes(TextUtils.isEmpty(et_send1.getText().toString().trim().toUpperCase()) ?
                                    "123" : et_send1.getText().toString().trim());
                            mOutputStream.write(bytes);
                            Log.i("test", "发送成功:1" + i);
                        } catch (Exception e) {
                            Log.i("test", "发送失败");
                            e.printStackTrace();
                        }
                    }
                };
                sendThread.start();
            }
        });


        btn_receive.setOnClickListener(new View.OnClickListener() {//接收
            @Override
            public void onClick(View v) {
                closeSerialPort();
                list = new ArrayList<String>();//300ms 后清空,大于等于300ms，发送比较好 ,即PC端两次发送间隔在300ms以上为好，两次数据好分割
            }
        });
    }

    private void initView() {
//        提取局部变量：Ctrl+Alt+V
//
//        提取全局变量：Ctrl+Alt+F
//
//        提取方法：Shit+Alt+M
        list = new ArrayList<>();
        et_prot = (EditText) findViewById(R.id.et_prot);
        et_num = (EditText) findViewById(R.id.et_num);
        btn_set = (Button) findViewById(R.id.btn_set);//设置
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_receive = (Button) findViewById(R.id.btn_receive);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_send1 = (EditText) findViewById(R.id.et_send);//发送的数据输入框
        btn_clear1 = (Button) findViewById(R.id.btn_clear);//清空
        list_view = (ListView) findViewById(R.id.list_view);
        txt_show = (TextView) findViewById(R.id.txt_show);

        et_send_value1 = (EditText) findViewById(R.id.et_send_value);//设置权值

        add_dev1 = (Button) findViewById(R.id.add_dev);//[0xD1=网关读取ZigBee模块的信息]

        device_write1 = (Button) findViewById(R.id.device_write);//[0xD6=网关写入ZigBee模块的信息]

        btn_reset_zigbee1 = (Button) findViewById(R.id.btn_reset_zigbee);//[0xD9=网关复位ZigBee模块]
        btn_test = (Button) findViewById(R.id.btn_test);//测试A802网关发送给设备信心

        accend_normal_model = (Button) findViewById(R.id.accend_normal_model);//
        //退出加退模式之后，进入正常模式

    }

    private void receiveThread() {

        // 接收
        receiveThread = new Thread() {
            @Override
            public void run() {//延时50ms，每整条之间的时间间距为50ms
                while (!closeSerial) {
                    int size;
                    byte[] buffer = new byte[1024];//内存泄漏GC_CONCURRENT，当分配的对象大小超过384K时触发
                    try {
                        if (mInputStream == null)
                            return;
//                        size = mInputStream.read (buffer);
                        size = mInputStream.read(buffer,0,1024);//这里有一个bug，OS机通过串口发送数据过来，卡信息数据大小为205个字节。但是分成100+105  100+50+100等分段情况发过来
//                        flagAdd = 0;
//                        count += size;
//                        Log.e("robin","count:" + count);
                        add = 0;
                        //串口分段时，数据容易错位，之前的是 size = mInputStream.read (buffer);
                        //改成size = mInputStream.read (buffer,0,1024);之后数据没有错位啦
                        if (size > 0) {
//                                String recinfo = new String(buffer,0,size);
                            String recinfo = bytesToHexString(buffer,size);//将byte转换为16进制字符串
                            Log.i("test", "接收到串口信息:" + recinfo);
                            stringBuffer.append(recinfo);
                        }
                     } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }

    /**
     * byte转换为16进制字符串
     */
    public static String bytesToHexString(byte[] src, int size){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        closeSerial = true;//guan bi cheng gong
    }

    /**
     * 将16进制字符串转换为16进制byte
     * @param data
     * @return
     */
    public byte[] StringtoBytes(String data){
        if(data == null || data.equals("")){
            return null;
        }
        data = data.toUpperCase();
//		char[] datachar = data.tochararray();
        char[] datachar = data.toCharArray();
        byte[] getbytes = new byte[data.length() / 2];
        for( int i = 0; i < data.length() / 2 ; i++){
            int pos = i * 2;
            getbytes[i] = (byte) (charToByte(datachar[pos]) << 4 | charToByte(datachar[pos + 1]));
        }
        return getbytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear://清空
//                listBaseAdapter.setListEmpty ();//添加某一项，进行显示
//                listBaseAdapter.notifyDataSetChanged();
            txt_show.setText("");
            break;

            case R.id.add_dev :

                read_zigbee_info();

                break;//[0xD1=网关读取ZigBee模块的信息]

            case R.id.device_write:
                write_zigbee_info ("add_del");
                break;//[0xD6=网关写入ZigBee模块的信息]

            case R.id.btn_reset_zigbee:
                reset_zigbee_info ();
                break;//[0xD9=网关复位ZigBee模块]

            case R.id.btn_test :
                //[0xD6=网关写入ZigBee模块的信息]
                byte[] bytes = StringtoBytes("0201A8021261A20400200761A001FF0004A001140004010EC103");
                try {
                    mOutputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;//测试网关发送给设备写死数据（A802）

            case R.id.accend_normal_model:
             /*   退出加退模式之后，进入正常模式
                0xD6  设备短地址 是读取来的设备短地址(0678),不写死

                信道改成14  ，PanID 和网络短地址 61A00100  0004

                目标网络地址: 0FFF（无效）

                重发次数和重发时间为0

                格式变成单播 00

                0xD9 =网关复位ZigBee模块   2001变成实际的ShortAddress即(0678)不写死
*/
                write_zigbee_info ("normal");//正常模式下0xD6
                //0xD9= 网关复位zigBee模块 ，2001变成实际的ShortAddress即(0678)不写死
//                reset_zigbee_info();
                break;
            //退出加退模式之后，进入正常模式
        }
    }

    /***
     * zigbee fu wei [0xD9=网关复位ZigBee模块]
     */
    private void reset_zigbee_info() {
       String HEAD = (String) write_zigbee_back_map.get("HEAD");
        String CMD = (String) write_zigbee_back_map.get("CMD");
        String short_address = (String) write_zigbee_back_map.get("short_address");
//        String HEAD = (String) write_zigbee_back_map.get("HEAD"); //00 01//设备类型
        //SUM 校验 (HEAD+CMD+DATA)&0xFF
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(HEAD);
        stringBuffer.append("D9");
        stringBuffer.append(short_address);
        stringBuffer.append("0001");//设备类型

        long sum = 0;
        for (int i = 0; i < stringBuffer.length(); i+=2) {
            String set_zig_item = stringBuffer.substring(i,i+2);
            sum += Long.parseLong(set_zig_item, 16);
        }

        String SUM =  Long.toHexString(sum & 0xff);
        if (SUM.length() == 1) {
            SUM = "0" + SUM;
        }

        stringBuffer.append(SUM);//ABBCCDD105

        String write_zigbee = stringBuffer.toString().trim().toUpperCase();
        Log.e("robin debug","0xD9复位模式:" + write_zigbee);
        //[0xD6=网关写入ZigBee模块的信息]
        byte[] bytes = StringtoBytes(write_zigbee);
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写zigbee信息 --[0xD6=网关写入ZigBee模块的信息]
     */
    private void write_zigbee_info(String type) {
       String HEAD = (String) map_set_zigbee.get("HEAD");//map_set_zigbee
       String CMD = (String) map_set_zigbee.get("CMD");
       String DeviceName = (String) map_set_zigbee.get("DeviceName");
       String DevicePassword = (String) map_set_zigbee.get("DevicePassword");
       String routing = (String) map_set_zigbee.get("routing");
       String channel = (String) map_set_zigbee.get("channel");
       String PanID = (String) map_set_zigbee.get("PanID");
       String short_address = (String) map_set_zigbee.get("short_address");
       String others = (String) map_set_zigbee.get("others");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(HEAD.toUpperCase());
        stringBuffer.append("D6");
        stringBuffer.append(short_address);//设备短地址--这个不变
        stringBuffer.append(DeviceName.toUpperCase());
        stringBuffer.append(DevicePassword.toUpperCase());
        stringBuffer.append("01");
        switch (type) {
            case "add_del" :
                stringBuffer.append("19");//这个Channel信道容易被写死 -加退模式下的信道为19
                stringBuffer.append("0678");//PanID
                stringBuffer.append("0678");//网络地址
                break;//加退模式
            case "normal" :
                stringBuffer.append("14");//正常模式下信道为14
                stringBuffer.append("0004");//PanID
                stringBuffer.append("0004");//网络地址
                break;//正常模式
        }

       String other1 = others.substring(0,16);
        String PanIDs = others.substring(16,20);  //2002 //0678--PanID（可变的）
        String other2 = others.substring(20);  //2002 //0678--PanID
        //格式为广播
        String other3 = other2.substring(0,other2.length() - 2);
        stringBuffer.append(other1);

        switch (type) {
            case "add_del" :
                stringBuffer.append("FFFF");//2002 //0678--（可变的）目标网络地址
                stringBuffer.append(other3);
                stringBuffer.append("01");//格式广播-广播模式
                break;//加退模式
            case "normal" :
                stringBuffer.append("0FFF");//2002 //0678--（可变的）目标网络地址（无效）
//                stringBuffer.append(other3);
                String other4 = other3.substring(0,20);
                String send_again_count = other3.substring(20,22);//重发次数
                String send_again_time = other3.substring(22,24);//重发时间
                String other5 = other3.substring(24);
                stringBuffer.append(other4);
                stringBuffer.append("00");//重发次数
                stringBuffer.append("00");//重发时间
                stringBuffer.append(other5);
                stringBuffer.append("00");//格式广播-单播模式
                break;//正常模式
        }

        stringBuffer.toString().toUpperCase();
//        stringBuffer.append(others.toUpperCase());//others
        //SUM	(HEAD+CMD+DATA)&0xFF ----SUM 校验 ,两两个字节相加
        long sum = 0;
      for (int i = 0; i < stringBuffer.length(); i+=2) {
         String set_zig_item = stringBuffer.substring(i,i+2);
          sum += Long.parseLong(set_zig_item, 16);
      }

        String SUM =  Long.toHexString(sum & 0xff);
        if (SUM.length() == 1) {
            SUM = "0" + SUM;
        }

        stringBuffer.append(SUM);//ABBCCDD105

        String write_zigbee = stringBuffer.toString().trim().toUpperCase();

        Log.e("robin debug","0xD6由加退模式进入到工作模式:" + write_zigbee);

        //[0xD6=网关写入ZigBee模块的信息]
        byte[] bytes = StringtoBytes(write_zigbee);
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取zigee信息[0xD1=网关读取ZigBee模块的信息]
     */
    private void read_zigbee_info() {
        //AB BC CD //3Byte为读取模块信息的HEAD固定值
        //D1//命令字
        //DATA//空
        //SUM//校验值=AB+BC+CD+D1取1Byte,eg.=05
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ABBCCD");
        stringBuffer.append("D1");

        //SUM	(HEAD+CMD+DATA)&0xFF ----SUM 校验
        int AB = Integer.parseInt("AB", 16);
        int BC = Integer.parseInt("BC", 16);
        int CD = Integer.parseInt("CD", 16);
        int y = Integer.parseInt("D1", 16);
        String SUM =  Long.toHexString((AB + BC + CD + y) & 0xff);
        if (SUM.length() == 1) {
            SUM = "0" + SUM;
        }

        stringBuffer.append(SUM);//ABBCCDD105

        String read_zigbee = stringBuffer.toString().trim().toUpperCase();
        //[0xD1=网关读取ZigBee模块的信息]

        byte[] bytes = StringtoBytes(read_zigbee);
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeSerialPort();//closeSerialPort ();//activity销毁时，关闭串口
        stringContent = new StringBuffer();
    }
}
