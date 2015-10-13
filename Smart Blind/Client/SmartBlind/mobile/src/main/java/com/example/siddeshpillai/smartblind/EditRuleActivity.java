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
 * This class allows you to edit a rule.
 * Created by siddeshpillai on 10/8/15.
 */
public class EditRuleActivity extends Activity {

    // Member variables

    Button submit;
    Spinner temperatureValue, temperatureConjection, adjunction, lightIntensityValue,
            lightIntensityConjection, blindValue;
    RuleList rules = null;
    String tempISorISNOT = null, tempVal = null, adj = null, lightISorISNOT = null,
            lightVal = null, blindVal = null;
    Integer currentRule;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rules);

        // To get the values from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rules = (RuleList) extras.getSerializable("allrules");
            currentRule = (Integer) extras.getSerializable("currentrule");
        }

        temperatureConjection = (Spinner) findViewById(R.id.conjucts_options1);
        temperatureValue = (Spinner) findViewById(R.id.temp_options);
        adjunction = (Spinner) findViewById(R.id.rule_conjuncts);
        lightIntensityConjection = (Spinner) findViewById(R.id.conjucts_options2);
        lightIntensityValue = (Spinner) findViewById(R.id.light_intensity_options);
        blindValue = (Spinner) findViewById(R.id.blind_action_options);

        // Populate the fields of the selected item
        try {
            new AsyncAction().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Submit the rules to the server to update the fuzzy rules.
        submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidator()) {
                    // Async Task to submit rule
                    new SubmitAction().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the fields properly",
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

        if (tempISorISNOT.equals("") && tempVal.equals("") && adj.equals("")) {
            if (!lightISorISNOT.equals("") && !lightVal.equals("") && !blindVal.equals("")) {
                rules.getRuleList().get(currentRule).setAFFIRMATION_ONE(tempISorISNOT);
                rules.getRuleList().get(currentRule).setAFFIRMATION_TWO(lightISorISNOT);
                rules.getRuleList().get(currentRule).setCONDITION_ONE(adj);
                rules.getRuleList().get(currentRule).setLIGHT_INTENSITY_VALUE(lightVal);
                rules.getRuleList().get(currentRule).setRESULT(blindVal);
                rules.getRuleList().get(currentRule).setTEMPERATURE_VALUE(tempVal);
            }
        } else if (!tempISorISNOT.equals("") && !tempVal.equals("") && !adj.equals("") &&
                !lightISorISNOT.equals("") && !lightVal.equals("") && !blindVal.equals("")) {
            rules.getRuleList().get(currentRule).setAFFIRMATION_ONE(tempISorISNOT);
            rules.getRuleList().get(currentRule).setAFFIRMATION_TWO(lightISorISNOT);
            rules.getRuleList().get(currentRule).setCONDITION_ONE(adj);
            rules.getRuleList().get(currentRule).setLIGHT_INTENSITY_VALUE(lightVal);
            rules.getRuleList().get(currentRule).setRESULT(blindVal);
            rules.getRuleList().get(currentRule).setTEMPERATURE_VALUE(tempVal);
            return true;
        }
        return false;
    }

    /**
     * Gives the index of the item selected
     * @param rule
     * @param ruleProperty
     * @return
     */
    public int returnIndexOfRuleKind(Rules rule, String ruleProperty) {

        int index = 0;

        switch (ruleProperty) {
            case "temperatureConjection":
                return 1;

            case "temperatureValue":
                if (rule.getTEMPERATURE_VALUE().toUpperCase().equals("FREEZING")) return 1;
                else if (rule.getTEMPERATURE_VALUE().toUpperCase().equals("COLD")) return 2;
                else if (rule.getTEMPERATURE_VALUE().toUpperCase().equals("COMFORT")) return 3;
                else if (rule.getTEMPERATURE_VALUE().toUpperCase().equals("WARM")) return 4;
                else return 5;

            case "adjunction":
                if (rule.getCONDITION_ONE().toUpperCase().equals("AND")) return 1;
                else return 2;

            case "lightIntensityConjection":
                return 1;

            case "lightIntensityValue":
                if (rule.getLIGHT_INTENSITY_VALUE().toUpperCase().equals("DARK")) return 1;
                else if (rule.getLIGHT_INTENSITY_VALUE().toUpperCase().equals("DIM")) return 2;
                else return 3;

            case "blindValue":
                if (rule.getRESULTS().toUpperCase().equals("OPEN")) return 1;
                else if (rule.getRESULTS().toUpperCase().equals("HALF")) return 2;
                else return 3;

        }

        return index;

    }


    /**
     * To populate values of the selected item to the container
     */
    private class AsyncAction extends AsyncTask<Void, Void, Void> {

        private final ProgressDialog dialog = new ProgressDialog(EditRuleActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading selected rule...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            return null;
        }

        protected void onPostExecute(Void result) {
            Rules temp = rules.getRuleList().get(currentRule);

            if (temp.getTEMPERATURE_VALUE().equals("hot")) {
                temperatureValue.setSelection(5);
            } else if (temp.getTEMPERATURE_VALUE().equals("cold")) {
                temperatureValue.setSelection(2);
            } else if (temp.getTEMPERATURE_VALUE().equals("freezing")) {
                temperatureValue.setSelection(1);
            } else if (temp.getTEMPERATURE_VALUE().equals("warm")) {
                temperatureValue.setSelection(4);
            } else if (temp.getTEMPERATURE_VALUE().equals("comfort")) {
                temperatureValue.setSelection(3);
            }

            temperatureConjection.setSelection(returnIndexOfRuleKind(temp, "temperatureConjection"));
            temperatureValue.setSelection(returnIndexOfRuleKind(temp, "temperatureValue"));
            adjunction.setSelection(returnIndexOfRuleKind(temp, "adjunction"));
            lightIntensityConjection.setSelection(returnIndexOfRuleKind(temp, "lightIntensityConjection"));
            lightIntensityValue.setSelection(returnIndexOfRuleKind(temp, "lightIntensityValue"));
            blindValue.setSelection(returnIndexOfRuleKind(temp, "blindValue"));
            dialog.dismiss();
        }

    }


    private class SubmitAction extends AsyncTask<Void, Void, Void> {
        public static final String SERVER_IP = "10.10.10.105";
        public static final int SERVER_PORT = 8080;
        ObjectOutputStream out = null;
        ProgressDialog pd;
        Socket socket = null;
        private final ProgressDialog dialog = new ProgressDialog(EditRuleActivity.this);
        Rules l = new Rules();
        List<String> ls = new ArrayList<String>();
        String add = "add";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Updating rules...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(add);
                l = rules.getRuleList().get(currentRule);
                ls.add(l.getAFFIRMATION_ONE());
                ls.add(l.getTEMPERATURE_VALUE());
                ls.add(l.getCONDITION_ONE());
                ls.add(l.getAFFIRMATION_TWO());
                ls.add(l.getLIGHT_INTENSITY_VALUE());
                ls.add(l.getRESULTS());

                HashMap<Integer, List<String>> myMap = new HashMap<Integer, List<String>>();
                myMap.put(currentRule, ls);
                out.writeObject(myMap);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    if (socket != null) {
                        socket.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
