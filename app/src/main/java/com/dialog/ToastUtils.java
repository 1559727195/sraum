package com.dialog;

/**
 * Created by zhu on 2016/9/8.
 *
 * @version ${VERSION}
 * @decpter
 */
public class ToastUtils {

    private static ToastUtils instances;
    private static CommonDialogListener mListener;

    private ToastUtils(){

    }

    public void setListener(CommonDialogListener listener){
        this.mListener = listener;
    }

    public static  ToastUtils getInstances(){
        if (instances == null)
        {
            synchronized (ToastUtils.class)
            {
                if (instances == null)
                {
                    instances = new ToastUtils();
                }
            }
        }
        return instances;
    }


    public void showDialog(String str){
        if(mListener!=null){
            mListener.show(str);
        }
    }

    public void cancel(){
        if(mListener!=null){
            mListener.cancel();
        }
    }

    public void show_fourbackground (String str) {
        if (mListener != null) {
            mListener.show_isfourbackground(str);
        }
    }
}
