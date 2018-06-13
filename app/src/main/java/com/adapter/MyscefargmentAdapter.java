package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;

import java.util.ArrayList;
import java.util.List;

import static cn.ciaapp.sdk.CIAService.context;


/**
 * Created by masskywcy on 2017-03-21.
 */
//我的场景adapter
public class MyscefargmentAdapter extends android.widget.BaseAdapter {
    private List<User.scenelist> list = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private boolean viewFlag;
    private  Context context;

    public MyscefargmentAdapter(Context context, List list, List<Integer> listint,
                                List<Integer> listintwo, boolean viewFlag) {
        this.list = list;
        this.listint = listint;
        this.listintwo = listintwo;
        this.viewFlag = viewFlag;
        this.context = context;
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
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gritviewitem, null);
            mHolder.gitemimage = (ImageView) convertView.findViewById(R.id.gitemimage);
            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageitem_id);
            mHolder.gitemtext = (TextView) convertView.findViewById(R.id.gitemtext);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.textitem_id);
            mHolder.relativegrit_id = (RelativeLayout) convertView.findViewById(R.id.relativegrit_id);
            mHolder.itemrela_id = (RelativeLayout) convertView.findViewById(R.id.itemrela_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        if (viewFlag) {
            mHolder.itemrela_id.setVisibility(View.VISIBLE);
            mHolder.relativegrit_id.setVisibility(View.GONE);
        } else {
            mHolder.itemrela_id.setVisibility(View.GONE);
            mHolder.relativegrit_id.setVisibility(View.VISIBLE);
        }
//        if (list.get(position).sceneStatus.equals("1")) {
//            mHolder.gitemtext.setTextColor(Color.parseColor("#e2c896"));
//            mHolder.textitem_id.setTextColor(Color.parseColor("#e2c896"));
//            mHolder.gitemimage.setImageResource(listintwo.get(position));
//            mHolder.imageitem_id.setImageResource(listintwo.get(position));
//        } else {
//            mHolder.gitemtext.setTextColor(Color.parseColor("#303030"));
//            mHolder.textitem_id.setTextColor(Color.parseColor("#303030"));
//            mHolder.gitemimage.setImageResource(listint.get(position));
//            mHolder.imageitem_id.setImageResource(listint.get(position));
//        }
        mHolder.textitem_id.setText(list.get(position).name);
        mHolder.gitemtext.setText(list.get(position).name);
        return convertView;
    }

    public void setList_s(List<User.scenelist> scenelist, List<Integer> listint, List<Integer> listintwo, boolean b) {
        this.list = new ArrayList<>();
        this.listint = new ArrayList<>();
        this.listintwo = new ArrayList<>();
        this.list = scenelist;
        this.listint = listint;
        this.listintwo = listintwo;
        this.viewFlag = b;
    }

    class ViewHolder {
        ImageView gitemimage, imageitem_id;
        TextView gitemtext, textitem_id;
        RelativeLayout relativegrit_id, itemrela_id;
    }

}