package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataforTransfer implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String temp;
    String light;
    String timestamp;

    public DataforTransfer() {

    }
    
    DataforTransfer(String temp, String light, String timestamp) {
    	this.temp = temp;
    	this.light = light;
    	this.timestamp = timestamp;
    }
    
    private void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(temp);
        o.writeObject(light);
        o.writeObject(timestamp);
        
    }

    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {
    	temp = (String) o.readObject();
    	light = (String) o.readObject();
    	timestamp = (String) o.readObject();
    }
    
    public static DataforTransfer getLatestData() {
    	return new DataforTransfer(Data.getTemperature(), Data.getLight(), Data.getTimestamp());
    }
    
    public String getTemp() {
    	return this.temp;
    }
    public String getlight() {
    	return this.light;
    }
    public String gettimestam() {
    	return this.timestamp;
    }
    

}