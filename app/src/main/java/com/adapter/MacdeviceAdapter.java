package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by masskywcy on 2016-09-23.
 */
//智能设备界面adapter使用
public class MacdeviceAdapter extends BaseAdapter<User.device> {
    private List<User.device> list;
    private int lastPosition = -1;
    private Vector<Boolean> vector = new Vector<Boolean>();

    public MacdeviceAdapter(Context context, List list) {
        super(context, list);
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            vector.add(false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.macdeviceitem, null);
            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageone);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.macname_id);
            mHolder.yeviewone = (View) convertView.findViewById(R.id.yeviewone);
            mHolder.yeviewtwo = (View) convertView.findViewById(R.id.yeviewtwo);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textitem_id.setText(list.get(position).name);
//        if (vector.elementAt(position) == true) {
//            mHolder.yeviewone.setVisibility(View.VISIBLE);
//            mHolder.yeviewtwo.setVisibility(View.VISIBLE);
//        } else {
//            mHolder.yeviewone.setVisibility(View.GONE);
//            mHolder.yeviewtwo.setVisibility(View.GONE);
//        }
        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖
        switch (list.get(position).type) {
            case "1":
                mHolder.imageitem_id.setImageResource(R.drawable.marklamph);
                break;
            case "2":
                mHolder.imageitem_id.setImageResource(R.drawable.dimminglights);
                break;
            case "3":
                mHolder.imageitem_id.setImageResource(R.drawable.home_26);
                break;
            case "4":
                mHolder.imageitem_id.setImageResource(R.drawable.home_curtain);
                break;
            case "5":
                mHolder.imageitem_id.setImageResource(R.drawable.freshair);
                break;
            case "6":
                mHolder.imageitem_id.setImageResource(R.drawable.floorheating);
                break;
            case "7":
                mHolder.imageitem_id.setImageResource(R.drawable.magnetic_door_s);
                break;
            case "8":
                mHolder.imageitem_id.setImageResource(R.drawable.human_induction_s);
                break;
            case "9":
                mHolder.imageitem_id.setImageResource(R.drawable.water_s);
                break;
            case "10":
                mHolder.imageitem_id.setImageResource(R.drawable.pm25_s);
                break;
            case "11":
                mHolder.imageitem_id.setImageResource(R.drawable.emergency_button_s);
                break;
            default:
                mHolder.imageitem_id.setImageResource(R.drawable.marklamph);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageitem_id;
        TextView textitem_id;
        View yeviewone, yeviewtwo;
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
        if (lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }
}
