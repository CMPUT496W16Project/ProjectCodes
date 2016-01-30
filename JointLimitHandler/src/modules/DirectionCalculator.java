package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tree.Node;
import tree.Tree;

public class DirectionCalculator {
	
	private Node upperLegA;
	private Node upperLegB;
	private Node spine;

	public DirectionCalculator(Tree tree){
		this.upperLegA=tree.getNodebyIndex(2);
		this.upperLegB=tree.getNodebyIndex(8);
		this.spine=tree.getNodebyIndex(14);
	}
	
	public Map<String,Integer> getDirectionInfromation(){
		
		String roughFacePlane=this.getRoughFacePlane();
		
		ArrayList<Integer> planeAxisIndexes=new ArrayList<Integer>();
		
		if(roughFacePlane.equals("ZY")){
			planeAxisIndexes.add(1);
			planeAxisIndexes.add(2);
		}
		else if(roughFacePlane.equals("XY")){
			planeAxisIndexes.add(0);
			planeAxisIndexes.add(2);
		}
		else if(roughFacePlane.equals("XZ")){
			planeAxisIndexes.add(0);
			planeAxisIndexes.add(1);
		}
		else{
			return null;
		}
		
		int upAxisIndex=-1;
		int upDirection=0;
		
		int faceAxisIndex=-1;
		int faceDirection=0;
		
		ArrayList<Integer> angleRelatedAxis=new ArrayList<Integer>();
		
		if(Math.abs(spine.getOffset(planeAxisIndexes.get(0)))>Math.abs(planeAxisIndexes.get(1))){
			faceAxisIndex=planeAxisIndexes.get(1);
			upAxisIndex=planeAxisIndexes.get(0);
			
			angleRelatedAxis=planeAxisIndexes;
		}
		else if(Math.abs(spine.getOffset(planeAxisIndexes.get(0)))<Math.abs(planeAxisIndexes.get(1))){
			faceAxisIndex=planeAxisIndexes.get(0);
			upAxisIndex=planeAxisIndexes.get(1);
			
			angleRelatedAxis.add(planeAxisIndexes.get(1));
			angleRelatedAxis.add(planeAxisIndexes.get(0));
		}
		else if(Math.abs(spine.getOffset(planeAxisIndexes.get(0)))==Math.abs(planeAxisIndexes.get(1))){
			return null;
		}
		
		if(spine.getOffset(upAxisIndex)>=0){
			upDirection=1;
		}
		else{
			upDirection=-1;
		}
		
		if(upAxisIndex==2){
			upDirection=-upDirection;
		}
		
		
		Double upperLegAxisAAvg=(upperLegA.getOffset(angleRelatedAxis.get(0))+upperLegB.getOffset(angleRelatedAxis.get(0)))/2;
		Double upperLegAxisBAvg=(upperLegA.getOffset(angleRelatedAxis.get(1))+upperLegB.getOffset(angleRelatedAxis.get(1)))/2;
		
		if(spine.getOffset(angleRelatedAxis.get(1))>0 && upperLegAxisBAvg>0){
			faceDirection=1;
		}
		else if(spine.getOffset(angleRelatedAxis.get(1))<0 && upperLegAxisBAvg<0){
			faceDirection=-1;
		}
		else{
			Double spineAxisA=Math.abs(spine.getOffset(angleRelatedAxis.get(0)));
			Double spineAxisB=Math.abs(spine.getOffset(angleRelatedAxis.get(1)));
			Double spineAngle=Math.toDegrees(Math.atan(spineAxisB/spineAxisA));
			Double upperLegAngle=Math.toDegrees(Math.atan(Math.abs(upperLegAxisBAvg)/Math.abs(upperLegAxisAAvg)));
			
			if(upperLegAngle-spineAngle>0 && upperLegAxisBAvg<0){
				faceDirection=-1;
			}
			else if(upperLegAngle-spineAngle>0 && upperLegAxisBAvg>0){
				faceDirection=1;
			}
			else if(upperLegAngle-spineAngle<0 && upperLegAxisBAvg>0){
				faceDirection=-1;
			}
			else if(upperLegAngle-spineAngle<0 && upperLegAxisBAvg<0){
				faceDirection=1;
			}
		}
		
		if(faceAxisIndex==2){
			faceDirection=-faceDirection;
		}
		
		Map<String,Integer> directionInformation=new HashMap<String,Integer>();
		directionInformation.put("Face Direction",faceDirection);
		System.out.println(faceDirection+" "+upDirection);
		System.out.println(upDirection+" "+upAxisIndex);
		directionInformation.put("Face Axis",faceAxisIndex);
		directionInformation.put("Up Direction",upDirection);
		directionInformation.put("Up Axis",upAxisIndex);
		
		return directionInformation;
	}
	
	private Double getLengthToOrigin(Node node){
		return Math.sqrt(Math.pow(node.getOffset(0),2)+Math.pow(node.getOffset(1),2)+Math.pow(node.getOffset(2),2));
	}
	
	private String getRoughFacePlane(){
		
		Double lengthA=this.getLengthToOrigin(upperLegA);
		Double lengthB=this.getLengthToOrigin(upperLegB);
		Double avgLength=(lengthA+lengthB)/2;
		
		Double diffX=Math.abs(upperLegA.getOffset(0)-upperLegB.getOffset(0));
		Double diffZ=Math.abs(upperLegA.getOffset(1)-upperLegB.getOffset(1));
		Double diffY=Math.abs(upperLegA.getOffset(2)-upperLegB.getOffset(2));
		
		Boolean flagX=false;
		Boolean flagZ=false;
		Boolean flagY=false;
		
		if(diffX/avgLength>=0.5){
			flagX=true;
		}
		if(diffZ/avgLength>=0.5){
			flagZ=true;
		}
		if(diffY/avgLength>=0.5){
			flagY=true;
		}
		
		if(flagX && flagZ==false && flagY==false){
			return "ZY";
		}
		else if(flagX==false && flagZ && flagY==false){
			return "XY";
		}
		else if(flagX==false && flagZ==false && flagY){
			return "XZ";
		}
		else{
			Double max=Math.max(diffX,Math.max(diffZ,diffY));
			
			if(max==diffX){
				return "ZY";
			}
			else if(max==diffZ){
				return "XY";
			}
			else if(max==diffY){
				return "XZ";
			}
			else{
				return null;
			}
		}
	}
}
