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
		
		this.popluateLeftLegRule();
		this.popluateRightLegRule();
		
		this.popluateHeadRule();
		
		this.populateLeftForeArmRule();
		this.populateRightForeArmRule();
		
		this.popluateLeftUpLegRule();
		this.popluateRightUpLegRule();
		
		this.popluateSpineRule();
		
		this.populateLeftArmRule();
		this.populateRightArmRule();
	}
	
	private void popluateHeadRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-35.00);
		faceAxisbounds.add(35.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-70.00);
		upAxisbounds.add(70.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-80.00);
		remainAxisbounds.add(50.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("Head",rule);
	}
	
	private void populateLeftForeArmRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-5.00);
		faceAxisbounds.add(150.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-150.00);
		upAxisbounds.add(5.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-90.00);
		remainAxisbounds.add(90.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("LeftForeArm",rule);
	}
	
	private void populateRightForeArmRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-150.00);
		faceAxisbounds.add(5.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-5.00);
		upAxisbounds.add(150.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-90.00);
		remainAxisbounds.add(90.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("RightForeArm",rule);
	}
	
	
	private void populateLeftArmRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-135.00);
		faceAxisbounds.add(90.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-130.00);
		upAxisbounds.add(45.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-150.00);
		remainAxisbounds.add(90.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("LeftArm",rule);
	}
	
	private void populateRightArmRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-90.00);
		faceAxisbounds.add(135.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-45.00);
		upAxisbounds.add(130.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-150.00);
		remainAxisbounds.add(90.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("RightArm",rule);
	}
	
	private void popluateSpineRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-35.00);
		faceAxisbounds.add(35.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-85.00);
		upAxisbounds.add(85.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-75.00);
		remainAxisbounds.add(30.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("Spine",rule);
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
		upAxisbounds.add(-40.00);
		upAxisbounds.add(45.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-30.00);
		remainAxisbounds.add(120.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("RightUpLeg",rule);
	}
	
	private void popluateLeftLegRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-2.00);
		faceAxisbounds.add(2.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-2.00);
		upAxisbounds.add(10.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-130.00);
		remainAxisbounds.add(0.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("LeftLeg",rule);
	}
	
	private void popluateRightLegRule(){
		Map<String,ArrayList<Double>> rule=new HashMap<String,ArrayList<Double>>();
		
		ArrayList<Double> faceAxisbounds=new ArrayList<Double>();
		faceAxisbounds.add(-2.00);
		faceAxisbounds.add(2.00);
		
		ArrayList<Double> upAxisbounds=new ArrayList<Double>();
		upAxisbounds.add(-10.00);
		upAxisbounds.add(2.00);
		
		ArrayList<Double> remainAxisbounds=new ArrayList<Double>();
		remainAxisbounds.add(-130.00);
		remainAxisbounds.add(0.00);
		
		rule.put("+faceAxis",faceAxisbounds);
		rule.put("+upAxis",upAxisbounds);
		rule.put("+remainAxis",remainAxisbounds);
		
		this.limitBook.put("RightLeg",rule);
	}
}
