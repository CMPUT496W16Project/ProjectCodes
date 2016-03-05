package pca;

import java.util.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class Main {

	public static void main(String[] args) {
		SampleReader sampleReader=new SampleReader();
		sampleReader.setBvhFilePath("C:\\Users\\Xuping Fang\\Desktop\\CMPUT496\\BVH files\\01\\X.bvh");
		sampleReader.readCurrentBvhFileAsSample();
		
		MatrixConverter mc=new MatrixConverter();
		
		// Matrix sampleData=mc.convertMatrix(sampleReader.getSampleFrames());
		Matrix stdErr=mc.convertMatrix(sampleReader.getStdErrsOfCurrentSample());
		
		CovarianceMatrixConverter cmc=new CovarianceMatrixConverter(stdErr);
		
		Matrix covarianceMatrix=cmc.getCovatianceMatrix();
		System.out.println(Arrays.deepToString(covarianceMatrix.getArrayCopy()));
		
		EigenvalueDecomposition ed=covarianceMatrix.eig();
		Matrix eigenvectorMatrix=ed.getV();
		System.out.println(Arrays.deepToString(eigenvectorMatrix.getArrayCopy()));
	}

}
