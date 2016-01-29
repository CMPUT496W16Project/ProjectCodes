package modules;

import java.util.ArrayList;

import tree.Node;
import tree.Tree;

public class DirectionCalculator {

	public DirectionCalculator(){}
	
	public void getFaceAxis(Tree tree){
		String roughFaceDirection=this.getRoughFacePlane(tree);
		Node spine=tree.getNodebyIndex(14);
		
		ArrayList<Integer> axisIndexes=new ArrayList<Integer>();
		if(roughFaceDirection.equals("ZY")){
			axisIndexes.add(1);
			axisIndexes.add(2);
		}
		else if(roughFaceDirection.equals("XY")){
			axisIndexes.add(0);
			axisIndexes.add(2);
		}
		else if(roughFaceDirection.equals("XZ")){
			axisIndexes.add(0);
			axisIndexes.add(1);
		}
		else{
			return;
		}
		
		int faceAxisIndex=0;
		int faceDirection=0;
		
		ArrayList<Integer> angleRelatedAxis=new ArrayList<Integer>();
		
		if(Math.abs(spine.getOffset(axisIndexes.get(0)))>Math.abs(axisIndexes.get(1))){
			faceAxisIndex=axisIndexes.get(1);
			angleRelatedAxis=axisIndexes;
		}
		else if(Math.abs(spine.getOffset(axisIndexes.get(0)))<Math.abs(axisIndexes.get(1))){
			faceAxisIndex=axisIndexes.get(0);
			angleRelatedAxis.add(axisIndexes.get(1));
			angleRelatedAxis.add(axisIndexes.get(0));
		}
		else if(Math.abs(spine.getOffset(axisIndexes.get(0)))==Math.abs(axisIndexes.get(1))){
			return;
		}
		
		
		Node upperLegA=tree.getNodebyIndex(2);
		Node upperLegB=tree.getNodebyIndex(8);
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
		
		System.out.println(faceDirection+" "+faceAxisIndex);
	}
	
	private String getRoughFacePlane(Tree tree){
		Node upperLegA=tree.getNodebyIndex(2);
		Node upperLegB=tree.getNodebyIndex(8);
		
		Double lengthA=Math.sqrt(Math.pow(upperLegA.getOffset(0),2)+Math.pow(upperLegA.getOffset(1),2)+Math.pow(upperLegA.getOffset(2),2));
		Double lengthB=Math.sqrt(Math.pow(upperLegB.getOffset(0),2)+Math.pow(upperLegB.getOffset(1),2)+Math.pow(upperLegB.getOffset(2),2));
		Double avgLength=(lengthA+lengthB)/2;
		
		Double diffA=Math.abs(upperLegA.getOffset(0)-upperLegB.getOffset(0));
		Double diffB=Math.abs(upperLegA.getOffset(1)-upperLegB.getOffset(1));
		Double diffC=Math.abs(upperLegA.getOffset(2)-upperLegB.getOffset(2));
		
		Boolean flagA=false;
		Boolean flagB=false;
		Boolean flagC=false;
		
		if(diffA/avgLength>=0.5){
			flagA=true;
		}
		if(diffB/avgLength>=0.5){
			flagB=true;
		}
		if(diffC/avgLength>=0.5){
			flagC=true;
		}
		
		if(flagA && flagB==false && flagC==false){
			return "ZY";
		}
		else if(flagA==false && flagB && flagC==false){
			return "XY";
		}
		else if(flagA==false && flagB==false && flagC){
			return "XZ";
		}
		else{
			Double max=Math.max(diffA,Math.max(diffB,diffC));
			
			if(max==diffA){
				return "ZY";
			}
			else if(max==diffB){
				return "XY";
			}
			else if(max==diffC){
				return "XZ";
			}
			else{
				return "Unknown";
			}
		}
	}
}
