package com.massky.sraum;

import android.view.View;
import android.widget.ListView;
import com.adapter.ListViewEditeAdapter;
import com.adapter.PanelDifereTypeAdapter;
import com.base.Basecactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by zhu on 2017/10/27.
 */

public class ListViewChangeItemActiivty extends Basecactivity{
    @InjectView(R.id.listview)
    ListView listview;
    @Override
    protected int viewId() {
        return R.layout.listview_change_item_act;
    }

    @Override
    protected void onView() {
//        ListViewEditeAdapter listViewEditeAdapter = new ListViewEditeAdapter();
//        listview.setAdapter(listViewEditeAdapter);
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map map = new HashMap();
            map.put("type","4");
            list.add(map);
        }

        PanelDifereTypeAdapter panelDifereTypeAdapter = new PanelDifereTypeAdapter(this,list);
        listview.setAdapter(panelDifereTypeAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
