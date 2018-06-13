package com.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.massky.sraum.R;
//用于我的房间自定义横向listview  adapter
public class HorizontalListViewAdaptertwo extends BaseAdapter{
    private int[] mIconIDs;
    private int[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdaptertwo(Context context, int[] titles, int[] ids){
        this.mContext = context;
        this.mIconIDs = ids;
        this.mTitles = titles;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mIconIDs.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.myroomitem, null);
            holder.mrrelativetwo=(RelativeLayout) convertView.findViewById(R.id.mrrelativetwo);
            holder.mrimage_id=(ImageView)convertView.findViewById(R.id.mrimage_id);
            holder.mTitle=(TextView)convertView.findViewById(R.id.retextone_id);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.mTitle.setText(mTitles[position]);
        holder.mrimage_id.setImageResource(mIconIDs[position]);
        if(position == selectIndex){
            convertView.setSelected(true);
            holder.mTitle.setTextColor(Color.parseColor("#e2c896"));
            holder.mrrelativetwo.setBackgroundResource(R.drawable.smallcirclegold);
        }else{
            convertView.setSelected(false);
            holder.mTitle.setTextColor(Color.parseColor("#303030"));
            holder.mrrelativetwo.setBackgroundResource(R.drawable.smallcircle);
        }
        return convertView;
    }
    private static class ViewHolder {
        private TextView mTitle ;
        private ImageView mrimage_id;
        private RelativeLayout mrrelativetwo;
    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}