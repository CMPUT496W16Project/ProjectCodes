package modules;

import java.util.ArrayList;
import java.util.Map;

public class LimitRule {
	
	private String faceAxis;
	private Integer faceDirection;
	
	private String upAxis;
	private Integer upDirection;
	
	ArrayList<String> axises=new ArrayList<String>();
	
	public LimitRule(Map<String,Integer> directionInformation){
		axises.add("X");
		axises.add("Z");
		axises.add("Y");
		
		this.faceAxis=axises.get(directionInformation.get("Face Axis"));
		this.upAxis=axises.get(directionInformation.get("Up Axis"));
		
		this.faceDirection=directionInformation.get("Face Direction");
		this.upDirection=directionInformation.get("Up Direction");
	}
	
	
}
