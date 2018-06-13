package com.massky.sraum;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.adapter.HostoryPmListAdapter;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by zhu on 2017/12/29.
 */

public class HostoryPmActivity extends Basecactivity{
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.chart)
    LineChartView chart;
    private static final String TAG = "MessageSendActivity";

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private Axis axisX;
    private Axis axisY;
    private ProgressDialog progressDialog;
    private boolean isBiss;
    @InjectView(R.id.xListView_scan)
    ListView xListView_scan;
    private List<Map> list_hand_scene;
    private HostoryPmListAdapter hostory_pmlist_adapter;


    @Override
    protected int viewId() {
        return R.layout.hostory_pm_act;
    }

    @Override
    protected void onView() {
//        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
//            statusView.setBackgroundColor(Color.BLACK);
//        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        hostory_pm_list();
        progressDialog = new ProgressDialog(this);
        generateData(7);
    }



    /**
     * pm历史数据
     */
    private void hostory_pm_list() {
        list_hand_scene = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map map = new HashMap();
            map.put("date", "11月19日");
            map.put("time", "19:57");
            map.put("pm2.5", "37/ug/m³");
            map.put("temp", "26.9℃");
            map.put("shidu", "56.3%");
            list_hand_scene.add(map);
        }

        hostory_pmlist_adapter = new HostoryPmListAdapter(
                HostoryPmActivity.this,
                list_hand_scene);
        xListView_scan.setAdapter(hostory_pmlist_adapter);
    }

//    private void init_permissions() {
//
//        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
//        RxPermissions permissions = new RxPermissions(this);
//        permissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                HostoryPmActivity.this.finish();
                break;
        }
    }

    /**
     * 动态添加点到图表
     *
     * @param count
     */
    private void addDataPoint(int count) {
        Line line = chart.getLineChartData().getLines().get(0);
        List<PointValue> values = line.getValues();
        int startIndex = values.size();

        for (int i = 0; i < count; i++) {
            int newIndex = startIndex + i;
            Log.i(TAG, "addDataPoint: newIndex=" + newIndex);
            values.add(new PointValue(newIndex, (float) Math.random() * 100f));
        }

        line.setValues(values);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData lineData = new LineChartData(lines);
        lineData.setAxisXBottom(axisX);
        lineData.setAxisYLeft(axisY);
        chart.setLineChartData(lineData);

        //根据点的横坐标实时变换X坐标轴的视图范围
        Viewport port = initViewPort(startIndex + 1, startIndex + 7);
//        chart.setMaximumViewport(port);
        chart.setCurrentViewport(port);

        final float firstXValue = values.get(values.size() - 1).getX();
        //向右拉取本月数据
        //向左拉取历史数据
        chart.setViewportChangeListener(new ViewportChangeListener() {
            @Override
            public void onViewportChanged(Viewport viewport) {
                Log.i(TAG, "onViewportChanged: " + viewport.toString());
                if (!isBiss && viewport.right == firstXValue) {
                    isBiss = true;
                    loadData();
                }
            }
        });
    }

    /**
     * 模拟网络请求动态加载数据
     */
    private void loadData() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDataPoint(10);
                            isBiss = false;
                            progressDialog.dismiss();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置视图
     *
     * @param left
     * @param right
     * @return
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 200;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        port.left = left;//X轴左边界，变化
        port.right = right;//X轴右边界，变化
        return port;
    }

    /**
     * 初始化图表
     *
     * @param numberOfPoints 初始数据
     */
    private void generateData(int numberOfPoints) {

        List<Line> lines = new ArrayList<Line>();
        int numberOfLines = 1;
        List<PointValue> values = null;
        for (int i = 0; i < numberOfLines; ++i) {

            values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; j++) {//PointValue [x=-1.0, y=94.90094]
                int newIndex = j * 1;
                Log.i(TAG, "generateData: newIndex=" + newIndex);
                values.add(new PointValue(newIndex + 1, (float) Math.random() * 200f));
            }

            Line line = new Line(values);
            line.setColor(Color.GRAY);//设置折线颜色
            line.setStrokeWidth(1);//设置折线宽度
            line.setFilled(false);//设置折线覆盖区域颜色
            line.setCubic(false);//节点之间的过渡
            line.setPointColor(getResources().getColor(R.color.colororiange));//设置节点颜色
            line.setPointRadius(10);//设置节点半径
            line.setHasLabels(true);//是否显示节点数据
            line.setHasLines(true);//是否显示折线
            line.setHasPoints(true);//是否显示节点
            line.setShape(ValueShape.CIRCLE);//节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
            line.setHasLabelsOnlyForSelected(false);//隐藏数据，触摸可以显示
            lines.add(line);
        }

        LineChartData data = new LineChartData(lines);

        if (hasAxes) {
            axisX = new Axis();
            axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("星期");
                axisX.setInside(false);
                axisY.setName("pm2.5");
                axisX.setHasLines(true);//是否显示X轴网格线
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }


        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

        final float firstXValue = values.get(values.size() - 1).getX();
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.top = 200;
        v.bottom = 0;
        v.left = 0;
        v.right = 7;//写死每个Viewport的区间范围0 - 9
        chart.setCurrentViewport(v);
        chart.setViewportChangeListener(new ViewportChangeListener() {
            @Override
            public void onViewportChanged(Viewport viewport) {
                Log.i(TAG, "onViewportChanged: " + viewport.toString());
                if (!isBiss && viewport.right == firstXValue) {//历史数据
                    isBiss = true;
                    loadData();
                }
            }
        });
    }
}
