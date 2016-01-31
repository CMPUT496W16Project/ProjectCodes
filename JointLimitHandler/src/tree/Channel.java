package tree;

public class Channel{
	
	private Integer channelIndex;
	private String channelName;
	
	public Channel(int channelIndex,String channelName){
		this.channelIndex=channelIndex;
		this.channelName=channelName;
	}
	
	public int getChannelIndex(){
		return this.channelIndex;
	}
	
	public String getChannelName(){
		return this.channelName;
	}
	
	public void printChannelInformation(){
		System.out.print("["+this.channelIndex.toString()+" "+this.channelName+"] ");
	}
}
