package main;

import modules.BvhFileReader;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("Hard code your BVH file path here.");
		bvhFileReader.getCurrentBvhFileDataAsObject();
	}

}
