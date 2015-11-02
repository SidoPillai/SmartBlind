package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by siddeshpillai on 10/7/15.
 */
public class Rules {

  public  String IF = "IF";

   public String TEMPERATURE = "TEMPERATURE";
   public String AFFIRMATION_ONE;
   public String TEMPERATURE_VALUE;

   public String CONDITION_ONE;

   public  String LIGHT_INTENSTITY = "LIGHT_INTENSITY";

   public  String AFFIRTMATION_TWO;
   public  String LIGHT_INTENSITY_VALUE;

   public  String THEN = "THEN";

   public  String RESULT;

   public    Rules(){
        this.AFFIRMATION_ONE = null;
        this.AFFIRTMATION_TWO = null;
        this.CONDITION_ONE = null;
        this.LIGHT_INTENSITY_VALUE = null;
        this.TEMPERATURE_VALUE = null;
        this.RESULT = null;
    }

   public   Rules(String AFFIRMATION_ONE, String TEMPERATURE_VALUE, String CONDITION_ONE, String AFFIRTMATION_TWO, String LIGHT_INTENSITY_VALUE, String RESULT) {
        this.AFFIRMATION_ONE = AFFIRMATION_ONE;
        this.AFFIRTMATION_TWO = TEMPERATURE_VALUE;
        this.CONDITION_ONE = CONDITION_ONE;
        this.LIGHT_INTENSITY_VALUE = AFFIRTMATION_TWO;
        this.TEMPERATURE_VALUE = LIGHT_INTENSITY_VALUE;
        this.RESULT = RESULT;
    }


   public  String[] getValues(){
        String[] values = new String[] {this.IF, this.TEMPERATURE, this.AFFIRMATION_ONE, this.TEMPERATURE_VALUE, this.CONDITION_ONE, this.LIGHT_INTENSTITY,
        this.AFFIRTMATION_TWO, this.LIGHT_INTENSITY_VALUE, this.THEN, "BLIND", "IS", this.RESULT};

        return values;
    }


}