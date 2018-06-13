package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.Util.LogUtil;
import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by masskywcy on 2017-03-22.
 */
//关联面板适配器管理
public class AsccociatedpanelAdapter extends BaseAdapter {
    private List<User.panellist> list;
    private List<Boolean> checkList;

    public AsccociatedpanelAdapter(Context context, List list, List<Boolean> checkList) {
        super(context, list);
        this.list = list;
        this.checkList = checkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.addsignitem, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        String type = list.get(position).type.toString().trim();
        String name = list.get(position).name.toString().trim();
        LogUtil.eLength("数据查看", name + "数据查看" + type);
        switch (type) {
            case "A201":
                mHolder.imageone.setImageResource(R.drawable.mianban01);
                break;
            case "A202":
                mHolder.imageone.setImageResource(R.drawable.mianban02);
                break;
            case "A203":
                mHolder.imageone.setImageResource(R.drawable.mianban03);
                break;
            case "A204":
                mHolder.imageone.setImageResource(R.drawable.mianban04);
                break;
            case "A302":
                mHolder.imageone.setImageResource(R.drawable.mianban04);
                break;
            case "A301":
                mHolder.imageone.setImageResource(R.drawable.mianban04);
                break;
            default:
                mHolder.imageone.setImageResource(R.drawable.mianban04);
        }
        mHolder.macname_id.setText(list.get(position).name);
        mHolder.cb.setChecked(checkList.get(position));
        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.imageone)
        ImageView imageone;
        @InjectView(R.id.macname_id)
        TextView macname_id;
        @InjectView(R.id.checkbox)
        CheckBox cb;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
