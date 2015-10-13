package edu.rit.csci759.jsonrpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class RuleList implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public List<Rules> rulesList;

	public List<Rules> getRuleList() {
		return this.rulesList;
	}

	public void setRuleList(List<Rules> ruleList) {
		this.rulesList = ruleList;
	}

	public int size() {
		return rulesList.size();
	}

	public void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(rulesList);
    }

	public  void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {
		rulesList = (List<Rules>) o.readObject();
	}

}




