package com.zhongtianli.overalls.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.activity.DetailActivity;
import com.zhongtianli.overalls.bean.GetClothesBean;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by zhu on 2016/12/26.
 */

public class StoreAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private List<GetClothesBean.InClothes> InClothes =  new ArrayList<>();
    private  Context context;
    public StoreAdapter (Context context, GetClothesBean getClothesBean) {
        layoutInflater= LayoutInflater.from(context);
        this.context = context;
        if (getClothesBean.getInClothes() != null) {
            InClothes = getClothesBean.getInClothes();
        }
    }

    @Override
    public int getCount() {
        return InClothes.size();
    }

    @Override
    public Object getItem(int position) {
        return InClothes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.xlist_store_item,parent,false);
            holder = new ViewHolder();
            holder.name_label = (TextView) convertView.findViewById(R.id.name_label);//员工编号
            holder.inTime = (TextView) convertView.findViewById(R.id.inTime);//入货时间
            holder.yuan_gong_name = (TextView) convertView.findViewById(R.id.yuan_gong_name);//员工姓名
            holder.clothes_id = (TextView) convertView.findViewById(R.id.clothes_id);//衣服标签号
            //在这里添加item的点击事件
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name_label.setText(InClothes.get(position).getId());
        holder.clothes_id.setText(InClothes.get(position).getRFID());
        holder.inTime.setText(InClothes.get(position).getInTime());
        holder.yuan_gong_name.setText(InClothes.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情界面,把详情数据传到详情界面
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("InClothes",InClothes.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void setList(GetClothesBean getclothesBean) {
        this.InClothes = new ArrayList<>();
        if (getclothesBean.getInClothes() != null) {
            InClothes = getclothesBean.getInClothes();
        }
    }

    private class ViewHolder {
        private TextView name_label;//员工编号
        private  TextView inTime;//入货时间
        private  TextView yuan_gong_name;//员工姓名
        private  TextView clothes_id;//衣服标签号
    }
}
