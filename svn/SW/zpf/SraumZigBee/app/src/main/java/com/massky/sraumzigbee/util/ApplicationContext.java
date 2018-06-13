package com.massky.sraumzigbee.util;
import android.app.Application;

/**
 * 
 * @author Administrator
 *
 */
public class ApplicationContext extends Application {

	private static ApplicationContext _instance;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		_instance = this;
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getInstance(){
		return _instance;
	}
}
