package main;

import modules.BvhFileData;
import modules.BvhFileReader;
import modules.DirectionCalculator;
import modules.TreeConverter;
import tree.Tree;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("C:\\Users\\Xuping Fang\\Desktop\\CMPUT496\\BVH files\\01\\01_01.bvh");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		TreeConverter tc=new TreeConverter();
		Tree tree=tc.convert(bvhFileData.getBvhHeader());
		tree.printAllNodeInformation();
		
		DirectionCalculator dc=new DirectionCalculator(tree);
		dc.getDirectionInfromation();
	}

}
