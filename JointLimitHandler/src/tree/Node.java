package tree;

import java.util.ArrayList;

public class Node {
	
	private NodeKey nodeKey;
	private ArrayList<Channel> channels;
	private ArrayList<Integer> childs;
	
	private Double offsetX;
	private Double offsetY;
	private Double offsetZ;
	
	public Node(NodeKey nodeKey){
		this.nodeKey=nodeKey;
		this.childs=new ArrayList<Integer>();
	}
	
	public void setChannels(ArrayList<Channel> channels){
		this.channels=channels;
	}
	
	public void setOffset(Double offsetX,Double offsetY,Double offsetZ){
		this.offsetX=offsetX;
		this.offsetY=offsetY;
		this.offsetZ=offsetZ;
	}
	
	public Double getOffset(String axis){
		if(axis.equals("X")){
			return this.offsetX;
		}
		else if(axis.equals("Y")){
			return this.offsetY;
		}
		return this.offsetZ;
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
	
	public void addChildByIndex(Integer nodeIndex){
		this.childs.add(nodeIndex);
	}
	
	public ArrayList<Integer> getChildIndexes(){
		return this.childs;
	}
	
	public void printNodeInformation(){
		System.out.println("Node Index : "+this.nodeKey.getNodeIndex().toString());
		System.out.println("Node Name  : "+this.nodeKey.getNodeName());
		System.out.println("Offsets    : "+this.offsetX.toString()+" "+this.offsetY.toString()+" "+this.offsetZ.toString());
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
