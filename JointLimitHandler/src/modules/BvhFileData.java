package modules;

import java.util.ArrayList;
import java.util.Map;

public class BvhFileData {
	
	private String bvhFilePath;
	
	public ArrayList<String> bvhHeader;
	
	private Map<Integer,ArrayList<Double>> frames;
	
	public BvhFileData(String bvhFilePath,ArrayList<String> bvhHeader,Map<Integer,ArrayList<Double>> frames){
		this.bvhFilePath=bvhFilePath;
		this.bvhHeader=bvhHeader;
		this.frames=frames;
	}
	
	public String getBvhFilePath(){
		return this.bvhFilePath;
	}
	
	public ArrayList<Double> getFrameByFrameIndex(int frameIndex){
		return this.frames.get(frameIndex);
	}
}
