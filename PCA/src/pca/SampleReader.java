package pca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SampleReader {
	
	private String bvhFilePath;
	
	public SampleReader(){}
	
	private ArrayList<Double> makeCopy(ArrayList<Double> src){
		
		ArrayList<Double> cpy=new ArrayList<Double>();
		
		for(Double currentVal : src){
			cpy.add(currentVal);
		}
		
		return cpy;
	}
	
	public void setBvhFilePath(String bvhFilePath){
		this.bvhFilePath=bvhFilePath;
	}
	
	public String getCurrentBvhFilePath(){
		return this.bvhFilePath;
	}
	
	public Map<Integer,ArrayList<Double>> getCurrentBvhFileAsSample(){
		
		Map<Integer,ArrayList<Double>> sampleFrames=new HashMap<Integer,ArrayList<Double>>();
		
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
				
				sampleFrames.put(frameIndex,this.makeCopy(currentFrame));
				
				frameIndex++;
				currentFrame.clear();
			}
			
			fileReader.close();
			bufferedReader.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		
		return sampleFrames;
	}
	
}
