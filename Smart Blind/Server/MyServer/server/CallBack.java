package edu.rit.csci759.jsonrpc.server;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.sql.Timestamp;
import edu.rit.csci759.fuzzylogic.MyTipperClass;

public class CallBack implements Runnable{

	public float prev_temp=0;
	public float current_temp=0;
	public int ambientIntensity=0;
	RpiGetStatus rpi=null;
	int PORT = 6000;
	Socket reachClient;
	DataforTransfer data = null;
	Socket connect;
	ObjectOutputStream oos = null;
	Date time;
	public boolean notify=false;
	
	public CallBack(Socket socket) throws IOException
	{
		rpi = new RpiGetStatus();
		reachClient=socket;
		prev_temp = rpi.read_temperature(); 
		ambientIntensity = rpi.read_ambient_light_intensity();
	}

	public void setNotify(boolean val)
	{
		notify = val;
	}

	public void run()
	{
		while (true)
		{
			if(notify)

				while (true)
				{
					current_temp = rpi.read_temperature();
					if ((Math.abs(current_temp-prev_temp)>=2) || (Math.abs(current_temp-prev_temp)<=2))
					{
						prev_temp = current_temp;

						Data.temp = String.valueOf(current_temp); 
						Data.light=MyTipperClass.getambient(rpi.read_ambient_light_intensity());
						Data.timestamp =String.valueOf(new Timestamp(time.getTime()));
						try{	
							connect = new Socket(reachClient.getInetAddress(),PORT);
							oos= new ObjectOutputStream(connect.getOutputStream());
							data = DataforTransfer.getLatestData();
							oos.writeObject(data);
							oos.flush();

						}
						catch(Exception e)
						{
							e.printStackTrace();	
						}

					}
				}

		}
	}
}





