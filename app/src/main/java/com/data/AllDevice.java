package com.data;

/**
 * Created by masskywcy on 2017-01-10.
 */
//全部设备信息
public class AllDevice {
    public String type;
    public String number;
    public String name;
    public String status;
    public String mode;
    public String dimmer;
    public String temperature;
    public String speed;

    public AllDevice() {

    }

    public AllDevice(String type, String number, String name, String status, String mode,
                     String dimmer, String temperature, String speed) {
        this.type = type;
        this.number = number;
        this.name = name;
        this.status = status;
        this.mode = mode;
        this.dimmer = dimmer;
        this.temperature = temperature;
        this.speed = speed;
    }
}
