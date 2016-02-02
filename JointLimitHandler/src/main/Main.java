package main;

import core.Core;
import modules.BvhFileData;
import modules.BvhFileReader;
import modules.RotationLimit;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("XXXXXXXXX");
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		RotationLimit rotationLimit=new RotationLimit();
		Core core=new Core(bvhFileData,rotationLimit);
		
		core.execute();
		
		core.writeFixedFile();
	}

}
