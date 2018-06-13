package com.massky.sraum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.Util.DialogUtil;
import com.Util.LogUtil;
import com.Util.SerializableMap;
import com.Util.SharedPreferencesUtil;
import com.base.Basecactivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2017-04-07.
 */
//用于添加场景选择中设备后选择具体的设备信息
public class AddlamplightActivity extends Basecactivity implements SeekBar.OnSeekBarChangeListener {
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.mainairrea_id)
    RelativeLayout mainairrea_id;
    @InjectView(R.id.noairconditioned_id)
    RelativeLayout noairconditioned_id;
    @InjectView(R.id.tempmark_id)
    ImageView tempmark_id;
    @InjectView(R.id.windspeed_id)
    TextView windspeed_id;
    @InjectView(R.id.tempstate_id)
    TextView tempstate_id;
    @InjectView(R.id.windspeedrelative)
    RelativeLayout windspeedrelative;
    @InjectView(R.id.switchrelative)
    RelativeLayout switchrelative;
    @InjectView(R.id.patternrelative)
    RelativeLayout patternrelative;
    @InjectView(R.id.dimmerrelative)
    RelativeLayout dimmerrelative;
    @InjectView(R.id.id_seekBar)
    SeekBar id_seekBar;
    @InjectView(R.id.tempimage_id)
    TextView tempimage_id;
    @InjectView(R.id.statusopen)
    ImageView statusopen;
    @InjectView(R.id.dimmerimageview)
    ImageView dimmerimageview;
    @InjectView(R.id.curditextone_id)
    TextView curditextone_id;
    @InjectView(R.id.curditextonetwo_id)
    TextView curditextonetwo_id;
    @InjectView(R.id.curditextthree_id)
    TextView curditextthree_id;
    @InjectView(R.id.curopenrelativethree_id)
    RelativeLayout curopenrelativethree_id;
    @InjectView(R.id.curopenrelativetwo_id)
    RelativeLayout curopenrelativetwo_id;
    @InjectView(R.id.curopenrelative_id)
    RelativeLayout curopenrelative_id;
    @InjectView(R.id.curimage_id)
    ImageView curimage_id;
    @InjectView(R.id.curimagetwo_id)
    ImageView curimagetwo_id;
    @InjectView(R.id.curimagethree_id)
    ImageView curimagethree_id;
    @InjectView(R.id.curoffrelative_id)
    RelativeLayout curoffrelative_id;
    @InjectView(R.id.curoffrelativetwo_id)
    RelativeLayout curoffrelativetwo_id;
    @InjectView(R.id.curoffrelativethree_id)
    RelativeLayout curoffrelativethree_id;
    @InjectView(R.id.curoffimathree_id)
    ImageView curoffimathree_id;
    @InjectView(R.id.curoffimatwo_id)
    ImageView curoffimatwo_id;
    @InjectView(R.id.curoffima_id)
    ImageView curoffima_id;
    @InjectView(R.id.sucrela)
    RelativeLayout sucrela;
    @InjectView(R.id.sucrelaimage)
    ImageView sucrelaimage;
    @InjectView(R.id.windspeedtwo_id)
    TextView windspeedtwo_id;
    @InjectView(R.id.open)
    RelativeLayout open;
//    @InjectView(R.id.openbtn)
//    ImageView openbtn;
    @InjectView(R.id.close_btn_lignt)
    ImageView close_btn_light;
    @InjectView(R.id.change_btn)
    ImageView change_btn;
    @InjectView(R.id.openbtn)
    ImageView open_btn_light;

    /**
     * 空调
     */
    @InjectView(R.id.open_kong_tiao)
    RelativeLayout open_kong_tiao;
    //    @InjectView(R.id.openbtn)
//    ImageView openbtn;
    @InjectView(R.id.close_btn_lignt_kong_tiao)
    ImageView close_btn_light_kong_tiao;
    @InjectView(R.id.change_btn_kong_tiao)
    ImageView change_btn_kong_tiao;
    @InjectView(R.id.openbtn_kong_tiao)
    ImageView open_btn_light_kong_tiao;
    /**
     * 空调-----
     */

    /**
     * 新风
     */
    @InjectView(R.id.open_xin_feng)
    RelativeLayout open_xin_feng;
    //    @InjectView(R.id.openbtn)
//    ImageView openbtn;
    @InjectView(R.id.close_btn_lignt_xin_feng)
    ImageView close_btn_light_xin_feng;
    @InjectView(R.id.change_btn_xin_feng)
    ImageView change_btn_xin_feng;
    @InjectView(R.id.openbtn_xin_feng)
    ImageView open_btn_light_xin_feng;
    @InjectView(R.id.windspeedrelative_xin_feng)
    RelativeLayout windspeedrelative_xin_feng;
    /**
     * 新风
     */


    //设备类型和设备编号，token，网关编号，状态值,调光值,空调模式，空调温度
    private String type, number, boxnumber, statusflag = "1", modeflag = "1",
            temperature, windflag = "1", loginPhone, name1, name2, name, curtain = "",
            addspeed = "", addmode = "", itemcode, dimmer,
    //增加虚拟值窗帘判断1开2关3暂停
    flagone, flagtwo, flagthree, openone = "1", opentwo = "1";
    private DialogUtil dialogUtil;
    //whriteone,whritetwo,whritethree进行判断是否清空操作
    private boolean addflag = true, whriteone = true,
            whritetwo = true, whritethree = true, openflagone = false, openflagtwo = false;
    private Map<String, Object> listmap;
    private boolean actflag;
    private boolean isStatus_back;////说明是历史按钮记录，

    @Override
    protected int viewId() {
        return R.layout.mainpageitemone;
    }

    @Override
    protected void onView() {
       //关灯

        close_btn_light.setOnClickListener(this);
        //切换灯的状态
        change_btn.setOnClickListener(this);

        //开灯
        open_btn_light.setOnClickListener(this);

        /**
         * 空调
         */

        close_btn_light_kong_tiao.setOnClickListener(this);
        //切换灯的状态
        change_btn_kong_tiao.setOnClickListener(this);

        //开灯
        open_btn_light_kong_tiao.setOnClickListener(this);
        id_seekBar.setOnSeekBarChangeListener(this);
        /**
         * 空调
         */

        /**
         * 新风
         */

        close_btn_light_xin_feng.setOnClickListener(this);
        //切换灯的状态
        change_btn_xin_feng.setOnClickListener(this);

        //开灯
        open_btn_light_xin_feng.setOnClickListener(this);

        //新风-风速
        windspeedrelative_xin_feng.setOnClickListener(this);
        /**
         * 新风
         */


        loginPhone = (String) SharedPreferencesUtil.getData(AddlamplightActivity.this, "loginPhone", "");//用户名
        boxnumber = (String) SharedPreferencesUtil.getData(AddlamplightActivity.this, "boxnumber", "");//网关编号
        dialogUtil = new DialogUtil(AddlamplightActivity.this);
        backrela_id.setOnClickListener(this);
        windspeedrelative.setOnClickListener(this);
        patternrelative.setOnClickListener(this);
        id_seekBar.setOnSeekBarChangeListener(this);

        open.setOnClickListener(this);
        switchrelative.setOnClickListener(this);
        //调光的开关状态
        dimmerrelative.setOnClickListener(this);
        //窗帘全开点击
        curopenrelativethree_id.setOnClickListener(this);
        //窗帘全关点击
        curoffrelativethree_id.setOnClickListener(this);
        //窗帘1开关状态
        //点击窗帘1打开
        curopenrelative_id.setOnClickListener(this);
        //点击窗帘1关闭
        curoffrelative_id.setOnClickListener(this);
        //点击窗帘2打开
        curopenrelativetwo_id.setOnClickListener(this);
        //点击窗帘1关闭
        curoffrelativetwo_id.setOnClickListener(this);
        //暂停
        sucrela.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        bundle.putBoolean("actflag",actflag);//这个是添加场景和编辑场景的标志
        actflag = bundle.getBoolean("actflag");
//        bundle.putBoolean(" isStatus_back", isStatus_back);//说明是历史按钮记录，
        isStatus_back = bundle.getBoolean("isStatus_back");
        type = bundle.getString("type");
        number = bundle.getString("number");
        name1 = bundle.getString("name1");
        name2 = bundle.getString("name2");
        name = bundle.getString("name");
        itemcode = bundle.getString("itemcode");
        listmap = ((SerializableMap) bundle.get("listmap")).getMap();
        LogUtil.eLength("查看值", name1 + name2);
        curditextone_id.setText(name1);
        curditextonetwo_id.setText(name2);
        curditextthree_id.setText(name);
        titlecen_id.setText(name);
        //根据类型判断界面（除却类型为1的灯泡）
        id_seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean seekflag;
                if (statusflag.equals("0")) {
                    seekflag = false;
                } else {
                    seekflag = true;
                }
                return !seekflag;
            }
        });

        /**
         * 空调
         */
        //根据类型判断界面（除却类型为1的灯泡）
        id_seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean seekflag;
                if (statusflag.equals("0")) {//statusflag = 0即关的时候seekbar不能用
                    seekflag = false;
                } else {
                    seekflag = true;
                }
                return !seekflag;
            }
        });
        /**
         * 空调
         */


        switch (type) {
            //普通灯
            case "1":
                open.setVisibility(View.VISIBLE);
                noairconditioned_id.setVisibility(View.GONE);//空调
                mainairrea_id.setVisibility(View.GONE);
                break;
            //调光灯
            case "2":
                tempimage_id.setText("0");
                noairconditioned_id.setVisibility(View.VISIBLE);
                //调光的开关状态
//                dimmerrelative.setVisibility(View.VISIBLE);//调光
                mainairrea_id.setVisibility(View.GONE);
                tempmark_id.setVisibility(View.GONE);
                windspeed_id.setVisibility(View.GONE);
                tempstate_id.setVisibility(View.GONE);
                windspeedrelative.setVisibility(View.GONE);
                switchrelative.setVisibility(View.GONE);
                patternrelative.setVisibility(View.GONE);
                break;
            //空调
            case "3":
                //判断展示值是否加16
                addflag = false;
                tempimage_id.setText("16");
                id_seekBar.setMax(14);
                noairconditioned_id.setVisibility(View.VISIBLE);
                mainairrea_id.setVisibility(View.GONE);
                open_xin_feng.setVisibility(View.GONE);//空调来了-新风消失
                windspeedrelative.setVisibility(View.VISIBLE);//空调调风的风速控制
                windspeedrelative_xin_feng.setVisibility(View.GONE);//新风的风速控制
                break;
            //窗帘
            case "4":
                noairconditioned_id.setVisibility(View.GONE);
                mainairrea_id.setVisibility(View.VISIBLE);

                open_xin_feng.setVisibility(View.GONE);//窗帘来了-新风消失
                open_kong_tiao.setVisibility(View.GONE);//窗帘来了--空调消失
                windspeedrelative.setVisibility(View.GONE);//窗帘来了-//空调调风的风速控制
                windspeedrelative_xin_feng.setVisibility(View.GONE);//窗帘来了-//新风的风速控制
                break;
            //新风设备
            case "5":
                addflag = false;
                tempimage_id.setText("16");
                id_seekBar.setMax(14);
                noairconditioned_id.setVisibility(View.VISIBLE);
                mainairrea_id.setVisibility(View.GONE);
                patternrelative.setVisibility(View.GONE);
                tempstate_id.setVisibility(View.GONE);
                windspeedtwo_id.setVisibility(View.VISIBLE);
                windspeed_id.setVisibility(View.GONE);
                open_xin_feng.setVisibility(View.VISIBLE);//新风来了-空调消失
                open_kong_tiao.setVisibility(View.GONE);//新风来了-空调消失
                windspeedrelative.setVisibility(View.GONE);//空调调风的风速控制
                windspeedrelative_xin_feng.setVisibility(View.VISIBLE);//新风的风速控制
                break;
            //地暖
            case "6"://地暖和新风是一样的
                addflag = false;
                tempimage_id.setText("16");
                id_seekBar.setMax(14);
                noairconditioned_id.setVisibility(View.VISIBLE);
                mainairrea_id.setVisibility(View.GONE);
                patternrelative.setVisibility(View.GONE);
                tempstate_id.setVisibility(View.GONE);
                windspeedtwo_id.setVisibility(View.VISIBLE);
                windspeed_id.setVisibility(View.GONE);

                open_xin_feng.setVisibility(View.VISIBLE);//新风来了-空调消失
                open_kong_tiao.setVisibility(View.GONE);//新风来了-空调消失
                windspeedrelative.setVisibility(View.GONE);//空调调风的风速控制
                windspeedrelative_xin_feng.setVisibility(View.VISIBLE);//新风的风速控制
                break;
        }
        initState();//初始化状态
    }

    private void initState() {
        String mapstatus = (String) listmap.get("status");
        if (mapstatus != null) {

            //进行判断是否为窗帘
            statusflag = mapstatus;
            LogUtil.eLength("下载数据", statusflag);
            if (type.equals("4")) {
                switch (mapstatus) {  // //增加虚拟值窗帘判断1开2关3暂停 ---增加虚拟值窗帘判断1开2关3暂停
                    case "0":
                        flagone = "2";
                        flagtwo = "2";
                        flagthree = "2";
                        break;
                    case "1":
                        flagone = "1";
                        flagtwo = "1";
                        flagthree = "1";
                        break;
                    //暂停
                    case "2":
                        flagone = "3";
                        flagtwo = "3";
                        flagthree = "4";
                        break;
                    //3-组 1 开组 2 关
                    case "3":
                        flagone = "1";
                        flagtwo = "2";
                        flagthree = "3";
                        break;
                    //4-组 1 开组 2 暂停
                    case "4":
                        flagone = "1";
                        flagtwo = "3";
                        flagthree = "3";
                        break;
                    //5-组 1 关组 2 开
                    case "5":
                        flagone = "2";
                        flagtwo = "1";
                        flagthree = "3";
                        break;
                    //6-组 1 关组 2 暂停
                    case "6":
                        flagone = "2";
                        flagtwo = "3";
                        flagthree = "3";
                        break;
                    //7-组 1 暂停 组 2 关
                    case "7":
                        flagone = "3";
                        flagtwo = "2";
                        flagthree = "3";
                        break;
                    //8-组 1 暂停组 2 开
                    case "8":
                        flagone = "3";
                        flagtwo = "1";
                        flagthree = "3";
                        break;
                }
                switchState(flagone, flagtwo, flagthree);
            } else {
                //不为窗帘开关状态
                if (actflag) {//编辑场景，actflag = true则为编辑已有的场景,//根据有没有被选中，来判断是编辑还是添加场景设备
                    init_add_secene();
                } else {//如果不是编辑已经存在的场景，则为添加场景，进来时默认为开
                    if (isStatus_back) { //说明是历史记录
                        init_add_secene();
                    } else {
                        switch (type) {
                            case "1":
                                statusflag = "1";
                                open_btn_light.setImageResource(R.drawable
                                        .open_white_word);
                                break;//灯
                            case "2":
                            case "3"://空调和调光用一套，开，关，切换按钮
                                statusflag = "1";
                                open_btn_light_kong_tiao.setImageResource(R.drawable
                                        .open_white_word);
                                break;//空调
                            case "5":
                            case "6"://新风和地暖是一样的
                                statusflag = "1";
                                open_btn_light_xin_feng.setImageResource(R.drawable
                                        .open_white_word);
                                break;//地暖
                        }
                    }
                }


                dimmer = (String) listmap.get("dimmer");
                modeflag = (String) listmap.get("mode");
                temperature = (String) listmap.get("temperature");
                windflag = (String) listmap.get("speed");
                if (type.equals("3") || type.equals("5") || type.equals("6")) {
                    if (dimmer != null && !dimmer.equals("")) {
                        tempimage_id.setText(dimmer);
                        id_seekBar.setProgress(Integer.parseInt(dimmer) - 16);
                    }
                    if (temperature != null && !temperature.equals("")) {
                        tempimage_id.setText(temperature);
                        id_seekBar.setProgress(Integer.parseInt(temperature) - 16);
                    }
                }
                if (type.equals("2")) {
                    if (dimmer != null && !dimmer.equals("")) {
                        tempimage_id.setText(dimmer);
                        id_seekBar.setProgress(Integer.parseInt(dimmer));
                    }
                }
                setModetwo();
                setSpeed();
                LogUtil.eLength("查看是否进去", temperature + "进入方法" + dimmer);
                if (mapstatus.equals("0")) {
                    getBoxclose();
                    LogUtil.eLength("没有进去", "进入方法");
                } else {
                    LogUtil.eLength("你看", "进入方法");
                    getBoxopen();
                }
            }
        }
    }

    /**
     *场景按钮状态选择
     */
    private void init_add_secene() {
        switch (type) {
            case "1":
                switch (statusflag) {
                    case "0":
                        close_btn_light.setImageResource(R.drawable
                                .guan_white_word);
                        break;//状态-开
                    case "1":
                        open_btn_light.setImageResource(R.drawable
                                .open_white_word);
                        break;//状态-关
                    case "3":
                        change_btn.setImageResource(R.drawable
                                .change_white_word);
                        break;//状态-切换
                }
                break;//灯
            case "2":
            case "3"://空调和调光用一套，开，关，切换按钮
                switch (statusflag) {
                    case "0":
                        close_btn_light_kong_tiao.setImageResource(R.drawable
                                .guan_white_word);
                        break;//状态-开
                    case "1":
                        open_btn_light_kong_tiao.setImageResource(R.drawable
                                .open_white_word);
                        break;//状态-关
                    case "3":
                        change_btn_kong_tiao.setImageResource(R.drawable
                                .change_white_word);
                        break;//状态-切换
                }
                break;//空调
            case "5":
            case "6"://新风和地暖是一样的
                switch (statusflag) {
                    case "0":
                        close_btn_light_xin_feng.setImageResource(R.drawable
                                .guan_white_word);
                        break;//状态-开
                    case "1":
                        open_btn_light_xin_feng.setImageResource(R.drawable
                                .open_white_word);
                        break;//状态-关
                    case "3":
                        change_btn_xin_feng.setImageResource(R.drawable
                                .change_white_word);
                        break;//状态-切换
                }
                break;//地暖
        }
    }

    private void setModetwo() {
        String strmode = "";
        switch (modeflag) {
            case "1":
                strmode = "制冷";
                break;
            case "2":
                strmode = "制热";
                break;
            case "3":
                strmode = "除湿";
                break;
            case "4":
                strmode = "自动";
                break;
            case "5":
                strmode = "通风";
                break;
            default:
                break;
        }
        tempstate_id.setText(strmode);
    }

    private void setSpeed() {
        String strwind = "";
        switch (windflag) {
            case "1":
                strwind = "低风";
                break;
            case "2":
                strwind = "中风";
                break;
            case "3":
                strwind = "高风";
                break;
            case "4":
                strwind = "强力";
                break;
            case "5":
                strwind = "送风";
                break;
            case "6":
                strwind = "自动";
                break;
            default:
                break;
        }
        windspeedtwo_id.setText(strwind);
        windspeed_id.setText(strwind);
    }

    /*非窗帘全关状态设置*/
    private void getBoxclose() {
        //全部关闭状态
        //调光灯状态开关
//        open.setBackgroundResource(R.drawable.hsmall_circle);
//        openbtn.setImageResource(R.drawable.hopen);
        dimmerrelative.setBackgroundResource(R.drawable.hsmall_circle);
        dimmerimageview.setImageResource(R.drawable.hopen);
        //空调开关状态
        switchrelative.setBackgroundResource(R.drawable.hsmall_circle);
        statusopen.setImageResource(R.drawable.hopen);
    }

    /*非窗帘全开状态设置*/
    private void getBoxopen() {
        //目前是全开的状态
        //调光灯状态开关
//        open.setBackgroundResource(R.drawable.hsmall_black);
//        openbtn.setImageResource(R.drawable.hairclose_word);
        dimmerrelative.setBackgroundResource(R.drawable.hsmall_black);
        dimmerimageview.setImageResource(R.drawable.hairclose_word);
        //空调开关状态
        switchrelative.setBackgroundResource(R.drawable.hsmall_black);
        statusopen.setImageResource(R.drawable.hairclose_word);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /**
             * 新风-和地暖是一样的
             */
            case R.id.close_btn_lignt_xin_feng://close
                close_btn_light_xin_feng.setImageResource(R.drawable
                        .guan_white_word);
                change_btn_xin_feng.setImageResource(R.drawable
                        .change_black_word);
                open_btn_light_xin_feng.setImageResource(R.drawable
                        .open_black_word);
//                open();
                statusflag = "0";
                break;//关灯
            case R.id.change_btn_xin_feng:
                change_btn_xin_feng.setImageResource(R.drawable
                        .change_white_word);
                close_btn_light_xin_feng.setImageResource(R.drawable
                        .guan_black_word);
                open_btn_light_xin_feng.setImageResource(R.drawable
                        .open_black_word);

                statusflag = "3";
                break;//切换灯的状态
            case R.id.openbtn_xin_feng://打开灯
                open_btn_light_xin_feng.setImageResource(R.drawable
                        .open_white_word);
                change_btn_xin_feng.setImageResource(R.drawable
                        .change_black_word);
                close_btn_light_xin_feng.setImageResource(R.drawable
                        .guan_black_word);
                statusflag = "1";
                break;
            case R.id.windspeedrelative_xin_feng:
                //风速状态
                switch (windflag) {
                    case "1":
                        windspeedtwo_id.setText("中风");
                        windspeed_id.setText("中风");
                        windflag = "2";
                        break;
                    case "2":
                        windspeedtwo_id.setText("高风");
                        windspeed_id.setText("高风");
                        windflag = "3";
                        break;
                    case "3":
                        windspeedtwo_id.setText("强力");
                        windspeed_id.setText("强力");
                        windflag = "4";
                        break;
                    case "4":
                        windspeedtwo_id.setText("送风");
                        windspeed_id.setText("送风");
                        windflag = "5";
                        break;
                    case "5":
                        windspeedtwo_id.setText("自动");
                        windspeed_id.setText("自动");
                        windflag = "6";
                        break;
                    case "6":
                        windspeedtwo_id.setText("低风");
                        windspeed_id.setText("低风");
                        windflag = "1";
                        break;
                    default:
                        break;
                }
                break;//新风-风速
            /**
             * 新风
             */


            /**
             * 空调，和调光共用开，关，切换这三个按钮
             */
            case R.id.close_btn_lignt_kong_tiao://close
                close_btn_light_kong_tiao.setImageResource(R.drawable
                        .guan_white_word);
                change_btn_kong_tiao.setImageResource(R.drawable
                        .change_black_word);
                open_btn_light_kong_tiao.setImageResource(R.drawable
                        .open_black_word);
//                open();
                statusflag = "0";
                break;//关灯
            case R.id.change_btn_kong_tiao:
                change_btn_kong_tiao.setImageResource(R.drawable
                        .change_white_word);
                close_btn_light_kong_tiao.setImageResource(R.drawable
                        .guan_black_word);
                open_btn_light_kong_tiao.setImageResource(R.drawable
                        .open_black_word);

                statusflag = "3";
                break;//切换灯的状态
            case R.id.openbtn_kong_tiao://打开灯
                open_btn_light_kong_tiao.setImageResource(R.drawable
                        .open_white_word);
                change_btn_kong_tiao.setImageResource(R.drawable
                        .change_black_word);
                close_btn_light_kong_tiao.setImageResource(R.drawable
                        .guan_black_word);
                statusflag = "1";
                break;
            /**
             * 空调
             */

            case R.id.close_btn_lignt://close
                close_btn_light.setImageResource(R.drawable
                .guan_white_word);
                change_btn.setImageResource(R.drawable
                        .change_black_word);
                open_btn_light.setImageResource(R.drawable
                        .open_black_word);
//                open();
                statusflag = "0";
                break;//关灯
            case R.id.change_btn:
                change_btn.setImageResource(R.drawable
                        .change_white_word);
                close_btn_light.setImageResource(R.drawable
                        .guan_black_word);
                open_btn_light.setImageResource(R.drawable
                        .open_black_word);

                statusflag = "3";
                break;//切换灯的状态
            case R.id.openbtn://打开灯
                open_btn_light.setImageResource(R.drawable
                        .open_white_word);
                change_btn.setImageResource(R.drawable
                        .change_black_word);
                close_btn_light.setImageResource(R.drawable
                        .guan_black_word);
                statusflag = "1";
                break;

            case R.id.open:
                break;
            case R.id.backrela_id:
                finshAdd();
                break;
            //窗帘1打开
            case R.id.curopenrelative_id://
                curtain = "1";//1-窗帘1打开
                onccurtain();
                break;
            //窗帘1关闭
            case R.id.curoffrelative_id://
                curtain = "2";//2-窗帘1关闭
                onccurtain();
                break;
            //窗帘2打开
            case R.id.curopenrelativetwo_id:
                curtain = "3";//2-窗帘2打开
                onccurtain();
                break;
            //窗帘2关闭
            case R.id.curoffrelativetwo_id:
                curtain = "4";//2-窗帘2关闭
                onccurtain();
                break;
            //窗帘全开点击
            case R.id.curopenrelativethree_id:
                curtain = "5";//窗帘全开
                onccurtain();
                break;
            //窗帘全关点击
            case R.id.curoffrelativethree_id:
                curtain = "6";//窗帘全关
                onccurtain();
                break;
            //窗帘全关点击
            case R.id.sucrela:
                curtain = "7";//窗帘全关
                onccurtain();
                break;
            case R.id.windspeedrelative:
                //风速状态
                switch (windflag) {
                    case "1":
                        windspeedtwo_id.setText("中风");
                        windspeed_id.setText("中风");
                        windflag = "2";
                        break;
                    case "2":
                        windspeedtwo_id.setText("高风");
                        windspeed_id.setText("高风");
                        windflag = "3";
                        break;
                    case "3":
                        windspeedtwo_id.setText("强力");
                        windspeed_id.setText("强力");
                        windflag = "4";
                        break;
                    case "4":
                        windspeedtwo_id.setText("送风");
                        windspeed_id.setText("送风");
                        windflag = "5";
                        break;
                    case "5":
                        windspeedtwo_id.setText("自动");
                        windspeed_id.setText("自动");
                        windflag = "6";
                        break;
                    case "6":
                        windspeedtwo_id.setText("低风");
                        windspeed_id.setText("低风");
                        windflag = "1";
                        break;
                    default:
                        break;
                }
                break;
            case R.id.patternrelative:
                setMode();
                break;
            //空调开关状态
            case R.id.switchrelative:
                open();
                break;
            //调光的开关状态
            case R.id.dimmerrelative:
                open();
                break;
        }
    }

    private void curSet() {
        switch (statusflag) {
            case "0":
                openone = "2";
                opentwo = "2";//关
                break;
            case "1":
                openone = "1";
                opentwo = "1";//1-开
                break;
            case "2":
                openone = "3";
                opentwo = "3";//3-暂停
                break;
            case "3":
                openone = "1";//组一开，
                opentwo = "2";//组二关
                break;
            case "4":
                openone = "1";//组一开，
                opentwo = "3";//组二暂停
                break;
            case "5":
                openone = "2";//组一关
                opentwo = "1";//组二开
                break;
            case "6":
                openone = "2";//组一关
                opentwo = "3";//组二暂停
                break;
            case "7":
                openone = "3";//组一暂停
                opentwo = "2";//组二关
                break;
            case "8":
                openone = "3";//组一暂停
                opentwo = "1";//组二开
                break;
            default:
                break;
        }
    }

    private void onccurtain() {
        curSet();
        switch (curtain) {
            //窗帘1打开
            case "1":
                flagone = "1";
                flagtwo = "3";
                flagthree = "3";
                whriteone = true;
                whritetwo = false;
                whritethree = true;
                openflagone = false;
                openone = "1";
                switch (opentwo) {
                    case "1":
                        statusflag = "1";
                        break;
                    case "2":
                        statusflag = "3";
                        break;
                    case "3":
                        statusflag = "4";
                        break;
                    default:
                        break;
                }
                break;
            //窗帘1关闭
            case "2":
                flagone = "2";
                flagtwo = "3";
                flagthree = "3";
                whriteone = true;
                whritetwo = false;
                whritethree = true;
                openflagone = true;
                openone = "2";
                switch (opentwo) {
                    case "1":
                        statusflag = "5";
                        break;
                    case "2":
                        statusflag = "0";
                        break;
                    case "3":
                        statusflag = "6";
                        break;
                    default:
                        break;
                }
                break;
            //窗帘2打开
            case "3":
                flagone = "3";
                flagtwo = "1";
                flagthree = "3";
                whriteone = false;
                whritetwo = true;
                whritethree = true;
                openflagtwo = false;
                opentwo = "1";
                switch (openone) {
                    case "1":
                        statusflag = "1";
                        break;
                    case "2":
                        statusflag = "5";
                        break;
                    case "3":
                        statusflag = "8";
                        break;
                    default:
                        break;
                }
                break;
            //窗帘2关闭
            case "4":
                flagone = "3";
                flagtwo = "2";
                flagthree = "3";
                whriteone = false;
                whritetwo = true;
                whritethree = true;
                openflagtwo = true;
                opentwo = "2";
                switch (openone) {
                    case "1":
                        statusflag = "3";
                        break;
                    case "2":
                        statusflag = "0";
                        break;
                    case "3":
                        statusflag = "7";
                        break;
                    default:
                        break;
                }
                break;
            //全开
            case "5":
                flagone = "1";
                flagtwo = "1";
                flagthree = "1";
                whriteone = true;
                whritetwo = true;
                whritethree = true;
                openflagone = false;
                openflagtwo = false;
                openone = "1";
                opentwo = "1";
                statusflag = "1";
                break;
            //全关
            case "6":
                flagone = "2";
                flagtwo = "2";
                flagthree = "2";
                whriteone = true;
                whritetwo = true;
                whritethree = true;
                openflagone = true;
                openflagtwo = true;
                openone = "2";
                opentwo = "2";
                statusflag = "0";
                break;
            //暂停
            case "7":
                flagone = "3";
                flagtwo = "3";
                flagthree = "4";
                whriteone = true;
                whritetwo = true;
                whritethree = true;
                openflagone = false;
                openflagtwo = false;
                openone = "3";
                opentwo = "3";
                statusflag = "2";
                break;
            default:
                break;
        }
        switchState(flagone, flagtwo, flagthree);
        LogUtil.eLength("这是状态值", statusflag + "传入窗帘状态");
    }

    //finsh之前方法
    private void finshAdd() {
        String dimmer = "", mode = "", temperature = "", speed = "";
        switch (type) {
            //普通灯
            case "1":
                break;
            //调光灯
            case "2":
                dimmer = tempimage_id.getText().toString();
                break;
            //空调
            case "3":
                mode = tempstate_id.getText().toString();
                speed = windspeed_id.getText().toString();
                temperature = tempimage_id.getText().toString();
                break;
            //窗帘
            case "4":
                break;
            //新风
            case "5":
                speed = windspeed_id.getText().toString();
                temperature = tempimage_id.getText().toString();
                break;
            //地暖
            case "6":
                speed = windspeed_id.getText().toString();
                temperature = tempimage_id.getText().toString();
                break;
            default:
                break;
        }
        getSpeed(speed.trim());
        getMode(mode.trim());
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("number", number);
        map.put("status", statusflag);
        map.put("dimmer", dimmer);
        map.put("mode", addmode);
        map.put("temperature", temperature);
        map.put("speed", addspeed);
        SerializableMap myMap = new SerializableMap();
        myMap.setMap(map);
        myMap.setItemcode(itemcode);
        Intent intentadd = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", myMap);
        intentadd.putExtras(bundle);
        this.setResult(RESULT_OK, intentadd);
        this.finish();
    }

    private void getSpeed(String speedstr) {
        switch (speedstr) {
            case "低风":
                addspeed = "1";
                break;
            case "中风":
                addspeed = "2";
                break;
            case "高风":
                addspeed = "3";
                break;
            case "强力":
                addspeed = "4";
                break;
            case "送风":
                addspeed = "5";
                break;
            case "自动":
                addspeed = "6";
                break;
            default:
                addspeed = "";
                break;
        }
    }

    private void getMode(String modestr) {
        switch (modestr) {
            case "制冷":
                addmode = "1";
                break;
            case "制热":
                addmode = "2";
                break;
            case "除湿":
                addmode = "3";
                break;
            case "自动":
                addmode = "4";
                break;
            case "通风":
                addmode = "5";
                break;
            default:
                addmode = "";
                break;
        }
    }

    private void open() {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        LogUtil.i("这是滑动值", progress + "onProgressChanged: ");
        if (addflag) {
            tempimage_id.setText(progress + "");
        } else {
            tempimage_id.setText((16 + progress) + "");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LogUtil.i("开始滑动", "onStartTrackingTouch: ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LogUtil.i("停止滑动", "onStopTrackingTouch: ");
    }

    private void setMode() {
        //模式状态
        switch (modeflag) {
            case "1":
                tempstate_id.setText("制热");
                modeflag = "2";
                break;
            case "2":
                tempstate_id.setText("除湿");
                modeflag = "3";
                break;
            case "3":
                tempstate_id.setText("自动");
                modeflag = "4";
                break;
            case "4":
                tempstate_id.setText("通风");
                modeflag = "5";
                break;
            case "5":
                tempstate_id.setText("制冷");
                modeflag = "1";
                break;
            default:
                break;
        }
    }

    /*窗帘各个开关状态设置*/
    private void switchState(String flagone, String flagtwo, String flagthree) {
        //进行清空各个操作
        statusClear();
        if (flagone.equals("1")) {
            curopenrelative_id.setBackgroundResource(R.drawable.hsmall_black);
            curimage_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagone.equals("2")) {
            curoffrelative_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffima_id.setImageResource(R.drawable.hairclose_word);
        }

        if (flagtwo.equals("1")) {
            curopenrelativetwo_id.setBackgroundResource(R.drawable.hsmall_black);
            curimagetwo_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagtwo.equals("2")) {
            curoffrelativetwo_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffimatwo_id.setImageResource(R.drawable.hairclose_word);
        }

        if (flagthree.equals("1")) {
            curopenrelativethree_id.setBackgroundResource(R.drawable.hsmall_black);
            curimagethree_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagthree.equals("2")) {
            curoffrelativethree_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffimathree_id.setImageResource(R.drawable.hairclose_word);
        } else if (flagthree.equals("4")) {
            //暂停
            sucrela.setBackgroundResource(R.drawable.hsmall_black);
            sucrelaimage.setImageResource(R.drawable.hairpause_word_white);
        }
    }

    private void statusClear() {
        if (whriteone) {
            curopenrelative_id.setBackgroundResource(R.drawable.hairopen);
            curimage_id.setImageResource(R.drawable.hairopen_word);
            curoffrelative_id.setBackgroundResource(R.drawable.hairopen);
            curoffima_id.setImageResource(R.drawable.hairclose_word_black);
        }

        if (whritetwo) {
            curopenrelativetwo_id.setBackgroundResource(R.drawable.hairopen);
            curimagetwo_id.setImageResource(R.drawable.hairopen_word);
            curoffrelativetwo_id.setBackgroundResource(R.drawable.hairopen);
            curoffimatwo_id.setImageResource(R.drawable.hairclose_word_black);
        }

        if (whritethree) {
            curopenrelativethree_id.setBackgroundResource(R.drawable.hairopen);
            curimagethree_id.setImageResource(R.drawable.hairopen_word);
            curoffrelativethree_id.setBackgroundResource(R.drawable.hairopen);
            curoffimathree_id.setImageResource(R.drawable.hairclose_word_black);
            //暂停
            sucrela.setBackgroundResource(R.drawable.hairopen);
            sucrelaimage.setImageResource(R.drawable.hairpause_word);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
