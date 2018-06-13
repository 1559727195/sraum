package com.wujay.fund.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wujay.fund.common.AppUtil;
import com.wujay.fund.common.Constants;
import com.wujay.fund.entity.GesturePoint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 手势密码路径绘制
 *
 */
public class GestureDrawline extends View {
	private int mov_x;// 声明起点坐标
	private int mov_y;
	private Paint paint;// 声明画笔
	private Canvas canvas;// 画布
	private Bitmap bitmap;// 位图
	private List<GesturePoint> list;// 装有各个view坐标的集合
	private List<Pair<GesturePoint, GesturePoint>> lineList;// 记录画过的线
	private Map<String, GesturePoint> autoCheckPointMap;// 自动选中的情况点
	private boolean isDrawEnable = true; // 是否允许绘制


	/**
	 * 屏幕的宽度和高度
	 */
	private int[] screenDispaly;

	/**
	 * 手指当前在哪个Point内
	 */
	private GesturePoint currentPoint;
	/**
	 * 用户绘图的回调
	 */
	private GestureCallBack callBack;

	/**
	 * 用户当前绘制的图形密码
	 */
	private StringBuilder passWordSb;

	/**
	 * 是否为校验
	 */
	private boolean isVerify;

	/**
	 * 用户传入的passWord
	 */
	private String passWord;

	public GestureDrawline(Context context, List<GesturePoint> list, boolean isVerify,
			String passWord, GestureCallBack callBack) {
		super(context);
		screenDispaly = AppUtil.getScreenDispaly(context);
		paint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔
		bitmap = Bitmap.createBitmap(screenDispaly[0], screenDispaly[0], Bitmap.Config.ARGB_8888);
		canvas = new Canvas();
		canvas.setBitmap(bitmap);
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth(10);// 笔宽5像素
		paint.setColor(Color.rgb(245, 142, 33));// 设置默认连线颜色
		paint.setAntiAlias(true);// 不显示锯齿

		this.list = list;
		this.lineList = new ArrayList<Pair<GesturePoint, GesturePoint>>();
		
		initAutoCheckPointMap();
		this.callBack = callBack;

		// ��ʼ�����뻺��
		this.isVerify = isVerify;
		this.passWordSb = new StringBuilder();
		this.passWord = passWord;
	}
	
	private void initAutoCheckPointMap() {
		autoCheckPointMap = new HashMap<String,GesturePoint>();
		autoCheckPointMap.put("1,3", getGesturePointByNum(2));
		autoCheckPointMap.put("1,7", getGesturePointByNum(4));
		autoCheckPointMap.put("1,9", getGesturePointByNum(5));
		autoCheckPointMap.put("2,8", getGesturePointByNum(5));
		autoCheckPointMap.put("3,7", getGesturePointByNum(5));
		autoCheckPointMap.put("3,9", getGesturePointByNum(6));
		autoCheckPointMap.put("4,6", getGesturePointByNum(5));
		autoCheckPointMap.put("7,9", getGesturePointByNum(8));
	}
	
	private GesturePoint getGesturePointByNum(int num) {
		for (GesturePoint point : list) {
			if (point.getNum() == num) {
				return point;
			}
		}
		return null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isDrawEnable == false) {
			return true;
		}
		paint.setColor(Color.rgb(245, 142, 33));
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mov_x = (int) event.getX();
			mov_y = (int) event.getY();
			currentPoint = getPointAt(mov_x, mov_y);
			if (currentPoint != null) {
				currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
				passWordSb.append(currentPoint.getNum());
			}
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			clearScreenAndDrawList();

			GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
			// ����ǰ�û���ָ���ڵ����֮ǰ
			if (currentPoint == null && pointAt == null) {
				return true;
			} else {
				if (currentPoint == null) {
					currentPoint = pointAt;
					currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
					passWordSb.append(currentPoint.getNum());
				}
			}
			if (pointAt == null || currentPoint.equals(pointAt) || Constants.POINT_STATE_SELECTED == pointAt.getPointState()) {
				canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), event.getX(), event.getY(), paint);
			} else {
				canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), pointAt.getCenterX(), pointAt.getCenterY(), paint);// ����
				pointAt.setPointState(Constants.POINT_STATE_SELECTED);

				GesturePoint betweenPoint = getBetweenCheckPoint(currentPoint, pointAt);
				if (betweenPoint != null && Constants.POINT_STATE_SELECTED != betweenPoint.getPointState()) {
					Pair<GesturePoint, GesturePoint> pair1 = new Pair<GesturePoint, GesturePoint>(currentPoint, betweenPoint);
					lineList.add(pair1);
					passWordSb.append(betweenPoint.getNum());
					Pair<GesturePoint, GesturePoint> pair2 = new Pair<GesturePoint, GesturePoint>(betweenPoint, pointAt);
					lineList.add(pair2);
					passWordSb.append(pointAt.getNum());
					betweenPoint.setPointState(Constants.POINT_STATE_SELECTED);
					currentPoint = pointAt;
				} else {
					Pair<GesturePoint, GesturePoint> pair = new Pair<GesturePoint, GesturePoint>(currentPoint, pointAt);
					lineList.add(pair);
					passWordSb.append(pointAt.getNum());
					currentPoint = pointAt;
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if (isVerify) {
				if (passWord.equals(passWordSb.toString())) {
					callBack.checkedSuccess();
				} else {
					callBack.checkedFail();
				}
			} else {
				callBack.onGestureCodeInput(passWordSb.toString());
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 指定时间去清除绘制的状态
	 * @param delayTime 延迟执行时间
	 */
	public void clearDrawlineState(long delayTime) {
		if (delayTime > 0) {
			isDrawEnable = false;
			drawErrorPathTip();
		}
		new Handler().postDelayed(new clearStateRunnable(), delayTime);
	}

	/**
	 * 清除绘制状态的线程
	 */
	final class clearStateRunnable implements Runnable {
		public void run() {
			// 重置passWordSb
			passWordSb = new StringBuilder();
			// 清空保存点的集合
			lineList.clear();
			// 重新绘制界面
			clearScreenAndDrawList();
			for (GesturePoint p : list) {
				p.setPointState(Constants.POINT_STATE_NORMAL);
			}
			invalidate();
			isDrawEnable = true;
		}
	}

	/**
	 * 通过点的位置去集合里面查找这个点是包含在哪个Point里面的
	 *
	 * @param x
	 * @param y
	 * @return 如果没有找到，则返回null，代表用户当前移动的地方属于点与点之间
	 */
	private GesturePoint getPointAt(int x, int y) {

		for (GesturePoint point : list) {
			// 先判断x
			int leftX = point.getLeftX();
			int rightX = point.getRightX();
			if (!(x >= leftX && x < rightX)) {
				continue;
			}

			int topY = point.getTopY();
			int bottomY = point.getBottomY();
			if (!(y >= topY && y < bottomY)) {
				continue;
			}
			return point;
		}

		return null;
	}
	
	private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
		int startNum = pointStart.getNum();
		int endNum = pointEnd.getNum();
		String key = null;
		if (startNum < endNum) {
			key = startNum + "," + endNum;
		} else {
			key = endNum + "," + startNum;
		}
		return autoCheckPointMap.get(key);
	}

	/**
	 * 清掉屏幕上所有的线，然后画出集合里面的线
	 */
	private void clearScreenAndDrawList() {
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		for (Pair<GesturePoint, GesturePoint> pair : lineList) {
			canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
					pair.second.getCenterX(), pair.second.getCenterY(), paint);
		}
	}

	/**
	 * 校验错误/两次绘制不一致提示
	 */
	private void drawErrorPathTip() {
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		paint.setColor(Color.rgb(154, 7, 21));
		for (Pair<GesturePoint, GesturePoint> pair : lineList) {
			pair.first.setPointState(Constants.POINT_STATE_WRONG);
			pair.second.setPointState(Constants.POINT_STATE_WRONG);
			canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
					pair.second.getCenterX(), pair.second.getCenterY(), paint);
		}
		invalidate();
	}


	public interface GestureCallBack {

		/**
		 * 用户设置/输入了手势密码
		 */
		public abstract void onGestureCodeInput(String inputCode);

		/**
		 * 代表用户绘制的密码与传入的密码相同
		 */
		public abstract void checkedSuccess();

		/**
		 * 代表用户绘制的密码与传入的密码不相同
		 */
		public abstract void checkedFail();
	}

}
