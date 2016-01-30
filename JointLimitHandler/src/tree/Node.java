package tree;

import java.util.ArrayList;

public class Node {
	
	private NodeKey nodeKey;
	private ArrayList<Channel> channels;
	private ArrayList<Integer> childs;
	
	private Double offsetX;
	private Double offsetZ;
	private Double offsetY;
	
	public Node(NodeKey nodeKey){
		this.nodeKey=nodeKey;
		this.childs=new ArrayList<Integer>();
	}
	
	public void setChannels(ArrayList<Channel> channels){
		this.channels=channels;
	}
	
	public void setOffset(Double offsetX,Double offsetZ,Double offsetY){
		this.offsetX=offsetX;
		this.offsetZ=offsetZ;
		this.offsetY=offsetY;
	}
	
	public Double getOffset(int axis){
		if(axis==0){
			return this.offsetX;
		}
		else if(axis==1){
			return this.offsetZ;
		}
		return this.offsetY;
	}
	
	public NodeKey getNodeKey(){
		return this.nodeKey;
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
	
	public boolean setChannelValueByIndex(int channelIndex,Double channelValue){
		for(int index=0;index<this.channels.size();index++){
			if(this.channels.get(index).getChannelIndex()==channelIndex){
				Channel channel=this.channels.get(index);
				channel.setChannelValue(channelValue);
				this.channels.set(index,channel);
				return true;
			}
		}
		return false;
	}
	
	public void addChildByIndex(Integer nodeIndex){
		this.childs.add(nodeIndex);
	}
	
	public ArrayList<Integer> getChildIndexes(){
		return this.childs;
	}
	
	public void printNodeInformation(){
		System.out.println("Node Index : "+this.nodeKey.getNodeIndex().toString());
		System.out.println("Node Name  : "+this.nodeKey.getNodeName());
		System.out.println("Offsets    : "+this.offsetX.toString()+" "+this.offsetZ.toString()+" "+this.offsetY.toString());
		System.out.println("Channels   :");
		if(this.channels!=null){
			for(Channel c : this.channels){
				c.printChannelInformation();
			}
		}
		System.out.print("Childs     : ");
		for(Integer i : this.childs){
			System.out.print(i.toString()+" ");
		}
		System.out.println();
	}
}
