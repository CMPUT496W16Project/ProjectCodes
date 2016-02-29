package pca;

import java.util.ArrayList;
// import java.util.Arrays;
import java.util.Map;

import Jama.Matrix;

public class MatrixConverter {

	
	public MatrixConverter(){}
	
	
	public Matrix convertMatrix(Map<Integer,ArrayList<Double>> rawData){
		
		ArrayList<ArrayList<Double>> tmp=new ArrayList<ArrayList<Double>>();
		for(int frameIndex=0; frameIndex<rawData.size();frameIndex++){
			tmp.add(rawData.get(frameIndex));
		}
		
		double [][] array=new double[tmp.size()][tmp.get(0).size()];
		
		int outerIndex=0;
		for(ArrayList<Double> nested : tmp){
			
			int innerIndex=0;
			for(double val : nested){
				array[outerIndex][innerIndex]=val;
				innerIndex++;
			}
			
			outerIndex++;
		}
		
		// System.out.println(Arrays.deepToString(array));
		
		return new Matrix(array);
	}
}
