package com.example.siddeshpillai.smartblind;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.rit.csci759.jsonrpc.server.Rules;
import edu.rit.csci759.jsonrpc.server.RuleList;

/**
 * Created by siddeshpillai on 10/7/15.
 */
public class AddRuleActivity extends Activity {

    Button submit;
    Spinner temperatureValue, temperatureConjection, adjunction, lightIntensityValue,
            lightIntensityConjection, blindValue;
    RuleList rules = null;
    String tempISorISNOT = null, tempVal = null, adj = null, lightISorISNOT = null,
            lightVal = null, blindVal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rules);

        // To get the values from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rules = (RuleList) extras.getSerializable("allrules");
        }

        temperatureConjection = (Spinner) findViewById(R.id.conjucts_options1);
        temperatureValue = (Spinner) findViewById(R.id.temp_options);
        adjunction = (Spinner) findViewById(R.id.rule_conjuncts);
        lightIntensityConjection = (Spinner) findViewById(R.id.conjucts_options2);
        lightIntensityValue = (Spinner) findViewById(R.id.light_intensity_options);
        blindValue = (Spinner) findViewById(R.id.blind_action_options);

        // Submit the rules to the server to update the fuzzy rules.
        submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidator()) {
                    new SubmitAction().execute();
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter the fields properly",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Validates the Input from the spinner before updating to the server.
     *
     * @return boolean success.
     */
    public boolean inputValidator() {
        tempISorISNOT = temperatureConjection.getSelectedItem().toString();
        tempVal = temperatureValue.getSelectedItem().toString();
        adj = adjunction.getSelectedItem().toString();
        lightISorISNOT = lightIntensityConjection.getSelectedItem().toString();
        lightVal = lightIntensityValue.getSelectedItem().toString();
        blindVal = blindValue.getSelectedItem().toString();

        if(tempISorISNOT.equals("") && tempVal.equals("") && adj.equals("")) {
            if(!lightISorISNOT.equals("") && !lightVal.equals("") && !blindValue.equals("")) {
                Rules rule = new Rules(rules.size(), tempISorISNOT, tempVal, adj, lightISorISNOT,
                        lightVal, blindVal);
                rules.getRuleList().add(rule);
                return true;
            }
        } else if (!tempISorISNOT.equals("") && !tempVal.equals("") && !adj.equals("") &&
                !lightISorISNOT.equals("") && !lightVal.equals("") && !blindVal.equals("")) {
            Rules rule = new Rules(rules.size(), tempISorISNOT, tempVal, adj, lightISorISNOT,
                    lightVal, blindVal);
            rules.getRuleList().add(rule);
            return true;
        }
        return false;
    }

    /**
     * Submit class to add the fuzzy logic rules in the server
     */
    private class SubmitAction extends AsyncTask<Void, Void, Void> {
        public static final String SERVER_IP = "10.10.10.105";
        public static final int SERVER_PORT = 8080;
        ObjectOutputStream out = null;
        ProgressDialog pd;
        Socket socket = null;
        Rules l = new Rules();
        List<String> ls = new ArrayList<String>();
        private final ProgressDialog dialog = new ProgressDialog(AddRuleActivity.this);
        String add = "add";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Adding rules...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(add);
                l = rules.getRuleList().get(rules.size()-1);
                ls.add(l.getAFFIRMATION_ONE());
                ls.add(l.getTEMPERATURE_VALUE());
                ls.add(l.getCONDITION_ONE());
                ls.add(l.getAFFIRMATION_TWO());
                ls.add(l.getLIGHT_INTENSITY_VALUE());
                ls.add(l.getRESULTS());

                HashMap<Integer, List<String>> myMap = new HashMap<Integer, List<String>>();
                myMap.put(rules.getRuleList().size(), ls);
                out.writeObject(myMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            dialog.dismiss();
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
