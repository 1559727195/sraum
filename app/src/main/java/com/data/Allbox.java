package com.data;


/**
 * Created by masskywcy on 2017-01-10.
 */
public class Allbox {
    public String type;
    public String number;
    public String name;
    public String status;
    public String sign;
    public int boxtop;

    public Allbox(String type, String number, String name, String status, String sign) {
        this.type = type;
        this.number = number;
        this.name = name;
        this.status = status;
        this.sign = sign;
    }

    public Allbox(String type, String number, String name, String status, String sign, int boxtop) {
        this.type = type;
        this.number = number;
        this.name = name;
        this.status = status;
        this.sign = sign;
        this.boxtop = boxtop;
    }

    public Allbox() {

    }

}
