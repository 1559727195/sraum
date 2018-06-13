package com.example.zhu.listview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, String>> list;
    private ListView listmView;
    private Button btn_s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInitView();
        getInitData();//ALT+shift+M产生方法
        initEvent();

    }

    private void initEvent() {
      listmView.setAdapter(new MyAdapter(this,list));
    }

    private void getInitView() {
        listmView = (ListView) findViewById(R.id.list_id);
        btn_s = (Button) findViewById(R.id.button1);
    }

    private void getInitData() {
        Map<String,String> map = new HashMap<>();

        map.put("1","li");
        map.put("2","xiao");
        map.put("3","ming");

        list = new ArrayList<>();
        list.add(map);

        map = new HashMap<>();

        map.put("1","li-1");
        map.put("2","xiao-1");
        map.put("3","ming-1");

        list.add(map);

        map = new HashMap<>();

        map.put("1","li-2");
        map.put("2","xiao-2");
        map.put("3","ming-2");
        list.add(map);

    }


    private class MyAdapter extends BaseAdapter {
        private  Context context;
        private  List<Map<String,String>> list;
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        public  MyAdapter (Context context,List<Map<String,String>> list){
            this.context = context;
            this.list = list;
            this.mInflater = LayoutInflater.from(context);
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
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item,null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.tv_1 = (TextView) convertView.findViewById(R.id.tv_1);
                holder.tv_2 = (TextView) convertView.findViewById(R.id.tv_2);
                holder.tv_3 = (TextView) convertView.findViewById(R.id.tv_3);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.tv_1.setText(list.get(position).get("1"));
            holder.tv_2.setText(list.get(position).get("2"));
            holder.tv_3.setText(list.get(position).get("3"));

           /* *//**为Button添加点击事件*//*
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("MyListViewBase", "你点击了按钮" + position);//打印Button的点击信息
                }
            });*/
            return convertView;
        }

    }

    /**存放控件*/
    public final class ViewHolder{
        public TextView tv_1;
        public TextView tv_2;
        public TextView tv_3;
    }

}
