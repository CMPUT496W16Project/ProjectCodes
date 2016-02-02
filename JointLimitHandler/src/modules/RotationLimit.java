package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RotationLimit {

	private Map<String,Map<String,ArrayList<Double>>> limitBook;
	
	
	public RotationLimit(){
		this.limitBook=new HashMap<String,Map<String,ArrayList<Double>>>();
		this.populateRule();
	}
	
	public Map<String,ArrayList<Double>> getRuleByName(String nodeName){
		return this.limitBook.get(nodeName);
	}
	
	private void populateRule(){
		this.popluateLeftUpLegRule();
		this.popluateRightUpLegRule();
	}
	
	private void popluateLeftUpLegRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-35.00);
		faceAxisbounds.add(50.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-45.00);
		upAxisbounds.add(40.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-30.00);
		remainAxisbounds.add(120.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("LeftUpLeg",rule);
	}
	
	private void popluateRightUpLegRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-50.00);
		faceAxisbounds.add(35.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-45.00);
		upAxisbounds.add(40.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-30.00);
		remainAxisbounds.add(120.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("RightUpLeg",rule);
	}
}
