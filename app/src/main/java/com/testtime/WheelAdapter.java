package com.testtime;
/** 
 * @author xufuchao
 * 类说明 
 */
public interface WheelAdapter {
	/**
     * 获取条目的个数
     * 
     * @return 
     * 		WheelView 的条目个数
     */
    public int getItemsCount();

    /**
     * 根据索引位置获取 WheelView 的条目
     * 
     * @param index
     *            条目的索引
     * @return 
     * 		WheelView 上显示的条目的值
     */
    public String getItem(int index);

    /**
     * 获取条目的最大长度. 用来定义 WheelView 的宽度. 如果返回 -1, 就会使用默认宽度
     * 
     * @return 
     * 		条目的最大宽度 或者 -1
     */
    public int getMaximumLength();
}
