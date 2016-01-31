package modules;

import java.util.ArrayList;
import java.util.HashMap;
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
	private Map<String,Integer> bodyPartsDictionary=new HashMap<String,Integer>();
	
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
		System.out.println("==========================================");
		System.out.println("Core system initializing...");
		System.out.println("Facing axis      	: "+this.faceAxis);
		System.out.println("Facing direction 	: "+this.faceDirection);
		System.out.println("Up axis 			: "+this.upAxis);
		System.out.println("Up direction		: "+this.upDirection);
		System.out.println("Remain Axis         : "+this.remainAxis);
		System.out.println("------------------------------------------");
	}
	
	private void generateBodyPartIndex(){
		bodyPartsDictionary.put("Hip",0);
	}
}
