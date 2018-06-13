package com.Util;

import android.text.Editable;

/**
 * Created by xfc on 2017-02-28.
 */
// 给edittext控件控件设置光标在文字的后边
public class SelectionEnd {
    public static void setSelectionEnd(ClearEditText editText) {
        Editable b = editText.getText();
        editText.setSelection(b.length());
    }
}
