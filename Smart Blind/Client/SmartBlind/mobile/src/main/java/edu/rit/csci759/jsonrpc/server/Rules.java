package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by siddeshpillai on 10/7/15.
 */
public class Rules implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public  String IF;
    public String TEMPERATURE;
    public String AFFIRMATION_ONE;
    public String TEMPERATURE_VALUE;
    public String CONDITION_ONE;
    public String LIGHT_INTENSITY;
    public String AFFIRTMATION_TWO;
    public String LIGHT_INTENSITY_VALUE;
    public String THEN;
    public String RESULT;
    public String BLIND;
    public String IS;
    public int ruleNumber;
    public    Rules(){
        this.AFFIRMATION_ONE = null;
        this.AFFIRTMATION_TWO = null;
        this.CONDITION_ONE = null;
        this.LIGHT_INTENSITY_VALUE = null;
        this.TEMPERATURE_VALUE = null;
        this.RESULT = null;
    }

    public   Rules(int ruleNumber,String AFFIRMATION_ONE, String TEMPERATURE_VALUE, String CONDITION_ONE, String AFFIRTMATION_TWO, String LIGHT_INTENSITY_VALUE, String RESULT) {
        IF = "IF";
        TEMPERATURE = "TEMPERATURE";
        LIGHT_INTENSITY = "LIGHT_INTENSITY";
        THEN = "THEN";
        BLIND = "BLIND";
        IS = "IS";
        this.ruleNumber = ruleNumber;
        this.AFFIRMATION_ONE = AFFIRMATION_ONE;
        this.AFFIRTMATION_TWO = AFFIRTMATION_TWO;
        this.CONDITION_ONE = CONDITION_ONE;
        this.LIGHT_INTENSITY_VALUE = LIGHT_INTENSITY_VALUE;
        this.TEMPERATURE_VALUE = TEMPERATURE_VALUE;
        this.RESULT = RESULT;
    }

    public String getAFFIRMATION_ONE()
    {
        return this.AFFIRMATION_ONE;
    }

    public String getAFFIRMATION_TWO()
    {
        return this.AFFIRTMATION_TWO;

    }
    public String getCONDITION_ONE()
    {
        return this.CONDITION_ONE;

    }

    public String getLIGHT_INTENSITY_VALUE()
    {
        return this.LIGHT_INTENSITY_VALUE;
    }

    public String getTEMPERATURE_VALUE()
    {
        return this.TEMPERATURE_VALUE;
    }
    public String getRESULTS()
    {
        return this.RESULT;
    }
    public int getRuleNumber()
    {
        return this.ruleNumber;
    }

    public void setAFFIRMATION_ONE(String AFFIRMATION_ONE)
    {
        this.AFFIRMATION_ONE=AFFIRMATION_ONE;
    }


    public void setAFFIRMATION_TWO(String AFFIRMATION_TWO)
    {
        this.AFFIRTMATION_TWO = AFFIRMATION_TWO;
    }

    public void setCONDITION_ONE(String CONDITION_ONE)
    {
        this.CONDITION_ONE = CONDITION_ONE;

    }
    public void setLIGHT_INTENSITY_VALUE(String LIGHT_INTENSITY_VALUE)
    {

        this.LIGHT_INTENSITY_VALUE = LIGHT_INTENSITY_VALUE;
    }

    public void setTEMPERATURE_VALUE(String TEMPERATURE_VALUE)
    {
        this.TEMPERATURE_VALUE = TEMPERATURE_VALUE;
    }

    public void setRESULT(String RESULT)
    {
        this.RESULT = RESULT;
    }

    public void writeObject(ObjectOutputStream o) throws IOException
    {
        o.writeObject(IF);
        o.writeObject(TEMPERATURE);
        o.writeObject(AFFIRMATION_ONE);
        o.writeObject(TEMPERATURE_VALUE);
        o.writeObject(CONDITION_ONE);
        o.writeObject(LIGHT_INTENSITY);
        o.writeObject(AFFIRTMATION_TWO);
        o.writeObject(LIGHT_INTENSITY_VALUE);
        o.writeObject(THEN);
        o.writeObject(RESULT);


    }


    public void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException
    {
        IF = (String)o.readObject();
        TEMPERATURE = (String)o.readObject();
        AFFIRMATION_ONE=(String)o.readObject();
        TEMPERATURE_VALUE = (String)o.readObject();
        CONDITION_ONE = (String)o.readObject();
        LIGHT_INTENSITY = (String)o.readObject();
        AFFIRTMATION_TWO = (String)o.readObject();
        LIGHT_INTENSITY_VALUE = (String)o.readObject();
        THEN = (String)o.readObject();
        RESULT = (String)o.readObject();
    }

}


