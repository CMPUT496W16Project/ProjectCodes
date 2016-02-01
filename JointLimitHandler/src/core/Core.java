package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modules.BvhFileData;
import modules.DirectionCalculator;
import modules.TreeConverter;
import tree.Tree;

public class Core {
	
	
	private BvhFileData bvhFileData;
	private Tree tree;
	
	//Axis and directions:
	private String faceAxis;
	private Integer faceDirection;
	
	private String upAxis;
	private Integer upDirection;
	
	private String remainAxis;
	
	//=====================
	
	//Channel order:
	private ArrayList<String> channels=new ArrayList<String>();
	
	//=============
	
	//private Map<String,Integer> bodyPartsDictionary=new HashMap<String,Integer>();
	
	public Core(BvhFileData bvhFileData){
		
		// Read-in Frame data and convert it in to the tree class.
		this.bvhFileData=bvhFileData;
		
		//Populate the tree class.
		TreeConverter treeConverter=new TreeConverter();
		this.tree=treeConverter.convert(this.bvhFileData.getBvhHeader());
		this.tree.printAllNodeInformation();
		
		//Populate channel order:
		this.channels.add("Y");
		this.channels.add("Z");
		this.channels.add("X");
		
		// Get the direction information.
		this.extractDirectionInfromation();
		
		System.out.println("==========================================");
		System.out.println("Core system initializing...");
		System.out.println("Facing axis         : "+this.faceAxis);
		System.out.println("Facing direction    : "+this.faceDirection);
		System.out.println("Up axis             : "+this.upAxis);
		System.out.println("Up direction        : "+this.upDirection);
		System.out.println("Remain Axis         : "+this.remainAxis);
		System.out.println("------------------------------------------");
	}
	
	private void extractDirectionInfromation(){
		
		DirectionCalculator directionCalculator=new DirectionCalculator(this.tree);
		Map<String,Integer> directionInformation=directionCalculator.getDirectionInfromation();
		
		ArrayList<String> axises=new ArrayList<String>();
		
		axises.add("X");
		axises.add("Z");
		axises.add("Y");
		
		int faceAxisIndex=directionInformation.get("Face Axis");
		this.faceAxis=axises.get(faceAxisIndex);
		
		int upAxisIndex=directionInformation.get("Up Axis");
		this.upAxis=axises.get(upAxisIndex);
		
		if(upAxisIndex>faceAxisIndex){
			axises.remove(upAxisIndex);
			axises.remove(faceAxisIndex);
		}
		else{
			axises.remove(faceAxisIndex);
			axises.remove(upAxisIndex);
		}
		
		this.remainAxis=axises.get(0);
		this.faceDirection=directionInformation.get("Face Direction");
		this.upDirection=directionInformation.get("Up Direction");
	}
	
	//private void generateBodyPartIndex(){
		
	//}
	
	
}
