package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.massky.sraum.R;


/**
 * Created by masskywcy on 2016-12-05.
 */
//用于添加场景adapter实现
public class MysceneactivityAdapter extends BaseAdapter {
    private int[] icon;
    private int[] icontwo;
    private Context context;
    private int defItem;//声明默认选中的项

    public MysceneactivityAdapter(Context context, int[] icon, int[] icontwo) {
        this.context = context;
        this.icon = icon;
        this.icontwo = icontwo;
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

    /**
     * 适配器中添加这个方法
     */
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mysceneactivityitem, null);
            mHolder.imageitemmyscene_id = (ImageView) convertView.findViewById(R.id.imageitemmyscene_id);
            mHolder.itemrelamyscene_id = (RelativeLayout) convertView.findViewById(R.id.itemrelamyscene_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0 || position == 1 || position == 4 || position == 5) {
            mHolder.itemrelamyscene_id.setVisibility(View.GONE);
        } else {
            mHolder.itemrelamyscene_id.setVisibility(View.VISIBLE);
            mHolder.imageitemmyscene_id.setImageResource(icon[position]);
            mHolder.itemrelamyscene_id.setBackgroundResource(R.drawable.add_scene_bg);
            if (defItem == position) {
                mHolder.imageitemmyscene_id.setImageResource(icontwo[position]);
                mHolder.itemrelamyscene_id.setBackgroundResource(R.drawable.add_scene_bg_checked);
            } else {
                mHolder.imageitemmyscene_id.setImageResource(icon[position]);
                mHolder.itemrelamyscene_id.setBackgroundResource(R.drawable.add_scene_bg);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageitemmyscene_id;
        RelativeLayout itemrelamyscene_id;
    }

}
