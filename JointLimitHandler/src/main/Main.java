package main;

import modules.BvhFileReader;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("Hard code your path here.");
		bvhFileReader.getCurrentBvhFileDataAsObject();
	}

}
