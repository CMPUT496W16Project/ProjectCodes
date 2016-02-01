package main;

import core.Core;
import modules.BvhFileData;
import modules.BvhFileReader;
import modules.DirectionCalculator;
import modules.TreeConverter;
import tree.Tree;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("C:\\Users\\Xuping Fang\\Desktop\\CMPUT496\\BVH files\\Test.bvh");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		
		Core core=new Core(bvhFileData);
		
	}

}
