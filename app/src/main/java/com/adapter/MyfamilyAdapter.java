package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenDialog;
import com.Util.TokenUtil;
import com.base.BaseAdapter;
import com.data.User;
import com.google.gson.Gson;
import com.massky.sraum.R;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static android.R.attr.accountType;

/**
 * Created by masskywcy on 2016-09-21.
 */
//我的家庭页面adapter
public class MyfamilyAdapter extends BaseAdapter<User.familylist> {
    private List<User.familylist> list;
    private DialogUtil dialogUtil;
    private  String accountType;
    public MyfamilyAdapter(Context context, List list,
                           String token, DialogUtil dialogUtil,String accountType) {
        super(context, list);
        this.list = list;
        this.dialogUtil = dialogUtil;
        this.accountType = accountType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.myfamilyitem, null);
            mHolder.mytext = (TextView) convertView.findViewById(R.id.myfamitemtext);
            mHolder.myfamtext = (TextView) convertView.findViewById(R.id.myfamtext);
            mHolder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mytext.setText(list.get(position).name);
        mHolder.myfamtext.setText(list.get(position).mobilePhone);
        final View finalConvertView = convertView;

        switch (accountType) {//判断家庭成员类型
            case "1":
                ((SwipeMenuLayout) finalConvertView).setSwipeEnable(true);
                break;//业主
            case "2":
                ((SwipeMenuLayout) finalConvertView).setSwipeEnable(false);
                break;//成员
        }

        mHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除动画关闭
                ((SwipeMenuLayout) finalConvertView).quickClose();
                deleteFamily(list.get(position).mobilePhone, position);
            }
        });
        return convertView;
    }

    //删除家庭成员
    private void deleteFamily(final String mobilePhone, final int x) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(context));
        map.put("mobilePhone", mobilePhone);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_deleteFamily, map, new Mycallback(new AddTogglenInterfacer(){
            @Override
            public void addTogglenInterfacer() {
                deleteFamily(mobilePhone,x);
            }
        },
            context, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                list.remove(x);
                notifyDataSetChanged();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });

    }

    class ViewHolder {
        TextView mytext, myfamtext;
        Button btnDelete;
    }
}
