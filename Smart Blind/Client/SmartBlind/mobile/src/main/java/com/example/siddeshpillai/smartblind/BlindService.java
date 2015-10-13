package com.example.siddeshpillai.smartblind;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The background service class responsible for notifications and callbacks
 * Created by siddeshpillai on 10/11/15.
 */
public class BlindService extends IntentService {
    ServerSocket serverSocket = null;
    final int PORT = 6000;
    ObjectInputStream ois;
    List<String> data = new ArrayList<String>();
    String tempInstance = null;
    String lightInstance = null;
    String timestampInstance = null;
    String type = null;
    public static final String NOTIFICATION = "com.example.siddeshpillai.smartblind";

    public BlindService() {
        super("BlindService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            serverSocket = new ServerSocket(PORT);
            Socket socket = serverSocket.accept();
            ois = new ObjectInputStream(socket.getInputStream());

            data = (ArrayList<String>) ois.readObject();

            if (data.get(0).equals("callback")) {
                type = "callback";
                tempInstance = data.get(1);
                lightInstance = data.get(2);
                timestampInstance = data.get(3);
                publishResults(type, tempInstance, lightInstance, timestampInstance);
            } else if (data.get(0).equals("notifications")) {
                type = "notifications";
                tempInstance = data.get(1);
                lightInstance = data.get(2);
                timestampInstance = data.get(3);
                publishResults(type, tempInstance, lightInstance, timestampInstance);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void publishResults(String type, String tempInstance, String lightInstance, String timestampInstance) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("type", type);
        intent.putExtra("temp", tempInstance);
        intent.putExtra("light", lightInstance);
        intent.putExtra("time", timestampInstance);
        sendBroadcast(intent);
    }
}
