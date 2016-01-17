package tree;

public class Channel{
	
	private Integer channelIndex;
	private String channelName;
	private Double channelValue;
	
	public Channel(int channelIndex,String channelName,double channelValue){
		this.channelIndex=channelIndex;
		this.channelName=channelName;
		this.channelValue=channelValue;
	}
	
	public int getChannelIndex(){
		return this.channelIndex;
	}
	
	public String getChannelName(){
		return this.channelName;
	}
	
	public void setChannelValue(Double channelValue){
		this.channelValue=channelValue;
	}
	
	public Double getChannelValue(){
		return this.channelValue;
	}
	
	public void printChannelInformation(){
		System.out.println(this.channelIndex.toString()+" "+this.channelName);
	}
}
