package com.testtime;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.massky.sraum.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author xufuchao
 * @version 创建时间：2016-7-9 下午8:11:41 类说明
 */
public class DateTimeTwoPickDialog {
	private Calendar calendar = Calendar.getInstance();
	private int curYear, curMonth, curDay, curHour, curMinute;
	private List<String> yearList = new ArrayList<String>();

	private String yearArray[] = new String[] {};
	private String monthArray[] = new String[] {};
	private String day30Array[] = new String[] {};
	private String day31Array[] = new String[] {};
	private String day28Array[] = new String[] {};
	private String day29Array[] = new String[] {};
	private String hourArray[] = new String[] {};
	private String minuteArray[] = new String[] {};

	private WheelView yearWV, monthWV, dayWV, hourWV, minuteWV;
	private View dateTimeLayout;
	private TextView titleTv, dateTv;
	private Button cancelBtn, confirmBtn;

	private PopupWindow popupWindow;
	
	public DateTimeTwoPickDialog(String time) {
		
		curYear=Integer.parseInt(DateUtil.ToYear(time));
		curMonth = Integer.parseInt(DateUtil.ToMonth(time))-1;
		curDay = Integer.parseInt(DateUtil.ToDay(time));
		curHour = Integer.parseInt(DateUtil.ToHour(time));
		curMinute = Integer.parseInt(DateUtil.ToMinute(time))-1;
//		curHour = calendar.get(Calendar.HOUR_OF_DAY);
//		curMinute = calendar.get(Calendar.MINUTE);

		int startYear = curYear - 50;
		int endYear = curYear + 50;
		for (int i = startYear; i < endYear; i++) {
			yearList.add(String.valueOf(i));
		}

		yearArray = (String[]) yearList.toArray(new String[0]);
		monthArray = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12" };
		day31Array = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28", "29", "30", "31" };
		day30Array = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28", "29", "30" };
		day28Array = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28" };
		day29Array = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28", "29" };
		hourArray = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "00" };
		minuteArray = new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28", "29", "30", "31", "32", "33", "34", "35", "36", "37",
				"38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
				"48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
				"58", "59", "00" };
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param view
	 *            需要设置的日期时间view
	 * @param title
	 *            弹出框显示的标题
	 * @return
	 */
	public void dateTimePicKDialog(final Context context, final View view,
			String title) {

		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		dateTimeLayout = mLayoutInflater.inflate(
				R.layout.layout_popup_datepicker, null);
		popupWindow = new PopupWindow(dateTimeLayout,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		yearWV = (WheelView) dateTimeLayout.findViewById(R.id.year_wv);
		monthWV = (WheelView) dateTimeLayout.findViewById(R.id.month_wv);
		dayWV = (WheelView) dateTimeLayout.findViewById(R.id.day_wv);
		hourWV = (WheelView) dateTimeLayout.findViewById(R.id.hh_wv);
		minuteWV = (WheelView) dateTimeLayout.findViewById(R.id.mm_wv);

		yearWV.setVisibleItems(5);
		yearWV.setCyclic(true);
		yearWV.setLabel("年");
		yearWV.setAdapter(new ArrayWheelAdapter<String>(yearArray));

		monthWV.setVisibleItems(5);
		monthWV.setCyclic(true);
		monthWV.setLabel("月");
		monthWV.setAdapter(new ArrayWheelAdapter<String>(monthArray));

		dayWV.setVisibleItems(5);
		dayWV.setCyclic(true);
		dayWV.setLabel("日");
		dayWV.setAdapter(new ArrayWheelAdapter<String>(day31Array));

		hourWV.setVisibleItems(5);
		hourWV.setCyclic(true);
		hourWV.setLabel("时");
		hourWV.setAdapter(new ArrayWheelAdapter<String>(hourArray));

		minuteWV.setVisibleItems(5);
		minuteWV.setCyclic(true);
		minuteWV.setLabel("分");
		minuteWV.setAdapter(new ArrayWheelAdapter<String>(minuteArray));

		titleTv = (TextView) dateTimeLayout.findViewById(R.id.title_tv);
		dateTv = (TextView) dateTimeLayout.findViewById(R.id.date_tv);
		if (TextUtils.isEmpty(title)) {
			titleTv.setText("选择日期");
		} else {
			titleTv.setText(title);
		}

		yearWV.setCurrentItem(50); // 当前年份。
		monthWV.setCurrentItem(curMonth); // 当前月份。
		dayWV.setCurrentItem(curDay - 1); // 需-1才是当前天。
		hourWV.setCurrentItem(curHour - 1);// 需-1才是当时刻。
		minuteWV.setCurrentItem(curMinute);

		int yearIndex = yearWV.getCurrentItem();
		int monthIndex = monthWV.getCurrentItem();
		int dayIndex = dayWV.getCurrentItem();
		int hhIndex = hourWV.getCurrentItem();
		int mmIndex = minuteWV.getCurrentItem();

		// 显示当前年月日
		dateTv.setText(yearArray[yearIndex]
				+ "年"
				+ monthArray[monthIndex]
				+ "月"
				+ day31Array[dayIndex]
				+ "日"
				+ hourArray[hhIndex]
				+ "时"
				+ minuteArray[mmIndex]
				+ "分"
				+ DateUtil.getWeek(yearArray[yearIndex] + "-"
						+ monthArray[monthIndex] + "-" + day31Array[dayIndex]));

		cancelBtn = (Button) dateTimeLayout.findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		confirmBtn = (Button) dateTimeLayout.findViewById(R.id.confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				if (view instanceof TextView) {
					((TextView) view).setText(year.trim() + "-" + month.trim()
							+ "-" + day.trim() + " " + hour.trim() + ":"
							+ minute.trim());
				} else if (view instanceof EditText) {
					((EditText) view).setText(year.trim() + "-" + month.trim()
							+ "-" + day.trim() + " " + hour.trim() + ":"
							+ minute.trim());
				}

				popupWindow.dismiss();

				// if (!compareDate()) {
				// txtaddwangdai09.setText(productDate());
				// popupWindow.dismiss();
				// } else {
				// Toast.makeText(MainActivity.this, "结束日期必须大于开始日期！",
				// Toast.LENGTH_SHORT).show();
				// }
			}
		});

		hourWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				if (hour.equalsIgnoreCase("01") || hour.equalsIgnoreCase("02")
						|| hour.equalsIgnoreCase("03")
						|| hour.equalsIgnoreCase("04")
						|| hour.equalsIgnoreCase("05")
						|| hour.equalsIgnoreCase("06")
						|| hour.equalsIgnoreCase("07")
						|| hour.equalsIgnoreCase("08")
						|| hour.equalsIgnoreCase("09")
						|| hour.equalsIgnoreCase("10")
						|| hour.equalsIgnoreCase("11")
						|| hour.equalsIgnoreCase("12")
						|| hour.equalsIgnoreCase("13")
						|| hour.equalsIgnoreCase("14")
						|| hour.equalsIgnoreCase("15")
						|| hour.equalsIgnoreCase("16")
						|| hour.equalsIgnoreCase("17")
						|| hour.equalsIgnoreCase("18")
						|| hour.equalsIgnoreCase("19")
						|| hour.equalsIgnoreCase("20")
						|| hour.equalsIgnoreCase("21")
						|| hour.equalsIgnoreCase("22")
						|| hour.equalsIgnoreCase("23")
						|| hour.equalsIgnoreCase("24")) {

					minuteWV.setAdapter(new ArrayWheelAdapter<String>(
							minuteArray));
					minuteWV.setCurrentItem(0);
				}
			}
		});

		minuteWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				// 设置右侧的 WheelView 的适配器
				if (hour.equalsIgnoreCase("01") || hour.equalsIgnoreCase("02")
						|| hour.equalsIgnoreCase("03")
						|| hour.equalsIgnoreCase("04")
						|| hour.equalsIgnoreCase("05")
						|| hour.equalsIgnoreCase("06")
						|| hour.equalsIgnoreCase("07")
						|| hour.equalsIgnoreCase("08")
						|| hour.equalsIgnoreCase("09")
						|| hour.equalsIgnoreCase("10")
						|| hour.equalsIgnoreCase("11")
						|| hour.equalsIgnoreCase("12")
						|| hour.equalsIgnoreCase("13")
						|| hour.equalsIgnoreCase("14")
						|| hour.equalsIgnoreCase("15")
						|| hour.equalsIgnoreCase("16")
						|| hour.equalsIgnoreCase("17")
						|| hour.equalsIgnoreCase("18")
						|| hour.equalsIgnoreCase("19")
						|| hour.equalsIgnoreCase("20")
						|| hour.equalsIgnoreCase("21")
						|| hour.equalsIgnoreCase("22")
						|| hour.equalsIgnoreCase("23")
						|| hour.equalsIgnoreCase("24")) {

					minuteWV.setAdapter(new ArrayWheelAdapter<String>(
							minuteArray));
					// minuteWV.setCurrentItem(1);
				}
			}
		});

		monthWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];

				if (month.equalsIgnoreCase("01")
						|| month.equalsIgnoreCase("03")
						|| month.equalsIgnoreCase("05")
						|| month.equalsIgnoreCase("07")
						|| month.equalsIgnoreCase("08")
						|| month.equalsIgnoreCase("10")
						|| month.equalsIgnoreCase("12")) {

					dayWV.setAdapter(new ArrayWheelAdapter<String>(day31Array));
					dayWV.setCurrentItem(1);
				} else if (month.equalsIgnoreCase("02")) {
					if (Integer.valueOf(year) % 4 == 0
							&& Integer.valueOf(year) % 100 != 0
							|| Integer.valueOf(year) % 400 == 0) {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day29Array));
						dayWV.setCurrentItem(1);
					} else {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day28Array));
						dayWV.setCurrentItem(1);
					}
				} else {
					dayWV.setAdapter(new ArrayWheelAdapter<String>(day30Array));
					dayWV.setCurrentItem(1);
				}
			}
		});

		yearWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];

				// 设置右侧的 WheelView 的适配器
				if (month.equalsIgnoreCase("01")
						|| month.equalsIgnoreCase("03")
						|| month.equalsIgnoreCase("05")
						|| month.equalsIgnoreCase("07")
						|| month.equalsIgnoreCase("08")
						|| month.equalsIgnoreCase("10")
						|| month.equalsIgnoreCase("12")) {

					dayWV.setAdapter(new ArrayWheelAdapter<String>(day31Array));
				} else if (month.equalsIgnoreCase("02")) {
					if (Integer.valueOf(year) % 4 == 0
							&& Integer.valueOf(year) % 100 != 0
							|| Integer.valueOf(year) % 400 == 0) {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day29Array));
					} else {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day28Array));
					}
				} else {
					dayWV.setAdapter(new ArrayWheelAdapter<String>(day30Array));
				}
			}
		});

		dayWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String year = yearArray[yearWV.getCurrentItem()];
				String month = monthArray[monthWV.getCurrentItem()];

				// 设置右侧的 WheelView 的适配器
				if (month.equalsIgnoreCase("01")
						|| month.equalsIgnoreCase("03")
						|| month.equalsIgnoreCase("05")
						|| month.equalsIgnoreCase("07")
						|| month.equalsIgnoreCase("08")
						|| month.equalsIgnoreCase("10")
						|| month.equalsIgnoreCase("12")) {
					dayWV.setAdapter(new ArrayWheelAdapter<String>(day31Array));
				} else if (month.equalsIgnoreCase("02")) {
					if (Integer.valueOf(year) % 4 == 0
							&& Integer.valueOf(year) % 100 != 0
							|| Integer.valueOf(year) % 400 == 0) {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day29Array));
					} else {
						dayWV.setAdapter(new ArrayWheelAdapter<String>(
								day28Array));
					}
				} else {
					dayWV.setAdapter(new ArrayWheelAdapter<String>(day30Array));
				}
			}
		});

		yearWV.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				dateTv.setText(year.trim() + "年" + month.trim() + "月"
						+ day.trim() + "日" + hour.trim() + "时" + minute.trim()
						+ "分"
						+ DateUtil.getWeek(year + "-" + month + "-" + day));
			}
		});

		monthWV.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				dateTv.setText(year.trim() + "年" + month.trim() + "月"
						+ day.trim() + "日" + hour.trim() + "时" + minute.trim()
						+ "分"
						+ DateUtil.getWeek(year + "-" + month + "-" + day));
			}
		});

		dayWV.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				dateTv.setText(year.trim() + "年" + month.trim() + "月"
						+ day.trim() + "日" + hour.trim() + "时" + minute.trim()
						+ "分"
						+ DateUtil.getWeek(year + "-" + month + "-" + day));
			}
		});

		hourWV.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				dateTv.setText(year.trim() + "年" + month.trim() + "月"
						+ day.trim() + "日" + hour.trim() + "时" + minute.trim()
						+ "分"
						+ DateUtil.getWeek(year + "-" + month + "-" + day));
			}
		});
		minuteWV.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				final String year = yearArray[yearWV.getCurrentItem()];
				final String month = monthArray[monthWV.getCurrentItem()];
				final String day = day31Array[dayWV.getCurrentItem()];
				final String hour = hourArray[hourWV.getCurrentItem()];
				final String minute = minuteArray[minuteWV.getCurrentItem()];

				dateTv.setText(year.trim() + "年" + month.trim() + "月"
						+ day.trim() + "日" + hour.trim() + "时" + minute.trim()
						+ "分"
						+ DateUtil.getWeek(year + "-" + month + "-" + day));
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams layoutParams = ((Activity) context)
				.getWindow().getAttributes();
		layoutParams.alpha = 0.4f;
		((Activity) context).getWindow().setAttributes(layoutParams);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(dateTimeLayout, Gravity.BOTTOM, 0, 0);
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.update();
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = ((Activity) context)
						.getWindow().getAttributes();
				lp.alpha = 1f;
				((Activity) context).getWindow().setAttributes(lp);
			}
		});
	}
}
