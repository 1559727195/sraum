package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by masskywcy on 2017-03-20.
 */
//用于添加场景后智能设备选择adapter使用
public class AddsignAdapter extends BaseAdapter<User.device> {
    private List<User.device> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    private List<Boolean> listmark;

    public AddsignAdapter(Context context, List list, List<Boolean> listmark) {
        super(context, list);
        this.list = list;
        this.listmark = listmark;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < listmark.size(); i++) {
            getIsSelected().put(i, listmark.get(i));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.addsignitem, null);
            mHolder.imageone = (ImageView) convertView.findViewById(R.id.imageone);
            mHolder.macname_id = (TextView) convertView.findViewById(R.id.macname_id);
            mHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖
        switch (list.get(position).type) {
            case "1":
                mHolder.imageone.setImageResource(R.drawable.marklamph);
                break;
            case "2":
                mHolder.imageone.setImageResource(R.drawable.dimminglights);
                break;
            case "3":
                mHolder.imageone.setImageResource(R.drawable.home_26);
                break;
            case "4":
                mHolder.imageone.setImageResource(R.drawable.home_curtain);
                break;
            case "5":
                mHolder.imageone.setImageResource(R.drawable.freshair);
                break;
            case "6":
                mHolder.imageone.setImageResource(R.drawable.floorheating);
                break;
            case "7":
                mHolder.imageone.setImageResource(R.drawable.magnetic_door);
                break;
            case "8":
                mHolder.imageone.setImageResource(R.drawable.human_induction);
                break;
            case "9":
                mHolder.imageone.setImageResource(R.drawable.water);
                break;
            case "10":
                mHolder.imageone.setImageResource(R.drawable.pm25);
                break;
            case "11":
                mHolder.imageone.setImageResource(R.drawable.emergency_button);
                break;
            default:
                mHolder.imageone.setImageResource(R.drawable.marklamph);
                break;
        }
        mHolder.macname_id.setText(list.get(position).name);
        // 根据isSelected来设置checkbox的选中状况
        mHolder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    class ViewHolder {
        TextView macname_id;
        ImageView imageone;
        CheckBox cb;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        AddsignAdapter.isSelected = isSelected;
    }
}
