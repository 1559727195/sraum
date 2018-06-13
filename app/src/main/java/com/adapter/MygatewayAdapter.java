package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.LogUtil;
import com.base.BaseAdapter;
import com.data.Allbox;
import com.massky.sraum.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by masskywcy on 2017-01-14.
 */
//我的网关中间小圆圈展示
public class MygatewayAdapter extends BaseAdapter<Allbox> {
    private List<Allbox> list;
    private int lastPosition = -1;
    private Vector<Boolean> vector = new Vector<Boolean>();

    public MygatewayAdapter(Context context, List<Allbox> list) {
        super(context, list);
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).sign.trim().equals("1")) {
                vector.add(true);
            } else {
                vector.add(false);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mygateitem, null);
            mHolder.selectimage_id = (ImageView) convertView.findViewById(R.id.selectimage_id);
            mHolder.textone = (TextView) convertView.findViewById(R.id.textone);
            mHolder.topmygate = (TextView) convertView.findViewById(R.id.topmygate);
            mHolder.message_id = (TextView) convertView.findViewById(R.id.message_id);
            mHolder.textstatus = (TextView) convertView.findViewById(R.id.textstatus);
            mHolder.gaterelative_id = (RelativeLayout) convertView.findViewById(R.id.gaterelative_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textone.setText(list.get(position).boxtop + "");
        mHolder.message_id.setText(list.get(position).name.trim());
        //根据类型选择网关
        if (vector.elementAt(position) == true) {
            mHolder.topmygate.setTextColor(Color.parseColor("#e2c896"));
            mHolder.textone.setTextColor(Color.parseColor("#e2c896"));
            mHolder.selectimage_id.setImageResource(R.drawable.goldcircleh);
        } else {
            mHolder.topmygate.setTextColor(Color.parseColor("#cecece"));
            mHolder.textone.setTextColor(Color.parseColor("#cecece"));
            mHolder.selectimage_id.setImageResource(R.drawable.graycircleh);
        }
        if (position == 0) {
            mHolder.topmygate.setVisibility(View.VISIBLE);
        } else {
            mHolder.topmygate.setVisibility(View.GONE);
        }
        LogUtil.eLength("调用数据", list.get(position).status.trim() + "");
        if (list.get(position).status.trim().equals("0")) {
            mHolder.textstatus.setText("(" + "离线" + ")");
        } else {
            mHolder.textstatus.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        ImageView selectimage_id;
        TextView textone, topmygate, message_id, textstatus;
        RelativeLayout gaterelative_id;
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
        for (int x = 0; x < vector.size(); x++) {
            if (x != position) {
                vector.setElementAt(!vector.elementAt(x), x);
            }
        }
        if (lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }
}
