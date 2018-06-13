package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.massky.sraum.R;

import java.util.Vector;

/**
 * Created by masskywcy on 2016-11-29.
 */

//用于我的场景界面adapter
public class SceneAdapter extends BaseAdapter {
    private int[] icon;
    private int[] iconName;
    private int[] icontwo;
    private Context context;
    private int lastPosition = -1;
    private Vector<Boolean> vector = new Vector<Boolean>();

    public SceneAdapter(Context context, int[] icon, int[] iconName, int[] icontwo) {
        this.context = context;
        this.icon = icon;
        this.iconName = iconName;
        this.icontwo = icontwo;
        for (int i = 0; i < icon.length; i++) {
            vector.add(false);
        }
    }

    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.macgrititem, null);
            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageitem_id);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.textitem_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textitem_id.setText(iconName[position]);
        if (vector.elementAt(position) == true) {
            mHolder.textitem_id.setTextColor(Color.parseColor("#e2c896"));
            mHolder.imageitem_id.setImageResource(icontwo[position]);
        } else {
            mHolder.imageitem_id.setImageResource(icon[position]);
            mHolder.textitem_id.setTextColor(Color.parseColor("#303030"));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageitem_id;
        TextView textitem_id;
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
