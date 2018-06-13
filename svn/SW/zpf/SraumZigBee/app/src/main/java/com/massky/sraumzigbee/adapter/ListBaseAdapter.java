package com.massky.sraumzigbee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.massky.sraumzigbee.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2017/3/3.
 */

public class ListBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> list = new ArrayList<>();

    public ListBaseAdapter(Context context, List<String> list
                           ) {
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.chuan_kou,parent,false);
            holder = new ViewHolder();
            holder.txt_chuan = (TextView)convertView.findViewById(R.id.txt_chuan);
            //在这里添加item的点击事件
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.txt_chuan.setText(list.get(position));
        return convertView;
    }

    public void setList(String list) {
        this.list.add(list.toString());
    }

    /**
     * 清空数据
     */
    public void setListEmpty() {
       this.list = new ArrayList<>();
    }

    private class ViewHolder{
        TextView txt_chuan;
    }
}
