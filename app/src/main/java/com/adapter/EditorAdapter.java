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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by masskywcy on 2017-03-22.
 */

public class EditorAdapter extends BaseAdapter {

    private List<User.device> list;
    private static HashMap<Integer, Boolean> isSelected;

    public EditorAdapter(Context context, List list) {
        super(context, list);
        this.list = list;
        isSelected = new HashMap<Integer, Boolean>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.addsignitem, null);
            mHolder = new ViewHolder(convertView);
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
        @InjectView(R.id.imageone)
        ImageView imageone;
        @InjectView(R.id.macname_id)
        TextView macname_id;
        @InjectView(R.id.checkbox)
        CheckBox cb;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        EditorAdapter.isSelected = isSelected;
    }
}
