package com.example.siddeshpillai.smartblind;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The main activity of the smart blind project
 */
public class MainActivity extends Activity {

    TextView lightIntensity = null, temperature = null;
    TextClock timestamp = null;
    TextView viewRules = null;
    Switch notifications = null;
    CardView lightcv, tempcv;
    String tempS, lightS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lightcv = (CardView) findViewById(R.id.ambience);
        tempcv = (CardView) findViewById(R.id.temperature);

        // To view the light you will have to tap on the cardview
        lightcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                alertDialog.setTitle("Ambience");
                alertDialog.setMessage(lightS);
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);

                alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();
            }
        });

        // to view the temperature you will have to tap on the cardview
        tempcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                alertDialog.setTitle("Temperature");
                alertDialog.setMessage(tempS + " degree " + " celcius");
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);

                alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();
            }
        });

        lightIntensity = (TextView) findViewById(R.id.lightIntensityText);
        temperature = (TextView) findViewById(R.id.tempText);
        timestamp = (TextClock) findViewById(R.id.time);

        viewRules = (TextView) findViewById(R.id.viewStatus);
        notifications = (Switch) findViewById(R.id.notificationsSwitch);

        // Fetching the on start values of the pi.
        new AsyncAction().execute();

        // Populate the rules by calling the View Rules Activity
        viewRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        ViewRule.class);
                startActivity(myIntent);
            }
        });


        // Set the notification enable/disabled to the callback server.
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                Runnable r = new Runnable() {
                    String SERVER_IP = "10.10.10.105";
                    int SERVER_PORT = 8080;
                    ObjectInputStream in = null;
                    ObjectOutputStream out = null;
                    Socket socket = null;

                    @Override
                    public void run() {
                        try {
                            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                            socket = new Socket(serverAddr, SERVER_PORT);
                            out = new ObjectOutputStream(socket.getOutputStream());
                            if (isChecked) out.writeObject("subscribe");
                            else out.writeObject("unsubscribe");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread t = new Thread(r);
                t.start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(BlindService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * update the data periodically and psh notification on callback.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String type = intent.getStringExtra("type");
            String temp_received = intent.getStringExtra("temp");
            String light_received = intent.getStringExtra("light");
            String timestamp_received = intent.getStringExtra("time");

            if (type.equals("notifications")) {
                createNotification(findViewById(android.R.id.content));

            } else {
                lightIntensity.setText(light_received);
                temperature.setText(temp_received);
                timestamp.setText(timestamp_received);
            }
        }
    };

    /**
     * Push notifications on callback
     * @param view
     */
    public void createNotification(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Temperature dropped by 2 degrees").build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

    /**
     * Async task to populate the initial values from the pi.
     */
    private class AsyncAction extends AsyncTask<Void, Void, Void> {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        ArrayList<String> values = new ArrayList<String>();
        String home = "home";

        public static final String SERVER_IP = "10.10.10.105";
        public static final int SERVER_PORT = 8080;

        Socket socket = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading ...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject(home);

                values = (ArrayList<String>) ois.readObject();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startService(new Intent(MainActivity.this, BlindService.class));
                    }
                });

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            tempS = values.get(0);
            lightS = values.get(1);
            dialog.dismiss();
        }
    }

}
