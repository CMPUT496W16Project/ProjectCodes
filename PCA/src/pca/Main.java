package pca;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the bvh file path : ");
		String bvhFilePath=sc.next();
		
		SampleReader sampleReader=new SampleReader();
		sampleReader.setBvhFilePath(bvhFilePath);
		
		String txtFilePath=bvhFilePath+".txt";
		
		sc.close();

		sampleReader.readCurrentBvhFileAsSample();
		
		MatrixConverter mc=new MatrixConverter();
		
		Matrix sampleData=mc.convertMatrix(sampleReader.getSampleFrames());
		Matrix stdErr=mc.convertMatrix(sampleReader.getStdErrsOfCurrentSample());
		CovarianceMatrixConverter cmc=new CovarianceMatrixConverter(stdErr);
		Matrix covarianceMatrix=cmc.getCovatianceMatrix();
		EigenvalueDecomposition ed=covarianceMatrix.eig();
		ArrayList<Integer> maxIndexList=Sort.getMax(ed.getD());
		System.out.println("FLAG7");
		double[][] eigArray=ed.getV().getArrayCopy();
		double[][] maxEigArray=new double[ed.getV().getRowDimension()][2];
		
		for(int index=0;index<ed.getV().getRowDimension();index++){
			maxEigArray[index][0]=eigArray[index][maxIndexList.get(0)];
			maxEigArray[index][1]=eigArray[index][maxIndexList.get(1)];
		}
		
		Matrix maxEigMat=new Matrix(maxEigArray);
		
		Matrix compressedData=sampleData.times(maxEigMat);
		//*************************************************************
		
		double[][] compressedDataArray=compressedData.getArrayCopy();
		
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(txtFilePath,"UTF-8");
			for(int index=0;index<compressedData.getRowDimension();index++){
				
				if(index%2==0){
					String line="";
					line+=Double.toString(compressedDataArray[index][0]);
					line+=" ";
					line+=Double.toString(compressedDataArray[index][1]);
					printWriter.println(line);
				}
				
			}
			
			printWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	


}
