package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Sends the update to client every 30 seconds
 */
public class SendDataUpdate extends Thread{

	// Member Variables
	Socket sc = null;
	Socket client = null;
	int PORT = 6000;
	ObjectOutputStream oos = null;
	TLT data = null;
	Data d = new Data();
	
	public SendDataUpdate(Socket socketTCP) {
		this.sc = socketTCP;
	}
	
	public void run() {
		try {
			client = new Socket(this.sc.getInetAddress(), PORT);
			oos = new ObjectOutputStream(sc.getOutputStream());
			
			while(true) {
				data = new TLT(Data.temp, Data.light, Data.timestamp); 
				oos.writeObject(data);
				sleep(30000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class TLT implements Serializable {
		String temp;
	    String light;
	    String timestamp;
	    
	    TLT(String temp, String light, String timestamp) {
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
	    
	    public String getTemp()
	    {
	    	return this.temp;
	    }
	    public String getlight()
	    {
	    	return this.light;
	    }
	    public String gettimestamp()
	    {
	    	return this.timestamp;
	    }
	    
	}

}