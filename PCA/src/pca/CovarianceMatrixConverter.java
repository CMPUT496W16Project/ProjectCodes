package pca;

import java.util.ArrayList;

import Jama.Matrix;

public class CovarianceMatrixConverter {
	
	private Matrix src;
	private ArrayList<Double> avgs;
	
	public CovarianceMatrixConverter(Matrix src){
		this.src=src;
		
		this.avgs=new ArrayList<Double>();
		
		for(int rowIndex=0;rowIndex<this.src.getColumnDimension();rowIndex++){
			
			Double colSum=0.0;
			for(int colIndex=0;colIndex<this.src.getRowDimension();colIndex++){
				
				colSum+=this.src.get(colIndex,rowIndex);
			}
			
			this.avgs.add(colSum/this.src.getRowDimension());
		}
	}
	
	private double getCovatiance(int index1,int index2){
		
		Double sum=0.0;
		
		for(int colIndex=0;colIndex<this.src.getRowDimension();colIndex++){
			sum+=(this.src.get(colIndex,index1)-this.avgs.get(index1))*(this.src.get(colIndex,index2)-this.avgs.get(index2));
		}
		
		return sum/(this.src.getRowDimension()-1);
	}
	
	public Matrix getCovatianceMatrix(){
		
		Matrix covatianceMatrix=new Matrix(this.src.getColumnDimension(),this.src.getColumnDimension());
		
		for(int colIndex=0;colIndex<this.src.getColumnDimension();colIndex++){
			
			for(int rowIndex=0;rowIndex<this.src.getColumnDimension();rowIndex++){
				
				covatianceMatrix.set(colIndex,rowIndex,this.getCovatiance(colIndex,rowIndex));
			}
		}
		
		return covatianceMatrix;
	}
}
