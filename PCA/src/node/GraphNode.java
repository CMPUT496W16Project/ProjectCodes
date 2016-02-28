package node;

import java.util.ArrayList;
import java.util.Map;

public class GraphNode {
	
	private Integer nodeIndex;
	private String nodeName;
	
	private Map<String,Integer> connectingNodes;
	
	private ArrayList<Double> stdFrame;
	
	public GraphNode(int index,String name,ArrayList<Double> stdFrame){
		this.nodeIndex=index;
		this.nodeName=name;
		this.stdFrame=stdFrame;
	}
	
	public Integer getNodeIndex(){
		return this.nodeIndex;
	}
	
	public String getNodeName(){
		return this.nodeName;
	}
}
