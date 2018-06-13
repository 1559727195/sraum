package com.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.massky.sraum.R;

import java.util.HashMap;

/**
 * 项目名称 MyApplication
 * 创建时间 2016/12/14
 * TODO xxxx
 * Author mi
 */

public class ListViewEditeAdapter extends BaseAdapter {


    public HashMap<Integer, String> contents = new HashMap<>();

    public ListViewEditeAdapter() {
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ListviewEditViewHolder holder = null;
        if (view == null) {
            holder = new ListviewEditViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_edit_item_layout, viewGroup, false));
            view = holder.itemView;
            view.setTag(holder);
            holder.edt.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
        } else {
            holder = (ListviewEditViewHolder) view.getTag();
        }

        holder.edt.setTag(i);//注意设置position

        if (!TextUtils.isEmpty(contents.get(i))) {//不为空的时候 赋值给对应的edittext
            holder.edt.setText(contents.get(i));
        } else {//置空
            holder.edt.getEditableText().clear();
        }
        return view;
    }


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
                int position = (int) holder.edt.getTag();
                contents.put(position,editable.toString());
            }
        }
    }



    public static class ListviewEditViewHolder {

        public View itemView;
        public TextView tv;
        public EditText edt;

        public ListviewEditViewHolder(View v) {
            itemView = v;
            tv = (TextView) v.findViewById(R.id.tv);
            edt = (EditText) v.findViewById(R.id.edittext);
        }
    }
}