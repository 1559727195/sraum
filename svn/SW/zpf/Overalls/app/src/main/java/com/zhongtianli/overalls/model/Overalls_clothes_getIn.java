package com.zhongtianli.overalls.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhongtianli.overalls.bean.GetClothesBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import static com.zhongtianli.overalls.activity.LoginActivity.JSON;

/**
 * Created by zhu on 2016/12/28.
 */
public class Overalls_clothes_getIn implements Get_overalls_clothes_getIn{

    private String TAG = "robin debug";
    private GetClothesBean getClothesBean = new GetClothesBean();
    private  Context context;

    @Override
    public void get_overalls_clothes_getIn(final Context context, String ipString,final
                                           Overalls_clothes_getIn_listener overalls_clothes_getIn_listener) {
         //在这里进行网络加载.	获取库存信息（Clothes_GetIn）
        //拼接URL添加上用户的Ip
//        String url = "http://xxx.xxx.xxx.xxx:25789/api/RFID/Clothes_GetIn";
        this.context = context;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://");
        if (ipString == null || ipString.isEmpty()) {//说明用户还没有配服务器地址,给他个默认的IP地址
           ipString = "192.168.169.220";
        }
        stringBuffer.append(ipString);
        stringBuffer.append(":25789/api/RFID/Clothes_GetIn");
        Log.e(TAG,stringBuffer.toString());
        Map<String,String> map = new HashMap<>();
        map.put("User","massky");
        OkHttpUtils
                .postString()
                .url(stringBuffer.toString())
                .tag(context)
                .mediaType(JSON)
                .content(new Gson().toJson(map))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG,""+ e.toString());
                        overalls_clothes_getIn_listener.overalls_clothes_getIn_listener_fail();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析json字符串
                        getClothesBean = new Gson().fromJson(response,GetClothesBean.class);
                        Log.e(TAG,"getClothesBean.getResult():" + getClothesBean.getResult() + "response:" + response);
                        if( getClothesBean.getResult().equals("100")) {
                            overalls_clothes_getIn_listener.overalls_clothes_getIn_listener_sus(getClothesBean);
                        }else if ( getClothesBean.getResult().equals("1")){
                                  toast("参数不正确或者不存在");
                            overalls_clothes_getIn_listener.overalls_clothes_getIn_listener_fail();
                        } else if ( getClothesBean.getResult().equals("2")) {
                            toast("json格式解析失败");
                            overalls_clothes_getIn_listener.overalls_clothes_getIn_listener_fail();
                        } else if ( getClothesBean.getResult().equals("101")) {
                            toast("无数据");
                            overalls_clothes_getIn_listener.overalls_clothes_getIn_listener_fail();
                        }
                    }
                });
    }

    private  void toast (String content) {
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
