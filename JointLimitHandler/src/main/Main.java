package main;

import modules.BvhFileReader;

public class Main {

	public static void main(String[] args){
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath("C:\\Users\\xiaocong\\Desktop\\cmuconvert-daz-01-09\\01\\01_01.bvh");
		bvhFileReader.getCurrentBvhFileDataAsObject();
	}

}
