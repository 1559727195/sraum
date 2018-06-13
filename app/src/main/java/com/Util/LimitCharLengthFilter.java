package com.Util;

import java.io.UnsupportedEncodingException;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 限制输入的字符的数量
 *
 * @author xfc
 */
public class LimitCharLengthFilter implements InputFilter {
    private int mMax = 0;

    public LimitCharLengthFilter(int mMax) {
        super();
        this.mMax = mMax;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int bytes = 0;
        int sourceBytes = 0;
        int keep = 0;
        try {
            bytes = dest.toString().getBytes("GBK").length;
            sourceBytes = source.toString().getBytes("GBK").length;
            if (bytes + sourceBytes <= mMax) {
                keep = source.length();
            } else {
                for (int i = 0; i < source.length(); i++) {
                    int currentSoureBytes = source.subSequence(0, i + 1)
                            .toString().getBytes("GBK").length;

                    if (bytes + currentSoureBytes > mMax) {
                        keep = i;
                        break;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (keep <= 0) {
            return "";

        }
        return source.subSequence(start, keep + start);
    }
}
