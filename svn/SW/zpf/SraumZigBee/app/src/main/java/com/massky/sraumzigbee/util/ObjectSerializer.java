package com.massky.sraumzigbee.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ObjectSerializer {
	
	/**
	 * read object
	 * @param fileName
	 * @return
	 */
	public static <T> T deSerialize(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			File file = ApplicationContext.getInstance().getFileStreamPath(fileName);
			if(!file.exists()){
				return null;
			}
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			T t = (T)ois.readObject();
			return t;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		finally{
			if(null != ois){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != fis){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Save Object
	 * @param t
	 * @param fileName
	 * @return
	 */
	public static <T> Boolean serialize(T t , String fileName){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = ApplicationContext.getInstance().openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(t);
			oos.flush();
			fos.flush();
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		finally{
			if(null != oos){
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
