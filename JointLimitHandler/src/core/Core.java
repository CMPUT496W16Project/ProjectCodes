package core;

import java.util.ArrayList;
import java.util.Map;

import modules.BvhFileData;
import modules.DirectionCalculator;
import modules.RotationLimit;
import modules.TreeConverter;
import tree.Node;
import tree.Tree;

public class Core {
	
	
	private BvhFileData bvhFileData;
	private Tree tree;
	private RotationLimit rotationLimit;
	
	//Axis and directions:
	private String faceAxis;
	private Integer faceDirection;
	
	private String upAxis;
	private Integer upDirection;
	
	private String remainAxis;
	private Integer remainDirection;
	
	//=====================
	
	public Core(BvhFileData bvhFileData,RotationLimit rotationLimit){
		
		// Read-in Frame data and convert it in to the tree class.
		this.bvhFileData=bvhFileData;
		this.rotationLimit=rotationLimit;
		
		//Populate the tree class.
		TreeConverter treeConverter=new TreeConverter();
		this.tree=treeConverter.convert(this.bvhFileData.getBvhHeader());
		this.tree.printAllNodeInformation();
		
		// Get the direction information.
		this.extractDirectionInfromation();
		
		System.out.println("==========================================");
		System.out.println("Core system initializing...");
		System.out.println("Facing axis         : "+this.faceAxis);
		System.out.println("Facing direction    : "+this.faceDirection);
		System.out.println("Up axis             : "+this.upAxis);
		System.out.println("Up direction        : "+this.upDirection);
		System.out.println("Remain Axis         : "+this.remainAxis);
		System.out.println("Remain direction    : "+this.remainDirection);
		System.out.println("------------------------------------------");
	}
	
	private void extractDirectionInfromation(){
		
		DirectionCalculator directionCalculator=new DirectionCalculator(this.tree);
		Map<String,Integer> directionInformation=directionCalculator.getDirectionInfromation();
		
		ArrayList<String> axis=new ArrayList<String>();
		
		axis.add("X");
		axis.add("Z");
		axis.add("Y");
		
		int faceAxisIndex=directionInformation.get("Face Axis");
		this.faceAxis=axis.get(faceAxisIndex);
		
		int upAxisIndex=directionInformation.get("Up Axis");
		this.upAxis=axis.get(upAxisIndex);
		
		int remainAxisIndex=directionInformation.get("Remain Axis");
		this.remainAxis=axis.get(remainAxisIndex);
		
		this.faceDirection=directionInformation.get("Face Direction");
		this.upDirection=directionInformation.get("Up Direction");
		this.remainDirection=directionInformation.get("Remain Direction");
	}
	
	public void applyLimitOnAFrame(int frameIndex){
		this.applyLimitOnANode(this.bvhFileData.getFrameByFrameIndex(frameIndex),"LeftUpLeg");
	}
	
	
	private ArrayList<Double> applyLimitOnANode(ArrayList<Double> frame,String nodeName){
		Node node=this.tree.getNodeSetByName(nodeName).get(0);
		Map<String,ArrayList<Double>> rule=this.rotationLimit.getRuleByName(nodeName);
		
		ArrayList<Double> faceAxisbounds=rule.get("+faceAxis");
		if(this.faceDirection==-1){
			faceAxisbounds=this.reverseSign(faceAxisbounds);
		}
		
		ArrayList<Double> upAxisbounds=rule.get("+upAxis");
		if(this.upDirection==-1){
			upAxisbounds=this.reverseSign(upAxisbounds);
		}
		
		ArrayList<Double> remainAxisbounds=rule.get("+remainAxis");
		
		int faceAxisChannelIndex=-1;
		int upAxisChannelIndex=-1;
		int remainAxisChannelIndex=-1;
		
		if(this.faceAxis.equals("X")){
			faceAxisChannelIndex=node.getGlobalChannelIndex(2);
		}
		else if(this.faceAxis.equals("Y")){
			faceAxisChannelIndex=node.getGlobalChannelIndex(0);
		}
		else if(this.faceAxis.equals("Z")){
			faceAxisChannelIndex=node.getGlobalChannelIndex(1);
		}
		
		if(this.upAxis.equals("X")){
			upAxisChannelIndex=node.getGlobalChannelIndex(2);
		}
		else if(this.upAxis.equals("Y")){
			upAxisChannelIndex=node.getGlobalChannelIndex(0);
		}
		else if(this.upAxis.equals("Z")){
			upAxisChannelIndex=node.getGlobalChannelIndex(1);
		}
		
		if(this.remainAxis.equals("X")){
			remainAxisChannelIndex=node.getGlobalChannelIndex(2);
		}
		else if(this.remainAxis.equals("Y")){
			remainAxisChannelIndex=node.getGlobalChannelIndex(0);
		}
		else if(this.remainAxis.equals("Z")){
			remainAxisChannelIndex=node.getGlobalChannelIndex(1);
		}
		
		//=======================================================
		
		if(frame.get(faceAxisChannelIndex)>faceAxisbounds.get(1)){
			frame.set(faceAxisChannelIndex,faceAxisbounds.get(1));
			System.out.println("Bad angle in faceAxis of node: "+nodeName+", resetValue");
		}
		else if(frame.get(faceAxisChannelIndex)<faceAxisbounds.get(0)){
			frame.set(faceAxisChannelIndex,faceAxisbounds.get(0));
			System.out.println("Bad angle in faceAxis of node: "+nodeName+", resetValue");
		}
		
		if(frame.get(upAxisChannelIndex)>upAxisbounds.get(1)){
			frame.set(upAxisChannelIndex,upAxisbounds.get(1));
			System.out.println("Bad angle in upAxis of node: "+nodeName+", resetValue");
		}
		else if(frame.get(upAxisChannelIndex)<upAxisbounds.get(0)){
			frame.set(upAxisChannelIndex,upAxisbounds.get(0));
			System.out.println("Bad angle in upAxis of node: "+nodeName+", resetValue");
		}
		
		if(frame.get(faceAxisChannelIndex)>faceAxisbounds.get(1)){
			frame.set(faceAxisChannelIndex,faceAxisbounds.get(1));
			System.out.println("Bad angle in remainAxis of node: "+nodeName+", resetValue");
		}
		else if(frame.get(remainAxisChannelIndex)<remainAxisbounds.get(0)){
			frame.set(remainAxisChannelIndex,remainAxisbounds.get(0));
			System.out.println("Bad angle in remainAxis of node: "+nodeName+", resetValue");
		}
		
		return frame;
	}
	
	private ArrayList<Double> reverseSign(ArrayList<Double> bound){
		Double tmp0=-bound.get(0);
		Double tmp1=-bound.get(1);
		bound.set(0,tmp1);
		bound.set(1,tmp0);
		return bound;
	}
}
