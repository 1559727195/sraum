package com.Util;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

/**
 * Created by masskywcy on 2017-04-14.
 */

public class MaxLimitTextWatcher implements TextWatcher {

    private int mMaxBytes;
    private EditText mEditText;

    public MaxLimitTextWatcher(EditText editText, int maxBytes) {
        mEditText = editText;
        mMaxBytes = maxBytes;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Editable editable = mEditText.getText();
        int len = editable.toString().getBytes().length;

        if (len > mMaxBytes) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = getWholeText(str, mMaxBytes);
            mEditText.setText(newStr);
            editable = mEditText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static String getWholeText(String text, int byteCount) {
        try {
            if (text != null && text.getBytes("utf-8").length > byteCount) {
                char[] tempChars = text.toCharArray();
                int sumByte = 0;
                int charIndex = 0;
                for (int i = 0, len = tempChars.length; i < len; i++) {
                    char itemChar = tempChars[i];
                    // 根据Unicode值，判断它占用的字节数
                    if (itemChar >= 0x0000 && itemChar <= 0x007F) {
                        sumByte += 1;
                    } else if (itemChar >= 0x0080 && itemChar <= 0x07FF) {
                        sumByte += 2;
                    } else {
                        sumByte += 3;
                    }
                    if (sumByte > byteCount) {
                        charIndex = i;
                        break;
                    }
                }
                return String.valueOf(tempChars, 0, charIndex);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return text;
    }

}
