package edu.rit.csci759.jsonrpc.server;

import edu.rit.csci759.fuzzylogic.MyTipperClass;

import java.util.Date;
import java.sql.Timestamp;
public class Data {
	
	RpiGetStatus rpi;
    static String temp ; 
    static String light;
    static String timestamp;
    Date time=null;
    
    Data(){
    	time = new Date();
    	rpi = new RpiGetStatus();
    	temp = String.valueOf(rpi.read_temperature());
    	light = MyTipperClass.getambient(rpi.read_ambient_light_intensity());
    	timestamp = String.valueOf(new Timestamp(time.getTime()));    	
    }

    public static String getTemperature() {
    	return temp;
    }
    
    public static String getLight() {
    	return light;
    }
    
    public static String getTimestamp() {
    	return timestamp;
    }

}