package com.massky.sraumzigbee.dao;
import com.massky.sraumzigbee.util.ApplicationContext;
import com.massky.sraumzigbee.util.ObjectSerializer;
import java.io.File;

/**
 * Created by zhu on 2017/3/8.
 */

public class Dao {

    private  static  final String SERIAL_INFO = "serial.bin";

    /**
     * get local serialInfo
     * @return
     */
    public static StringBuffer getLocalSerialInfo(){//ctrl+alt+h查看方法引用
        return ObjectSerializer.deSerialize(SERIAL_INFO);
    }


    /**
     * save member to local
     * @param stringBuffer
     * @return
     */
    public static Boolean saveMember(StringBuffer stringBuffer){
        return ObjectSerializer.serialize(stringBuffer, SERIAL_INFO);
    }

    /**
     * remove local member
     * @return
     */
    public static Boolean removeLocaSerialInfo(){
        try{
            File file = ApplicationContext.getInstance().getFileStreamPath(SERIAL_INFO);
            if(file.exists()){
                return file.delete();
            }
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
