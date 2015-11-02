package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddeshpillai on 10/8/15.
 */
public class AllRules implements Serializable {

    Map<Integer, Rules> allRules = new HashMap<Integer, Rules>();


    AllRules(HashMap<Integer, Rules> allRules) {
        this.allRules = allRules;
    }

    Map<Integer, Rules> getAllRules() {
        return this.allRules;
    }

    private void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(allRules);
    }

    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {
        allRules = (HashMap<Integer, Rules>) o.readObject();
    }

    public void addRule(Rules obj) {
        allRules.put(allRules.size(), obj);
    }

    public void remove(Integer i) {
        allRules.remove(i);
    }

    public Rules getRuleByIndex(Integer i) {
        return allRules.get(i);
    }

}