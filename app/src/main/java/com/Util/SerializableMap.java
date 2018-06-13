package com.Util;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by masskywcy on 2017-04-07.
 */

/**
 * 序列化map供Bundle传递map使用
 */
public class SerializableMap implements Serializable {
    private Map<String, Object> map;
    private String itemcode;
    private boolean itemflag;

    public boolean isItemflag() {
        return itemflag;
    }

    public void setItemflag(boolean itemflag) {
        this.itemflag = itemflag;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
