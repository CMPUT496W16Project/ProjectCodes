package main;

import modules.BvhFileData;
import modules.BvhFileReader;
import modules.DirectionCalculator;
import modules.TreeConverter;
import tree.Tree;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("XXXXXXXXXX");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		TreeConverter treeConverter=new TreeConverter();
		Tree tree=treeConverter.convert(bvhFileData.getBvhHeader());
		tree.printAllNodeInformation();
		
		DirectionCalculator directionCalculator=new DirectionCalculator(tree);
		
	}

}
