package com.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.massky.sraum.R;

/**
 * Created by masskywcy on 2016-09-06.
 */
//用于第一个界面gritview  adapter
public class SelectWifiDevAdapter extends BaseAdapter {
    private Context context;
    private int[] icon;
    private int[] iconName;
//    private List<User.device> list;

//    public SelectDevTypeAdapter(Context context, List<Map> list) {
//        super(context, list);
//    }

    public SelectWifiDevAdapter(Context context, int[] icon, int[] iconName) {
        this.context = context;
        this.icon = icon;
        this.iconName = iconName;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.selwifi_typ_item, null);
            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageitem_id);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.textitem_id);
            mHolder.itemrela_id = (RelativeLayout) convertView.findViewById(R.id.itemrela_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.imageitem_id.setImageResource(icon[position]);
        mHolder.textitem_id.setText(iconName[position]);

        return convertView;
    }

    class ViewHolder {
        ImageView imageitem_id;
        TextView textitem_id;
        RelativeLayout itemrela_id;
    }
}
