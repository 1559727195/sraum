package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseAdapter;
import com.massky.sraum.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhu on 2017/7/4.
 */

public class ShowUdpServerDetailAdapter extends BaseAdapter {

    public ShowUdpServerDetailAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.show_udp_rev_item, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.show_txt_udp.setText(getList().get(position).toString());
        return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.txt_udp)
        TextView show_txt_udp;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
