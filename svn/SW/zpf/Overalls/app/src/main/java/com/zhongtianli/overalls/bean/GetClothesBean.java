package com.zhongtianli.overalls.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhu on 2016/12/28.
 */

public class GetClothesBean implements Serializable {
    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        Result = Result;
    }

    public void setInClothes(ArrayList<GetClothesBean.InClothes> InClothes) {
        InClothes = InClothes;
    }

    public ArrayList<GetClothesBean.InClothes> getInClothes() {
        return InClothes;
    }

    //    "Result": "100",
//            “InClothes”:
//            [{"Id":"12345",
//        “RFID”:”405300000000000000000000000000000011”,
//        "Name":"张三",
//                "InTime":"2016-12-27 12:22:25"
//    },
//    {"Id":"12346",
//        “RFID”:”405300000000000000000000000000000012”,
//        "Name":"李四",
//            "InTime":"2016-12-27 13:22:25"
//    }]
     private  String Result;
    private ArrayList<InClothes> InClothes;
    public class InClothes implements Serializable {
        public void setId(String id) {
            Id = id;
        }

        public void setRFID(String RFID) {
            this.RFID = RFID;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setInTime(String inTime) {
            InTime = inTime;
        }

        private  String Id;
        private  String RFID;

        public String getId() {
            return Id;
        }

        public String getRFID() {
            return RFID;
        }

        public String getName() {
            return Name;
        }

        public String getInTime() {
            return InTime;
        }

        private  String Name;
        private  String InTime;
    }
}
