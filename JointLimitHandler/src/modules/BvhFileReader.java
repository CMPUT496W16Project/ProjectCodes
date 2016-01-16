package modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BvhFileReader {

	private String bvhFilePath;
	
	public BvhFileReader(){}
	
	public void setBvhFilePath(String bvhFilePath){
		this.bvhFilePath=bvhFilePath;
	}
	
	public String getCurrentBvhFilePath(){
		return this.bvhFilePath;
	}
	
	public BvhFileData getCurrentBvhFileDataAsObject(){
		
		ArrayList<String> bvhHeader=new ArrayList<String>();
		Map<Integer,ArrayList<Double>> frames=new HashMap<Integer,ArrayList<Double>>();
		
		FileReader fileReader;
		BufferedReader bufferedReader;
		
		try{
			fileReader=new FileReader(new File(this.bvhFilePath));
			bufferedReader=new BufferedReader(fileReader);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("BVH file not found, exit with error.");
			return null;
		}
		
		// Read the file line by line
		try {
					
			String line;	
			
			while ((line=bufferedReader.readLine()) != null){
				
				bvhHeader.add(line);
				System.out.println(line);
					
				if (line.contains("Frame Time")){
					break;
				}
			}
					
			
			int frameIndex=0;
			
			ArrayList<Double> currentFrame=new ArrayList<Double>();
					
			while ((line=bufferedReader.readLine()) != null){
				
				String[] currentFrameArray = line.split("\\s+");
				
				for(int dataIndex=0;dataIndex<currentFrameArray.length;dataIndex++){
					currentFrame.add(Double.parseDouble(currentFrameArray[dataIndex]));
				}
				
				frames.put(frameIndex,this.makeCopy(currentFrame));
				
				frameIndex++;
				currentFrame.clear();
			}
			
			fileReader.close();
			bufferedReader.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		
		BvhFileData bvhFileData=new BvhFileData(this.bvhFilePath,bvhHeader,frames);
		return bvhFileData;
	}
	
	
	private ArrayList<Double> makeCopy(ArrayList<Double> src){
		
		ArrayList<Double> cpy=new ArrayList<Double>();
		
		for(Double currentVal : src){
			cpy.add(currentVal);
		}
		
		return cpy;
	}
}
