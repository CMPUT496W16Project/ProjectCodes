package tree;

import java.util.ArrayList;

public class Node {
	
	private NodeKey nodeKey;
	private ArrayList<Channel> channels;
	private ArrayList<NodeKey> childs;
	
	public Node(NodeKey nodeKey,ArrayList<Channel> channels,ArrayList<NodeKey> childs){
		this.nodeKey=nodeKey;
		this.channels=channels;
		this.childs=childs;
	}
	
	public String getNodeName(){
		return this.nodeKey.getNodeName();
	}
	
	public Integer getNodeIndex(){
		return this.nodeKey.getNodeIndex();
	}
	
	public Double getChannelValueByIndex(int channelIndex){
		for(int index=0;index<this.channels.size();index++){
			if(this.channels.get(index).getChannelIndex()==channelIndex){
				return this.channels.get(index).getChannelValue();
			}
		}
		return null;
	}
	
	public void setChannelValueByIndex(int channelIndex,Double channelValue){
		for(int index=0;index<this.channels.size();index++){
			if(this.channels.get(index).getChannelIndex()==channelIndex){
				Channel channel=this.channels.get(index);
				channel.setChannelValue(channelValue);
				this.channels.set(index,channel);
				return;
			}
		}
	}
	
	public ArrayList<NodeKey> getChilds(){
		return this.childs;
	}
}
