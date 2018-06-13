package com.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;
import java.util.List;

/**
 * Created by masskywcy on 2016-09-06.
 */
//用于第一个界面gritview  adapter
public class MacFragAdapter extends BaseAdapter<User.device> {
//    private List<User.device> list;

    public MacFragAdapter(Context context, List<User.device> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.macgrititem, null);
            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageitem_id);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.textitem_id);
            mHolder.itemrela_id = (RelativeLayout) convertView.findViewById(R.id.itemrela_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textitem_id.setText(list.get(position).name);
        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖
        switch (list.get(position).type) {
            case "1":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.marklamph);
                break;
            case "2":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.dimminglights);
                break;
            case "3":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.home_26);
                break;
            case "4":
                String curstatus = list.get(position).status;
                if (curstatus.equals("1") || curstatus.equals("3") || curstatus.equals("4")
                        || curstatus.equals("4") || curstatus.equals("5") || curstatus.equals("8")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.home_curtain);
                break;
            case "5":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.freshair);
                break;
            case "6":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.floorheating);
                break;


            case "7":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.magnetic_door_s);
                break;
            case "8":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.human_induction_s);
                break;
            case "9":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.water_s);
                break;
            case "10":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
                mHolder.imageitem_id.setImageResource(R.drawable.pm25_s);
                break;
            case "11":
                if (list.get(position).status.equals("1")) {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                } else {
                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                }
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
        RelativeLayout itemrela_id;
    }
}
