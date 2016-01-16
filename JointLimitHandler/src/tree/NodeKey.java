package tree;

public class NodeKey {
	
	private Integer nodeIndex;
	private String nodeName;
	
	public NodeKey(Integer nodeIndex,String nodeName){
		this.nodeIndex=nodeIndex;
		this.nodeName=nodeName;
	}
	
	public Integer getNodeIndex(){
		return this.nodeIndex;
	}
	
	public String getNodeName(){
		return this.nodeName;
	}
}
