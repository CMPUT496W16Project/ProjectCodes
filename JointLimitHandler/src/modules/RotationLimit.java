package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RotationLimit {

	private Map<String,Map<String,ArrayList<Double>>> limitBook;
	
	
	public RotationLimit(){
		this.limitBook=new HashMap<String,Map<String,ArrayList<Double>>>();
	}
	
	private void popluateLeftUpLegRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-50.00);
		faceAxisbounds.add(30.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-50.00);
		faceAxisbounds.add(30.00);
		
		rule.put("+faceAxis",faceAxisbounds);
	}
}
