package com.zhongtianli.overalls.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.activity.DetailActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/12/26.
 */

public class TakeOutAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private  List<String> list = new ArrayList<>();
    private  Context context;
    public TakeOutAdapter(Context context, List<String> list) {
        layoutInflater= LayoutInflater.from(context);
        this.context = context;
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
            convertView= layoutInflater.inflate(R.layout.xlist_take_out_item,parent,false);
            holder = new ViewHolder();
            holder.name_take_out = (TextView) convertView.findViewById(R.id.name_take_out);//自定义的textView
            //在这里添加item的点击事件
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.name_take_out.setText(list.get(position));
         convertView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //跳转到详情界面
                 context.startActivity(new Intent(context,DetailActivity.class));
             }
         });
        return convertView;
    }

    private class ViewHolder {
           TextView name_take_out;
    }
}
