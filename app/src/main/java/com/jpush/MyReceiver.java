package com.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.NullStringToEmptyAdapterFactory;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.base.Basecfragment;
import com.base.Basecfragmentactivity;
import com.data.Allbox;
import com.data.User;
import com.fragment.MacFragment;
import com.fragment.MacdeviceFragment;
import com.fragment.MygatewayFragment;
import com.fragment.MysceneFragment;
import com.fragment.RoomFragment;
import com.fragment.SceneFragment;
import com.google.gson.GsonBuilder;
import com.massky.sraum.FastEditPanelActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.jpush.android.api.JPushInterface;

import static com.massky.sraum.AddZigbeeDevActivity.ACTION_SRAUM_SETBOX;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Context context;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private String action = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        LogUtil.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//        ToastUtil.showToast(context,"接收到推送下来的自定义消息");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtil.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            SharedPreferencesUtil.saveData(context, "regId", regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            ToastUtil.showToast(context,"接收到推送下来的自定义消息");
            processCustomMessage_toMainActivity(context, bundle);
            //接收下来的json数据
            User user = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).
                    create().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), User.class);
            LogUtil.i(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            //进行广播通知是否刷新设备和场景
            processCustomMessage(Integer.parseInt(user.type),bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //接收下来的json数据
            User user = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).
                    create().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), User.class);
            LogUtil.i(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            //进行广播通知是否刷新设备和场景
            processCustomMessage(Integer.parseInt(user.type), bundle);
            LogUtil.i(TAG, Integer.parseInt(user.type) + "");
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            User user = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).
                    create().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), User.class);
            SharedPreferencesUtil.saveData(context, "usertype", user.type);
            //2代表场景信息1代表设备刷新信息
            //Intent i = null;
            if (context instanceof MainfragmentActivity) {
                MainfragmentActivity fca = (MainfragmentActivity) context;
                if (user.type.equals("2")) {
                    fca.setTabSelection(3);
                } else if (user.type.equals("1")) {
                    fca.setTabSelection(0);
                }
            } else {
                //IntentUtil.startActivity(context, MainfragmentActivity.class, "usertype", user.type);
                /*
                *   i = new Intent(context, MainfragmentActivity.class);
                if (i != null) {
                    //打开自定义的Activity
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }*/
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtil.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtil.i(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtil.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void processCustomMessage_toMainActivity(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//账号已在别处登录！
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);////{"type":"2"}
        if (Basecactivity.isForegrounds || Basecfragment.isForegrounds || Basecfragmentactivity.isForegrounds) {//app可见时，才发送消息
            Intent msgIntent = new Intent(MainfragmentActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            JSONObject extraJson = null;
            String type = "";
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    extraJson = new JSONObject(extras);
                    type = extraJson.getString("type");
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainfragmentActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                }
            }

            if (type.equals("7")) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
            }
        } else {//app不可见,保存本地
//			SharedPreferencesUtil.saveData(context,"extras_login",extras);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                LogUtil.e(TAG, bundle.getString(JPushInterface.EXTRA_EXTRA) + "数据");
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]" + "1");
                    }
                } catch (JSONException e) {
                    LogUtil.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //进行广播通知是否刷新设备和场景
    private void processCustomMessage(int notifactionId, Bundle bundle) {
        action = "";
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);////{"type":"2"}
        if (notifactionId == 2) {
            if (TokenUtil.getPagetag(context).equals("3")) {
                action = SceneFragment.ACTION_INTENT_RECEIVER;
            } else if (TokenUtil.getPagetag(context).equals("6")) {
                action = MysceneFragment.ACTION_INTENT_RECEIVER;
            }
            sendBroad(notifactionId,"");
        } else if (notifactionId == 1) {//
            JSONObject extraJson;
            if (!ExampleUtil.isEmpty(extras)) {//设备状态改变
               // 在extras中增加了字段panelid
                try {
                    extraJson = new JSONObject(extras);
                    String panelid  = extraJson.getString("panelid");
                    if (panelid != null) {
//                        if (panelid.equals("")) {
//                            action = MacFragment.ACTION_INTENT_RECEIVER;
//                            sendBroad(notifactionId, "");
//                        } else {//快捷编辑
//                            action = FastEditPanelActivity.ACTION_SRAUM_FAST_EDIT;
//                            sendBroad(notifactionId, panelid);
//                        }
//                    } else {
//                        action = MacFragment.ACTION_INTENT_RECEIVER;
//                        sendBroad(notifactionId, "");
//                    }
                        action = MacFragment.ACTION_INTENT_RECEIVER;
                        sendBroad(notifactionId, "");

                        action = FastEditPanelActivity.ACTION_SRAUM_FAST_EDIT;
                        sendBroad(notifactionId, panelid);
                    }
                } catch (JSONException e) {

                }
            }
        } else if (notifactionId == 5 || notifactionId == 3 || notifactionId == 4) {
            Map<String, Object> mapbox = new HashMap<>();
            mapbox.put("token", TokenUtil.getToken(context));
            getBox(mapbox, notifactionId);
        } else if (notifactionId == 8) {//notifactionId = 8 ->设置网关模式，sraum_setBox
            action = ACTION_SRAUM_SETBOX;
            JSONObject extraJson;
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    extraJson = new JSONObject(extras);
                    String panelid  = extraJson.getString("panelid");
                    if (extraJson.length() > 0) {
                        sendBroad(notifactionId,panelid);
                    }
                } catch (JSONException e) {

                }
            }
        }
    }

    private void sendBroad(int notifactionId, String second) {
        Intent mIntent = new Intent(action);
        mIntent.putExtra("notifactionId", notifactionId);
        mIntent.putExtra("panelid",second);
        context.sendBroadcast(mIntent);
    }

    private void getBox(Map<String, Object> mapbox, final int notifactionId) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                Map<String, Object> mapbox = new HashMap<>();
                mapbox.put("token", TokenUtil.getToken(context));
                getBox(mapbox, notifactionId);

            }
        }, context, null) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                int boxtop = 1;
                allboxList.clear();
                for (User.box bo : user.boxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign, boxtop);
                    allboxList.add(allbox);
                    boxtop++;
                }
                if (allboxList.size() == 0) {
                    SharedPreferencesUtil.saveData(context, "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.trim().equals("1")) {
                            SharedPreferencesUtil.saveData(context, "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(context, "boxnumber", ab.number);
                        }
                    }
                }
                String pagetag = TokenUtil.getPagetag(context);
                LogUtil.eLength("这是数据", pagetag + "数据" + TokenUtil.getBoxnumber(context));
                switch (pagetag) {
                    case "1":
                        action = MacFragment.ACTION_INTENT_RECEIVER;
                        break;
                    case "2":
                        action = RoomFragment.ACTION_INTENT_RECEIVER;
                        break;
                    case "3":
                        action = SceneFragment.ACTION_INTENT_RECEIVER;
                        break;
                    case "4":
                        action = MacdeviceFragment.ACTION_INTENT_RECEIVER;
                        break;
                    case "5":
                        action = MygatewayFragment.ACTION_INTENT_RECEIVER;
                        break;
                    case "6":
                        action = MysceneFragment.ACTION_INTENT_RECEIVER;
                        break;
                    default:
                        action = "";
                        break;
                }
                sendBroad(notifactionId, "");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }
}

