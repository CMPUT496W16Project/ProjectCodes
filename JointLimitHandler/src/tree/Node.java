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
	
	public void addChildByIndex(Integer nodeIndex){
		this.childs.add(nodeIndex);
	}
	
	public ArrayList<Integer> getChildIndexes(){
		return this.childs;
	}
	
	public Channel getChannelInformation(int localChannelIndex){
		return this.channels.get(localChannelIndex);
	}
	
	public Integer getGlobalChannelIndex(int localChannelIndex){
		return this.channels.get(localChannelIndex).getChannelIndex();
	}
	
	public void printNodeInformation(){
		System.out.println("");
		System.out.println("Node Index : "+this.nodeKey.getNodeIndex().toString());
		System.out.println("Node Name  : "+this.nodeKey.getNodeName());
		System.out.println("Offsets    : "+this.offsetX.toString()+" "+this.offsetZ.toString()+" "+this.offsetY.toString());
		System.out.println("Channels   :");
		if(this.channels!=null){
			for(Channel c : this.channels){
				c.printChannelInformation();
			}
			System.out.println("");
		}
		System.out.print("Childs     : ");
		for(Integer childIndex : this.childs){
			System.out.print(childIndex.toString()+" ");
		}
		System.out.println("");
	}
}
