package main;

import modules.BvhFileData;
import modules.BvhFileReader;
import modules.DirectionCalculator;
import modules.TreeConverter;
import tree.Tree;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("C:\\Users\\xiaocong\\Desktop\\BVHfiles\\01\\01_01.bvh");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		TreeConverter tc=new TreeConverter();
		Tree tree=tc.convert(bvhFileData.getBvhHeader());
		tree.printAllNodeInformation();
		
		DirectionCalculator dc=new DirectionCalculator(tree);
		dc.getDirectionInfromation();
	}

}
