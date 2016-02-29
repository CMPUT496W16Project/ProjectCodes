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
	private Map<Integer,ArrayList<Double>> sampleFrames;
	
	public SampleReader(){}
	
	private ArrayList<Double> makeCopy(ArrayList<Double> src){
		
		ArrayList<Double> cpy=new ArrayList<Double>();
		
		for(Double currentVal : src){
			cpy.add(currentVal);
		}
		
		return cpy;
	}
	
	private ArrayList<Double> getStdErr(ArrayList<Double> src){
		
		Double sum=0.0;
		for(Double data : src){
			sum+=data;
		}
		
		Double avg=sum/src.size();
		
		ArrayList<Double> stdErr=new ArrayList<Double>();
		
		for(Double data : src){
			stdErr.add(data-avg);
		}
		
		return stdErr;
	}
	
	public void setBvhFilePath(String bvhFilePath){
		this.bvhFilePath=bvhFilePath;
	}
	
	public String getCurrentBvhFilePath(){
		return this.bvhFilePath;
	}
	
	public void readCurrentBvhFileAsSample(){
		
		this.sampleFrames=new HashMap<Integer,ArrayList<Double>>();
		
		FileReader fileReader;
		BufferedReader bufferedReader;
		
		try{
			fileReader=new FileReader(new File(this.bvhFilePath));
			bufferedReader=new BufferedReader(fileReader);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("BVH file not found, exit with error.");
			return;
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
					
					if(dataIndex>2){
						currentFrame.add(Double.parseDouble(currentFrameArray[dataIndex]));
					}
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
	}
	
	public Map<Integer,ArrayList<Double>> getSampleFrames(){
		//
		for(int frameIndex=0;frameIndex<this.sampleFrames.size();frameIndex++){
			for(Double val : this.sampleFrames.get(frameIndex)){
				System.out.print(val.toString()+" ");
			}
			System.out.println("");
		}
		//
		return this.sampleFrames;
	}
	
	public Map<Integer,ArrayList<Double>> getStdErrs(){
		
		if(this.sampleFrames==null){
			return null;
		}
		
		Map<Integer,ArrayList<Double>> stdErrs = new HashMap<Integer,ArrayList<Double>>();
		
		for(int frameIndex=0; frameIndex<this.sampleFrames.size();frameIndex++){
			stdErrs.put(frameIndex,this.getStdErr(this.sampleFrames.get(frameIndex)));
		}
		
		
		//
		for(int frameIndex=0;frameIndex<stdErrs.size();frameIndex++){
			for(Double val : stdErrs.get(frameIndex)){
				System.out.print(val.toString()+" ");
			}
			System.out.println("");
		}
		//
		
		return stdErrs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
