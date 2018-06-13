package com.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.TokenUtil;
import com.base.BaseAdapter;
import com.data.User;
import com.example.swipemenuview.SwipeMenuLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.PaneldetailActivity;
import com.massky.sraum.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-05-16.
 */

public class PanelAdapter extends BaseAdapter {
    private final String accountType;
    private List<User.panellist> list;
    private  SlidingMenu mySlidingMenu;

    public PanelAdapter(Context context, List list, SlidingMenu mySlidingMenu, String accountType, PanelAdapterListener panelAdapterListener) {
        super(context, list);
        this.list = list;
        this.mySlidingMenu = mySlidingMenu;
        this.accountType = accountType;
        this.panelAdapterListener = panelAdapterListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.panelitem, null);
            mHolder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            mHolder.imageone = (ImageView) convertView.findViewById(R.id.imageone);
            mHolder.macname_id = (TextView) convertView.findViewById(R.id.macname_id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        String type = list.get(position).type;
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
            case "A501":
                mHolder.imageone.setImageResource(R.drawable.mianban05);
                break;
            case "A601":
                mHolder.imageone.setImageResource(R.drawable.mianban05);
                break;
            case "A701":
                mHolder.imageone.setImageResource(R.drawable.mianban01);
                break;
            default:
                mHolder.imageone.setImageResource(R.drawable.mianban04);
                break;
        }
//        mHolder.relative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User.panellist upl = list.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("panelmac", upl.mac);
//                bundle.putString("paneltype", upl.type);
//                bundle.putString("panelname", upl.name);
//                bundle.putString("panelnumber", upl.id);
//                IntentUtil.startActivity(context, PaneldetailActivity.class, bundle);
//            }
//        });
        mHolder.macname_id.setText(list.get(position).name);
        final View finalConvertView = convertView;
//        mHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //删除动画关闭
//                ((SwipeMenuLayout) finalConvertView).quickClose();
//                sraum_deletepanel();
//            }
//
//        });
        ((SwipeMenuLayout)convertView).setAccountType(accountType);
        ((SwipeMenuLayout)convertView).setPosition(position);
        ((SwipeMenuLayout)convertView).setOnMenuClickListener(new SwipeMenuLayout.OnMenuClickListener() {
            @Override
            public void onMenuClick(View v, int position) {
//                mAppList.remove(position);
                sraum_deletepanel(position);
//                SwipeMenuLayout.clearSideView();
//                mAdapter.notifyDataSetChanged();
//                delate_my_alarm(position,finalConvertView);
            }

            @Override
            public void onItemClick(View v, int position) {
//                backToFragmentListener.backtofragment(position);
                User.panellist upl = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("panelmac", upl.mac);
                bundle.putString("paneltype", upl.type);
                bundle.putString("panelname", upl.name);
                bundle.putString("panelnumber", upl.id);
                IntentUtil.startActivity(context, PaneldetailActivity.class, bundle);
            }

            @Override
            public void onInterceptTouch() {//和slidemenu争夺事件权限
                mySlidingMenu.setTouchModeAbove(
                        SlidingMenu.TOUCHMODE_NONE);
            }

            @Override
            public void onInterceptTouch_end() {
                mySlidingMenu.setTouchModeAbove(
                        SlidingMenu.TOUCHMODE_FULLSCREEN);
            }
        });
        return convertView;
    }

    class ViewHolder {
        Button btnDelete;
        ImageView imageone;
        TextView macname_id;
        RelativeLayout relative;
    }

    private void sraum_deletepanel(final int position) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", TokenUtil.getToken(context));
        map.put("boxNumber", TokenUtil.getBoxnumber(context));
        map.put("panelNumber", list.get(position).id);
        MyOkHttp.postMapObject(ApiHelper.sraum_deletePanel, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_deletepanel(position);
                    }
                }, context, null) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        List<User.panellist> lup = new ArrayList<User.panellist>();
                        lup.add(list.get(position));
                        list.removeAll(lup);
                        notifyDataSetChanged();
                        SwipeMenuLayout.clearSideView();
                        if (panelAdapterListener != null)
                            panelAdapterListener.panel_adapter_listener();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }
    private  PanelAdapterListener panelAdapterListener;
    public interface PanelAdapterListener {
        void   panel_adapter_listener();
    }
}
