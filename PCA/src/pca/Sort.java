package pca;

import java.util.ArrayList;

import Jama.Matrix;

public class Sort {
	
	public static ArrayList<Integer> getMax(Matrix src){
		
		ArrayList<Integer> maxIndexList=new ArrayList<Integer>();
		int numOfCol=src.getColumnDimension();
		
		Double maxVal=src.get(0,0);
		Integer maxIndex=0;
		
		for(int index=0;index<numOfCol;index++){
			
			if(maxVal<src.get(index,index)){
				maxVal=src.get(index,index);
				maxIndex=index;
			}
		}
		
		maxIndexList.add(maxIndex);
		
		Double maxVal2=src.get(0,0);
		Integer maxIndex2=0;
		
		for(int index=0;index<numOfCol;index++){
			
			if(index==maxIndex){
				continue;
			}
			
			if(maxVal2<src.get(index,index)){
				maxVal2=src.get(index,index);
				maxIndex2=index;
			}
		}
		
		maxIndexList.add(maxIndex2);
		
		return maxIndexList;
	}
}
