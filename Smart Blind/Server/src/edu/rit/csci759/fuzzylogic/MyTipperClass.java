package edu.rit.csci759.fuzzylogic;
import edu.rit.csci759.jsonrpc.server.RpiGetStatus;
import edu.rit.csci759.jsonrpc.server.Rules;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.*;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.*;
import java.util.*;

public class MyTipperClass {
	private static final Variable temperature = null;
	private static final Variable ambient= null;
	private static final Variable blind = null;
	static String filename="FuzzyLogic/smartblind.fcl";
	static FIS fis = FIS.load(filename, true);
	
	public static boolean update_blind()
	{
		double openValue;
		double halfValue;
		double closeValue;
		boolean status = false;
		RpiGetStatus rpi=new RpiGetStatus();
		float tempValue =rpi.read_temperature();
		float ambientValue = rpi.read_ambient_light_intensity();
		FunctionBlock fb=fis.getFunctionBlock(null);
		fb.setVariable("tempearture", tempValue);
		fb.setVariable("ambient", ambientValue);
		fb.evaluate();
		openValue = fb.getVariable("blind").getMembership("open");
		halfValue = fb.getVariable("blind").getMembership("half");
		closeValue = fb.getVariable("blind").getMembership("close");
		if(openValue>halfValue && openValue > closeValue)
		{
			rpi.led_when_high();
			status = true;
		}
		else if(halfValue>openValue && halfValue>closeValue){
			rpi.led_when_mid();
			status = true;
		}
		else 
		{
			rpi.led_when_low();
			status = true;
		}
		return status;

	}

	/*
	 * To get the ambient status
	 * @param - Light intensity value from the light sensor.  	
	 */

	public static String getambient(int intensity)
	{
		double dark;
		double dim;
		double bright;
		String ambientValue=null;
		fis.setVariable("ambient", intensity);
		dark=fis.getFunctionBlock(null).getVariable("ambient").getMembership("dark");
		dim = fis.getFunctionBlock(null).getVariable("ambient").getMembership("dim");
		bright = fis.getFunctionBlock(null).getVariable("ambient").getMembership("bright");
		if (dark>dim && dark>bright)
		{
			ambientValue = "dark";
		}
		else if (dim>dark && dim > bright)
		{
			ambientValue = "dim";
		}
		else if (bright>dark && bright >dim)
		{
			ambientValue = "bright";
		}
		return ambientValue;

	}
	/*
	 * The method adds or update fuzzy logic rules to the fuzzy logic block
	 */
	public static void  addFuzzyRule( Map<Integer,Rules> mapName)
	{
		Iterator it = mapName.entrySet().iterator();
		Rules rValue ;
		int rNo;
		String ruleNo;
		net.sourceforge.jFuzzyLogic.rule.RuleBlock ruleBlock =fis.getFunctionBlock("smartblind").getFuzzyRuleBlock("No1");
		Rule rulenew;
		while(it.hasNext())
		{
			Map.Entry<Integer,Rules> pair = (Map.Entry<Integer,Rules>) it.next();
			rNo = pair.getKey();
			rValue = (Rules)pair.getValue();
			ruleNo = "Rule"+rNo;
			rulenew = new Rule(ruleNo,ruleBlock);
	
			if (rValue.TEMPERATURE_VALUE =="")
			{
				RuleTerm term1 = new RuleTerm(ambient,rValue.LIGHT_INTENSITY_VALUE,false);
				RuleExpression a = new RuleExpression();
				a.setTerm1(term1);
				rulenew.setAntecedents(a);
				rulenew.addConsequent(blind, rValue.RESULT,false);

			}
			if(rValue.LIGHT_INTENSITY_VALUE=="")
			{
				RuleTerm term1 = new RuleTerm(temperature,rValue.TEMPERATURE_VALUE,false);
				RuleExpression a = new RuleExpression();
				a.setTerm1(term1);
				rulenew.setAntecedents(a);
				rulenew.addConsequent(blind, rValue.RESULT, false);
			}
			else if(rValue.CONDITION_ONE =="AND")
			{
				RuleTerm term1 = new RuleTerm(temperature, rValue.TEMPERATURE_VALUE,false);
				RuleTerm term2 = new RuleTerm(ambient,rValue.LIGHT_INTENSITY_VALUE,false);
				RuleExpression aAnd = new RuleExpression(term1,term2,RuleConnectionMethodAndMin.get());
				rulenew.setAntecedents(aAnd);
				rulenew.addConsequent(blind, rValue.RESULT, false);
				ruleBlock.add(rulenew);
			}
			else if (rValue.CONDITION_ONE=="OR")
			{
				RuleTerm term1 = new RuleTerm(temperature, rValue.TEMPERATURE_VALUE,false);
				RuleTerm term2 = new RuleTerm(ambient,rValue.LIGHT_INTENSITY_VALUE,false);
				RuleExpression aOr = new RuleExpression(term1,term2,RuleConnectionMethodOrMax.get());
				rulenew.setAntecedents(aOr);
				rulenew.addConsequent(blind, rValue.RESULT, false);
				ruleBlock.add(rulenew);
			}


		}

	}

	/*
	 * The function returns a HashMap of all the rules in the system.
	 */

	public static HashMap<Integer,Rules> getRules()
	{
		String rule_Name;
		HashMap<Integer,Rules> myMap = new HashMap<Integer,Rules>();
		String [] tempArray = {"freezing","cold","comfort","warm","hot"};
		String [] ambientArray = {"dark","dim","bright"};
		String [] blindArray = {"close","half","open"};

		String tValue="";
		String aValue="";
		String bValue="";
		String condValue="";

		int count =0;
		//String [] ambientArray = {"dark","dim","bright"};
		for (Rule r :fis.getFunctionBlock("smartblind").getFuzzyRuleBlock("No1").getRules())
		{
			rule_Name=r.toStringFcl().toString();
			for (int i =0;i<tempArray.length;i++)
			{
				if (rule_Name.contains(tempArray[i]))
				{
					tValue = tempArray[i];
					break;
				}

			}
			for (int i = 0; i<ambientArray.length;i++)
			{
				if(rule_Name.contains(ambientArray[i]))
				{
					aValue = ambientArray[i];
					break;
				}
			}
			for (int i = 0; i<blindArray.length;i++)
			{
				if(rule_Name.contains(blindArray[i]))
				{
					bValue = blindArray[i];
					break;
				}

			}
			if (rule_Name.contains("AND"))
			{
				condValue = "AND";
			}
			else if(rule_Name.contains("OR"))
			{
				condValue = "OR";
			}
			myMap.put(count, new Rules("IS",tValue,condValue,"IS",aValue,bValue));	
			count++;
		}
		return myMap;
	}


}
