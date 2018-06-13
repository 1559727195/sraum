package com.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.Util.ClearEditText;
import com.data.User;
import com.massky.sraum.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.smssdk.gui.layout.Res.id.tv_name;

/**
 * Created by zhu on 2017/10/30.
 */

public class PanelDifereTypeAdapter extends BaseAdapter {
    private Context context;
    private List<Map> list;
    private LayoutInflater inflater;

    public HashMap<Integer, String> contents = new HashMap<>();

    public PanelDifereTypeAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
    public int getItemViewType(int position) {
      String item = (String) list.get(position).get("type");
        if (item.equals("4")) {//0窗帘
            return 0;
        } else {
            return 1;//其他
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Object item = getItem(position);

        int type = getItemViewType(position);

//        switch (type) {
//            case 0:
//                UserBean userBean = (UserBean) item;
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.item_light_type, parent, false);
//                } else {
//
//                }
//                TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
//                TextView tv_age = ViewHolder.get(convertView, R.id.tv_age);
//                TextView tv_sex = ViewHolder.get(convertView, R.id.tv_sex);
//
//                tv_name.setText(userBean.getName());
//                tv_age.setText(userBean.getAge());
//                tv_sex.setText(userBean.getSex());
//
//                break;
//            case 1:
//                CompanyBean comBean = (CompanyBean) item;
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.item_window_type, parent, false);
//                }
//                TextView tv_company = ViewHolder.get(convertView, R.id.tv_company);
//                TextView tv_position = ViewHolder.get(convertView, R.id.tv_position);
//
//                tv_company.setText(comBean.getCompany());
//                tv_position.setText(comBean.getPosition());
//                break;
//        }


        ListviewEditViewHolder holder = null;
        switch (type) {
            case 0://窗帘
                if (view == null) {
                    holder = new ListviewEditViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_window_type, viewGroup, false));
                    view = holder.itemView;
                    view.setTag(holder);
                    holder.editextone.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
                    holder.editexthree.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
                    holder.editextfour.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
                } else {
                    holder = (ListviewEditViewHolder) view.getTag();
                }
                Map map = new HashMap();
                map.put("position",position);
                map.put("edit","editextone");
                holder.editextone.setTag(map);//注意设置position
                if (!TextUtils.isEmpty(contents.get(position))) {//不为空的时候 赋值给对应的edittext
                    holder.editextone.setText(contents.get(position));
                } else {//置空
                    holder.editextone.getEditableText().clear();
                }

                Map map_three = new HashMap();
                map.put("position",position);
                map.put("edit","editextthree");
                holder.editexthree.setTag(map_three);//注意设置position
                if (!TextUtils.isEmpty(contents.get(position))) {//不为空的时候 赋值给对应的edittext
                    holder.editexthree.setText(contents.get(position));
                } else {//置空
                    holder.editexthree.getEditableText().clear();
                }

                Map map_four = new HashMap();
                map.put("position",position);
                map.put("edit","editextfour");
                holder.editextfour.setTag(map_four);//注意设置position
                if (!TextUtils.isEmpty(contents.get(position))) {//不为空的时候 赋值给对应的edittext
                    holder.editextfour.setText(contents.get(position));
                } else {//置空
                    holder.editextfour.getEditableText().clear();
                }

                break;

            case 1://灯控
                if (view == null) {
                    holder = new ListviewEditViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_light_type, viewGroup, false));
                    view = holder.itemView;
                    view.setTag(holder);
                    holder.editextone.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
                } else {
                    holder = (ListviewEditViewHolder) view.getTag();
                }

                holder.editextone.setTag(position);//注意设置position
                break;
        }

        return view;
    }

//    class ViewHolder {
//        TextView macdethreetexttwo;//macdethreetexttwo
////        ImageView imageone;//editextone ,editexthree,editextfour
//        ClearEditText editextone;
//        ClearEditText editexthree;
//        ClearEditText editextfour;
//    }

    public class MyTextChangedListener implements TextWatcher {

        public ListviewEditViewHolder holder;
        public HashMap<Integer, String> contents;

        public MyTextChangedListener(ListviewEditViewHolder holder,HashMap<Integer, String> contents){
            this.holder = holder;
            this.contents = contents;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(holder != null && contents != null){
                Map map = (HashMap) holder.editextone.getTag();
//                contents.put(position,editable.toString());
            }
            Log.e("fei","editable:" + editable.toString());
        }
    }



    public static class ListviewEditViewHolder {

        public View itemView;
//        public TextView tv;
//        public EditText edt;

        TextView macdethreetexttwo;//macdethreetexttwo
        //        ImageView imageone;//editextone ,editexthree,editextfour
        ClearEditText editextone;
        ClearEditText editexthree;
        ClearEditText editextfour;

        public ListviewEditViewHolder(View v) {
            itemView = v;
            editextone = (ClearEditText) v.findViewById(R.id.editextone);
            editexthree = (ClearEditText) v.findViewById(R.id.editexthree);
            editextfour = (ClearEditText) v.findViewById(R.id.editextfour);
            macdethreetexttwo = (TextView) v.findViewById(R.id.macdethreetexttwo);
        }
    }
}
