package main;

import modules.BvhFileData;
import modules.BvhFileReader;
import modules.TreeConverter;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("C:\\Users\\Xuping Fang\\Desktop\\CMPUT496\\BVH files\\cmuconvert-daz-01-09\\01\\01_01.bvh");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		TreeConverter tc=new TreeConverter();
		tc.convert(bvhFileData.getBvhHeader()).printAllNodeInformation();;
	}

}
