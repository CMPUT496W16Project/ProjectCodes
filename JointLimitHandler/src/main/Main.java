package main;

import modules.BvhFileData;
import modules.BvhFileReader;
import modules.TreeConverter;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("Hard code file path");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		TreeConverter tc=new TreeConverter();
		tc.convert(bvhFileData.getBvhHeader()).printAllNodeInformation();;
	}

}
