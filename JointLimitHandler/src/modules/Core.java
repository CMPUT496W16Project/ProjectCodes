package modules;

import java.util.ArrayList;
import java.util.Map;

import tree.Node;
import tree.Tree;

public class Core {
	
	private String faceAxis;
	private Integer faceDirection;
	
	private String upAxis;
	private Integer upDirection;
	
	private String remainAxis;
	
	private ArrayList<String> axises=new ArrayList<String>();
	private ArrayList<String> channels=new ArrayList<String>();
	
	public Core(Map<String,Integer> directionInformation){
		
		axises.add("X");
		axises.add("Z");
		axises.add("Y");
		
		channels.add("Y");
		channels.add("Z");
		channels.add("X");
		
		int axisIndex=directionInformation.get("Face Axis");
		this.faceAxis=axises.remove(axisIndex);
		
		axisIndex=directionInformation.get("Up Axis");
		this.upAxis=axises.remove(axisIndex);
		
		this.remainAxis=axises.get(0);
		
		this.faceDirection=directionInformation.get("Face Direction");
		this.upDirection=directionInformation.get("Up Direction");
	}
}
