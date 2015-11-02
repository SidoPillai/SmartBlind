package edu.rit.csci759.jsonrpc.server;

import java.io.ObjectOutputStream;
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
	DataforTransfer data = null;
	
	public SendDataUpdate(Socket socketTCP) {
		this.sc = socketTCP;
	}
	
	public void run() {
		try {
			client = new Socket(this.sc.getInetAddress(), PORT);
			oos = new ObjectOutputStream(sc.getOutputStream());
			
			while(true) {
				data = DataforTransfer.getLatestData(); 
				oos.writeObject(data);
				sleep(30000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}