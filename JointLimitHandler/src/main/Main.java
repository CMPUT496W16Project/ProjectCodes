package main;

import java.util.Scanner;

import core.Core;
import modules.BvhFileData;
import modules.BvhFileReader;
import modules.RotationLimit;

public class Main {

	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the bvh file path : ");
		String bvhFilePath=sc.next();
		
		BvhFileReader bvhFileReader=new BvhFileReader();
		bvhFileReader.setBvhFilePath(bvhFilePath);
		BvhFileData bvhFileData=bvhFileReader.getCurrentBvhFileDataAsObject();
		
		RotationLimit rotationLimit=new RotationLimit();
		Core core=new Core(bvhFileData,rotationLimit);
		
		core.execute();
		
		System.out.println("Enter the fixed bvh file path : ");
		String fixedFilePath=sc.next();
		core.writeFixedFile(fixedFilePath);
		
		sc.close();
	}

}
