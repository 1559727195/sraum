package com.Util;

import com.data.User;

/**
 * Created by xfc on 2017-03-02.
 */

/**
 * 用于接口返回数据进行判断状态
 */
public interface ApiResult {
    /**
     * 成功解析
     *
     * @param user 100
     */
    public void onSuccess(User user);

    /**
     * token错误 101
     */
    public void wrongToken();

    /**
     * token错误 102
     */
    public void wrongBoxnumber();

    /**
     * 解析失败 1
     */
    public void pullDataError();

    /**
     * 返回数据为空
     */
    public void emptyResult();

    /**
     * 返回值code 103
     */
    public void threeCode();

    /**
     * 返回值code 104
     */
    public void fourCode();

    /**
     * 返回值code 105
     */
    public void fiveCode();

    /**
     * 返回值code 106
     */
    public void sixCode();

    /**
     * 返回值code 107
     */
    public void sevenCode();

    /**
     * 默认方法执行
     */
    public void defaultCode();

}
